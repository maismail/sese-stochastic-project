package se.kth.sese.stochastic;

import java.io.File;
import java.io.IOException;
import se.kth.sese.stochastic.objfunc.BraninFunction;
import se.kth.sese.stochastic.objfunc.CyclonFunction;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.objfunc.RosenbrockFunction;
import se.kth.sese.stochastic.optimization.Optimizer;
import se.kth.sese.stochastic.optimization.SimulatedAnnealing;
import se.kth.sese.stochastic.sampling.LatinHypercube;
import se.kth.sese.stochastic.sampling.RandomSampler;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public class Main {
  
  public static void main(String[] args) throws IOException{
    File outDir = new File("/home/mahmoud/stochasticOut");
    //shuffle length ratio , cache size
    double[] min = new double[]{-5 , 0};
    double[] max = new double[]{10, 15};
    ObjectiveFunction func = new BraninFunction();
    Sampler random = new RandomSampler(min, max, false);
    Optimizer sa = new SimulatedAnnealing(random, func, outDir, 1000);
    SamplePoint opt = sa.getOptimal();
    System.out.println("Optimum = " + opt);
  }
}
