package edu.yu.introtoalgs;

import java.text.DecimalFormat;
import java.util.*;

/** Defines the API for the WealthTransfer assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class WealthTransfer {
    private int populationSize;
    private Map<Integer, Boolean> isSquared;
    private Map<Integer, Integer> parent;
    //private Set<Integer> parents;
    private Map<Integer, Double> weight;
    private Map<Integer, Integer> wealth;
    private Map<Integer,Integer> percentageTotal;

    /** Constructor: specifies the size of the population.
     *
     * @param populationSize a positive integer specifying the number of people
     * in the population.  Members of the population are uniquely identified by
     * an integer 1..populationSize.  Initial wealth transfer must be initiated
     * by the person with id of "1".
     */
    public WealthTransfer(final int populationSize) {
        this.populationSize = populationSize;
        this.isSquared = new HashMap<>();
        this.parent = new HashMap<>();
        this.weight = new HashMap<>();
        this.wealth = new HashMap<>();
        this.percentageTotal = new HashMap<>();
        //this.parents = new HashSet<>();
        // fill me in
    } // constructor

    /** Specifies that one person want to make a wealth transfer to another
     * person.
     *
     * @param from specifies who is doing the wealth transfer, must correspond to
     * a valid population id
     * @param to specifies who is receiving the wealth transfer, must correspond
     * to a valid population id, and can't be identical to "from"
     * @param percentage the percentage of "from"'s wealth that will be
     * transferred to "to": must be an integer between 1..100
     * @param isWealthSquared if true, the wealth received is the square of the
     * money transferred
     * @throws IllegalArgumentException if the parameter Javadoc specifications
     * aren't satisfied or if this "from" has previously specified a wealth
     * transfer to this "to".
     */
    public void intendToTransferWealth(final int from, final int to, final int percentage, final boolean isWealthSquared) {
        if(from < 1 || from > this.populationSize){
            throw new IllegalArgumentException();
        }else if(to == from || to < 1 || to > this.populationSize){
            throw new IllegalArgumentException();
        } else if(percentage < 1 || percentage > 100){
            throw  new IllegalArgumentException();
        }else if(to == 1){
            throw new IllegalArgumentException();
        }else if(this.wealth.get(from) != null){
            throw new IllegalArgumentException();
        }else if(this.parent.get(to) != null){
            throw new IllegalArgumentException();
        }
        this.percentageTotal.putIfAbsent(from, 100);
        if(this.percentageTotal.get(from) - percentage < 0){
            throw new IllegalArgumentException();
        }
        parent.put(to, from);
        double multiplier = 100.0/percentage;
        this.weight.put(to, multiplier);
        this.isSquared.put(to, isWealthSquared);
        this.percentageTotal.put(from, this.percentageTotal.get(from) - percentage);
    }

    /** Specifies the wealth that the person must have in order for the overall
     * wealth transfer problem to be considered solved.
     *
     * @param id must correspond to a member of the population from 2..populationSize
     * @param wealth the wealth that the designated person must have as a result
     * of wealth transfers, must be positive.
     * @throw IllegalArgumentException if parameter Javadoc specifications aren't
     * met.
     */
    public void setRequiredWealth(final int id, final int wealth) {
        if(id < 2 || id > this.populationSize){
            throw new IllegalArgumentException();
        }else if(wealth <= 0){
            throw new IllegalArgumentException();
        }
        if(this.wealth.get(id) != null){
            throw new IllegalArgumentException();
        }
        if(this.parent.containsValue(id)){
            throw new IllegalArgumentException();
        }
        this.wealth.put(id, wealth);
        // fill me in
    }

    /** Solves the wealth transfer problem by determining the MINIMAL amount of
     * wealth that "person with id of 1" must transfer such that all members of
     * the population receive the wealth that they have been promised.
     *
     * The amount of wealth that a person has been promised is specified by
     * invocations of setRequiredWealth().  The amount of wealth that a person
     * actually receives is specified by invocations of intendToTransferWealth().
     * The "person with id of 1" initiates all wealth transfers between members
     * of the population.  This method returns the minimum amount of that
     * initiating wealth transfer that will satisfy the remaining population's
     * needs.
     *
     * NOTE: at the time that this method is invoked, all persons transfering
     * wealth MUST be on record as intending to transfer 100 percent of their
     * wealth.  If this pre-condition doesn't hold, the implementation MUST throw
     * an IllegalStateException in lieu of solving the problem.
     *
     * @return the minimum amount transfered by person with id #1: must be
     * accurate to four digits after the decimal point.
     */
    public double solveIt() {
        for(int j: percentageTotal.values()){
            if(j != 0){
                throw new IllegalStateException();
            }
        }
        DecimalFormat mow = new DecimalFormat("#.####");
        double max = 0;
        for(int i: this.wealth.keySet()){
            if(getRoot(i) != 1){
                throw new IllegalArgumentException();
            }
            double cash = this.wealth.get(i);
            int k = i;
            while(this.parent.get(k) != null){
                if(this.weight.get(k) != null){
                    if(isSquared.get(k)){
                        cash = Math.sqrt(cash);
                    }
                    cash *= this.weight.get(k);
                    k  = this.parent.get(k);
                }
            }
            if(cash > max){
                max = cash;
            }
        }
        return Double.parseDouble(mow.format(max));
    }

    private int getRoot(int id){
        while(this.parent.get(id) != null){
            id  = this.parent.get(id);
        }
        return id;
    }


} // class