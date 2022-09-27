﻿## Калькулятор массива

Программа вычисляет сумму элементов массива и среднее арифметическое.

После запуска были получены следующие примерные результаты для 4-х потоков:

|Метод|Время выборки<br>10 000 000|Время выборки<br>500 000 000|
|---|---|---|
|singleThreadStats   | 20 | 511 |
|streamStats         | 27 | 535 |
|parallelStreamStats | 26 | 365 |
|recursiveTaskStats  | 33 | 368 |
|multiThreadedBatches| 26 | 374 |

### Однопоточное выполнение

Самым быстрым способом суммировать элементы **небольшого** массива будет обычное однопоточное сложение в цикле `for`.

В других случаях сказываются накладные расходы на создание `Stream` или создание задач в пуле потоков.

Увеличение времени выполнения многопоточного варианта по сравнению с однопоточным может составить до 57% - 65%.

### Многопоточное выполнение

При достаточно **большом** количестве элементов самым быстрым и удобным способом оказался подсчет в параллельном потоке.

Многопоточный вариант может сократить время выполения на 29% - 44% на таких примитивных операциях как суммирование.
