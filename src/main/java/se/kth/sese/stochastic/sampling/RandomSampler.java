package se.kth.sese.stochastic.sampling;

import org.apache.commons.math3.random.RandomDataGenerator;

public class RandomSampler extends Sampler {

  private final RandomDataGenerator rnd;
  
  public RandomSampler(double[] min, double[] max, boolean isInteger) {
    super(min, max, isInteger);
    this.rnd = new RandomDataGenerator();
  }

  public SamplePoint getSample() {
    double[] s = new double[getDim()];
    for(int i=0;i<s.length;i++){
      s[i] = rnd.nextUniform(min[i], max[i], true);
      if(isInteger())
        s[i] = Math.round(s[i]);
    }
    return new SamplePoint(s);
  }
  
}
