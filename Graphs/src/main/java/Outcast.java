public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int max = -1;
        String outcast = null;
        for (String noun : nouns) {
            int distance = 0;
            for (String noun1 : nouns) {
                distance += wordnet.distance(noun, noun1);
            }
            if (distance > max) {
                max = distance;
                outcast = noun;
            }
        }
        return outcast;
    }
}
