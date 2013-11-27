package se.kth.sese.stochastic.sampling;

public abstract class Sampler {
  
  protected final double[] min;
  protected final double[] max;
  private final boolean[] isInteger;
  public Sampler(double[] min, double[] max, boolean[] isInteger){
    this.min = min;
    this.max = max;
    this.isInteger = isInteger;
  }
  
  public abstract SamplePoint getSample();
  public abstract SamplePoint getSample(SamplePoint center, double neighberhoodFactor);
  
  public SamplePoint getInitialSample(){
    return new SamplePoint(min);
  }
  
  public int getDim(){
    return min.length;
  }
  
  public boolean isInteger(int var){
    return isInteger[var];
  }
}
