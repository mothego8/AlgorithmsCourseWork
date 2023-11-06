package edu.yu.da;

import java.util.*;

public class Dijkstra {
    private Set<Edge> edges = new HashSet<>();
    private int source;
    private double[] distTo;
    private int[] numOfPaths;
    private Edge[] edgeTo;


    private IndexMinPQ<Double> pq; // priority queue of vertices
    private boolean error = false;
    private boolean backward;

    public Dijkstra(Graph G, int s, boolean backward){
        source = s;
        this.backward = backward;
        distTo = new double[G.V() + 1];
        numOfPaths = new int [G.V() + 1];
        edgeTo = new Edge[G.V() + 1];
        for (int v = 1; v < G.V() + 1; v++){
            distTo[v] = Double.POSITIVE_INFINITY;
            numOfPaths[v] = 0;
        }
        distTo[s] = 0.0;
        numOfPaths[s] = 1;
        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if(G.adj(v) == null){
                error = true;
                return;
            }
            for (Edge e : G.adj(v)){
                if(edges.add(e)){
                    relax(e);
                }
            }
        }
    }

    public int getSource() {
        return source;
    }

    public boolean getError(){
        return this.error;
    }

    private void relax(Edge e) {
        int v = e.either(), w = e.other(v);
        HashSet<Integer> edge  = new HashSet<>();
        edge.add(v);
        edge.add(w);
        //boolean added = edges.add(edge);
        if (distTo[w] > distTo[v] + e.getWeight()) {
            distTo[w] = distTo[v] + e.getWeight();
            edgeTo[w] = e;
            numOfPaths[w] = numOfPaths[v];
            if (pq.contains(w)){
                pq.decreaseKey(w, distTo[w]);
            }else {
                pq.insert(w, distTo[w]);
            }
        }
        else //if(added){
            if(distTo[w] == distTo[v] + e.getWeight()){
                numOfPaths[w] = numOfPaths[w] + numOfPaths[v];
            }
        //}
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public int paths(int w){
        return numOfPaths[w];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public List<Edge> pathTo(int v) {
        if (!hasPathTo(v)){
            return null;
        }
        Stack<Edge> path = new Stack<Edge>();
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.either()]) {
            path.push(e);
        }
        return path;
    }

//possible solution to multiple minyamin same path
//    public List<Set<Integer>> pathTo(int v) {
//        if (!hasPathTo(v)){
//            return null;
//        }
//        List<Set<Integer>> path = new ArrayList<>();
//        for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.either()]) {
//            Set<Integer> temp = new HashSet<>(e.getVerticies());
//            path.add(temp);
//        }
//        return path;
//    }


    //other possible solution to same paths multiple minyan
//        public Set<Integer> pathTo(int v) {
//            if (!hasPathTo(v)){
//                return null;
//            }
//            Set<Integer> path = new HashSet<>();
//            for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.either()]) {
//                path.add(e.either());
//                path.add(e.other(e.either()));
//            }
//            return path;
//        }

}