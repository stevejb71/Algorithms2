import java.util.List;
import java.util.stream.Collectors;

class Path {
    private final Cube[] path = new Cube[1000];
    private int cubePtr = 0;
    private final boolean[] marked;
    private final TrieST.Path tstIter;
    private final int cols;

    Path(int cols, int rows, Cube start, TrieST.Path tstIter) {
        this(cols, rows, tstIter);
        add(start);
    }

    private Path(int cols, int rows, TrieST.Path tstIter) {
        this.marked = new boolean[cols * rows];
        this.tstIter = tstIter;
        this.cols = cols;
    }

    boolean add(Cube cube) {
        final int index = cube.x + cols * cube.y;
        if(marked[index]) {
            return false;
        }
        path[cubePtr++] = cube;
        marked[index] = true;
        tstIter.move(cube.letter);
        return true;
    }

    Cube retreat() {
        cubePtr--;
        final Cube cube = path[cubePtr];
        final int index = cube.x + cols * cube.y;
        marked[index] = false;
        tstIter.retreat();
        return cube;
    }

    List<Cube> next(Graph graph) {
        return graph.neighbours(path[cubePtr - 1]).stream()
                .filter(graphNeighbour -> !marked[graphNeighbour.x + cols * graphNeighbour.y])
                .collect(Collectors.toList());
    }

    boolean isPrefixOfWord() {
        return tstIter.node() != null;
    }

    String word() {
        return tstIter.nodeVal();
    }
}
