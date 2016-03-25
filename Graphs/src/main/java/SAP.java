import edu.princeton.cs.algs4.Digraph;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class SAP {
    private final Digraph graph;
    private int[] modifiedV;
    private int modifiedVPtr;
    private int[] modifiedW;
    private int modifiedWPtr;
    private final int[] distToFromV;
    private final int[] distToFromW;
    private final HashMap<CacheKey, Pair> cachedResults = new HashMap<>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        this.graph = new Digraph(graph);
        this.distToFromV = newTrackingArray();
        this.distToFromW = newTrackingArray();
        this.modifiedV = new int[graph.V()];
        this.modifiedW = new int[graph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        final int length = resultFor(v, w).length;
        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        final int ancestor = resultFor(v, w).ancestor;
        return ancestor == Integer.MAX_VALUE ? -1 : ancestor;
    }

    private Pair resultFor(int v, int w) {
        final CacheKey cacheKey = new CacheKey(v, w);
        final Pair cachedResult = cachedResults.get(cacheKey);
        if(cachedResult == null) {
            Pair pathInfo = fasterSearch(v, w);
            cachedResults.put(cacheKey, pathInfo);
            return pathInfo;
        } else {
            return cachedResult;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Pair pathInfo = fasterSearch(v, w);
        return pathInfo.length == Integer.MAX_VALUE ? -1 : pathInfo.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Pair pathInfo = fasterSearch(v, w);
        return pathInfo.ancestor == Integer.MAX_VALUE ? -1 : pathInfo.ancestor;
    }

    private Pair fasterSearch(int v, int w) {
        if(v == w) {
            return new Pair(v, 0);
        }
        reset();
        distToFromV[v] = 0;
        distToFromW[w] = 0;
        modifiedV[modifiedVPtr++] = v;
        modifiedW[modifiedWPtr++] = w;
        final ArrayDeque<IntAndArray> queue = new ArrayDeque<>(1024);
        queue.add(new IntAndArray(v, distToFromV, distToFromW, modifiedV));
        queue.add(new IntAndArray(w, distToFromW, distToFromV, modifiedW));

        return dualBfs(queue);
    }

    private boolean contains(int[] xs, int limit, int x) {
        for(int i = 0; i < limit; ++i) {
            if(xs[i] == x) {
                return true;
            }
        }
        return false;
    }

    private Pair fasterSearch(Iterable<Integer> v, Iterable<Integer> w) {
        reset();
        for (int node : v) {
            distToFromV[node] = 0;
            modifiedV[modifiedVPtr++] = node;
        }
        for (int node : w) {
            distToFromW[node] = 0;
            modifiedW[modifiedWPtr++] = node;
            if(contains(modifiedV, modifiedVPtr, node)) {
                return new Pair(node, 0);
            }
        }
        final ArrayDeque<IntAndArray> queue = new ArrayDeque<>(1024);
        final Iterator<Integer> vIter = v.iterator();
        final Iterator<Integer> wIter = w.iterator();
        while(vIter.hasNext() && wIter.hasNext()) {
            queue.add(new IntAndArray(vIter.next(), distToFromV, distToFromW, modifiedV));
            queue.add(new IntAndArray(wIter.next(), distToFromW, distToFromV, modifiedW));
        }
        while(vIter.hasNext()) {
            queue.add(new IntAndArray(vIter.next(), distToFromV, distToFromW, modifiedV));
        }
        while(wIter.hasNext()) {
            queue.add(new IntAndArray(wIter.next(), distToFromW, distToFromV, modifiedW));
        }

        return dualBfs(queue);
    }

    private Pair dualBfs(ArrayDeque<IntAndArray> queue) {
        Pair result = Pair.MAX;
        while(!queue.isEmpty()) {
            IntAndArray current = queue.removeLast();
            for (int next : graph.adj(current.id)) {
                if(current.dist[next] != Integer.MAX_VALUE) {
                    continue;
                }
                final int distance = 1 + current.dist();
                if(current.otherDist[next] != Integer.MAX_VALUE) {
                    final int commonAncestorDistance = distance + current.otherDist[next];
                    if(commonAncestorDistance < result.length) {
                        result = new Pair(next, commonAncestorDistance);
                        if(distance >= commonAncestorDistance) {
                            return result;
                        }
                    }
                }
                current.dist[next] = distance;
                if(current.tracker == modifiedV) {
                    modifiedV[modifiedVPtr++] = next;
                } else {
                    modifiedW[modifiedWPtr++] = next;
                }
                queue.addFirst(current.withId(next));
            }
        }
        return result;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private void reset() {
        for(int i = 0; i < modifiedVPtr; ++i) {
            distToFromV[modifiedV[i]] = Integer.MAX_VALUE;
        }
        for(int i = 0; i < modifiedWPtr; ++i) {
            distToFromW[modifiedW[i]] = Integer.MAX_VALUE;
        }
        modifiedVPtr = 0;
        modifiedWPtr = 0;
    }

    private int[] newTrackingArray() {
        final int[] distToFromV = new int[graph.V()];
        Arrays.fill(distToFromV, Integer.MAX_VALUE);
        return distToFromV;
    }

    private static final class Pair {
        private final int ancestor;
        private final int length;

        Pair(int ancestor, int length) {
            this.ancestor = ancestor;
            this.length = length;
        }

        static final Pair MAX = new Pair(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    private static final class CacheKey {
        private final int v;
        private final int w;

        CacheKey(int v, int w) {
            this.v = v;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            CacheKey cacheKey = (CacheKey) o;

            return v == cacheKey.v && w == cacheKey.w;

        }

        @Override
        public int hashCode() {
            return 31 * v + w;
        }
    }

    @SuppressWarnings("ImplicitArrayToString")
    private static class IntAndArray {
        private final int id;
        private final int[] dist;
        private final int[] otherDist;
        private final int[] tracker;

        public IntAndArray(int id, int[] dist, int[] otherDist, int[] tracker) {
            this.id = id;
            this.dist = dist;
            this.otherDist = otherDist;
            this.tracker = tracker;
        }

        public IntAndArray withId(int newId) {
            return new IntAndArray(newId, dist, otherDist, tracker);
        }

        public int dist() {
            return dist[id];
        }

        @Override
        public String toString() {
            return "IntAndArray{" +
                    "id=" + id +
                    ", dist=" + dist +
                    ", otherDist=" + otherDist +
                    '}';
        }
    }
}