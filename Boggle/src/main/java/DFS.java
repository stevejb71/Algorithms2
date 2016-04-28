import java.util.List;
import java.util.function.Predicate;

class DFS {
    void dfs(int cols, int rows, Graph graph, int startX, int startY, TrieST.Path tstIter, Predicate<Path> returnValue) {
        dfs(graph, new Path(cols, rows, graph.get(startX, startY), tstIter), returnValue);
    }

    @SuppressWarnings("Convert2streamapi")
    private void dfs(Graph graph, Path start, Predicate<Path> returnValue) {
        if (!returnValue.test(start)) {
            return;
        }
        final List<Cube> nexts = start.next(graph);
        for (Cube cube : nexts) {
            if (start.add(cube)) {
                dfs(graph, start, returnValue);
                start.retreat();
            }
        }
    }
}
