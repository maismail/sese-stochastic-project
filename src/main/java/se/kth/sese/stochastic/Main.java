package se.kth.sese.stochastic;

import java.io.File;
import java.io.IOException;
import se.kth.sese.stochastic.objfunc.BraninFunction;
import se.kth.sese.stochastic.objfunc.CyclonFunction;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.objfunc.RosenbrockFunction;
import se.kth.sese.stochastic.optimization.Optimizer;
import se.kth.sese.stochastic.optimization.DynamicNeighborSimulatedAnnealing;
import se.kth.sese.stochastic.optimization.SimulatedAnnealing;
import se.kth.sese.stochastic.optimization.TabuSearch;
import se.kth.sese.stochastic.optimization.TabuSimulatedAnnealing;
import se.kth.sese.stochastic.sampling.RandomSampler;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public class Main {

  public static void main(String[] args) throws IOException {
    File outDir = new File("/home/mahmoud/stochasticOut");
    
    //cyclon
    File cyclonDir = new File(outDir, "cyclon");
    //shuffle length ratio , cache size
    double[] min = new double[]{0.1, 10};
    double[] max = new double[]{1, 50};
    
    //Rosenbrock
    //double[] min = new double[]{0, 0};
    //double[] max = new double[]{5, 5};
    
    //Branin
    //double[] min = new double[]{-5, 0};
    //double[] max = new double[]{10, 15};
    
    boolean[] isInteger = new boolean[]{false, true};
    ObjectiveFunction func = new CyclonFunction(200, cyclonDir);
    Sampler random = new RandomSampler(min, max, isInteger);
    Optimizer sa = new TabuSearch(random, func, outDir, 10);
    long t = System.nanoTime();
    SamplePoint opt = sa.getOptimal();
    long el = (long) ((System.nanoTime() - t) / Math.pow(10, 6));
    System.out.println("Optimum = " + opt + " in " + el + " msecs");
  }
}
