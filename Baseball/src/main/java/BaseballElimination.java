import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BaseballElimination {
    private final LinkedHashMap<String, Integer> teamIndices = new LinkedHashMap<>();
    private final String[] teamNames;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;

    public BaseballElimination(String filename) {
        final In in = new In(filename);
        final String[] lines = in.readAllLines();
        final int numTeams = Integer.parseInt(lines[0]);
        teamNames = new String[numTeams];
        wins = new int[numTeams];
        losses = new int[numTeams];
        remaining = new int[numTeams];
        against = new int[numTeams][numTeams];
        for (int i = 0; i < lines.length - 1; ++i) {
            final String[] split = lines[i + 1].trim().split("\\s+");
            final String teamName = split[0];
            teamNames[i] = teamName;
            teamIndices.put(teamName, i);
            wins[i] = Integer.parseInt(split[1]);
            losses[i] = Integer.parseInt(split[2]);
            remaining[i] = Integer.parseInt(split[3]);
            for (int j = 4; j < numTeams + 4; j++) {
                against[i][j - 4] = Integer.parseInt(split[j]);
            }
        }
    }

    public int numberOfTeams() {
        return wins.length;
    }

    public Iterable<String> teams() {
        return teamIndices.keySet();
    }

    public int wins(String team) {
        return wins[teamIndex(team)];
    }

    public int losses(String team) {
        return losses[teamIndex(team)];
    }

    public int remaining(String team) {
        return remaining[teamIndex(team)];
    }

    public int against(String team1, String team2) {
        return against[teamIndex(team1)][teamIndex(team2)];
    }

    public boolean isEliminated(String team) {
        final List<String> trivialEliminators = triviallyEliminates(teamIndex(team));
        return !trivialEliminators.isEmpty() || certificateOfElimination(team) != null;
    }

    public Iterable<String> certificateOfElimination(String team) {
        final List<String> trivialEliminators = triviallyEliminates(teamIndex(team));
        if (!trivialEliminators.isEmpty()) {
            return trivialEliminators;
        }
        final int teamIndex = teamIndex(team);
        final int numGameVertices = (numberOfTeams() - 1) * (numberOfTeams() - 2) / 2;
        final FlowNetwork flowNetwork = new FlowNetwork(numGameVertices + numberOfTeams() + 1);
        final int source = 0;
        final int sink = flowNetwork.V() - 1;
        final int firstGameVertex = 1;
        final int firstTeamVertex = firstGameVertex + numGameVertices;
        int saturationCount = 0;
        {
            int teamVertexPtr = firstTeamVertex;
            int gameVertexPtr = firstGameVertex;
            for (int i = 0; i < numberOfTeams(); ++i) {
                if (i == teamIndex) {
                    continue;
                }
                final int toSinkCapacity = wins[teamIndex] + remaining[teamIndex] - wins[i];
                flowNetwork.addEdge(new FlowEdge(teamVertexPtr, sink, toSinkCapacity));
                teamVertexPtr++;
                for (int j = i + 1; j < numberOfTeams(); ++j) {
                    if (j == teamIndex) {
                        continue;
                    }
                    final int capacityFromSource = against[i][j];
                    flowNetwork.addEdge(new FlowEdge(source, gameVertexPtr, capacityFromSource));
                    saturationCount += capacityFromSource;
                    gameVertexPtr++;
                }
            }
        }
        {
            int gameVertexPtr = firstGameVertex;
            for (int i = 0; i < numberOfTeams() - 1; ++i) {
                if(i == teamIndex) {
                    continue;
                }
                final int team1Vertex = firstTeamVertex + i - (i > teamIndex ? 1 : 0);
                for (int j = i + 1; j < numberOfTeams(); ++j) {
                    if(j == teamIndex) {
                        continue;
                    }
                    final int team2Vertex = firstTeamVertex + j - (j > teamIndex ? 1 : 0);
                    flowNetwork.addEdge(new FlowEdge(gameVertexPtr, team1Vertex, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gameVertexPtr, team2Vertex, Double.POSITIVE_INFINITY));
                    gameVertexPtr++;
                }
            }
        }
        final FordFulkerson ff = new FordFulkerson(flowNetwork, source, sink);
        final int maxFlow = (int) ff.value();
        if (maxFlow >= saturationCount) {
            return null;
        }
        final ArrayList<String> eliminators = new ArrayList<>();
        int ptr = firstTeamVertex;
        for (int i = 0; i < numberOfTeams(); ++i) {
            if (i == teamIndex) {
                continue;
            }
            if (ff.inCut(ptr)) {
                eliminators.add(teamNames[i]);
            }
            ptr++;
        }
        return eliminators;
    }

    private List<String> triviallyEliminates(int team) {
        final List<String> eliminators = new ArrayList<>();
        for (int i = 0; i < numberOfTeams(); ++i) {
            if (i != team) {
                if (wins[team] + remaining[team] < wins[i]) {
                    eliminators.add(teamNames[i]);
                }
            }
        }
        return eliminators;
    }

    private int teamIndex(String team) {
        final Integer index = teamIndices.get(team);
        if(index == null) {
            throw new IllegalArgumentException();
        }
        return index;
    }
}
