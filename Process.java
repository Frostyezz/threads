import java.util.ArrayList;

class Process {
    String name;
    char alias;
    int startTime;
    int phasesCount;
    ArrayList<Phase> phases;
    int penaltyPoints = 0;
    int awardPoints = 0;
    int currentQueue = 1;
    int currentPhase = 0;
    boolean hasCompleted = false;

    public Process(String name, char alias, int startTime, int phasesCount, ArrayList<Phase> phases) {
      this.name = name;
      this.alias = alias;
      this.startTime = startTime;
      this.phasesCount = phasesCount;
      this.phases = phases;
    }

    public int getPriorityQueue(int penaltyLimit, int awardLimit) {
      if (penaltyPoints >= penaltyLimit) {
        boolean isInPq3 = this.currentQueue == 3;
        this.penaltyPoints -= isInPq3 ? 0 : penaltyLimit;
        int nextQueue = isInPq3 ? 3 : this.currentQueue + 1;
        this.currentQueue = nextQueue;
        return nextQueue;
      }

      if (awardPoints >= awardLimit) {
        boolean isInPq1 = this.currentQueue == 1;
        this.awardPoints -= isInPq1 ? 0 : awardLimit;
        int nextQueue = isInPq1 ? 1 : this.currentQueue - 1;
        this.currentQueue = nextQueue;
        return nextQueue;
      }

      return this.currentQueue;
    }
    
    public boolean hasMorePhases() {
      if(this.hasCompleted) return false;

      return this.currentPhase < this.phasesCount;
    }

    public Phase getCurrentPhase() {
      return this.phases.get(this.currentPhase);
    }

    public void moveToNextPhase(OutputManager outputManager) {
      this.currentPhase++;

      if (this.currentPhase == this.phasesCount) {
        outputManager.addProcessCompletion(this);
        this.hasCompleted = true;
        return;
      }
    }

    public boolean hasCpuTimeRemaining() {
      return this.getCurrentPhase().currentCpuTime > 0;
    }

    public boolean hasIoTimeRemaining() {
      return this.getCurrentPhase().currentIoTime > 0;
    }
}