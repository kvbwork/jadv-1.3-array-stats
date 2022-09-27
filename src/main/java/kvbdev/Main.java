package kvbdev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Exception {
        final int SMALL_ARRAY_SIZE = 10_000_000;
        final int BIG_ARRAY_SIZE = 500_000_000;
        final int BATCH_SIZE = 5_000_000;

        int[] arr = generateRandomArray(SMALL_ARRAY_SIZE);
        System.out.println("Small array size: " + SMALL_ARRAY_SIZE);
        testSumVariants(arr, BATCH_SIZE);

        arr = generateRandomArray(BIG_ARRAY_SIZE);
        System.out.println("Big array size: " + BIG_ARRAY_SIZE);
        testSumVariants(arr, BATCH_SIZE);
    }

    public static void testSumVariants(int[] arr, int batchSize) throws Exception {
        System.out.println("singleThreadStats:");
        singleThreadStats(arr);

        System.out.println("streamStats:");
        streamStats(arr);

        System.out.println("parallelStreamStats:");
        parallelStreamStats(arr);

        System.out.println("recursiveTaskStats:");
        recursiveForkJoinTaskStats(arr, batchSize);

        System.out.println("multiThreadedBatches:");
        multiThreadedBatches(arr, batchSize);
    }


    // Метод генерации целочисленного массива
    public static int[] generateRandomArray(int size) {
        return new Random()
                .ints(size)
                .toArray();
    }

    // Метод подсчета суммы значений элементов целочисленного массива
    public static void singleThreadStats(int[] arr) {
        long startTime = System.currentTimeMillis();
        long sum = 0L;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        double avg = (double) sum / arr.length;
        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
        System.out.println("time = " + (endTime - startTime));
    }

    public static void streamStats(int[] arr) {
        long startTime = System.currentTimeMillis();
        long sum = IntStream.of(arr).asLongStream().sum();
        double avg = (double) sum / arr.length;
        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
        System.out.println("time = " + (endTime - startTime));
    }

    public static void parallelStreamStats(int[] arr) {
        long startTime = System.currentTimeMillis();
        long sum = IntStream.of(arr).asLongStream().parallel().sum();
        double avg = (double) sum / arr.length;
        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
        System.out.println("time = " + (endTime - startTime));
    }

    // Специальная рекурсивная задача для подсчета суммы значений элементов целочисленного массива
    public static void recursiveForkJoinTaskStats(int[] arr, int batchSize) {
        ForkJoinPool threadPool = new ForkJoinPool();
        LongSumRecursiveTask myTask = new LongSumRecursiveTask(arr, batchSize, 0, arr.length);

        long startTime = System.currentTimeMillis();
        long sum = threadPool.invoke(myTask);
        double avg = (double) sum / arr.length;
        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
        System.out.println("time = " + (endTime - startTime));

        threadPool.shutdownNow();
    }

    public static void multiThreadedBatches(int[] arr, int batchSize) throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CallableSum> tasks = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        long sum = 0L;

        int batches = arr.length / batchSize;
        for (int i = 0; i < batches; i++) {
            tasks.add(new CallableSum(arr, i * batchSize, (i + 1) * batchSize));
        }
        if (batches * batchSize < arr.length)
            tasks.add(new CallableSum(arr, batches * batchSize, arr.length));

        List<Future<Long>> results = threadPool.invokeAll(tasks);
        for (Future<Long> result : results) {
            sum += result.get();
        }

        double avg = (double) sum / arr.length;
        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
        System.out.println("time = " + (endTime - startTime));

        threadPool.shutdownNow();
    }
}
