import java.util.ArrayList;
import java.util.Queue;

public class OutputManager {
  StringBuilder content = new StringBuilder();

  public OutputManager() {
    content.append("<html>")
        .append("<head>")
        .append("<title>THREADS SIMULATION</title>")
        .append("</head>")
        .append("<body>")
        .append("<a name=\"top\"></a>")
        .append("<br>")
        .append("<br>")
        .append("<center>")
        .append("<br>")
        .append("<br>")
        .append("<h1>THREADS SIMULATION</h1>")
        .append("<br>")
        .append("<br>");
  }
  
  public void addIndexTable(int processCount) {
    content.append("<table width=\"30%\">")
        .append("<tbody>")
        .append("<tr>")
        .append(
            "<td><a href=\"#sid\" onmouseover=\"ShowStatus('Simulation Input Data')\">Simulation Input Data</a></td>")
        .append("</tr>")
        .append("<tr>")
        .append(
            "<td valign=\"top\"><a href=\"#pd\" onmouseover=\"ShowStatus('Processes Data')\">Processes Data</a></td>")
        .append("<td>");

    for (int i = 1; i <= processCount; i++) {
      content.append("<a href=\"#p").append(i).append("\" onmouseover=\"ShowStatus('Process #").append(i)
          .append("')\">Process #").append(i).append("</a><br>");
    }

    content.append("</td>")
        .append("</tr>")
        .append("<tr>")
        .append(
            "<td><a href=\"#sod\" onmouseover=\"ShowStatus('Simulation Output Data')\">Simulation Output Data</a></td>")
        .append("</tr>")
        .append("</tbody>")
        .append("</table>")
        .append("<br>")
        .append("<br>")
        .append("<br>")
        .append("<br>")
        .append("<br>");
  }

  public void addSimulationData(int q1, int q2, int q3, int k, int r) {
    content.append("<table width=\"100%\" border=\"1\">")
        .append("<caption align=\"left\"><a name=\"sid\"></a><b>SIMULATION INPUT DATA</b></caption>")
        .append("<thead align=\"center\">")
        .append("<tr>")
        .append("<th>MAX PRIORITY</th>")
        .append("<th>NORMAL PRIORITY</th>")
        .append("<th>MIN PRIORITY</th>")
        .append("<th>PENALTY LIMIT</th>")
        .append("<th>AWARD LIMIT</th>")
        .append("</tr>")
        .append("</thead>")
        .append("<tbody align=\"center\">")
        .append("<tr>")
        .append("<td>q1 = ").append(q1).append("</td>")
        .append("<td>q2 = ").append(q2).append("</td>")
        .append("<td>q3 = ").append(q3).append("</td>")
        .append("<td>k = ").append(k).append("</td>")
        .append("<td>r = ").append(r).append("</td>")
        .append("</tr>")
        .append("</tbody>")
        .append("</table>")
        .append("</center>")
        .append("<a href=\"#top\" onmouseover=\"ShowStatus('top')\">top</a>");
  }
    
