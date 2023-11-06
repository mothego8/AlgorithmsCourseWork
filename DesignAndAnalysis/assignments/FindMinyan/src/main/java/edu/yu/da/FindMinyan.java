package edu.yu.da;

import java.util.*;


/** Defines the API for specifying and solving the FindMinyan problem (see the
 * requirements document).  Also defines an inner interface, and uses it as
 * part of the ArithmeticPuzzleI API definition.
 *
 * Students MAY NOT change the public API of this class, nor may they add ANY
 * constructor.
 *
 * @author Avraham Leff
 */

public class FindMinyan {
    private final int cities;
    private Set<Integer> minyanim = new HashSet<>();
    //private Set<Dijkstra> sp = new HashSet<>();
    private final Map<Integer, Integer> distances = new HashMap<>();
    private final Graph graph;
    private Dijkstra starter;
    private Dijkstra ender;
    private int min;
    private int totalPaths;
    private Set<Set<Integer>> mow = new HashSet<>();


    /** Constructor: clients specify the number of cities involved in the
     * problem.  Cities are numbered 1..n, and for convenience, the "start" city
     * is labelled as "1", and the goal city is labelled as "n".
     *
     * @param nCities number of cities, must be greater than 1.
     */
    public FindMinyan(final int nCities) {
        if(nCities <= 1){
            throw new IllegalArgumentException();
        }
        this.cities = nCities;
        this.graph = new Graph(nCities);
        this.starter = null;
        this.ender = null;
        this.min = -1;
        this.totalPaths = -1;
        // fill me in!
    }


    /** Defines a highway leading (bi-directionally) between two cities, of
     * specified duration.
     *
     * @param city1 identifies a 1 <= city <= n, must differ from city2
     * @param city2 identifies a 1 <= city <= n, must differ from city1
     * @param duration the bi-directional duration of a trip between the two
     * cities on this highway, must be non-negative
     */
    public void addHighway(final int city1, final int duration, final int city2) {
        // fill me in!
        if(city1 == city2 || city1 < 1 || city1 > this.cities || city2 < 1 || city2 > this.cities){
            throw new IllegalArgumentException();
        }
        if(duration < 0){
            throw new IllegalArgumentException();
        }
        Set<Integer> edge = new HashSet<>();
        edge.add(city1);
        edge.add(city2);
        if(!mow.add((edge))){
            throw new IllegalArgumentException();
        }
        Edge e = new Edge(city1, city2, duration);
        Edge e2 = new Edge(city2, city1, duration);
        graph.addEdge(e);
        graph.addEdge(e2);
        //add this edge to the graph
    }

    /** Specifies that a minyan can be found in the specified city.
     *
     * @param city identifies a 1 <= city <= n
     */
    public void hasMinyan(final int city) {
        // fill me in!
        if(city < 1 || city > this.cities){
            throw new IllegalArgumentException();
        }
        this.minyanim.add(city);
    }

    /** Find a solution to the FindMinyan problem based on state specified by the
     * constructor, addHighway(), and hasMinyan() API.  Clients access the
     * solution through the shortestDuration() and nShortestDurationTrips() APIs.
     */
    public void solveIt() {
        this.starter = new Dijkstra(this.graph, 1, false);
        this.ender = new Dijkstra(this.graph, this.cities, true);
        if(starter.getError() || ender.getError()){
            this.totalPaths = 0;
            this.min = 0;
            return;
        }
        this.totalPaths = 0;
        this.min = 0;

        //shortest duration
        boolean noPath = true;
        for(int m : this.minyanim){
            if(starter.hasPathTo(m)){
                noPath = false;
                distances.put(m, (int) this.starter.distTo(m));
            }
            if(distances.get(m) != null){
                if(ender.hasPathTo(m)){
                    noPath = false;
                    distances.put(m, distances.get(m) + (int) this.ender.distTo(m));
                }
            }
        }
        if(noPath){
            return;
        }

        this.min = Integer.MAX_VALUE;
        for(int i : distances.values()){
            if(this.min > i){
                this.min = i;
            }
        }

        //number of paths
//        List<Integer> goalaso = new ArrayList<>();
        Set<Integer> goalaso = new HashSet<>();
        for(int i : distances.keySet()){
            if(distances.get(i) == min){
                goalaso.add(i);
            }
        }

        Set<Integer> minyan = new HashSet<>();
        for(int i : goalaso){
            List<Edge> temp = new ArrayList<>();
            temp.addAll(starter.pathTo(i));
            temp.addAll(ender.pathTo(i));
            minyan.add(i);
            for (Edge e : temp) {
                if (goalaso.contains(e.either()) && !minyan.contains(e.either())) {
                    minyan.add(e.either());
                    this.totalPaths -= 1;
                } else if (goalaso.contains(e.other(e.either())) && !minyan.contains(e.other(e.either()))) {
                    minyan.add(e.other(e.either()));
                    this.totalPaths -= 1;
                }
            }
            this.totalPaths += starter.paths(i) * ender.paths(i);
        }

//possible solution to multiple paths same minyan
//        Set<Set<Integer>> sets = new HashSet<>();
//        for(int i : goalaso){
//            Set<Set<Integer>> mowShow = new HashSet<>();
//            mowShow.addAll(starter.pathTo(i));
//            mowShow.addAll(ender.pathTo(i));
//            if(!sets.addAll(mowShow)){
//                this.totalPaths -= 1;
//            }
//            this.totalPaths += starter.paths(i) * ender.paths(i);
//        }

//other possible solution to multiple paths same minyan
//        for(int i : goalaso){
//            Set<Integer> mowShow = new HashSet<>();
//            mowShow.addAll(starter.pathTo(i));
//            mowShow.addAll(ender.pathTo(i));
//            for(int j : goalaso){
//                if(j != i && mowShow.contains(j)){
//                    this.totalPaths -= 1;
//                }
//            }
//            this.totalPaths += starter.paths(i) * ender.paths(i);
//        }
        // fill me in!
    }

    /** Returns the duration of the shortest trip satisfying the FindMinyan
     * constraints.
     *
     * @return duration of the shortest trip, undefined if client hasn't
     * previously invoked solveIt().
     */
    public int shortestDuration() {
        if(this.min == -1){
            return -1;
        }
        return this.min;
    }

    /** Returns the number of distinct trips that satisfy the FindMinyan
     * constraints.
     *
     * @return number of shortest duration trips, undefined if client hasn't
     * previously invoked solveIt()..
     */
    public int numberOfShortestTrips() {
        if(this.totalPaths == -1){
            return -1;
        }
        return this.totalPaths;
    }
} // FindMinyan