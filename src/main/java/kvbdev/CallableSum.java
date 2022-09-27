package kvbdev;

import java.util.concurrent.Callable;

public class CallableSum implements Callable<Long> {
    protected final int[] arr;
    protected final int starIndex;
    protected final int endIndex;

    public CallableSum(int[] arr, int starIndex, int endIndex) {
        this.arr = arr;
        this.starIndex = starIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Long call() throws Exception {
        long sum = 0L;
        for (int i = starIndex; i < endIndex; i++) {
            sum += arr[i];
        }
        return sum;
    }
}