  public void addProcesses(ArrayList<Process> processes) {
    content.append("<center>")
        .append("<br><br><br><br><br>")
        .append("<a name=\"pd\"></a><p align=\"left\"><b>PROCESSES DATA</b></p>")
        .append("<p align=\"left\">Processes_Count = ").append(processes.size()).append("</p>");
    int i = 0;
    for (Process process : processes) {
      i++;
      content.append("<table width=\"100%\" border=\"1\">")
          .append("<caption align=\"left\"><a name=\"p").append(i).append("\"></a><b>PROCESS #").append(i)
          .append("</b></caption>")
          .append("<thead align=\"center\">")
          .append("<tr>")
          .append("<th>NAME</th>")
          .append("<th>ALIAS</th>")
          .append("<th>START TIME</th>")
          .append("<th>PHASES COUNT</th>")
          .append("</tr>")
          .append("</thead>")
          .append("<tbody align=\"center\">")
          .append("<tr>")
          .append("<td>").append(process.name).append("</td>")
          .append("<td>").append(process.alias).append("</td>")
          .append("<td>").append(process.startTime).append("</td>")
          .append("<td>").append(process.phasesCount).append("</td>")
          .append("</tr>")
          .append("</tbody>")
          .append("<thead align=\"center\">")
          .append("<tr>")
          .append("<th>PHASE COUNT</th>")
          .append("<th>CPU TIMES COUNT</th>")
          .append("<th>I/O TIMES COUNT</th>")
          .append("<th>REPEAT COUNT</th>")
          .append("</tr>")
          .append("</thead>")
          .append("<tbody align=\"center\">");
      int j = 0;
      for (Phase phase : process.phases) {
        j++;
        content.append("<tr>")
            .append("<td>").append(j).append("</td>")
            .append("<td>").append(phase.cpuTimesCount).append("</td>")
            .append("<td>").append(phase.ioTimesCount).append("</td>")
            .append("<td>").append(phase.repeatCount).append("</td>")
            .append("</tr>");
      }

      content.append("</tbody>")
          .append("</table>")
          .append("<a href=\"#top\" onmouseover=\"ShowStatus('top')\">top</a><br>");

    }

    content.append("</center>")
        .append("<br><br><br><br><br><br>")
        .append("<table width=\"100%\" border=\"1\">")
        .append("<caption align=\"left\"><a name=\"sod\"></a><b>SIMULATION OUTPUT DATA</b></caption>")
        .append("<thead align=\"center\">")
        .append(
            "<tr><th>TIME</th><th>CPU</th><th>I/O</th><th>Q1 Queue</th><th>Q2 Queue</th><th>Q3 Queue</th><th>I/O Queue</th></tr></thead>")
        .append("<tbody align=\"center\">");
  }

    public void addExecutionLog(int time, Queue<Process> q1, Queue<Process> q2, Queue<Process> q3, Queue<Process> io, Process currentProcess) {
        content.append("<tr>")
                .append("<td><font color=\"blue\">").append(String.format("%06d", time)).append("</font></td>")
                .append("<td>CPU: <a href=\"#p").append(currentProcess.alias).append("\">").append(currentProcess.alias).append("</a></td>")
                .append("<td>I/O: <a href=\"#p").append(getProcessAlias(io)).append("\">").append(getProcessAlias(io)).append("</a></td>")
                .append("<td>").append(getQueueContent(q1)).append("</td>")
                .append("<td>").append(getQueueContent(q2)).append("</td>")
                .append("<td>").append(getQueueContent(q3)).append("</td>")
                .append("<td>").append(getQueueContent(io)).append("</td>")
                .append("</tr>");
    }

    private String getProcessAlias(Queue<Process> queue) {
        return queue.isEmpty() ? "-" : String.valueOf(queue.peek().alias);
    }

    private String getQueueContent(Queue<Process> queue) {
        StringBuilder content = new StringBuilder();
        for (Process process : queue) {
            content.append(process.alias).append(", ");
        }
        return content.length() > 0 ? content.substring(0, content.length() - 2) : "-";
    }

    public void addPhaseCompletion(Process process) {
        content.append("<tr>")
                .append("<td colspan=\"7\" bgcolor=\"yellow\">")
                .append("Phase #").append(process.currentPhase + 1)
                .append(" of the Process ").append(process.alias).append(" is finished.")
                .append("</td>")
                .append("</tr>");
    }

        public void addProcessCompletion(Process process) {
        content.append("<tr>")
                .append("<td colspan=\"7\" bgcolor=\"red\">")
                .append("Process ").append(process.alias).append(" is finished.")
                .append("</td>")
                .append("</tr>");
    }

  public void writeOutput() {
      content
        .append("<tr>")
        .append("<td colspan=\"7\" bgcolor=\"red\">")
        .append("Simulation is finished.")
        .append("</td>")
        .append("</tr>")
        .append("</tbody>")
        .append("</table>")
        .append("</center>")
        .append("<a href=\"#top\" onmouseover=\"ShowStatus('top')\">top</a><br>")
        .append("</body>")
        .append("</html>");
    FileUtil.writeFile("output.htm", content.toString());
  }
}
