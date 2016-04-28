/******************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *
 *  A string symbol table for extended ASCII strings, implemented
 *  using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt 
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;

/**
 *  The <tt>TrieST</tt> class represents an symbol table of key-value
 *  pairs, with string keys and generic values.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides character-based methods for finding the string
 *  in the symbol table that is the <em>longest prefix</em> of a given prefix,
 *  finding all strings in the symbol table that <em>start with</em> a given prefix,
 *  and finding all strings in the symbol table that <em>match</em> a given pattern.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be <tt>null</tt>&mdash;setting the
 *  value associated with a key to <tt>null</tt> is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a 256-way trie.
 *  The <em>put</em>, <em>contains</em>, <em>delete</em>, and
 *  <em>longest prefix</em> operations take time proportional to the length
 *  of the key (in the worst case). Construction takes constant time.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
@SuppressWarnings("WeakerAccess")
public class TrieST {
    private static final int R = 26;

    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private String val;
        private Node[] next = new Node[R];
    }

    public final class Path {
        @SuppressWarnings("unchecked")
        private final Node[] trail = new Node[1000];
        private int trailPtr;

        public Path() {
            trailPtr = -1;
        }

        public Node move(char ch) {
            final Node start = trailPtr == -1 ? root : trail[trailPtr];
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

    public boolean contains(String key) {
        return get(key) != null;
    }

    public String get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d+1);
    }


    private Node getNext(Node x, char c) {
        return x == null ? null : x.next[c - 65];
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, String val) {
         root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, String val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) N++;
            x.val = val;
            return x;
        }
        char c = (char) (key.charAt(d) - 65);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }


}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
