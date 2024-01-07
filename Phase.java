class Phase {
    int cpuTimesCount;
    int ioTimesCount;
    int repeatCount;
    int currentCpuTime = 0;
    int currentIoTime = 0;

    public Phase(int cpuTimesCount, int ioTimesCount, int repeatCount) {
        this.cpuTimesCount = cpuTimesCount;
        this.ioTimesCount = ioTimesCount;
        this.repeatCount = repeatCount;
        this.currentCpuTime = cpuTimesCount;
        this.currentIoTime = ioTimesCount;
    }

    public void resetTimes() {
        this.currentCpuTime = this.cpuTimesCount;
        this.currentIoTime = this.ioTimesCount;
    }
}