public class CircularSuffixArray {
    private final int[] indices;

    public CircularSuffixArray(String s) {
        this.indices = new int[s.length()];
        for(int i = 0; i < indices.length; ++i) {
            indices[i] = i;
        }
        sort(s, 0, s.length() - 1, 0);
    }

    public int length() {
        return indices.length;
    }

    public int index(int i) {
        if(i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("Index " + i);
        }
        return indices[i];
    }

    private void swap(int i, int j) {
        int tmp = indices[i];
        indices[i] = indices[j];
        indices[j] = tmp;
    }

    private void sort(String s, int lo, int hi, int d) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + 5) {
            insertion(s, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = d == indices.length ? -1 : s.charAt((indices[lo] + d) % length());
        int i = lo + 1;
        while (i <= gt) {
            if(v == -1) {
                i++;
            } else {
                int t = s.charAt((indices[i] + d) % length());
                if (t < v) {
                    int i1 = lt++;
                    int j = i++;
                    swap(i1, j);
                } else if (t > v) {
                    int j = gt--;
                    swap(i, j);
                } else {
                    i++;
                }
            }
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(s, lo, lt - 1, d);
        if (v >= 0) sort(s, lt, gt, d + 1);
        sort(s, gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(String s, int lo, int hi, int d) {
        if(d == s.length()) {
            return;
        }
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(s, j, j - 1, d); j--) {
                swap(j, j - 1);
            }
        }
    }

    private boolean less(String s, int v, int w, int d) {
        final int length = indices.length;
        for (int i = d; i < length; i++) {
            final char vChar = s.charAt((indices[v] + i) % length);
            final char wChar = s.charAt((indices[w] + i) % length);
            if (vChar < wChar) return true;
            if (vChar > wChar) return false;
        }
        return false;
    }
}
