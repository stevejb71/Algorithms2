@SuppressWarnings("WeakerAccess")
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        MoveToFrontImpl.encode(new IO.BinaryStdInReader(), new IO.BinaryStdOutWriter());
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        MoveToFrontImpl.decode(new IO.BinaryStdInReader(), new IO.BinaryStdOutWriter());
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
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