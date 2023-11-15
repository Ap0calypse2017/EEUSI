package exp01;

import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.Collections;


public class Test {
    private static int REPEAT_TIMES = 100; // Number of repetitions for a multiple benchmark test
    private static int WARMUP_CYCLES = 25; // Cycles when warming up;
    private static String OUTPUT_DIR = "./results/";
    private static int STRING_LENGTH = 5;
    private static int[] POOLS = { 100, 1000, 5000 }; // sizes of arrays to test

    /*
     * Generate a random Integer array.
     */
    private static Integer[] genRandIntArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); 
        }
        return array;
    }

    /*
     * Generate an Integer array in ascending order.
     */
    private static Integer[] genAscIntArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); 
        }
        Arrays.sort(array);
        return array;
    }

    /*
     * Generate an Integer array in descending order.
     */
    private static Integer[] genDescIntArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); 
        }
        Arrays.sort(array, Collections.reverseOrder());
        return array;
    }

    /*
     * Generate a random String array.
     */
    private static String[] genRandStrArray(int size) {
        String[] array = new String[size];
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < STRING_LENGTH; j++) {
                int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            array[i] = buffer.toString();
        }
        return array;
    }

    /*
     * Generate a String array in ascending order.
     */
    private static String[] genAscStrArray(int size) {
        String[] array = new String[size];
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < STRING_LENGTH; j++) {
                int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            array[i] = buffer.toString();
        }
        Arrays.sort(array);

        return array;

    }

    /*
     * Generate a String array in descending order.
     */
    private static String[] genDescStrArray(int size) {
        String[] array = new String[size];
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < STRING_LENGTH; j++) {
                int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            array[i] = buffer.toString();
        }
        Arrays.sort(array, Collections.reverseOrder());

        return array;

    }

    /*
     * Generate a random Byte array.
     */
    private static Byte[] genRandByteArray(int size) {
        Byte[] array = new Byte[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = (byte) (random.nextInt(256) - 128);
        }
        return array;
    }

    /*
     * Generate a Byte array in ascending order.
     */
    private static Byte[] genAscByteArray(int size) {
        Byte[] array = new Byte[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = (byte) (random.nextInt(256) - 128);
        }
        // Sort the array
        Arrays.sort(array);
        return array;
    }

    /*
     * Generate a Byte array in descending order.
     */
    private static Byte[] genDescByteArray(int size) {
        Byte[] array = new Byte[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = (byte) (random.nextInt(256) - 128);
        }
        // Sort the array in reverse order
        Arrays.sort(array, Collections.reverseOrder());
        return array;
    }

    /*
     * Generate a random Double array.
     */
    private static Double[] genRandDoubleArray(int size) {
        Double[] array = new Double[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextDouble();
        }
        return array;
    }

    /*
     * Compute time(ns) taken for a sorter to sort an array.
     */
    private static <T extends Comparable<T>> long benchmarkSort(Sorter<T> sorter, T[] array) {
        long start, end;
        start = System.nanoTime();
        sorter.sort(array);
        end = System.nanoTime();

        return end - start;
    }

    /*
     * Warmups.
     */
    private static <T extends Comparable<T>> void warmup(Sorter<T> sorter, T[] array, int cycles) {
        for (int i = 0; i < cycles; i++) {
            benchmarkSort(sorter, cpArray(array));
        }
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

    /*
     * Prints on terminal the benchmark header
     */
    private static void printHeader() {
        System.out.println("\nRUNING TEST ON ARRAYS OF VARYING SIZES");
        System.out.printf("Each test is performed %d times\n", REPEAT_TIMES);
        System.out.println("-".repeat(65));
        System.out.format("%-10s%10s%15s%15s%15s\n", "Type", "Size", "PassPerItem", "UntilNoChange",
                "WhileNeeded");
        System.out.println("-".repeat(65));
    }

    /*
     * Prints on terminal a table row of results
     */
    private static void printBenchResult(String type, int pool, long[] ppi, long[] unc, long[] wn) {
        System.out.format("%-10s%10d%15d%15d%15d\n", type, pool, meanTime(ppi), meanTime(unc), meanTime(wn));
    }

    /*
     * Write array to file
     */
    public static void writeToFile(long array[], String filename) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(OUTPUT_DIR + filename);
            for (long time : array) {
                writer.write(time + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Duplicate an array.
     */
    public static <T> T[] cpArray(T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    /*
     * Perform a full cycle of benchmarks
     */
    public static <T extends Comparable<T>> void benchmark(BubbleSortPassPerItem<T> ppiSorter,
            BubbleSortUntilNoChange<T> uncSorter,
            BubbleSortWhileNeeded<T> wnSorter, Supplier<T[]> arrayGenerator, int pool, String type) {

        long[] ppiAttempts = new long[REPEAT_TIMES];
        long[] uncAttempts = new long[REPEAT_TIMES];
        long[] wnAttempts = new long[REPEAT_TIMES];

        for (int i = 0; i < REPEAT_TIMES; i++) {
            T[] array = arrayGenerator.get();
            ppiAttempts[i] = benchmarkSort(ppiSorter, cpArray(array));
            uncAttempts[i] = benchmarkSort(uncSorter, cpArray(array));
            wnAttempts[i] = benchmarkSort(wnSorter, cpArray(array));
        }

        writeToFile(ppiAttempts, type + "_ppi_" + pool + ".txt");
        writeToFile(uncAttempts, type + "_unc_" + pool + ".txt");
        writeToFile(wnAttempts, type + "_wn_" + pool + ".txt");

        printBenchResult(type, pool, ppiAttempts, uncAttempts, wnAttempts);
    }

    public static void main(String args[]) {
        /* Initialize sorters */
        BubbleSortPassPerItem<String> ppiSorterStr = new BubbleSortPassPerItem<String>();
        BubbleSortUntilNoChange<String> uncSorterStr = new BubbleSortUntilNoChange<String>();
        BubbleSortWhileNeeded<String> wnSorterStr = new BubbleSortWhileNeeded<String>();

        // BubbleSortPassPerItem<Double> ppiSorterDbl = new BubbleSortPassPerItem<Double>();
        // BubbleSortUntilNoChange<Double> uncSorterDbl = new BubbleSortUntilNoChange<Double>();
        // BubbleSortWhileNeeded<Double> wnSorterDbl = new BubbleSortWhileNeeded<Double>();

        BubbleSortPassPerItem<Integer> ppiSorterInt = new BubbleSortPassPerItem<Integer>();
        BubbleSortUntilNoChange<Integer> uncSorterInt = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> wnSorterInt = new BubbleSortWhileNeeded<Integer>();

        BubbleSortPassPerItem<Byte> ppiSorterByte = new BubbleSortPassPerItem<Byte>();
        BubbleSortUntilNoChange<Byte> uncSorterByte = new BubbleSortUntilNoChange<Byte>();
        BubbleSortWhileNeeded<Byte> wnSorterByte = new BubbleSortWhileNeeded<Byte>();

        printHeader();

        warmup(ppiSorterInt, genRandIntArray(750), WARMUP_CYCLES); // correct way of warming up here?

        /* Go through all test cases */
        for (int p : POOLS) {
            benchmark(ppiSorterInt, uncSorterInt, wnSorterInt, () -> genRandIntArray(p), p, "Integer~");
            benchmark(ppiSorterInt, uncSorterInt, wnSorterInt, () -> genAscIntArray(p), p, "Integer>");
            benchmark(ppiSorterInt, uncSorterInt, wnSorterInt, () -> genDescIntArray(p), p, "Integer<");

            benchmark(ppiSorterByte, uncSorterByte, wnSorterByte, () -> genRandByteArray(p), p, "Byte~");
            benchmark(ppiSorterByte, uncSorterByte, wnSorterByte, () -> genAscByteArray(p), p, "Byte>");
            benchmark(ppiSorterByte, uncSorterByte, wnSorterByte, () -> genDescByteArray(p), p, "Byte<");

            // benchmark(ppiSorterDbl, uncSorterDbl, wnSorterDbl, () -> genRandDoubleArray(p), p, "Double~");

            benchmark(ppiSorterStr, uncSorterStr, wnSorterStr, () -> genRandStrArray(p), p, "String~");
            benchmark(ppiSorterStr, uncSorterStr, wnSorterStr, () -> genAscStrArray(p), p, "String>");
            benchmark(ppiSorterStr, uncSorterStr, wnSorterStr, () -> genDescStrArray(p), p, "String<");
        }

        System.out.println("-".repeat(65));
    }
}
