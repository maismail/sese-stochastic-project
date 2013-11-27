package se.kth.sese.stochastic.sampling;

public class EvaluatedSamplePoint extends SamplePoint implements Comparable<EvaluatedSamplePoint>{

  private final double eval;

  public EvaluatedSamplePoint(SamplePoint sample, double eval) {
    super(sample.getData());
    this.eval = eval;
  }

  public double getEval() {
    return eval;
  }

  public SamplePoint getSamplePoint(){
    return new SamplePoint(getData());
  }
  
  public int compareTo(EvaluatedSamplePoint o) {
    return Double.compare(eval, o.eval);
  }

  @Override
  public String toString() {
    return super.toString() + " " + eval;
  }
  
}
