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
    for (double d : sample) {
      sb.append(d);
      sb.append(" ");
    }
    return sb.toString().trim();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 43 * hash + Arrays.hashCode(this.sample);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SamplePoint other = (SamplePoint) obj;
    if (!Arrays.equals(this.sample, other.sample)) {
      return false;
    }
    return true;
  }
}
