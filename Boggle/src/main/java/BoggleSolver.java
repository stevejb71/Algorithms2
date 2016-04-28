import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class BoggleSolver {
    private final TrieST dictionaryTrie = new TrieST();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (final String word : dictionary) {
            if (validWord(word)) {
                final String w = word.replaceAll("QU", "Q");
                dictionaryTrie.put(w, word);
            }
        }
    }

    private boolean validWord(String word) {
        if(word.length() <= 2 || word.endsWith("Q")) {
            return false;
        }
        for(int i = 0; i < word.length() - 1; ++i) {
            if(word.charAt(i) == 'Q' && word.charAt(i + 1) != 'U') {
                return false;
            }
        }
        return true;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        final Graph graph = new Graph(board);
        final DFS dfs = new DFS();
        final Set<String> words = new HashSet<>(2048);
        for(int x = 0; x < board.cols(); ++x) {
            for(int y = 0; y < board.rows(); ++y) {
                dfs.dfs(board.cols(), board.rows(), graph, x, y, dictionaryTrie.path(), path -> {
                    if(!path.isPrefixOfWord()) {
                        return false;
                    }
                    final String word = path.word();
                    if(path.word() != null) {
                        words.add(word);
                    }
                    return true;
                });
            }
        }
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if(!validWord(word)) {
            return 0;
        }
        final String adjusted = word.replaceAll("QU", "Q");
        if(!dictionaryTrie.contains(adjusted)) {
            return 0;
        }
        switch(word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

}