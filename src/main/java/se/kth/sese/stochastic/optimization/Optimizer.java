package se.kth.sese.stochastic.optimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.EvaluatedSamplePoint;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public abstract class Optimizer {

  protected Sampler sampler;
  protected ObjectiveFunction func;
  private BufferedWriter sampleWriter;
  
  public Optimizer(Sampler sampler, ObjectiveFunction func, File outDir) throws IOException {
    this.sampler = sampler;
    this.func = func;
    sampleWriter = new BufferedWriter(new FileWriter(new File(outDir, func.getClass().getSimpleName() + "-" + this.getClass().getSimpleName() + "-" + sampler.getClass().getSimpleName() +"-samples.dat")));
    sampleWriter.write("# Iteration newS newE bestS bestE rejectS rejectE");
    sampleWriter.newLine();
  }

  public abstract SamplePoint getOptimal() throws IOException;
  
   protected void writeSample(int it, EvaluatedSamplePoint sp, boolean accepted, EvaluatedSamplePoint best) throws IOException{
    String str = it + " " + sp.toString() + " " + best.toString();
    if(!accepted)
      str += " " + sp.toString();
    sampleWriter.write(str);
    sampleWriter.newLine();
    sampleWriter.flush();
  }
  
  protected void closeWriters() throws IOException{
    sampleWriter.close();
  }
  
  protected double error(double newBest, double lastBest){
    return (lastBest - newBest) / lastBest;
  }
  
}
