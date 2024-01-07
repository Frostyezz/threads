import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

class ThreadsSimulator {
    int q1, q2, q3, k, r, timeUnit = 0;
    ArrayList<Process> processes;
    OutputManager outputManager = new OutputManager();

    private Queue<Process> pq1 = new LinkedList<>();
    private Queue<Process> pq2 = new LinkedList<>();
    private Queue<Process> pq3 = new LinkedList<>();
    private Queue<Process> ioQueue = new LinkedList<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            ThreadsSimulator simulator = new ThreadsSimulator();
            String simConditions = reader.readLine();
            simulator.parseSimulationConditions(simConditions);
            simulator.parseProcesses(reader);
            reader.close();

            simulator.simulate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean arePqsClear() {
        return pq1.isEmpty() && pq2.isEmpty() && pq3.isEmpty();
    }

    private void checkPendingProcesses() {
        if(timeUnit == 0) return;
        for (Process process : processes) {
            if (process.startTime == timeUnit) {
                addToQueue(process);
            }
        }
    }

    private void executeCpu(Process process) {
        Phase currentPhase = process.getCurrentPhase();
        int initialCpuTimesCount = currentPhase.currentCpuTime;
        int currentQueueTimeLimit = process.currentQueue == 1 ? q1 : process.currentQueue == 2 ? q2 : q3;

        if(initialCpuTimesCount == 0) {
            return;
        }

        while (currentPhase.currentCpuTime > 0) {
            outputManager.addExecutionLog(timeUnit, pq1, pq2, pq3, ioQueue, process);
            timeUnit++;
            currentPhase.currentCpuTime--;

            executeIo(true);
            
            if (initialCpuTimesCount - currentPhase.currentCpuTime == currentQueueTimeLimit && currentPhase.currentCpuTime != 0) {
                process.penaltyPoints++;
                addToQueue(process);
                checkPendingProcesses();
                return;
            }
            
            checkPendingProcesses();
        }

        if (currentPhase.currentCpuTime != 0) {
            return;
        }

        if (currentQueueTimeLimit - currentPhase.cpuTimesCount > 0) {
            process.awardPoints++;
        }
    }

    private class CpuThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if (arePqsClear() && ioQueue.isEmpty()) {
                    outputManager.writeOutput();
                    System.exit(0);
                }

                Process currentProcess = null;

                synchronized (pq1) {
                    currentProcess = pq1.poll();
                }

                if (currentProcess == null) {
                    synchronized (pq2) {
                        currentProcess = pq2.poll();
                    }
                }

                if (currentProcess == null) {
                    synchronized (pq3) {
                        currentProcess = pq3.poll();
                    }
                }

                if(currentProcess == null) {
                    synchronized (ioQueue) {
                        currentProcess = ioQueue.peek();
                    }
                }

