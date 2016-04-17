import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Graph {
    private static final class Position {
        final int x, y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            Position position = (Position) o;

            return x == position.x && y == position.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
    private final Map<Position, List<Cube>> adjacencyList;
    private final Map<Position, Cube> cubesByPosition = new HashMap<>();

    Graph(BoggleBoard board) {
        adjacencyList = new HashMap<>(board.cols() * board.rows());
        final Cube[][] cubes = new Cube[board.cols()][board.rows()];
        for(int x = 0; x < board.cols(); ++x) {
            for(int y = 0; y < board.rows(); ++y) {
                final Cube cube = new Cube(x, y, board.getLetter(y, x));
                cubes[x][y] = cube;
                final Position pos = new Position(x, y);
                adjacencyList.put(pos, new ArrayList<>(8));
                cubesByPosition.put(pos, cube);
            }
        }
        for(int x = 0; x < board.cols(); ++x) {
            for(int y = 0; y < board.rows(); ++y) {
                for(int dx = -1; dx <= 1; ++dx) {
                    for(int dy = -1; dy <= 1; ++dy) {
                        if(dy == 0 && dx == 0) continue;
                        final int x1 = x + dx;
                        final int y1 = y + dy;
                        if(x1 >= 0 && x1 < board.cols() && y1 >= 0 && y1 < board.rows()) {
                            final Cube cubeAt = cubes[x][y];
                            adjacencyList.get(new Position(cubeAt.x, cubeAt.y)).add(cubes[x1][y1]);
                        }
                    }
                }
            }
        }
    }

    Cube get(int x, int y) {
        return cubesByPosition.get(new Position(x, y));
    }

    List<Cube> neighbours(Cube cube) {
        return adjacencyList.get(new Position(cube.x, cube.y));
    }
}
