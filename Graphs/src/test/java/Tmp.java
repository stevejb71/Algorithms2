import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.KosarajuSharirSCC;
import edu.princeton.cs.algs4.Topological;

public class Tmp {
    static final int A = 0;
    static final int B = 1;
    static final int C = 2;
    static final int D = 3;
    static final int E = 4;
    static final int F = 5;
    static final int G = 6;
    static final int H = 7;
    static final int I = 8;
    static final int J = 9;

    static Digraph graph;

    public static void main(String[] args) {
        kcc();
    }

    public static void top() {
        graph = new Digraph(8);
        graph.addEdge(A, E);
        graph.addEdge(B, F);
        graph.addEdge(B, A);
        graph.addEdge(B, C);
        graph.addEdge(C, H);
        graph.addEdge(C, G);
        graph.addEdge(D, C);
        graph.addEdge(D, H);
        graph.addEdge(F, A);
        graph.addEdge(F, E);
        graph.addEdge(F, C);
        graph.addEdge(F, G);
        graph.addEdge(H, G);
        Topological t = new Topological(graph);
        for (Integer node : t.order()) {
            System.out.print((char)('A' + node));
            System.out.print(" ");
        }
    }

    public static void kcc() {
        graph = new Digraph(10);
        addEdges(A, B, G);
        addEdges(B, G);
        addEdges(C, D, H, B);
        addEdges(D, H, I);
        addEdges(E, D, J);
        addEdges(F, A);
        addEdges(G, F);
        addEdges(H, I, G, B);
        addEdges(I, E);
        addEdges(J, I);

        KosarajuSharirSCC ks = new KosarajuSharirSCC(graph);
        System.out.println(ks);
    }

    private static void addEdges(int from, int... tos) {
        for (int to : tos) {
            graph.addEdge(from, to);
        }
    }
}
