@SuppressWarnings("WeakerAccess")
class BurrowsWheelerImpl {
    static void encode(IO.Reader reader, IO.Writer writer) {
        final String message = reader.readString();
        final CircularSuffixArray circle = new CircularSuffixArray(message);
        final int rowOfOriginal = rowOfOriginal(circle);
        final String lastColumn = lastColumn(circle, message);
        writer.writeInt(rowOfOriginal);
        writer.writeString(lastColumn);
        writer.flush();
    }

    private static int rowOfOriginal(CircularSuffixArray circle) {
        for(int i = 0; i < circle.length(); ++i) {
            if(circle.index(i) == 0) {
                return i;
            }
        }
        throw new AssertionError("expected to be unreachable code - original not found");
    }

    private static String lastColumn(CircularSuffixArray circle, String message) {
        final int length = message.length();
        final char[] col = new char[length];
        for(int i = 0; i < circle.length(); ++i) {
            final int index = (circle.index(i) + length - 1) % length;
            col[i] = message.charAt(index);
        }
        return new String(col);
    }

    static void decode(IO.Reader reader, IO.Writer writer) {
        final int first = reader.readInt();
        final String lastColumn = reader.readString();
        final char[] chars = new char[lastColumn.length()];
        final int[] nexts = new int[lastColumn.length()];
        keyIndexCountArrays(lastColumn, chars, nexts);
        int ptr = first;
        for(int i = 0; i < lastColumn.length(); ++i) {
            writer.writeChar(chars[ptr]);
            ptr = nexts[ptr];
        }
        writer.flush();
    }

    private static void keyIndexCountArrays(String lastColumn, char[] chars, int[] nexts) {
        final int N = lastColumn.length();
        final int[] count = new int[257];
        for (int i = 0; i < N; i++) {
            count[lastColumn.charAt(i) + 1]++;
        }
        for (int r = 0; r < 256; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < N; i++) {
            final int index = count[lastColumn.charAt(i)];
            chars[index] = lastColumn.charAt(i);
            nexts[index] = i;
            count[lastColumn.charAt(i)]++;
        }
    }
}