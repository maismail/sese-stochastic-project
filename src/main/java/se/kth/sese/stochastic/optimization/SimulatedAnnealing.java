package se.kth.sese.stochastic.optimization;

import java.io.File;
import java.io.IOException;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public class SimulatedAnnealing extends Optimizer{

  private final static int K = 1;
  private final int NUM_ITERATIONS;
  public SimulatedAnnealing(Sampler sampler, ObjectiveFunction func, File outDir, int numIterations) throws IOException {
    super(sampler, func, outDir);
    this.NUM_ITERATIONS = numIterations;
  }
  
  public SamplePoint getOptimal() throws IOException{
    double t = 1000;
    SamplePoint curr = sampler.getInitialSample();
    double currE = func.eval(curr);
    SamplePoint best = curr;
    double bestE = currE;
    int bestIt = 0;
    
    for(int it=0;it<NUM_ITERATIONS; it++) {
      System.out.println("SA ----- ITER = " + it + " T=" + t);
      SamplePoint newS = sampler.getSample();
      double newE = func.eval(newS);
      
      System.out.println("SA ----- Best =" + best + " --> " + bestE);
      System.out.println("SA ----- Curr =" + curr + " --> " + currE);
      System.out.println("SA ----- New =" + newS + " --> " + newE);
                  
      if(acceptMetropolis(currE, newE, t)){
       curr = newS;
       currE = newE;
       writeSample(it, newS, newE, true);
      }else{
        writeSample(it, newS, newE, false);
      }
      
      if(bestE > currE){
        bestE = currE;
        best = curr;
        bestIt = it;
      }
      
      t = cool(t);
    }
    closeWriters();
    System.out.println("Best IT " + bestIt);
    return best;
  }
  
  private double cool(double t){
    return t * 0.99;
  }

  private boolean acceptMetropolis(double engery, double newEngery, double temperature) {
    if (newEngery <= engery) {
      return true;
    }
    return Math.exp(-1 * (newEngery - engery) / (temperature * K)) > Math.random();
  }
}
