import java.util.Random;

import exp01.BubbleSortPassPerItem;

public class Test {
    public static void main(String args[]) {
        Random random = new Random();
        int dimension = 100;
        Integer[] list = new Integer[dimension];
        
        for (int i = 0; i < dimension; i++) {
            list[i] = random.nextInt(100) + 1;
            System.out.println(list[i]);
            
        }
        System.out.println("---------------------");
        long firstTime = System.nanoTime();
        BubbleSortPassPerItem<Integer> sorter = new BubbleSortPassPerItem<Integer>();
        sorter.sort(list);
        for (int i : list) {
            System.out.println(i);
        }
        long finalTime = System.nanoTime();
        System.out.println(finalTime - firstTime);
    }
}
