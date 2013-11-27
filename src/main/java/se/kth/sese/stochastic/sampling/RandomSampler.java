package se.kth.sese.stochastic.sampling;

import org.apache.commons.math3.random.RandomDataGenerator;

public class RandomSampler extends Sampler {

  private final RandomDataGenerator rnd;
  
  public RandomSampler(double[] min, double[] max, boolean[] isInteger) {
    super(min, max, isInteger);
    this.rnd = new RandomDataGenerator();
  }

  public SamplePoint getSample() {
    double[] s = new double[getDim()];
    for(int i=0;i<s.length;i++){
      s[i] = rnd.nextUniform(min[i], max[i], true);
      if(isInteger(i))
        s[i] = Math.round(s[i]);
    }
    return new SamplePoint(s);
  }

  @Override
  public SamplePoint getSample(SamplePoint center, double neighberhoodFactor) {
    if(neighberhoodFactor > 1)
      neighberhoodFactor = 1;
    if(neighberhoodFactor <= 0)
      neighberhoodFactor = 0.01;
    double[] c = center.getData();
    double[] s = new double[getDim()];
    for (int i = 0; i < s.length; i++) {
      double minC = c[i] - ((c[i] - min[i]) * neighberhoodFactor);
      double maxC = c[i] + ((max[i] - c[i]) * neighberhoodFactor);
      s[i] = rnd.nextUniform(minC, maxC);
      if (isInteger(i)) {
        s[i] = Math.round(s[i]);
      }
    }
    return new SamplePoint(s);
  }

  
}
