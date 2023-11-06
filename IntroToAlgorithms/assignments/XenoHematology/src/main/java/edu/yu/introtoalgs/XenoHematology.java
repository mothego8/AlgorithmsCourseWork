package edu.yu.introtoalgs;

import java.util.*;

/** Defines the API for the XenoHematology assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class XenoHematology{

    int [] compatible;
    int [] incompatible;
    int [] sz;
    int count;
    int populationSize;
    /** Constructor: specifies the size of the xeno population.
     *
     * @param populationSize a non-negative integer specifying the number of
     * aliens in the xeno population.  Members of the population are uniquely
     * identified by an integer 0..populationSize -1.
     */
    public XenoHematology(final int populationSize) {
        this.compatible = new int [populationSize];
        this.incompatible = new int [populationSize];
        for(int i = 0; i < populationSize; i++){
            this.compatible[i] = i;
            this.incompatible[i] = -1;
        }
        this.sz = new int [populationSize];
        Arrays.fill(this.sz, 1);
        this.count = populationSize;
        this.populationSize = populationSize;
        // fill me in!
    } // constructor


    /** Specifies that xeno1 and xeno2 are incompatible.  Once specified
     * as incompatible, the pair can never be specified as being
     * "compatible".  In that case, don't throw an exception, simply
     * treat the method invocation as a "no-op".  A xeno is always
     * compatible with itself, is never incompatible with itself:
     * directives to the contrary should be treated as "no-op"
     * operations.
     *
     * Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population.
     * @throws IllegalArgumentException if the supplied values are incompatible
     * with the above semantics or those specified by the requirements doc.
     */
    public void setIncompatible(int xeno1, int xeno2) {
        if(xeno1 < 0 || xeno2 < 0) throw new IllegalArgumentException();
        if(xeno1 > this.populationSize - 1 || xeno2 > this.populationSize-1) throw new IllegalArgumentException();
        if(xeno1 == xeno2){
            //automatically compatible nothing to be set
            return;
        }
        //first check if they were already set to be compatible
        if(areCompatible(xeno1, xeno2)){
            return;
        // check if they were already to set to InCompatible
        }else if(areIncompatible(xeno1, xeno2)){
            return;
        }
        else{
            //finding root of tree of compatible elements to xeno1
            int x = find(xeno1);
            if(this.incompatible[x] != -1){
                union(this.incompatible[x], xeno2);
                x = find(xeno1);
            }
            int y = find(xeno2);
            if(this.incompatible[y] != -1){
                union(this.incompatible[y], xeno1);
                y = find(xeno2);
            }
            this.incompatible[x] = y;
            this.incompatible[y] = x;
        }
        // fill me in!
    }

    /** Specifies that xeno1 and xeno2 are compatible.  Once specified
     * as compatible, the pair can never be specified as being
     * "incompatible".  In that case, don't throw an exception, simply
     * treat the method invocation as a "no-op".  A xeno is always
     * compatible with itself, is never incompatible with itself:
     * directives to the contrary should be treated as "no-op"
     * operations.
     *
     * Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population.
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @throws IllegalArgumentException if the supplied values are incompatible
     * with the above semantics or those specified by the requirements doc.
     */
    public void setCompatible(int xeno1, int xeno2) {
        //first check if they areInCompatible
        if(xeno1 < 0 || xeno2 < 0){
            throw new IllegalArgumentException();
        }
        if(xeno1 > this.populationSize - 1 || xeno2 > this.populationSize-1) throw new IllegalArgumentException();
        if(xeno1 == xeno2){
            //automatically compatible nothing to be set
            return;
        }

        if(areIncompatible(xeno1, xeno2)){
            return;
        //then check if they are already compatible
        }else if(areCompatible(xeno1, xeno2)){
            return;
        }
        else{
            union(xeno1, xeno2);
            //just do sedgewick's weighted union with path compression
            //then make their incompatible trees compatible to eachother
            if(this.incompatible[find(xeno1)] != -1 && this.incompatible[find(xeno2)] != -1){
                setCompatible(this.incompatible[find(xeno1)], this.incompatible[find(xeno2)]);
                //not going to overflow because check above this that checks whether two xenos are compatible
            }else if(this.incompatible[find(xeno1)] == -1 && this.incompatible[find(xeno2)] != -1){
                this.incompatible[find(xeno1)] = this.incompatible[find(xeno2)];
            }else if(this.incompatible[find(xeno1)] != -1 && this.incompatible[find(xeno2)] == -1){
                this.incompatible[find(xeno2)] = this.incompatible[find(xeno1)];
            }else{
                return;
            }
        }
        // fill me in!
    }

    /** Returns true iff xeno1 and xeno2 are compatible from a hematology
     * perspective, false otherwise (including if we don't know one way or the
     * other).  Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @return true iff compatible, false otherwise
     * @throws IllegalArgumentException if the supplied values are incompatible
     * with the above semantics or those specified by the requirements doc.
     */
    public boolean areCompatible(int xeno1, int xeno2) {
        if(xeno1 < 0 || xeno2 < 0){
            throw new IllegalArgumentException();
        }else if(xeno1 == xeno2){
            return true;
        }
        if(xeno1 > this.populationSize - 1 || xeno2 > this.populationSize-1) throw new IllegalArgumentException();

        //essentially check if the value stored at xeno1 index is equal to value stored at xeno2 index
        //meaning xeno1 and xeno2 are incompatible to same thing which makes  them compatible
        return find(xeno1) == find(xeno2);
    }

    /** Returns true iff xeno1 and xeno2 are incompatible from a hematology
     * perspective, false otherwise (including if we don't know one way or the
     * other).  Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @return true iff compatible, false otherwise
     * @throws IllegalArgumentException if the supplied values are incompatible
     * with the above semantics or those specified by the requirements doc.
     */
    public boolean areIncompatible(int xeno1, int xeno2) {
        if(xeno1 < 0 || xeno2 < 0){
            throw new IllegalArgumentException();
        }else if(xeno1 == xeno2){
            return false;
        }
        if(xeno1 > this.populationSize - 1 || xeno2 > this.populationSize-1) throw new IllegalArgumentException();
        //check whether the value stored at the xeno1 index is equal to xeno2
        int x = find(xeno2);
        int y =find(xeno1);
        if(this.incompatible[x] == -1 || this.incompatible[y] == -1){
            return false;
        }
        return(find(this.incompatible[y]) == x || find(this.incompatible[x]) == y);
    }
    //from sedwick
    public int find(int xeno){
        while (xeno != this.compatible[xeno]){
            this.compatible[xeno] = this.compatible[this.compatible[xeno]];
            xeno = this.compatible[xeno];
        }
        return xeno;

    }

    //from sedgewick
    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i == j) return;
        // Make smaller root point to larger one.
        if (sz[i] < sz[j]) {
            this.compatible[i] = j;
            sz[j] += sz[i];
        }
        else {
            this.compatible[j] = i;
            sz[i] += sz[j];
        }
        this.count--;
    }
}