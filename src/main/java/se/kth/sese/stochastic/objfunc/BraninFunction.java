package se.kth.sese.stochastic.objfunc;

import se.kth.sese.stochastic.sampling.SamplePoint;

public class BraninFunction implements ObjectiveFunction {

  //[-5, 0] -> [10, 15]
  public double eval(SamplePoint p) {
    double x1 = p.getData()[0];
    double x2 = p.getData()[1];
    return Math.pow((x2 - (5.1 / (4 * Math.pow(Math.PI, 2))) * Math.pow(x1, 2) + 5 * x1 / Math.PI - 6), 2) + 10 * (1 - 1 / (8 * Math.PI)) * Math.cos(x1) + 10;
  }
}
