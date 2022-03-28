package utils;

public class MinPQ<Key extends Comparable<Key>> {
    private Key[] pq;
    private int n;

    @SuppressWarnings("unchecked")
    public MinPQ(int capacity) {
        pq = (Key[]) new Comparable[capacity + 1];
        n = 0;
    }

    public MinPQ() {
        this(1);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public void insert(Key key) {
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }

        n++;
        pq[n] = key;
        swim(n);
    }

    public Key delMin() {
        Key min = pq[1];
        swap(1, n);
        n--;

        sink(1);
        pq[n+1] = null;

        if ((n > 0) && (n == (pq.length - 1) / 4)) {
            resize(pq.length / 2);
        }

        return min;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Key[] temp = (Key[]) new Comparable[capacity];

        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }

        pq = temp;
    }



    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            swap(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;

            if (j < n && greater(j, j+1)) {
                j++;
            }

            if (!greater(k, j)) {
                break;
            }
            swap(k, j);

            k = j;
        }
    }

    private boolean greater(int i, int j) {
        return pq[i].compareTo(pq[j]) > 0;
    }

    private void swap(int i, int j) {
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }
}