import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class WordNet {
    private final Digraph hypernyms;
    private final SAP sap;
    private final Map<Integer, Synset> synsets = new HashMap<>();
    private final Map<String, Set<Integer>> nouns = new HashMap<>();

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        throwIfNull(synsetsFile, hypernymsFile);

        readSynsets(synsetsFile);
        this.hypernyms = readHypernyms(this.synsets.size(), hypernymsFile);
        final DirectedCycle directedCycle = new DirectedCycle(this.hypernyms);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("has cycle");
        }
        int roots = 0;
        for (int v = 0; v < this.hypernyms.V(); ++v) {
            if (this.hypernyms.outdegree(v) == 0) {
                roots++;
                if (roots == 2) {
                    throw new IllegalArgumentException("more than 1 root");
                }
            }
        }
        this.sap = new SAP(this.hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        throwIfNull(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        throwIfNull(nounA, nounB);
        final Set<Integer> idA = getNounId(nounA);
        final Set<Integer> idB = getNounId(nounB);
        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throwIfNull(nounA, nounB);
        final Set<Integer> idA = getNounId(nounA);
        final Set<Integer> idB = getNounId(nounB);
        int ancestorId = sap.ancestor(idA, idB);
        final List<String> nounsList = synsets.get(ancestorId).nouns;
        final StringJoiner joiner = new StringJoiner(" ");
        for (String noun : nounsList) {
            joiner.add(noun);
        }
        return joiner.toString();
    }

    private void throwIfNull(Object... values) {
        Arrays.asList(values).stream().filter(x -> x == null).forEach(x -> {
            throw new NullPointerException();
        });
    }

    private Set<Integer> getNounId(String word) {
        final Set<Integer> id = nouns.get(word);
        if (id == null) {
            throw new IllegalArgumentException("not noun: " + word);
        }
        return id;
    }

    private void readSynsets(String filename) {
        final In in = new In(filename);
        final String[] lines = in.readAllLines();
        Arrays.asList(lines).stream().map(l -> l.split(",")).forEach(l -> {
            final int id = Integer.parseInt(l[0]);
            final List<String> nouns = Arrays.asList(l[1].split(" "));
            for (String noun : nouns) {
                Set<Integer> ids = this.nouns.get(noun);
                if (ids == null) {
                    ids = new HashSet<>();
                    this.nouns.put(noun, ids);
                }
                ids.add(id);
            }
            this.synsets.put(id, new Synset(id, nouns));
        });
    }

    private Digraph readHypernyms(int length, String filename) {
        final In in = new In(filename);
        final String[] lines = in.readAllLines();
        final Digraph hypernyms = new Digraph(length);
        Arrays.asList(lines).stream().map(l -> Arrays.asList(l.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList())).forEach(ids -> {
            final int id = ids.get(0);
            for (int i = 1; i < ids.size(); i++) {
                hypernyms.addEdge(id, ids.get(i));
            }
        });
        return hypernyms;
    }
}