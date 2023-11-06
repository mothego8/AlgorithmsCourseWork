package edu.yu.introtoalgs;

import java.sql.Driver;

/** Implements PrimeCalculator interface by using exactly two threads to
 * partition the range of primes between them.  Each thread uses the "naive"
 * SerialPrimes algorithm to solve its part of the problem.
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

public class TwoThreadPrimes implements PrimeCalculator {

    /** Constructor
     *
     */
    private long start;
    private long end;


    public TwoThreadPrimes() {
        // your code (if any) goes here
    }

    @Override
    public int nPrimesInRange(final long start, final long end){
        if(start < 2){
            throw new IllegalArgumentException();
        }
        if(end < start){
            throw new IllegalArgumentException();
        }
        if(!(end < Long.MAX_VALUE)){
            throw new IllegalArgumentException();
        }
        // your code (if any) goes here
        this.start = start;
        this.end = end;
        int sum = 0;

        class SumThread extends Thread {
            SumThread(long low, long high) {
                this.low = low;
                this.high = high;
            }

            public void run() {
                int counter = 0;
                for (long i = low; i <= high; i++){
                    if (isPrime (i)){
                        counter++;
                    }
                }
                theSum += counter;
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

            private final long low;
            private final long high;
            private int theSum = 0;

        }
        SumThread[] sumThreads = new SumThread[ 2];
        //for(int i=0; i < 2 ; i++) { // do parallel computations
            sumThreads[0] = new SumThread(this.start, (this.start + this.end)/2 - 1);
            sumThreads[0].start();
//            this.start = (this.start + this.end)/2 + 1;
//            this.end = end;
            sumThreads[1] = new SumThread((this.start + this.end)/2, this.end);
            sumThreads[1].start();
        //}
        // Combine the results
        for(int i=0; i < 2 ; i++) {
            try {
                sumThreads[ i ]. join () ;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sum = sum + (sumThreads[i].theSum);
        }
        return sum;
    }

}