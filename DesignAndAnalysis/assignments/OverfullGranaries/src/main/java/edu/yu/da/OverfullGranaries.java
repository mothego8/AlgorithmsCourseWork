package edu.yu.da;

/** Defines the API for specifying and solving the OverfullGranaries problem
 * (see the requirements document).
 *
 * Students MAY NOT change the public API of this class, nor may they add ANY
 * constructor.

 * @author Avraham Leff
 */


import java.util.*;

public class OverfullGranaries {

  /** Represents the 10_000 bushels of grain that must be moved from the
   * overfull granaries to the underfull granaries
   */
  public final static double BUSHELS_TO_MOVE = 10_000;
  private final Set<String> setOfX;
  private final Set<String> setOfY;
//  private String [] X;
//  private String [] Y;
  private boolean solved = false;
  private final String src = "SRC";
  //private NetworkFlow nf = new NetworkFlow();
  private final int inf = Integer.MAX_VALUE;
  private final Set<FlowEdge> edges = new HashSet<>();
  private final Map<String, Integer> stringMap = new HashMap<>();
  private FordFulkerson ff;
  private int counter = 0;
  private double maxFlow= 0;


  /** Constructor.
   *
   * @param X labelling of the overfull granaries, must contain at least one
   * element and no duplicates.  No element of X can be an element of Y.
   * @param Y labelling of the underfull granaries, must contain at least one
   * element and no duplicates.  No element of Y can be an element of X.
   */
  public OverfullGranaries(final String[] X, final String[] Y) {
    stringMap.put(this.src, counter++);
    if(!(X.length > 0) || !(Y.length > 0)){
      throw new IllegalArgumentException();
    }
    this.setOfX = new HashSet<>();
    this.setOfY = new HashSet<>();
    for(String s: X){
      if(!this.setOfX.add(s)){
        throw new IllegalArgumentException();
      }
      stringMap.put(s, counter++);
    }
    for(String s: Y){
      if(!this.setOfY.add(s)){
        throw new IllegalArgumentException();
      }else if(this.setOfX.contains(s)){
        throw new IllegalArgumentException();
      }
      stringMap.put(s, counter++);
    }
//    this.X = X;
//    this.Y = Y;
    for(String s: this.setOfX){
      edges.add(new FlowEdge(stringMap.get(this.src), stringMap.get(s), inf));
    }
    // fill me in please!
  }

  /** Specifies that an edge exists from the specified src to the specified
   * dest of specified capacity.  It is legal to invoke edgeExists between
   * nodes in X, between nodes in Y, from a node in X to a node in Y, or for
   * src and dest to be hitherto unknown nodes.  The method cannot specify a
   * node in Y to be the src, nor can it specify a node in X to be the dest.
   *
   * @param src must contain at least one character
   * @param dest must contain at least one character, can't equal src
   * @param capacity must be greater than 0, and is specified implicitly to be
   * "bushels per hour"
   */
  public void edgeExists(final String src, final String dest, final int capacity) {
    if(src.equals(dest) || capacity < 1){
      throw new IllegalArgumentException();
    }
    if(this.setOfY.contains(src) && this.setOfX.contains(dest)){
      throw new IllegalArgumentException();
    }
    if(!(this.stringMap.containsKey(src))){
      stringMap.put(src, counter++);
    }else if(!(this.stringMap.containsKey(dest))){
      stringMap.put(dest,counter++);
    }
    edges.add(new FlowEdge(stringMap.get(src), stringMap.get(dest), capacity));
    // fill me in please
  }

  /** Solves the OverfullGranaries problem.
   *
   * @return the minimum number hours needed to achieve the goal of moving
   * BUSHELS_TO_MOVE number of bushels from the X granaries to the Y granaries
   * along the specified road map.
   * @note clients may only invoke this method after all relevant edgeExists
   * calls have been successfully invoked.
   */
  public double solveIt() {
    this.solved = true;
    String dst = "DST";
    stringMap.put(dst, counter++);
    for(String s: this.setOfY){
      edges.add(new FlowEdge(stringMap.get(s), stringMap.get(dst), inf));
    }
    FlowNetwork fn = new FlowNetwork(this.counter);
    for(FlowEdge f: this.edges){
      fn.addEdge(f);
    }
    this.ff = new FordFulkerson(fn, stringMap.get(this.src), stringMap.get(dst));
    this.maxFlow = this.ff.value();
    if(this.maxFlow == 0){
      return Double.POSITIVE_INFINITY;
    }
    return 10_000/this.maxFlow;
  }

  /** Return the names of all vertices in the X side of the min-cut, sorted by
   * ascending lexicographical order.
   *
   * @return only the names of the vertices in the X side of the min-cut
   * @note clients may only invoke this method after solveIt has been
   * successfully invoked.  Else throw an ISE.
   */
  public List<String> minCut() {
    if(!this.solved){
      throw new IllegalStateException();
    }
    if(this.maxFlow == 0){
      return new ArrayList<>();
    }
    Set<String> min = new HashSet<>();
    for(String s: stringMap.keySet()){
      if(!s.equals(src)){
        if(this.ff.inCut(stringMap.get(s))){
          min.add(s);
        }
      }
    }
    List<String> show = new ArrayList<>(min);
    show.sort(Comparator.naturalOrder());
    return show;
  }

} // OverfullGranaries