                if (currentProcess != null) {
                    if(currentProcess.hasCompleted) {
                        continue;
                    }

                    executeCpu(currentProcess);

                    if (currentProcess.hasCpuTimeRemaining()) {
                        continue;
                    }

                    if (currentProcess.hasIoTimeRemaining()) {
                        synchronized (ioQueue) {
                            ioQueue.offer(currentProcess);
                        }
                        if (arePqsClear()) {
                            while (currentProcess.hasIoTimeRemaining()) {
                                outputManager.addExecutionLog(timeUnit, pq1, pq2, pq3, ioQueue, currentProcess);
                                executeIo(false);
                                timeUnit++;
                            }

                            if(currentProcess.getCurrentPhase().repeatCount <= 0) {
                                if (currentProcess.hasMorePhases()) {
                                    outputManager.addPhaseCompletion(currentProcess);
                                    currentProcess.moveToNextPhase(outputManager);
                                } 
                            } else {
                                currentProcess.getCurrentPhase().resetTimes();
                            }
                            addToQueue(currentProcess);
                        }
                        
                    } else {
                        if (currentProcess.hasMorePhases() && currentProcess.getCurrentPhase().repeatCount <= 0) {
                            outputManager.addPhaseCompletion(currentProcess);
                            currentProcess.moveToNextPhase(outputManager);
                            addToQueue(currentProcess);
                        }
                    }
                }
            }
        }
    }

    private void executeIo(boolean resetTimes) {
        if (!ioQueue.isEmpty()) {
                Process ioProcess;
                synchronized (ioQueue) {
                    ioProcess = ioQueue.peek();
                }

                Phase currentPhase = ioProcess.getCurrentPhase();
                currentPhase.currentIoTime -= 1;

                String data = ioProcess.name + " " + ioProcess.alias;
                FileUtil.writeFile("output.txt", data);
                FileUtil.readFile("output.txt");
                
                if (currentPhase.currentIoTime == 0) {
                    synchronized (ioQueue) {
                        ioQueue.poll();
                    }

                    ioProcess.getCurrentPhase().repeatCount--;
                    if (resetTimes == true && ioProcess.getCurrentPhase().repeatCount > 0) {
                        ioProcess.getCurrentPhase().resetTimes();
                        addToQueue(ioProcess);
                    }
                }
            }
    }

    private void addToQueue(Process process) {
        switch (process.getPriorityQueue(k, r)) {
            case 1:
                synchronized (pq1) {
                    pq1.offer(process);
                }
                break;
            case 2:
                synchronized (pq2) {
                    pq2.offer(process);
                }
                break;
            case 3:
                synchronized (pq3) {
                    pq3.offer(process);
                }
                break;
        }
    }

    public void simulate() {
        CpuThread cpuThread = new CpuThread();
        cpuThread.start();

        for (Process process : processes) {
            if (process.startTime > 0) {
                continue;
            }
            addToQueue(process);
        }
    }


    private void parseSimulationConditions(String simConditions) {
        String[] parsedConditions = simConditions.split("\\s+");
        String[] conditions = Arrays.copyOfRange(parsedConditions, 1, parsedConditions.length);
        for (String condition : conditions) {
            String[] parts = condition.split("=");
            String variable = parts[0].toLowerCase();
            int value = Integer.parseInt(parts[1]);
            
            switch (variable) {
                case "q1":
                    q1 = value;
                    break;
                case "q2":
                    q2 = value;
                    break;
                case "q3":
                    q3 = value;
                    break;
                case "k":
                    k = value;
                    break;
                case "r":
                    r = value;
                    break;
            }
        }
    }

    private void parseProcesses(BufferedReader reader) throws IOException {
        processes = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals("[PROCESSES]")) {
                int processesCount = Integer.parseInt(reader.readLine().split("=")[1].trim());

                if (processesCount == 0) {
                    throw new IllegalArgumentException("Nu exista procese de simulat!");
                }

                if (processesCount > 26) {
                    throw new IllegalArgumentException("Nu se pot simula mai mult de 26 de procese!");
                }

                reader.readLine();
                reader.readLine();

                char alias = 'A';
                for (int i = 0; i < processesCount; i++) {
                    parseProcess(reader, alias++);
                }
            }
        }
        
        outputManager.addIndexTable(processes.size());
        outputManager.addSimulationData(q1, q2, q3, k, r);
        outputManager.addProcesses(processes);
    }

    private void parseProcess(BufferedReader reader, char alias) throws IOException {
        String name = "";
        int startTime = 0;
        int phasesCount = 0;
        ArrayList<Phase> phases = new ArrayList<>();

        String line = reader.readLine();

        while (line != null && !(line.startsWith("Process"))) {
            if (line.startsWith("name=")) {
                name = line.split("=")[1].trim();
            } else if (line.startsWith("start_time=")) {
                startTime = Integer.parseInt(line.split("=")[1].trim());
            } else if (line.startsWith("phases_count=")) {
                phasesCount = Integer.parseInt(line.split("=")[1].trim());
            } else if (line.startsWith("Phase#")) {
                int cpuTimesCount = Integer.parseInt(line.split("CPU=")[1].split(" ")[0]);
                int ioTimesCount = Integer.parseInt(line.split("I/O=")[1].split(" ")[0]);
                int repeatCount = Integer.parseInt(line.split("REPEAT=")[1].trim());
                phases.add(new Phase(cpuTimesCount, ioTimesCount, repeatCount));
            }
            line = reader.readLine();
        }
        
        processes.add(new Process(name, alias, startTime, phasesCount, phases));
    }
}