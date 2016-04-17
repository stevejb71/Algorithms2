import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Path {
    private final ArrayDeque<Cube> path;
    private final Set<Cube> marked; // mutable bool array

    Path(Cube start) {
        this(new ArrayDeque<>(), new HashSet<>());
        add(start);
    }

    private Path(ArrayDeque<Cube> path, Set<Cube> marked) {
        this.path = path;
        this.marked = marked;
    }

    Path copy() {
        return new Path(new ArrayDeque<>(this.path), new HashSet<>(this.marked));
    }

    boolean add(Cube cube) {
        if(marked.contains(cube)) {
            return false;
        }
        path.addLast(cube);
        marked.add(cube);
        return true;
    }

    Cube retreat() {
        final Cube cube = path.removeLast();
        marked.remove(cube);
        return cube;
    }

    List<Cube> next(Graph graph) {
        return graph.neighbours(path.getLast()).stream()
                .filter(graphNeighbour -> !marked.contains(graphNeighbour))
                .collect(Collectors.toList());
    }

    String s() {
        String s = "";
        for (Cube cube : path) {
            s += cube.letter;
        }
        return s;
    }

    public String toString() {
        return String.format("%s:[%s]", s(), String.join(",", path.stream().map(Object::toString).collect(Collectors.toList())));
    }
}
