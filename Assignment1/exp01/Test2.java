package exp01;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class Test2 {
    private static int REPEAT_TIMES = 100; // Number of repetitions for a multiple benchmark test
    private static int WARMUP_CYCLES = 50; // Cycles when warming up;
    private static String OUTPUT_DIR = "./results/";
    private static int POOL_SIZE = 5; // Size of arrays to test
    private static int[] POOLS = { 100, 1000, 5000 }; // sizes of arrays to test

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
     * Generate a Double array in ascending order.
     */
    private static Double[] genAscDoubleArray(int size) {
        Double[] array = new Double[size];
        for (int i = 0; i < size; i++) {
            array[i] = (double) i;
        }
        return array;
    }

    /*
     * Generate a Double array in descending order.
     */
    private static Double[] genDescDoubleArray(int size) {
        Double[] array = new Double[size];
        for (int i = 0; i < size; i++) {
            array[i] = (double) (size - i);
        }
        return array;
    }
    
    private static Integer[] genRandIntArray(int size) {
        Integer[] array = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); // 0-99
        }
        return array;
    }

    /*
     * Generate an Integer array in ascending order.
     */
    private static Integer[] genAscIntArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    /*
     * Generate an Integer array in descending order.
     */
    private static Integer[] genDescIntArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    private static Byte[] genRandByteArray(int size) {
        Byte[] array = new Byte[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = (byte) (random.nextInt(256) - 128);
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
     * Repeat the same
     */
    private static <T extends Comparable<T>> void repeatBenchmark(Sorter<T> sorter, T[] array, int times) {
        /* Warmup */
        for (int i = 0; i < WARMUP_CYCLES; i++) {
            T[] burnerArray = Arrays.copyOf(array, array.length);
            benchmarkSort(sorter, burnerArray);
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
    public static <T extends Comparable<T>> void benchmark(long[] ppiAttempts, long[]uncAttempts, long[] wnAttempts  ,String type, int pool) {
        writeToFile(ppiAttempts, type + "_ppi_" + pool + ".txt");
        writeToFile(uncAttempts, type + "_unc_" + pool + ".txt");
        writeToFile(wnAttempts, type + "_wn_" + pool + ".txt");

        /* Print mean benchmarks */
        printBenchResult(type, pool, ppiAttempts, uncAttempts, wnAttempts);
    }

    /*
     * Prints on terminal the benchmark header
     */
    private static void printHeader() {
        System.out.println("\nRUNNING TEST ON ARRAYS OF VARYING SIZES");
        System.out.printf("Each test is performed %d times\n", REPEAT_TIMES);
        System.out.println("-".repeat(65));
        System.out.format("%-10s%10s%15s%15s%15s\n", "Type", "Size", "PassPerItem", "UntilNoChange", "WhileNeeded");
        System.out.println("-".repeat(65));
    }

    /*
     * Prints on terminal a table row of results
     */
    private static void printBenchResult(String type, int pool, long[] ppi, long[] unc, long[] wn) {
        System.out.format("%-10s%10d%15d%15d%15d\n", type, pool, meanTime(ppi), meanTime(unc), meanTime(wn));
    }

    public static void main(String args[]) {
        printHeader();
        /* Initialize sorters */
        BubbleSortPassPerItem<Double> ppiSorterDouble = new BubbleSortPassPerItem<Double>();
        BubbleSortUntilNoChange<Double> uncSorterDouble = new BubbleSortUntilNoChange<Double>();
        BubbleSortWhileNeeded<Double> wnSorterDouble = new BubbleSortWhileNeeded<Double>();
    
        BubbleSortPassPerItem<Integer> ppiSorterInt = new BubbleSortPassPerItem<Integer>();
        BubbleSortUntilNoChange<Integer> uncSorterInt = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> wnSorterInt = new BubbleSortWhileNeeded<Integer>();
    
        BubbleSortPassPerItem<Byte> ppiSorterByte = new BubbleSortPassPerItem<Byte>();
        BubbleSortUntilNoChange<Byte> uncSorterByte = new BubbleSortUntilNoChange<Byte>();
        BubbleSortWhileNeeded<Byte> wnSorterByte = new BubbleSortWhileNeeded<Byte>();
    
        Double[] warmupArray = genAscDoubleArray(POOL_SIZE);
        repeatBenchmark(ppiSorterDouble, warmupArray, WARMUP_CYCLES);
        repeatBenchmark(uncSorterDouble, warmupArray, WARMUP_CYCLES);
        repeatBenchmark(wnSorterDouble, warmupArray, WARMUP_CYCLES);
    
        for (int i = 0; i < 3; i++) {
            
            long[] ppi_attDouble = new long[REPEAT_TIMES];
            long[] unc_attDouble = new long[REPEAT_TIMES];
            long[] wn_attDouble = new long[REPEAT_TIMES];
            long[] ppi_attInt = new long[REPEAT_TIMES];
            long[] unc_attInt = new long[REPEAT_TIMES];
            long[] wn_attInt = new long[REPEAT_TIMES];
            long[] ppi_attByte = new long[REPEAT_TIMES];
            long[] unc_attByte = new long[REPEAT_TIMES];
            long[] wn_attByte = new long[REPEAT_TIMES];
            for (int j = 0; j < REPEAT_TIMES; j++) {
                Double[] doubleArrayRandom = genRandDoubleArray(POOLS[i]);
                
                Integer[] intArrayRandom = genRandIntArray(POOLS[i]);
                Byte[] byteArrayRandom = genRandByteArray(POOLS[i]);
                ppi_attDouble[j] = benchmarkSort(ppiSorterDouble, doubleArrayRandom);
                unc_attDouble[j] = benchmarkSort(uncSorterDouble, doubleArrayRandom);
                wn_attDouble[j] = benchmarkSort(wnSorterDouble, doubleArrayRandom);
    
                ppi_attInt[j] = benchmarkSort(ppiSorterInt, intArrayRandom);
                unc_attInt[j] = benchmarkSort(uncSorterInt, intArrayRandom);
                wn_attInt[j] = benchmarkSort(wnSorterInt, intArrayRandom);
    
                ppi_attByte[j] = benchmarkSort(ppiSorterByte, byteArrayRandom);
                unc_attByte[j] = benchmarkSort(uncSorterByte, byteArrayRandom);
                wn_attByte[j] = benchmarkSort(wnSorterByte, byteArrayRandom);
            }
            benchmark(ppi_attByte, unc_attByte, wn_attByte, "Byte", POOLS[i]);
            benchmark(ppi_attByte, unc_attInt, wn_attByte, "Integer", POOLS[i]);
            benchmark(ppi_attByte, unc_attDouble, wn_attByte, "Double", POOLS[i]);




    
        }
    
        System.out.println("-".repeat(65));
    }
    
}
