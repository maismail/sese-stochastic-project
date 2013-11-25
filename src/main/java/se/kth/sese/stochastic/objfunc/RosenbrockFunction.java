package se.kth.sese.stochastic.objfunc;

import se.kth.sese.stochastic.sampling.SamplePoint;

public class RosenbrockFunction implements ObjectiveFunction {

  //[0, 0] => [5, 5]
  public double eval(SamplePoint p) {
    assert p.getDim() == 2;
    double x = p.getData()[0];
    double y = p.getData()[1];
    return Math.pow((1 - x), 2) + 100 * Math.pow((y - Math.pow(x, 2)), 2);
  }
}
