import edu.princeton.cs.algs4.Queue;

@SuppressWarnings("WeakerAccess")
public class TST {
    private Node root;   // root of TST

    private static class Node {
        private char c;                        // character
        private Node left, mid, right;  // left, middle, and right subtries
        private String val;                     // value associated with string

        @Override
        public String toString() {
            return String.format("Node(%s, val=%s)", c, val);
        }
    }

    public final class Path {
        @SuppressWarnings("unchecked")
        private final Node[] trail = new Node[1000];
        private int trailPtr;

        public Path() {
            trailPtr = -1;
        }

        public Node move(char ch) {
            final Node start = trailPtr == -1 ? root : trail[trailPtr].mid;
            final Node next = getNext(start, ch);
            ++trailPtr;
            trail[trailPtr] = next;
            return next;
        }

        public Node retreat() {
            trailPtr--;
            return trail[trailPtr];
        }

        public Node node() {
            return trail[trailPtr];
        }

        public String nodeVal() {
            return node().val;
        }
    }

    public Path path() {
        return new Path();
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }


    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     * <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public String get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    // return subtrie corresponding to given key
    public Node getNext(Node x, char c) {
        if (x == null) return null;
        if (c < x.c) return getNext(x.left, c);
        else if (c > x.c) return getNext(x.right, c);
        else return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, String val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, String val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, key, val, d);
        else if (c > x.c) x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1) x.mid = put(x.mid, key, val, d + 1);
        else x.val = val;
        return x;
    }
}