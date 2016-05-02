import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

class IO {
    interface Reader {
        boolean isEmpty();
        char readChar();
        int readInt();
        String readString();
    }

    interface Writer {
        void writeInt(int n);
        void writeChar(char ch);
        void writeString(String s);
        void flush();
    }

    static final class BinaryStdInReader implements Reader {
        @Override
        public boolean isEmpty() {
            return BinaryStdIn.isEmpty();
        }

        @Override
        public char readChar() {
            return BinaryStdIn.readChar();
        }

        @Override
        public int readInt() {
            return BinaryStdIn.readInt();
        }

        @Override
        public String readString() {
            return BinaryStdIn.readString();
        }
    }

    static final class BinaryStdOutWriter implements Writer {
        @Override
        public void writeInt(int n) {
            BinaryStdOut.write(n);
        }

        @Override
        public void writeChar(char ch) {
            BinaryStdOut.write(ch);
        }

        @Override
        public void writeString(String s) {
            BinaryStdOut.write(s);
        }

        @Override
        public void flush() {
            BinaryStdOut.flush();
        }
    }
}
