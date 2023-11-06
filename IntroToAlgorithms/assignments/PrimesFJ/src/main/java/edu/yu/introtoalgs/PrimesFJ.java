package edu.yu.introtoalgs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/** Implements PrimeCalculator interface with Java's Fork/Join parallelism
 * framework.  The implementation must determine what threshold value to use,
 * but should favor thresholds that produce good results for "end" values of
 * (at least) hundreds of millions).
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

public class PrimesFJ implements PrimeCalculator {

    /** Constructor
     *
     */
    public PrimesFJ() {
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
        class MyRecursiveTask extends RecursiveTask<Long> {
            private final long threshold;
            private final long newStart;
            private final long newEnd;

            public MyRecursiveTask(long threshold, long newStart, long newEnd) {
                this.threshold = threshold;
                this.newStart = newStart;
                this.newEnd = newEnd;
            }

            protected Long compute() {
                int counter = 0;
                //if work is above threshold, break tasks up into smaller tasks
                if(newEnd - newStart <= threshold) {
                    for (long i = newStart; i <= newEnd; i++){
                        if (isPrime (i)){
                            counter++;
                        }
                    }
                    //return (long)counter;

                } else {
                    MyRecursiveTask left = new MyRecursiveTask(threshold, newStart, ((newStart + newEnd)/2) - 1);
                    MyRecursiveTask right = new MyRecursiveTask(threshold , (newStart + newEnd)/2, newEnd);
                    right.fork();
                    counter+=left.compute();
                    counter+=right.join();
                }
                return (long)counter;
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
        long sum = 0;
        int parallelism = Runtime.getRuntime().availableProcessors();
        int threshold = 500;
        ForkJoinTask<Long> task = new MyRecursiveTask(threshold, start, end);
        final ForkJoinPool fjPool = new ForkJoinPool(parallelism);
        sum = fjPool.invoke(task);
        fjPool.shutdown();
        return  (int)sum;
    }
}   // PrimesFJ