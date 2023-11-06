package edu.yu.da;

import java.util.HashSet;
import java.util.Set;

public class Edge {
    private int from;
    private int to;
    private int weight;
    private Set<Integer> verticies;

    public Edge(int from, int to, int weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.verticies = new HashSet<>();
        this.verticies.add(from);
        this.verticies.add(to);
    }

    public int getWeight(){
        return this.weight;
    }

    public int either(){
        return this.from;
    }

    public Set<Integer> getVerticies(){
        return this.verticies;
    }

    public int other(int v){
        if(v == this.from){
            return this.to;
        }else{
            return this.from;
        }
    }
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Edge e = (Edge) o;
        // field comparison
        return (e.getWeight() == this.weight) && (!e.getVerticies().containsAll(this.verticies));
    }

}