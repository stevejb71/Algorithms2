import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;

public class WordNet {

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        throwIfNull(synsets, hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        throw new Error();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        throwIfNull(word);
        throw new Error();

    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        throwIfNull(nounA, nounB);
        throw new Error();

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throwIfNull(nounA, nounB);
        throw new Error();

    }

    // do unit testing of this class
    public static void main(String[] args) {

    }

    private void throwIfNull(Object... values) {
        Arrays.asList(values).stream().filter(x -> x != null).forEach(x -> {throw new NullPointerException();});
    }
}