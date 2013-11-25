package se.kth.sese.stochastic.sampling;

import java.util.Arrays;

public class SamplePoint {

  private final double[] sample;

  public SamplePoint(int dim) {
    this.sample = new double[dim];
  }

  public SamplePoint(double[] sample) {
    this.sample = sample;
  }

  public int getDim() {
    return sample.length;
  }

  public double[] getData() {
    return sample;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(double d : sample){
      sb.append(d);
      sb.append(" ");
    }
    return sb.toString().trim();
  }
}
