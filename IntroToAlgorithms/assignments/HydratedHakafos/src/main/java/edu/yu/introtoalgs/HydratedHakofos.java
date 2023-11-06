package edu.yu.introtoalgs;

/** Defines the API for the HydratedHakofos assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class HydratedHakofos {
    public HydratedHakofos() {
        // fill in to taste
    }

    /** Determines whether or not a table exists such that hakofos, beginning
     * from this table, can successfully traverse the entire circuit without
     * getting dehydrated ("negative water level").  If multiple tables are
     * valid, return the lowest-numbered table (numbered 1..n).  Otherwise,
     * return the sentinel value of "-1".
     *
     * @param waterAvailable an array, indexed from 0..n-1, such that the ith
     * element represents the amount of water available at the ith table.
     * @param waterRequired an array, indexed from 0..n-1, such that the ith
     * element represents the amount of water required to travel from the ith
     * table to the next table.
     * @return a number from 1..n or -1 as appropriate.
     *
     * NOTE: if the client supplies arrays of differing lengths, or if the arrays
     * are null, or empty, or if the client supplies non-positive values in
     * either of these arrays, the result is undefined.  In other words: you
     * don't have to check for these conditions (unless you want to prevent
     * errors during development).
     */
    public int doIt(int[] waterAvailable, int[] waterRequired) {
        if(waterAvailable.length != waterRequired.length){
            throw new IllegalArgumentException();
        }
        if(waterAvailable == null || waterRequired == null || waterRequired.length == 0 || waterAvailable.length == 0){
            throw new IllegalArgumentException();
        }
        // fill in with your implementation
        int available = 0;
        int needed = 0;
        int sumOfWater = 0;
        int index = 1;
        for(int i = 0; i < waterAvailable.length; i++){
            sumOfWater += waterAvailable[i] - waterRequired[i];
            if(sumOfWater < 0) {
                sumOfWater = 0;
                index = i + 2;
            }
            available += waterAvailable[i];
            needed += waterRequired[i];
        }
        //checking if hydrated hakafos is even possible
        if(available - needed < 0){
            return -1;
        }
        return index;
    } // doIt
}
