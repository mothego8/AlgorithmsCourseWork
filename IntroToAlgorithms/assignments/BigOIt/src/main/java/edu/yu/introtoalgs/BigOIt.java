package edu.yu.introtoalgs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BigOIt extends BigOItBase{

    private double ratio = 0;
    private int n;

    /** Given the name of a class that implements the BigOMeasurable API, creates
   * and executes instances of the class, such that by measuring the resulting
   * performance, can return the "doubling ratio" for that algorithm's
   * performance.
   *
   * See extended discussion in Sedgewick, Chapter 1.4, on the topic of
   * doubling ratio experiments.
   *
   * @param bigOMeasurable name of the class for which we want to compute a
   * doubling ratio.  The client guarantees that the corresponding class
   * implements the BigOMeasurable API, and can be constructed with a
   * no-argument constructor.  This method is therefore able to first construct
   * instances of this class, invoke "setup(n)" for whatever values of "n" are
   * desired, and then invoke "execute()" to measure the performance of a
   * single invocation of the algorithm.  The client is responsible for
   * ensuring that invocation of setup(n) produces a suitably populated
   * (perhaps randomized) set of state scaled as a function of n.
   * @param timeOutInMs number of milliseconds allowed for the computation.  If
   * the implementation has not computed an answer by this time, it should
   * return NaN.
   * @return the doubling ratio for the specified algorithm if one can be
   * calculated, NaN otherwise.
   * @throws IllegalArgumentException if bigOMeasurable parameter doesn't
   * fulfil the contract specified above or if some characteristic of the
   * algorithm  is at odds with the doubling ratio assumptions.
   */

   //have to create executor service to run
    @Override
    public double doublingRatio(String bigOMeasurable, long timeOutInMs) {
        if(bigOMeasurable == null || bigOMeasurable.isEmpty() || bigOMeasurable.isBlank()){
            throw new IllegalArgumentException();
        }
        ManualClassLoader warmup = new ManualClassLoader();
        warmup.load();
        Callable<Double> callableTask = () -> {
            double average = 0;
            double iterations = 0;
            try {
                //gets class object from the string param 
                Class<?> myClass = Class.forName(bigOMeasurable);
                //gets the constructer from the class object
                Constructor<?> constructor = myClass.getConstructor();
                //creates an instance of the class using constructor
                Object instanceOfMyClass = constructor.newInstance();
                double prev;
                ((BigOMeasurable) instanceOfMyClass).setup(1250);
                //run for loop that executes program
                //sedgewick
                //via stackoverflow
                long start = System.nanoTime();
                ((BigOMeasurable) instanceOfMyClass).execute();
                long end = System.nanoTime();
                prev = (end - start);
                this.n = 2500;
                int stop = 5;
                while(iterations < 1000){
                    ((BigOMeasurable) instanceOfMyClass).setup(n);
                    //sedgewick
                    long startTime = System.nanoTime();
                    ((BigOMeasurable) instanceOfMyClass).execute();
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);
                    this.ratio = duration/prev;
                    average += this.ratio;
                    prev = duration;
                    if(iterations%stop == 0){
                        this.n = 1250;
                        ((BigOMeasurable) instanceOfMyClass).setup(this.n);
                        long start2 = System.nanoTime();
                        ((BigOMeasurable) instanceOfMyClass).execute();
                        long end2 = System.nanoTime();
                        prev = (end2 - start2);
                    }
                    this.n *= 2;
                    iterations++;
                    this.ratio = average/iterations;
                } 
                return average/iterations;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            return Double.NaN;
        };
        ExecutorService executor = Executors.newFixedThreadPool(1);
        List<Callable<Double>> tasksList = Arrays.asList(callableTask, callableTask, callableTask, callableTask, callableTask);
        try{
            double passedRatio = 0;
            double calls = 0;
            List<Future<Double>> results = executor.invokeAll(tasksList, timeOutInMs, TimeUnit.MILLISECONDS);
            for(Future<Double> result : results) {
                try{
                    passedRatio += result.get();
                    calls++;
                }catch(CancellationException l){
                    if(passedRatio > 0){
                        return passedRatio/calls;
                    }else if(this.ratio > 0){
                        return this.ratio;
                    }
                    return Double.NaN;
                }
            }
            if(passedRatio > 0){
                return passedRatio/calls;
            }else{
                return Double.NaN;
            }
        }catch (InterruptedException | ExecutionException e1){
            e1.printStackTrace();
        }
        executor.shutdownNow();
        return 0.0;
    }

    public static class Dummy {
        public void m() {
        }
    }
    
    public static class ManualClassLoader {
        protected void load() {
            for (int i = 0; i < 100000; i++) {
                Dummy dummy = new Dummy();
                dummy.m();
            }
        }
    }
}
