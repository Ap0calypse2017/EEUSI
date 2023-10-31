package exp01;

import java.io.File;
import java.io.IOException; 
import java.util.Arrays;
import java.util.Random;

import javax.swing.Popup;

public class Test {

    private static int REPEAT_TIMES = 1000; // Number of repetitions for a multiple benchmark test

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
        int targetStringLength = 10; //lenght of the string
        StringBuilder buffer = new StringBuilder(targetStringLength);
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < targetStringLength; j++) {
                int randomLimitedInt = leftLimit + (int) 
                  (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            array[i] = buffer.toString();
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
        /*Warmup of 100 cycles */
        for (int i = 0; i < 100; i++) {
            T[] burnerArray = Arrays.copyOf(array, array.length);
            attempts[i] = benchmarkSort(sorter, burnerArray);
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
        BubbleSortPassPerItem<Integer> ppiSorter = new BubbleSortPassPerItem<Integer>();
        BubbleSortUntilNoChange<Integer> uncSorter = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> wnSorter = new BubbleSortWhileNeeded<Integer>();

        int[] pools = new int[] { 100, 1000, 10000,}; // sizes of arrays to test
        System.out.println("\nRUNING TEST ON ARRAYS OF VARYING SIZES");
        System.out.printf("Each test is performed %d times\n", REPEAT_TIMES);
        System.out.println("-".repeat(55));
        System.out.format("%10s%15s%15s%15s\n", "Array Size", "PassPerItem", "UntilNoChange", "WhileNeeded");
        System.out.println("-".repeat(55));

        /* Go through all test cases */
        for (int p : pools) {
            /* Generate random array of size p */
            Integer[] array = generateRandomArray(p);

            /* Implementation for multiple benchmarks */
            long[] ppiAttempts = repeatBenchmark(ppiSorter, array, REPEAT_TIMES);
            long[] uncAttempts = repeatBenchmark(uncSorter, array, REPEAT_TIMES);
            long[] wnAttempts = repeatBenchmark(wnSorter, array, REPEAT_TIMES);

            /* Print mean benchmarks */
            System.out.format("%10d%15d%15d%15d\n", p, meanTime(ppiAttempts), meanTime(uncAttempts),
                    meanTime(wnAttempts));
            
        }
        System.out.println("STRING SORTING");
        System.out.println("-".repeat(55));
        System.out.format("%10s%15s%15s%15s\n", "Array Size", "PassPerItem", "UntilNoChange", "WhileNeeded");
        System.out.println("-".repeat(55));
        // for (int p : pools) {
        //     /* Generate random array of size p */
        //     String[] array = generateRandomStringArray(p);

        //     /* Implementation for multiple benchmarks */
        //     long[] ppiAttempts = repeatBenchmark(ppiSorter, array, REPEAT_TIMES);
        //     long[] uncAttempts = repeatBenchmark(uncSorter, array, REPEAT_TIMES);
        //     long[] wnAttempts = repeatBenchmark(wnSorter, array, REPEAT_TIMES);

        //     /* Print mean benchmarks */
        //     System.out.format("%10d%15d%15d%15d\n", p, meanTime(ppiAttempts), meanTime(uncAttempts),
        //             meanTime(wnAttempts));
            
        // }

        System.out.println("-".repeat(55));
    }
}
