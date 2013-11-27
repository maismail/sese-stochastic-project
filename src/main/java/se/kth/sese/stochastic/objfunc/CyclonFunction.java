package se.kth.sese.stochastic.objfunc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.sics.kompics.example.p2p.experiments.cyclon.Cyclon;
import se.sics.kompics.p2p.experiment.cyclon.CyclonDataPoint;

public class CyclonFunction implements ObjectiveFunction{

  private final static int SHUFFLE_PERIOD = 500;
  private final static int SHUFFLE_TIMEOUT = 1500;
  private final static int NUM_CYCLES = 70;
  
  private final static double ERR = 0.001;
  
  private final int NUM_PEERS;
  private File cyclonDir;
  private File cyclonExec;
  
  public CyclonFunction(int numPeers, File outDir){
    this.NUM_PEERS = numPeers;
    this.cyclonDir = outDir;
    this.cyclonExec = new File(cyclonDir, "cyclon.jar");
  }
  
  //[0, 1] -> [1, 100]
  public double eval(SamplePoint p) {
    double shuffleLengthRatio = p.getData()[0];
    int cacheSize = (int) p.getData()[1];
    int shuffleLength = (int)Math.round(shuffleLengthRatio * cacheSize);
     
    double cyclesC = 0;
    try {
      System.out.println("Run Cyclon with cacheSize=" + cacheSize + " , shuffleLength=" + shuffleLength);
      ArrayList<CyclonDataPoint> data = runExp(shuffleLength, cacheSize);
      int cycles = getCycles(data);
      writeData(data, cycles, shuffleLength, cacheSize);
      double CC=data.get(data.size()-1).clusteringCoefficient;
      System.out.println("Clustering Coefficient = " + CC);
      cyclesC = cycles * CC;
    } catch (Throwable ex) {
      ex.printStackTrace();
    }

    return cyclesC;
  }
  
  private ArrayList<CyclonDataPoint> runExp(int shuffleLength, int cacheSize) throws Throwable{
//    ArrayList<CyclonDataPoint> data = Cyclon.runExperiment(shuffleLength, cacheSize, NUM_CYCLES, SHUFFLE_PERIOD, SHUFFLE_TIMEOUT, NUM_PEERS);
//    return data;
    String params = shuffleLength + " " + cacheSize +" " + NUM_CYCLES + " " + SHUFFLE_PERIOD + " " + SHUFFLE_TIMEOUT + " " + NUM_PEERS;
    String cmd = "java -Xmx1024m -XX:PermSize=256m -XX:+CMSClassUnloadingEnabled -XX:-UseConcMarkSweepGC -jar " + cyclonExec.getAbsolutePath() + " " + params;
    System.out.println(cmd);
    Process ps = Runtime.getRuntime().exec(cmd);
    ps.waitFor();
    return Cyclon.readData();
  }
  
  private int getCycles(ArrayList<CyclonDataPoint> data ){
    int dataLen = data.size();
    int numPoints = 5;
    double prevRollingAvg = 0;
    for (int i = numPoints; i < dataLen - numPoints; i++) {
      double currentRollingAvg = 0;
      for (int j = 0; j <= numPoints ; j++) {
        currentRollingAvg += data.get(i - j).clusteringCoefficient;
      }
      currentRollingAvg /= (numPoints+1);
      if (Math.abs(currentRollingAvg - prevRollingAvg) < ERR) {
          return i;
        }else{
        prevRollingAvg = currentRollingAvg;
      }
    }
    return NUM_CYCLES;
  }
  
  private void writeData(ArrayList<CyclonDataPoint> data, int cycles, int shuffleLength, int cacheSize) throws IOException {
    File runFile = new File(cyclonDir, cycles + "-run-" + shuffleLength + "-" + cacheSize + ".dat");
    BufferedWriter writer = new BufferedWriter(new FileWriter(runFile));

    writer.write("# index clusteringCoefficient averagePathLength inDegreeMean outDegreeMean");
    writer.newLine();
    for (CyclonDataPoint dp : data) {
      writer.write(dp.index + " " + dp.clusteringCoefficient + " " + dp.averagePathLength + " " + dp.inDegree.getMean() + " " + dp.outDegree.getMean());
      writer.newLine();
    }
    writer.flush();
    writer.close();
  }

}
