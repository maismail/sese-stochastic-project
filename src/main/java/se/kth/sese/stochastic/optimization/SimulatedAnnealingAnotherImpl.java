package se.kth.sese.stochastic.optimization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import se.kth.sese.stochastic.objfunc.ObjectiveFunction;
import se.kth.sese.stochastic.sampling.SamplePoint;
import se.kth.sese.stochastic.sampling.Sampler;

/**
 * Implementation based on http://apmonitor.com/me575/index.php/Main/SimulatedAnnealing
 * @author mahmoud
 */
public class SimulatedAnnealingAnotherImpl extends Optimizer {

  private final static int NUM_CYCLES = 100;
  private final static int NUM_TRIALS_PER_CYCLE = 50;
  private final static double PROP_ACCEPTANCE_AT_START = 0.7;
  private final static double PROP_ACCEPTANCE_AT_END = 0.01;
  private final static double INITIAL_TEMP = 1000;//-1 / Math.log(PROP_ACCEPTANCE_AT_START);
  private final static double FINAL_TEMP = 1;//-1 / Math.log(PROP_ACCEPTANCE_AT_END);
  private final static double COOLING_FACTOR = Math.pow((FINAL_TEMP / INITIAL_TEMP), (1.0/NUM_CYCLES-1.0));
  
  public SimulatedAnnealingAnotherImpl(Sampler sampler, ObjectiveFunction func, File outDir) throws IOException {
    super(sampler, func, outDir);
  }

  /*
   
  # Number of cycles
n = 50
# Number of trials per cycle
m = 50
# Number of accepted solutions
na = 0.0
# Probability of accepting worse solution at the start
p1 = 0.7
# Probability of accepting worse solution at the end
p50 = 0.001
# Initial temperature
t1 = -1.0/math.log(p1)
# Final temperature
t50 = -1.0/math.log(p50)
# Fractional reduction every cycle
frac = (t50/t1)**(1.0/(n-1.0))
# Initialize x
x = np.zeros((n+1,2))
x[0] = x_start
xi = np.zeros(2)
xi = x_start
na = na + 1.0
# Current best results so far
xc = np.zeros(2)
xc = x[0]
fc = f(xi)
fs = np.zeros(n+1)
fs[0] = fc
# Current temperature
t = t1
# DeltaE Average
DeltaE_avg = 0.0
for i in range(n):
    print 'Cycle: ' + str(i) + ' with Temperature: ' + str(t)
    for j in range(m):
        # Generate new trial points
        xi[0] = xc[0] + random.random() - 0.5
        xi[1] = xc[1] + random.random() - 0.5
        # Clip to upper and lower bounds
        xi[0] = max(min(xi[0],1.0),-1.0)
        xi[1] = max(min(xi[1],1.0),-1.0)
        DeltaE = abs(f(xi)-fc)
        if (f(xi)>fc):
            # Initialize DeltaE_avg if a worse solution was found
            #   on the first iteration
            if (i==0 and j==0): DeltaE_avg = DeltaE
            # objective function is worse
            # generate probability of acceptance
            p = math.exp(-DeltaE/(DeltaE_avg * t))
            # determine whether to accept worse point
            if (random.random()<p):
                # accept the worse solution
                accept = True
            else:
                # don't accept the worse solution
                accept = False
        else:
            # objective function is lower, automatically accept
            accept = True
        if (accept==True):
            # update currently accepted solution
            xc[0] = xi[0]
            xc[1] = xi[1]
            fc = f(xc)
            # increment number of accepted solutions
            na = na + 1.0
            # update DeltaE_avg
            DeltaE_avg = (DeltaE_avg * (na-1.0) +  DeltaE) / na
    # Record the best x values at the end of every cycle
    x[i+1][0] = xc[0]
    x[i+1][1] = xc[1]
    fs[i+1] = fc
    # Lower the temperature for next cycle
    t = frac * t

# print solution
print 'Best solution: ' + str(xc)
print 'Best objective: ' + str(fc)

   */
  @Override
  public SamplePoint getOptimal() {
    double temp = INITIAL_TEMP;
    int num_accepted_solutions = 1;
    // K in the equation
    double deltaEAvg = 0;
    
    SamplePoint curr = sampler.getInitialSample();
    double currE = func.eval(curr);
    
    
    for(int c=0; c< NUM_CYCLES; c++){
      for(int tr=0; tr< NUM_TRIALS_PER_CYCLE ; tr++){
        
        boolean accept = false;
        SamplePoint newS = sampler.getSample();
        double newE = func.eval(newS);
        double deltaE = Math.abs(newE - currE);
        
        if(newE > currE){
          if(c==0 && tr==0){
            deltaEAvg = deltaE;
          }
          
          double p = Math.exp(-deltaE / (deltaEAvg * temp));
          if(p > Math.random()){
            accept = true;
          }
        }else{
          accept = true;
        }
        
        if(accept){
          curr = newS;
          currE = newE;
          num_accepted_solutions++;
          deltaEAvg = (deltaEAvg * (num_accepted_solutions-1.0) +  deltaE) / num_accepted_solutions;
        }
        
      }
      System.out.println(curr + " ==> " + currE);
      temp = temp * COOLING_FACTOR;
    }
    
    return curr;
  }
}
