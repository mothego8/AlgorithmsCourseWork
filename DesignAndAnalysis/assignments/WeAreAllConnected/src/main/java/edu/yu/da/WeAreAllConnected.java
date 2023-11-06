package edu.yu.da;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeAreAllConnected extends WeAreAllConnectedBase{

    final int INF = 9_999_999;

    public WeAreAllConnected(){
        //make graph
    }

    public static class Segment extends SegmentBase{

        /**
         * Constructor.
         *
         * @param x        one end of a communication segment, specified by a city id
         *                 (0..n-1)
         * @param y        one end of a communication segment, specified by a city id
         *                 (0..n-1).  You may assume that "x" differs from "y".
         * @param duration unit-less amount of time required for a message to
         *                 travel from either end of the segment to the other.  You may assume that
         *                 duration is non-negative.
         */
        public Segment(int x, int y, int duration) {
            super(x, y, duration);
//            if(x < 0 || y < 0 || duration < 0){
//                throw new IllegalArgumentException();
//            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            final SegmentBase O = (SegmentBase) obj;
            if ((this.x != O.x && this.y != O.y) && (this.x != O.y && this.y != O.x)) {
                return false;
            }
            return this.duration == O.duration;
        }

    }

    /** Given a list of the current communication system's segments and a list of
     * possible segments that can be added to the current system, select exactly
     * one segment from the set of possibilities to be added to the current
     * communication system.  You may assume that all segments supplied by the
     * client satisfy Segment semantics.
     *
     * @param n the ids of all cities referenced by SegmentBase instances lie in
     * the range 0..n-1 (inclusive).
     * @param current the current communication system defined as a list of
     * segments.  The client maintains ownership of this parameter.
     * @param possibilities one possible segment will be selected from this list
     * to be added to the current communication system.  The client maintains
     * ownership of this parameter.
     * @return the segment from the set of possibilities that provides the best
     * improvement in the total duration of the current system.  Total duration
     * is defined as the sum of the durations between all pairs of cities x and y
     * in the current system.  If more than one segment qualifies, return any of
     * those possibilities.
     */
    @Override
    public SegmentBase findBest(int n, List<SegmentBase> current, List<SegmentBase> possibilities){
        if(n < 0){
            throw new IllegalArgumentException();
        }
        if(current.isEmpty() || possibilities.isEmpty()){
            return null;
        }
        int [][] graph = new int [n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i == j){
                    graph[i][j] = 0;
                }else{
                    graph[i][j] = INF;
                }

            }
        }
        Set<Integer> verticies = new HashSet<>();
        for(SegmentBase sb : current){
            if(sb.duration < 0 || sb.x < 0 || sb.y < 0 || sb.x >= n || sb.y >= n || sb.x == sb.y){
                throw new IllegalArgumentException();
            }
            verticies.add(sb.x);
            verticies.add(sb.y);
            graph[sb.x][sb.y] = sb.duration;
            graph[sb.y][sb.x] = sb.duration;
        }
        int k, j, i;
        for (k = 0; k < n; k++) {
            for (i = 0; i < n; i++) {
                for (j = i; j < n; j++) {
                    if (graph[i][k] + graph[k][j] < graph[i][j]){
                        graph[i][j] = graph[i][k] + graph[k][j];
                        graph[j][i] = graph[i][k] + graph[k][j];
                    }
                }
            }
        }
        int initialDuration = 0;
        for(int m = 0; m < n; m++){
            for(int l = 0; l < graph[m].length; l++){
                if(graph[m][l] != INF){
                    initialDuration += graph[m][l];
                }
            }
        }
        int min = Integer.MAX_VALUE;
        SegmentBase returnSegment = null;
        for(SegmentBase sb : possibilities){
            if(sb.duration < 0 || sb.x < 0 || sb.y < 0 || sb.x >= n || sb.y >= n || sb.x == sb.y){
                throw new IllegalArgumentException();
            }
            if(verticies.add(sb.x) || verticies.add(sb.y)){
                return null;
            }
            int [][] tempGraph = new int [n][n];
            for(int l =0; l < n; l++){
                System.arraycopy(graph[l], 0, tempGraph[l], 0, n);
            }
            int duration = floydWarshall(n, tempGraph, sb);
            if(duration < min){
                min = duration;
                returnSegment = sb;
            }
        }
        if(initialDuration <= min){
            return null;
        }
        return returnSegment;
    }

//    public int floydWarshall(int n, int [][] graph, SegmentBase possible){
//        int i, j, k;
//        if(possible != null){
//            graph[possible.x][possible.y] = possible.duration;
//            graph[possible.y][possible.x] = possible.duration;
//        }
//        // Adding vertices individually
//        for (k = 0; k < n; k++) {
//            for (i = 0; i < n; i++) {
//                for (j = i; j < n; j++) {
//                    if (graph[i][k] + graph[k][j] < graph[i][j]){
//                        graph[i][j] = graph[i][k] + graph[k][j];
//                        graph[j][i] = graph[i][k] + graph[k][j];
//                    }
//                }
//            }
//        }
//        int duration = 0;
//        for(int m = 0; m < n; m++){
//            for(int l = 0; l < graph[m].length; l++){
//                if(graph[m][l] != INF){
//                    duration += graph[m][l];
//                }
//            }
//        }
//        return duration;
//    }

    public int floydWarshall(int n, int [][] graph, SegmentBase possible){
        // add edge to the graph
        graph[possible.x][possible.y] = possible.duration;
        graph[possible.y][possible.x] = possible.duration;

        // update the distance matrix with the new edge
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int newDist = graph[i][possible.x] + graph[possible.y][j] + possible.duration;
                if (newDist < graph[i][j]) {
                    graph[i][j] = newDist;
                    graph[j][i] = newDist;
                }
            }
        }
        int duration = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(graph[i][j] != INF){
                    duration += graph[i][j];
                }
            }
        }
        return duration;
    }
}
