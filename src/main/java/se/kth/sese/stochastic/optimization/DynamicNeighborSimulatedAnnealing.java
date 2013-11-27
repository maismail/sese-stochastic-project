package se.kth.sese.stochastic.optimization;

import java.io.File;
import java.io.IOException;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.EvaluatedSamplePoint;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public class DynamicNeighborSimulatedAnnealing extends Optimizer{

  private final static int K = 1;
  private final int NUM_ITERATIONS;
  public DynamicNeighborSimulatedAnnealing(Sampler sampler, ObjectiveFunction func, File outDir, int numIterations) throws IOException {
    super(sampler, func, outDir);
    this.NUM_ITERATIONS = numIterations;
  }
  
  public SamplePoint getOptimal() throws IOException {
    double T0 = 100;
    double t = T0;
   EvaluatedSamplePoint curr = getInitial();
   EvaluatedSamplePoint best = curr;
    for (int it = 0; it < NUM_ITERATIONS; it++) {
      System.out.println("SA ----- ITER = " + it + " T=" + t);
      
      EvaluatedSamplePoint newS = generateNewSample(curr, t/T0);
      
      System.out.println("SA ----- Best =" + best);
      System.out.println("SA ----- Curr =" + curr );
      System.out.println("SA ----- New =" + newS);

      if (acceptMetropolis(curr.getEval(), newS.getEval(), t)) {
        curr = newS;
        if (best.getEval() > curr.getEval()) {
          best = curr;
        }
        writeSample(it, newS, true, best);
      } else {
        writeSample(it, newS, false, best);
      }
      t = cool(t);
    }
    closeWriters();
    return best;
  }

  private EvaluatedSamplePoint getInitial() {
    SamplePoint curr = sampler.getSample();
    double currE = func.eval(curr);
    return new EvaluatedSamplePoint(curr, currE);
  }

  private EvaluatedSamplePoint generateNewSample(SamplePoint curr, double tRatio) {
    SamplePoint newS = sampler.getSample(curr, tRatio);
    double newE = func.eval(newS);
    return new EvaluatedSamplePoint(newS, newE);
  }
  private double cool(double t){
    return t * 0.995;
  }

  private double cool2(double t, int it){
    return t/(it+1);
  }
  
  private double cool3(double t, int it){
    return t/Math.log(it+1.1);
  }
  
  private boolean acceptMetropolis(double engery, double newEngery, double temperature) {
    if (newEngery <= engery) {
      return true;
    }
    return Math.exp(-1 * (newEngery - engery) / (temperature * K)) > Math.random();
  }
}
