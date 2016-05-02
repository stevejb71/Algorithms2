import java.util.LinkedList;

@SuppressWarnings("WeakerAccess")
class MoveToFrontImpl {
    static void encode(IO.Reader reader, IO.Writer writer) {
        final LinkedList<Character> chars = newChars();
        while(!reader.isEmpty()) {
            final char messageChar = reader.readChar();
            final int index = chars.indexOf(messageChar);
            writer.writeChar((char)index);
            chars.remove(index);
            chars.addFirst(messageChar);
        }
        writer.flush();
    }

    static void decode(IO.Reader reader, IO.Writer writer) {
        final LinkedList<Character> chars = newChars();
        while(!reader.isEmpty()) {
            final int index = reader.readChar();
            final char messageChar = chars.get(index);
            writer.writeChar(messageChar);
            chars.remove(index);
            chars.addFirst(messageChar);
        }
        writer.flush();
    }

    private static LinkedList<Character> newChars() {
        final LinkedList<Character> chars = new LinkedList<>();
        for(char c = 0; c < 256; ++c) {
            chars.add(c);
        }
        return chars;
    }
}