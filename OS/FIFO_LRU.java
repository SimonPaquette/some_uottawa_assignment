/**
 * Simon Paquette
 * 300044038
 * CSI 3531
 * Devoir 4
 * Question 2
 * 
 * javac Q2_300044038.java
 * java Q2_300044038
 */

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;


public class Q2_300044038 {

    //print the elements in the frames
    final static boolean DEBUG = false;

    public static void main(String[] args) {

        //Variable for the testing
        final int TOTAL_PAGES = 50; 
        final int MAX_REF = 10; //10
        final int MIN_FRAMES = 1; //1
        final int MAX_FRAMES = 7; //7
        
        //build the page-ref string
        int[] pages = new int[TOTAL_PAGES];
        pages[0] = (int) (MAX_REF*Math.random());
        String str = String.valueOf(pages[0]);
        for (int i = 1; i < TOTAL_PAGES; i++) {
            int rand = (int) (MAX_REF*Math.random());
            //remove any 2 same pages one after another
            if (pages[i-1] == rand) {
                rand = (rand+1)%MAX_REF;
            }
            pages[i] = rand;
            str+=","+rand;
        }
        if (DEBUG) {
            System.out.println("pages: "+str);
        }
        
        //run the programm for different number of frames
        for (int i = MIN_FRAMES; i <= MAX_FRAMES; i++) {
            FIFO fifo = new FIFO(i);
            LRU lru = new LRU(i);
            int fault;
            
            fault = fifo.replaceP(pages);
            System.out.println("FIFO with "+i+" frames produces "+
                                fault+" faults on "+TOTAL_PAGES+
                                " pages with "+MAX_REF+" differente references");
            fault = lru.replaceP(pages);
            System.out.println("LRU  with "+i+" frames produces "+
                                fault+" faults on "+TOTAL_PAGES+
                                " pages with "+MAX_REF+" differente references");

        }
    }
}

class FIFO {

    private Queue<Integer> fifo = new LinkedList<>();
    final private int FRAMES;

    public FIFO(int frames) {
        this.FRAMES = frames;
    }

    //page-replacment algo
    public int replaceP(int[] pages) {

        int fault = 0;
        for (int i = 0; i < pages.length; i++) {
            
            //initial build up
            if (fifo.size() < FRAMES && !fifo.contains(pages[i])) {
                fifo.add(pages[i]);
                fault++;
            }
            //need to replace the new page with the victim
            else if (!fifo.contains(pages[i])) {
                fifo.remove();
                fifo.add(pages[i]);
                fault++;
            }

            if (Q2_300044038.DEBUG) {
                printFIFO();
            }  
        }
        return fault;
    }

    //print elements
    private void printFIFO() {
        String str = "";
        for (int elem : fifo) {
            str += String.valueOf(elem) + " ";
        }
        System.out.println(str);
    }
}

class LRU {
    
    private HashMap<Integer, Integer> lru = new HashMap<Integer, Integer>();
    final private int FRAMES;

    public LRU(int frames) {
        this.FRAMES = frames;
    }

    //page-replacment algo
    public int replaceP(int[] pages) {

        int fault = 0;
        for (int i = 0; i < pages.length; i++) {

            //initial build up
            if (lru.size() < FRAMES && !lru.containsKey(pages[i])) {
                lru.put(pages[i], 0);
                fault++;
            }
            //need to replace the new page with the victim
            else if (!lru.containsKey(pages[i])) {
                int rep = getReplace();
                lru.remove(rep);
                lru.put(pages[i], 0);
                fault++;
            }
            //update counter for each page
            updateLRU(pages[i]);

            if (Q2_300044038.DEBUG) {
                printLRU();
            }
        }
        return fault;
    }

    //update counter for each page and reset to 0 the "except"
    private void updateLRU(int except) {
        for (int key : lru.keySet()) {
            if (key != except) {
                lru.replace(key, lru.get(key) + 1);
            }
        }
        lru.replace(except, 0);
    }

    //find the page least recently used
    private int getReplace() {
        int used = Integer.MIN_VALUE;   //value for least used
        int rep = -1;                   //page to replace
        for (int key : lru.keySet()) {
            if (lru.get(key) > used) {
                used = lru.get(key);
                rep = key;
            }
        }
        return rep;
    }

    //print elements
    private void printLRU() {
        String str = "";
        for (int elem : lru.keySet()) {
            str += String.valueOf(elem) + "-"+ String.valueOf(lru.get(elem)) +" ";
        }
        System.out.println(str);
    }
}