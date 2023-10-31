package exp01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {

    private static int REPEAT_TIMES = 10; // Number of repetitions for a multiple benchmark test
    private static int[] POOLS = new int[] { 100, 1000, 10000, 50000 }; // sizes of arrays to test

    private static int N_CORES = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newFixedThreadPool(N_CORES * (1 + 50 / 5));

    /*
     * Generate a random Integer array of given size.
     */
    private static Integer[] generateRandomArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100); // 0-99
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
     * Repeat the same sorting algorithm multiple times.
     */
    private static <T extends Comparable<T>> long[] repeatBenchmark(Sorter<T> sorter, T[] array, int times) {
        long[] attempts = new long[times];
        List<Future<Long>> futures = new ArrayList<Future<Long>>();

        for (int i = 0; i < times; i++) {
            /* Schedule each repetition */
            futures.add(executorService.submit(() -> {
                return benchmarkSort(sorter, Arrays.copyOf(array, array.length));
            }));
        }

        for (int i = 0; i < times; i++) {
            /* Wait for scheduled tasks to finish */
            try {
                attempts[i] = futures.get(i).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        long start, end;

        /* Initialize sorters */
        BubbleSortPassPerItem<Integer> ppiSorter = new BubbleSortPassPerItem<Integer>();
        BubbleSortUntilNoChange<Integer> uncSorter = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> wnSorter = new BubbleSortWhileNeeded<Integer>();

        System.out.println("\nRUNING TEST ON ARRAYS OF VARYING SIZES");
        System.out.printf("Each test is performed %d times\n", REPEAT_TIMES);
        System.out.println("-".repeat(55));
        System.out.format("%10s%15s%15s%15s\n", "Array Size", "PassPerItem", "UntilNoChange", "WhileNeeded");
        System.out.println("-".repeat(55));

        start = System.nanoTime();
        /* Go through all test cases */
        for (int p : POOLS) {
            /* Generate random array of size p */
            Integer[] array = generateRandomArray(p);

            /* Implementation for multiple benchmarks (schedule to execute) */
            Future<long[]> ppiFuture = executorService.submit(() -> repeatBenchmark(ppiSorter, Arrays.copyOf(array, array.length), REPEAT_TIMES));
            Future<long[]> uncFuture = executorService.submit(() -> repeatBenchmark(uncSorter, Arrays.copyOf(array, array.length), REPEAT_TIMES));
            Future<long[]> wnFuture = executorService.submit(() -> repeatBenchmark(wnSorter, Arrays.copyOf(array, array.length), REPEAT_TIMES));

            /* Wait for execution termination */
            try {
                long[] ppiAttempts = ppiFuture.get();
                long[] uncAttempts = uncFuture.get();
                long[] wnAttempts = wnFuture.get();

                /* Print mean benchmarks */
                System.out.format("%-10d%15d%15d%15d\n", p, meanTime(ppiAttempts), meanTime(uncAttempts),
                        meanTime(wnAttempts));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        end = System.nanoTime();
        System.out.println("-".repeat(55));
        System.out.printf("Finished %d tests in %dms. The algorithms were run a a total of %d times.\n",
                3 * POOLS.length, (end - start) / 1_000_000, 3 * REPEAT_TIMES * POOLS.length);

        /* Graceful shutdown */
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
