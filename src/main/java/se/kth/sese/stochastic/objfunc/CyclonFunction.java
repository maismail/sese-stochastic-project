package se.kth.sese.stochastic.objfunc;

import java.math.BigInteger;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.sics.kompics.example.p2p.experiments.cyclon.CyclonSimulationExperiments;
import se.sics.kompics.p2p.overlay.cyclon.CyclonConfiguration;

public class CyclonFunction implements ObjectiveFunction{

  private final static int SHUFFLE_PERIOD = 1000;
  private final static int SHUFFLE_TIMEOUT = 3000;
  private final static int COLLECT_DATA_AFTER = 60000;
  
  private final int NUM_PEERS;
  public CyclonFunction(int numPeers){
    this.NUM_PEERS = numPeers;
  }
  
  //[0, 5] -> [1, 100]
  public double eval(SamplePoint p) {
    double shuffleLengthRatio = p.getData()[0];
    int cacheSize = (int) p.getData()[1];
    int shuffleLength = (int)shuffleLengthRatio * cacheSize;
    
    CyclonConfiguration cyclonConfiguration = new CyclonConfiguration(shuffleLength, cacheSize, SHUFFLE_PERIOD, SHUFFLE_TIMEOUT,
            new BigInteger("2").pow(13), 20);
    try {
      CyclonSimulationExperiments.runCyclon(cyclonConfiguration, 0L, NUM_PEERS, COLLECT_DATA_AFTER);
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
    
    double criteria = Double.valueOf(System.getProperty("CLUSTERING_COEFFICIENT"));
    return criteria;
  }

}
