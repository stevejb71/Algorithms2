import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("WeakerAccess")
public class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        this.graph = new Digraph(graph);
        throwIfNull(graph);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Pair pathInfo = getPathInfo(v, w);
        return pathInfo == null ? -1 : pathInfo.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Pair pathInfo = getPathInfo(v, w);
        return pathInfo == null ? -1 : pathInfo.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        throwIfNull(v, w);
        Pair pathInfo = getPathInfo(v, w);
        return pathInfo == null ? -1 : pathInfo.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        throwIfNull(v, w);
        Pair pathInfo = getPathInfo(v, w);
        return pathInfo == null ? -1 : pathInfo.ancestor;
    }

    private Pair getPathInfo(Iterable<Integer> v, Iterable<Integer> w) {
        final BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
        final BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); ++i) {
            final int vLength = vPaths.distTo(i);
            final int wLength = wPaths.distTo(i);
            if (vLength != Integer.MAX_VALUE && wLength != Integer.MAX_VALUE) {
                final int totalLength = vLength + wLength;
                if (totalLength < minLength) {
                    minLength = totalLength;
                    ancestor = i;
                }
            }
        }
        return minLength == Integer.MAX_VALUE ? null : new Pair(ancestor, minLength);
    }

    private Pair getPathInfo(int v, int w) {
        return getPathInfo(Collections.singleton(v), Collections.singleton(w));
    }

    private static class Pair {
        private final int ancestor;
        private final int length;

        Pair(int ancestor, int length) {
            this.ancestor = ancestor;
            this.length = length;
        }
    }

    private void throwIfNull(Object... values) {
        Arrays.asList(values).stream().filter(x -> x == null).forEach(x -> {
            throw new NullPointerException();
        });
    }
}