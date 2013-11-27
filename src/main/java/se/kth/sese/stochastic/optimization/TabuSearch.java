package se.kth.sese.stochastic.optimization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.EvaluatedSamplePoint;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

public class TabuSearch extends Optimizer{

  private static final int TABU_LIST = 10;
  private static final int CANDIDATE_LIST_SIZE = 20;
  private static final double NEIGHBERHOOD_FACTOR = 0.2;
  
  private int NUM_ITERATIONS;
  
  public TabuSearch(Sampler sampler, ObjectiveFunction func, File outDir, int numIterations) throws IOException{
    super(sampler, func, outDir);
    this.NUM_ITERATIONS = numIterations;
  }
  
  @Override
  public SamplePoint getOptimal() throws IOException {
    EvaluatedSamplePoint curr = getInitial();
    EvaluatedSamplePoint best = curr;
    LinkedList<SamplePoint> tabuList = new LinkedList<SamplePoint>();
    for(int it=0;it<NUM_ITERATIONS;it++){
      List<EvaluatedSamplePoint> candidates = generateCandidates(curr, tabuList);
      EvaluatedSamplePoint best_candidate = candidates.get(0);
      if(best_candidate.getEval() < curr.getEval()){
        curr = best_candidate;
        if(best_candidate.getEval() < best.getEval()){
          best = best_candidate;
        }
        tabuList.add(best_candidate.getSamplePoint());
        while(tabuList.size() > TABU_LIST){
          tabuList.removeFirst();
        }
        writeSample(it, curr, true, best);
      }else{
        writeSample(it, curr, false, best);
      }
      System.out.println("TS ----> It=" + it + " Best -> " + best);;
    }
    return best.getSamplePoint();
  }
  
  private EvaluatedSamplePoint getInitial(){
    SamplePoint curr = sampler.getSample();
    double currE = func.eval(curr);
    return new EvaluatedSamplePoint(curr, currE);
  }
  
  private List<EvaluatedSamplePoint> generateCandidates(SamplePoint curr, LinkedList<SamplePoint> tabuList) {
    List<EvaluatedSamplePoint> candidates = new ArrayList<EvaluatedSamplePoint>();
    for(int i=0;i<CANDIDATE_LIST_SIZE;i++){
      SamplePoint s = sampler.getSample(curr, NEIGHBERHOOD_FACTOR);
      while(tabuList.contains(s)){
        s = sampler.getSample();
      }
      double eval = func.eval(s);
      candidates.add(new EvaluatedSamplePoint(s, eval));
    }
    Collections.sort(candidates);
    return candidates;
  }
  
  
}
