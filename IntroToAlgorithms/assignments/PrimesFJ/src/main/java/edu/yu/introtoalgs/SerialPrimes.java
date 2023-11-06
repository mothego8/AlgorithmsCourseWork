package edu.yu.introtoalgs;

import java.util.HashSet;
import java.util.Set;

/** Implements PrimeCalculator using a "naive" serial computation.
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

public class SerialPrimes implements PrimeCalculator {

    /** Constructor
     *
     */
    public SerialPrimes() {
        // your code (if any) goes here

    }

    @Override
    public int nPrimesInRange(final long start, final long end) {
        if(start < 2){
            throw new IllegalArgumentException();
        }
        if(end < start){
            throw new IllegalArgumentException();
        }
        if(!(end < Long.MAX_VALUE)){
            throw new IllegalArgumentException();
        }
        int counter = 0;
        for (long i = start; i <= end; i++){
            if (isPrime (i)){
                counter++;
            }
        }
        return counter;
    }
    public boolean isPrime(long n){
        if (n < 2){
            return false;
        }
        for (int i = 2; i <= Math.sqrt (n); i++) {
            if (n % i == 0){
                return false;
            }
        }
        return true;
    }
}