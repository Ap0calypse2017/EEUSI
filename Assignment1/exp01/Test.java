package exp01;

import java.util.Arrays;
import java.util.Random;

public class Test {

    private static int REPEAT_TIMES = 500; // Number of repetitions for a multiple benchmark test
    private static int WARMAP_CYCLES = 50; // Cycles when warming up;

    /*
     * Generate a random Integer array of given size.
     */
    private static Integer[] generateRandomArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); // 0-99
        }

        return array;
    }

    private static String[] generateRandomStringArray(int size) {
        String[] array = new String[size];
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10; // lenght of the string
        StringBuilder buffer = new StringBuilder(targetStringLength);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < targetStringLength; j++) {
                int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            array[i] = buffer.toString();
        }
        return array;
    }

    /*
     * Generate a random Integer array of given size.
     */
    private static Byte[] generateRandomByteArray(int size) {
        Byte[] array = new Byte[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = (byte) (random.nextInt(256) - 128);
        }

        return array;
    }

    /*
     * Compute time (nanoseconds) taken for a sorter to sort an array.
     */
    private static <T extends Comparable<T>> long benchmarkSort(Sorter<T> sorter, T[] array) {
        long start, end;

        start = System.nanoTime();
        sorter.sort(array);
        end = System.nanoTime();

        return end - start;
    }

    /*
     * Repeat the same
     */
    private static <T extends Comparable<T>> long[] repeatBenchmark(Sorter<T> sorter, T[] array, int times) {
        long[] attempts = new long[times];
        /* Warmup */
        for (int i = 0; i < WARMAP_CYCLES; i++) {
            T[] burnerArray = Arrays.copyOf(array, array.length);
            benchmarkSort(sorter, burnerArray);
        }

        for (int i = 0; i < times; i++) {
            T[] burnerArray = Arrays.copyOf(array, array.length);
            attempts[i] = benchmarkSort(sorter, burnerArray);
        }

        return attempts;
    }

    /*
     * Compute the mean value given an array of longs.
     */
    private static long meanTime(long[] attempts) {
        long sum = 0;
        for (long t : attempts) {
            sum += t;
        }

        return sum / attempts.length;
    }

    public static void main(String args[]) {
        /* Initialize sorters */
        BubbleSortPassPerItem<String> ppiSorterStr = new BubbleSortPassPerItem<String>();
        BubbleSortUntilNoChange<String> uncSorterStr = new BubbleSortUntilNoChange<String>();
        BubbleSortWhileNeeded<String> wnSorterStr = new BubbleSortWhileNeeded<String>();

        BubbleSortPassPerItem<Integer> ppiSorterInt = new BubbleSortPassPerItem<Integer>();
        BubbleSortUntilNoChange<Integer> uncSorterInt = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> wnSorterInt = new BubbleSortWhileNeeded<Integer>();

        BubbleSortPassPerItem<Byte> ppiSorterByte = new BubbleSortPassPerItem<Byte>();
        BubbleSortUntilNoChange<Byte> uncSorterByte = new BubbleSortUntilNoChange<Byte>();
        BubbleSortWhileNeeded<Byte> wnSorterByte = new BubbleSortWhileNeeded<Byte>();

        int[] pools = new int[] { 100, 1000, 5000, }; // sizes of arrays to test
        System.out.println("\nRUNING TEST ON ARRAYS OF VARYING SIZES");
        System.out.printf("Each test is performed %d times\n", REPEAT_TIMES);
        System.out.println("-".repeat(55));
        System.out.format("%10s%15s%15s%15s\n", "Array Size", "PassPerItem", "UntilNoChange", "WhileNeeded");
        System.out.println("-".repeat(55));

        /* Go through all test cases */
        for (int p : pools) {
            /* Generate random array of size p */
            String[] arrayString = generateRandomStringArray(p);
            Integer[] arrayInt = generateRandomArray(p);
            Byte[] arrayByte = generateRandomByteArray(p);

            /* Implementation for multiple benchmarks */
            /* STRING */
            long[] ppiAttemptsStr = repeatBenchmark(ppiSorterStr, Arrays.copyOf(arrayString, arrayString.length), REPEAT_TIMES);
            long[] uncAttemptsStr = repeatBenchmark(uncSorterStr, Arrays.copyOf(arrayString, arrayString.length), REPEAT_TIMES);
            long[] wnAttemptsStr = repeatBenchmark(wnSorterStr, Arrays.copyOf(arrayString, arrayString.length), REPEAT_TIMES);

            /* Print mean benchmarks */
            System.out.println("STRING");
            System.out.format("%10d%15d%15d%15d\n", p, meanTime(ppiAttemptsStr), meanTime(uncAttemptsStr),
                    meanTime(wnAttemptsStr));

            /* INTEGER */
            long[] ppiAttemptsInt = repeatBenchmark(ppiSorterInt, Arrays.copyOf(arrayInt, arrayInt.length), REPEAT_TIMES);
            long[] uncAttemptsInt = repeatBenchmark(uncSorterInt, Arrays.copyOf(arrayInt, arrayInt.length), REPEAT_TIMES);
            long[] wnAttemptsInt = repeatBenchmark(wnSorterInt, Arrays.copyOf(arrayInt, arrayInt.length), REPEAT_TIMES);
            System.out.println("INTEGER");
            System.out.format("%10d%15d%15d%15d\n", p, meanTime(ppiAttemptsInt), meanTime(uncAttemptsInt),
                    meanTime(wnAttemptsInt));

            /* BYTES */
            long[] ppiAttemptsByte = repeatBenchmark(ppiSorterByte, Arrays.copyOf(arrayByte, arrayByte.length), REPEAT_TIMES);
            long[] uncAttemptsByte = repeatBenchmark(uncSorterByte, Arrays.copyOf(arrayByte, arrayByte.length), REPEAT_TIMES);
            long[] wnAttemptsByte = repeatBenchmark(wnSorterByte, Arrays.copyOf(arrayByte, arrayByte.length), REPEAT_TIMES);
            System.out.println("BYTE");
            System.out.format("%10d%15d%15d%15d\n", p, meanTime(ppiAttemptsByte), meanTime(uncAttemptsByte),
                    meanTime(wnAttemptsByte));
        }

        System.out.println("-".repeat(55));
    }
}
