package exp01;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Test {
    private static int REPEAT_TIMES = 100; // Number of repetitions for a multiple benchmark test
    private static int WARMAP_CYCLES = 50; // Cycles when warming up;
    private static String OUTPUT_DIR = "./results/";

    /*
     * Generate a random Integer array of given size.
     */
    private static Integer[] generateRandomIntegerArray(int size) {
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

    public static <T> T[] cpArray(T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    /*
     * Perform a full cycle of benchmarks
     */
    public static <T extends Comparable<T>> void benchmark(BubbleSortPassPerItem<T> ppiSorter,
            BubbleSortUntilNoChange<T> uncSorter,
            BubbleSortWhileNeeded<T> wnSorter, T[] array, String type) {
        int pool = array.length;
        long[] ppiAttempts = repeatBenchmark(ppiSorter, cpArray(array), REPEAT_TIMES);
        writeToFile(ppiAttempts, type + "_ppi_" + pool + ".txt");
        long[] uncAttempts = repeatBenchmark(uncSorter, cpArray(array), REPEAT_TIMES);
        writeToFile(uncAttempts, type + "_unc_" + pool + ".txt");
        long[] wnAttempts = repeatBenchmark(wnSorter, cpArray(array), REPEAT_TIMES);
        writeToFile(wnAttempts, type + "_wn_" + pool + ".txt");

        /* Print mean benchmarks */
        printBenchResult(type, pool, ppiAttempts, uncAttempts, wnAttempts);
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
        printHeader();

        /* Go through all test cases */
        for (int p : pools) {
            /* Integer */
            benchmark(ppiSorterInt, uncSorterInt, wnSorterInt, generateRandomIntegerArray(p), "Integer");
            /* String */
            benchmark(ppiSorterStr, uncSorterStr, wnSorterStr, generateRandomStringArray(p), "String");
            /* Byte */
            benchmark(ppiSorterByte, uncSorterByte, wnSorterByte, generateRandomByteArray(p), "Byte");
        }

        System.out.println("-".repeat(65));
    }
}
