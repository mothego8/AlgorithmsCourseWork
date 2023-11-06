package edu.yu.da;

import java.util.*;

public class Graph{
    private int V;
    private int E;
    private Map<Integer, List<Edge>> adjacencyList;

    public Graph(int v){
        this.V = v;
        this.E = 0;
        this.adjacencyList = new HashMap<>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        this.adjacencyList.putIfAbsent(v, new LinkedList<>());
        this.adjacencyList.putIfAbsent(w, new LinkedList<>());
        this.adjacencyList.get(v).add(e);
        this.adjacencyList.get(w).add(e);
        E++;
    }

    public List<Edge> adj(int v) {
        return this.adjacencyList.get(v);
    }

    public List<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }
}
