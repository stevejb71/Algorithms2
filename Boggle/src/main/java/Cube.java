@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
class Cube {
    final int x;
    final int y;
    final char letter;

    Cube(int x, int y, char letter) {
        this.x = x;
        this.y = y;
        this.letter = letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Cube cube = (Cube) o;

        return x == cube.x && y == cube.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s@(%d,%d)", letter, x, y);
    }
}
