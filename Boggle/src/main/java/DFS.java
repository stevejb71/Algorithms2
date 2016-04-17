import java.util.List;
import java.util.function.Predicate;

class DFS {
    void dfs(Graph graph, int startX, int startY, Predicate<Path> returnValue) {
        dfs(graph, new Path(graph.get(startX, startY)), returnValue);
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
