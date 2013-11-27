package se.kth.sese.stochastic.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * http://www.mathworks.com/matlabcentral/fileexchange/4352-latin-hypercube-sampling/content/lhsu.m
 * @author mahmoud
 */
public class LatinHypercube extends Sampler {

  private final int numSamples;
  private final List<List<Double>> data = new ArrayList<List<Double>>();

  public LatinHypercube(double[] min, double[] max, boolean[] isInteger, int numSamples) {
    super(min, max, isInteger);
    this.numSamples = numSamples;
    init();
  }

  private void init() {
    for (int i = 0; i < getDim(); i++) {
      double value, step;
      step = (max[i] - min[i]) / numSamples;
      Random r = new Random();
      List<Double> row = new ArrayList<Double>();
      for (int j = 0; j < numSamples; j++) {
        value = min[i] + (j - r.nextDouble()) * step;
        if(isInteger(i))
          value = Math.round(value);
        row.add(value);
      }
      data.add(row);
    }
  }

  public SamplePoint getSample() {
    double[] sample = new double[getDim()];
    for (int i = 0; i < getDim(); i++) {
      List<Double> values = data.get(i);
      Collections.shuffle(values);
      sample[i] = values.remove(0);
    }
    return new SamplePoint(sample);
  }

  @Override
  public SamplePoint getSample(SamplePoint center, double neighberhoodFactor) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
