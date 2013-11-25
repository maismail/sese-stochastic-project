package se.kth.sese.stochastic.objfunc;

import se.kth.sese.stochastic.sampling.SamplePoint;

public interface ObjectiveFunction {
  
  public double eval(SamplePoint p);
}
