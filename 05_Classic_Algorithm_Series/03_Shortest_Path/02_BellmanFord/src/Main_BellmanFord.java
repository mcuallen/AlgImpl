
/**
 * http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_1_B
 */
/**
 * Result: AC
 * TC: O(V*E)
 * SC: O(V)
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main_BellmanFord {

    public static class BellmanFordShortestPath {

        Integer[] distTo; // index is vertex
        Integer[] edgeTo;
        boolean hasNegativeCycle;
        WeightedDigraph graph;
        int sourceVertex;

        public BellmanFordShortestPath(WeightedDigraph graph, int sourceVertex) {
            this.graph = graph;
            this.sourceVertex = sourceVertex;
            distTo = new Integer[graph.numVertex];
            edgeTo = new Integer[graph.numVertex];
            distTo[sourceVertex] = 0;
            edgeTo[sourceVertex] = 0;
            boolean hasUpdates = false;
            // V + 1 passes instead of V, the extra pass is used to detect negative cycle
            for (int pass = 0; pass <= graph.numVertex; pass++) {
                hasUpdates = relaxAll();
                if (!hasUpdates) {
                    break;
                }
            }
            hasNegativeCycle = hasUpdates;
        }

        private boolean relaxAll() {
            boolean hasChanges = false;
            for (int vertex = 0; vertex < graph.numVertex; vertex++) {
                for (WeightedDigraph.Edge edge : graph.srcEdges[vertex]) {
                    hasChanges |= relax(edge);
                }
            }
            return hasChanges;
        }

        private boolean relax(WeightedDigraph.Edge edge) {
            int src = edge.srcVertex;
            int dest = edge.targetVertex;
            if (distTo[src] == null) {
                return false;
            } else if (distTo[dest] == null
                    || distTo[dest] > distTo[src] + edge.weight) {
                distTo[dest] = distTo[src] + edge.weight;
                edgeTo[dest] = src;
                return true;
            }
            return false;
        }

        public boolean hasNegativeCycle() {
            return hasNegativeCycle;
        }

        /**
         *
         * @param vertex
         * @return null meaning INF
         */
        public Integer shortestPath(int vertex) {
            return distTo[vertex];
        }

        public List<Integer> paths(int vertex) {
            LinkedList<Integer> paths = new LinkedList<Integer>();
            while (edgeTo[vertex] != null) {
                paths.addFirst(edgeTo[vertex]);
                if (edgeTo[vertex] != vertex) {
                    vertex = edgeTo[vertex];
                } else {
                    break;
                }
            }
            return paths;
        }

    }

    public static class WeightedDigraph {

        int numVertex;

        List<Edge>[] srcEdges;

        @SuppressWarnings("unchecked")
        public WeightedDigraph(int numV) {
            this.numVertex = numV;
            srcEdges = new List[numV];

            for (int i = 0; i < numV; i++) {
                srcEdges[i] = new ArrayList<Edge>();
            }
        }

        public void addEdge(int srcVertex, int targetVertex, int weight) {
            Edge e = new WeightedDigraph.Edge(srcVertex, targetVertex, weight);
            srcEdges[srcVertex].add(e);
        }

        public static class Edge {

            int srcVertex;
            int targetVertex;
            int weight;

            public Edge(int srcVertex, int targetVertex, int weight) {
                this.srcVertex = srcVertex;
                this.targetVertex = targetVertex;
                this.weight = weight;
            }
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numVertex = scanner.nextInt();
        int numEdge = scanner.nextInt();
        WeightedDigraph graph = new WeightedDigraph(numVertex);
        int srcVertex = scanner.nextInt();
        while (numEdge-- > 0) {
            int srcV = scanner.nextInt();
            int targetV = scanner.nextInt();
            int weight = scanner.nextInt();
            graph.addEdge(srcV, targetV, weight);
        }
        BellmanFordShortestPath bellmanFord = new BellmanFordShortestPath(graph, srcVertex);
        if (bellmanFord.hasNegativeCycle()) {
            System.out.println("NEGATIVE CYCLE");
        } else {
            for (int i = 0; i < graph.numVertex; i++) {
                Integer v = bellmanFord.shortestPath(i);
                System.out.println(v == null ? "INF" : v);
            }
        }
    }

}
