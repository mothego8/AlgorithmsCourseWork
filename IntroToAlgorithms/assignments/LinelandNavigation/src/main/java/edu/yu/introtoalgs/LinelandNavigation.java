package edu.yu.introtoalgs;

import java.util.*;

/** Defines the API for the LinelandNavigation assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class LinelandNavigation {

    /** Even though Lineland extends forward (and backwards) infinitely, for
     * purposes of this problem, the navigation goal cannot exceed this value
     */
    public final static int MAX_FORWARD = 1_000_000;
    private Set<Integer> mines = new HashSet<>();
    private int m;
    private int g;
    private  boolean impossible = false;
    private Graph graph = new Graph();
    private Map<Integer, Integer> distTo = new HashMap<>();
    private static final int INFINITY = Integer.MAX_VALUE;


    /** Constructor.  When the constructor completes successfully, the navigator
     * is positioned at position 0.
     *
     * @param g a positive value indicating the minimim valued position for a
     * successful navigation (a successful navigation can result in a position
     * that is greater than g).  The value of this parameter ranges from 1 to
     * MAX_FORWARD (inclusive).
     * @param m a positive integer indicating the exact number of positions that
     * must always be taken in a forward move. The value of this parameter cannot
     * exceed MAX_FORWARD.
     * @throws IllegalArgumentException if the parameter values violate the
     * specified semantics.
     */
    public LinelandNavigation(final int g, final int m) {
        if(m <= 0 || g < 1 || g > MAX_FORWARD || m > MAX_FORWARD){
            throw new IllegalArgumentException();
        }
        // fill me in!
        this.m = m;
        this.g = g;
    }

    /** Adds a mined line segment to Lineland (an optional operation).
     *
     * NOTE: to simplify computation, assume that no two mined line segments can
     * overlap with one another, even at their end-points.  You need not test for
     * this (although if it's easy to do so, consider sprinkling asserts in your
     * code).
     *
     * @param start a positive integer representing the start (inclusive)
     * position of the line segment
     * @param end a positive integer represending the end (inclusive) position of
     * the line segment, must be greater or equal to start, and less than
     * MAX_FORWARD.
     */
    public void addMinedLineSegment(final int start, final int end) {
        // fill me in!
        if(end - start + 1 >= this.m){
            this.impossible = true;
        }
        if((start > end) || (end >= MAX_FORWARD) || start <= 0) {
            throw new IllegalArgumentException();
        }
        for(int i = start; i <= end; i++) {
            mines.add(i);
        }
    }


    /** Determines the minimum number of moves needed to navigate from position 0
     * to position g or greater, where forward navigation must exactly m
     * positions at a time and backwards navigation can be any number of
     * positions.
     *
     * @return the minimum number of navigation moves necessary, or "0" if no
     * navigation is possible under the specified constraints.
     */
    public final int solveIt() {
        if(impossible){
            return 0;
        }
        //create graph with edges that aren't mines
        for(int i = 0; i < this.g; i++){
            if(!this.mines.contains((i))){
                graph.addVertex(i);
                if(!this.mines.contains(i + m)){
                    graph.addEdge(i, i+ m);
                }else{
                    int j = i;
                    int k = 1;
                    j--;
                    while(j != -1 && !this.mines.contains(j) && k < m){
                        graph.addEdge(i, j);
                        j--;
                        k++;
                    }
                }
            }
        }
        return  bfs(graph, 0, g);
    }

    private int bfs(Graph G, int s, int g) {
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (int v: graph.getAdj().keySet())
            distTo.put(v, INFINITY);
        distTo.put(s, 0);
        visited.add(s);
        q.add(s);

        while (!q.isEmpty()) {
            int v = q.poll();
            if(v >= g){
                return distTo.get(v);
            }
            for (int w : graph.getAdjacencyList(v)) {
                if (!visited.contains(w)) {
                    distTo.put(w, distTo.get(v) + 1);
                    visited.add(w);
                    q.add(w);
                }
            }
        }
        return 0;
    }

    public class Graph {
        private Map<Integer, List<Integer>> adjacencyList;
        public Graph() {
            this.adjacencyList = new HashMap<>();
        }
        public Map<Integer, List<Integer>> getAdj(){
            return this.adjacencyList;
        }
        public void addVertex(int vertex) {
            adjacencyList.putIfAbsent(vertex, new LinkedList<>());
        }
        public void addEdge(int source, int destination) {
            adjacencyList.get(source).add(destination);
        }
        public List<Integer> getAdjacencyList(int vertex) {
            return adjacencyList.get(vertex);
        }
    }
} // LinelandNavigation