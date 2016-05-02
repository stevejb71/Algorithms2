@SuppressWarnings("WeakerAccess")
public class BurrowsWheeler {
    public static void encode() {
        BurrowsWheelerImpl.encode(new IO.BinaryStdInReader(), new IO.BinaryStdOutWriter());
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        BurrowsWheelerImpl.decode(new IO.BinaryStdInReader(), new IO.BinaryStdOutWriter());
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        switch(args[0].trim()) {
            case "-":
                encode();
                break;
            case "+":
                decode();
                break;
            default:
                throw new IllegalArgumentException(args[0]);
        }
    }
}