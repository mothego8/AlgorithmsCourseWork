package edu.yu.da;

import java.util.Iterator;

//from sedgewick
public class IndexMinPQ <Key extends Comparable<Key>> implements Iterable<Integer> {
    private int n;
    private int[] pq;
    private int[] qp;
    private Key[] keys;
    public IndexMinPQ(int maxN){
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1;
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(int i) {
        return qp[i] != -1;
    }

    public void insert(int i, Key key) {
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }
    public void decreaseKey(int i, Key key) {
        keys[i] = key;
        swim(qp[i]);
    }
    public int delMin() {
        int min = pq[1];
        exch(1, n--);
        sink(1);
        assert min == pq[n+1];
        //qp[min] = -1;
//        keys[min] = null;
//        pq[n+1] = -1;
        return min;
    }
    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return null;
    }
}
