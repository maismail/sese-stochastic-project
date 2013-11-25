package se.kth.sese.stochastic.optimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public abstract class Optimizer {

  protected Sampler sampler;
  protected ObjectiveFunction func;
  private BufferedWriter sampleWriter;
  
  public Optimizer(Sampler sampler, ObjectiveFunction func, File outDir) throws IOException {
    this.sampler = sampler;
    this.func = func;
    sampleWriter = new BufferedWriter(new FileWriter(new File(outDir, sampler.getClass().getSimpleName() + "-samples.dat")));
  }

  public abstract SamplePoint getOptimal() throws IOException;
  
   protected void writeSample(int it, SamplePoint p, double eval, boolean accepted) throws IOException{
    String str = it + " " + p.toString() + " " + eval;
    if(!accepted)
      str += " " + p.toString();
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
