package exp01;

import java.util.Arrays;
import java.util.Random;

public class Test {

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

    public static void main(String args[]) {
        Integer[] baseArray = generateRandomArray(100);

        BubbleSortPassPerItem<Integer> ppiSorter = new BubbleSortPassPerItem<Integer>();
        long ppiTime = benchmarkSort(ppiSorter, Arrays.copyOf(baseArray, baseArray.length));

        BubbleSortUntilNoChange<Integer> uncSorter = new BubbleSortUntilNoChange<Integer>();
        long uncTime = benchmarkSort(uncSorter, Arrays.copyOf(baseArray, baseArray.length));

        BubbleSortWhileNeeded<Integer> wnSorter = new BubbleSortWhileNeeded<Integer>();
        long wnTime = benchmarkSort(wnSorter, Arrays.copyOf(baseArray, baseArray.length));

        System.out.println("PassPerItem\t" + ppiTime + "ns");
        System.out.println("UntilNoChange\t" + uncTime + "ns");
        System.out.println("WhileNeeded\t" + wnTime + "ns");
    }
}
