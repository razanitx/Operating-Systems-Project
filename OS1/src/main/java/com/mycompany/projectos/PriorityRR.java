package com.mycompany.projectos;

import java.util.*;

/**
 * Priority + Round-Robin scheduler (uses Process.timeLeft as remaining time).
 */
public class PriorityRR {

    static class Segment {
        String pid;
        int start, end;

        Segment(String pid, int start, int end) {
            this.pid = pid;
            this.start = start;
            this.end = end;
        }
    }

    public static void runPriorityRR(List<Process> processes, int quantum) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        System.out.println("\n===== Priority Queue with Round Robin =====");

        int n = processes.size();

        
        int currentTime = processes.get(0).arrivalTime;

        
        int totalBurst = processes.stream().mapToInt(p -> p.CPUburst).sum();

        List<Segment> gantt = new ArrayList<>();

        Queue<Process> readyQueue = new LinkedList<>();
        Set<Process> arrived = new HashSet<>();
        List<Process> finished = new ArrayList<>();

        
        for (Process p : processes) {
            if (p.arrivalTime <= currentTime) {
                arrived.add(p);
                readyQueue.offer(p);
            }
            
            if (p.timeLeft <= 0) p.timeLeft = p.CPUburst;
        }

        while (finished.size() < n) {
            
            if (readyQueue.isEmpty()) {
                
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : processes) {
                    if (!arrived.contains(p) && p.arrivalTime < nextArrival) nextArrival = p.arrivalTime;
                }
                if (nextArrival != Integer.MAX_VALUE) {
                    currentTime = Math.max(currentTime, nextArrival);
                    for (Process p : processes) {
                        if (!arrived.contains(p) && p.arrivalTime <= currentTime) {
                            arrived.add(p);
                            readyQueue.offer(p);
                        }
                    }
                } else {
                    
                    break;
                }
            }

            
            int highestPriority = readyQueue.stream().mapToInt(p -> p.priority).min().orElse(Integer.MAX_VALUE);

            
            List<Process> samePriority = new ArrayList<>();
            for (Process p : readyQueue) {
                if (p.priority == highestPriority) samePriority.add(p);
            }

            if (samePriority.isEmpty()) {
                
                Process fallback = readyQueue.poll();
                if (fallback == null) continue;
                samePriority.add(fallback);
            }

            
            Process current = samePriority.get(0);
            
            readyQueue.remove(current);

            int slice = Math.min(quantum, current.timeLeft);
            int start = currentTime;
            int end = currentTime + slice;

            
            current.timeLeft -= slice;
            currentTime = end;

            
            gantt.add(new Segment(current.id, start, end));

            
            for (Process p : processes) {
                if (!arrived.contains(p) && p.arrivalTime <= currentTime) {
                    arrived.add(p);
                    readyQueue.offer(p);
                }
            }

            
            boolean higherArrived = readyQueue.stream().anyMatch(p -> p.priority < current.priority);

            if (current.timeLeft == 0) {
                
                if (!finished.contains(current)) finished.add(current);
            } else {
                
                readyQueue.offer(current);
            }
        }

        
        printGanttChart(gantt);

        
        double totalTurnaround = 0;
        double totalWaiting = 0;

        for (Process p : processes) {
            
            int finish = gantt.stream()
                    .filter(s -> s.pid.equals(p.id))
                    .mapToInt(s -> s.end)
                    .max().orElse(p.arrivalTime); 
            p.finish = finish;
            p.turnaroundTime = p.finish - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.CPUburst;

            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }

        double avgTurnaround = totalTurnaround / n;
        double avgWaiting = totalWaiting / n;

        
        int totalTime = 0;
        if (!gantt.isEmpty()) {
            int first = gantt.get(0).start;
            int last = gantt.get(gantt.size() - 1).end;
            totalTime = last - first;
            if (totalTime <= 0) totalTime = last; 
        }

        double cpuUtilization = (totalTime == 0) ? 0.0 : (100.0 * totalBurst / (double) totalTime);

        
        System.out.println("\nTime\tProcess");
        for (Segment s : gantt) {
            System.out.println(s.start + "-" + s.end + "\t" + s.pid);
        }

       
        System.out.println("\nPerformance Metrics");
        System.out.printf("Average Turnaround Time: %.2f%n", avgTurnaround);
        System.out.printf("Average Waiting Time: %.2f%n", avgWaiting);
        System.out.printf("CPU Utilization: %.0f%%%n", cpuUtilization);
    }

    
    public static void printGanttChart(List<Segment> gantt) {
        System.out.println("\nGantt Chart:");
        List<String> order = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        for (Segment s : gantt) {
            order.add(s.pid);
            if (times.isEmpty()) times.add(s.start);
            times.add(s.end);
        }

        if (order.isEmpty()) {
            System.out.println("(no execution segments)");
            return;
        }

        
        for (String p : order) System.out.print("------");
        System.out.println("-");

        
        for (String p : order) System.out.print("| " + p + " ");
        System.out.println("|");

        
        for (String p : order) System.out.print("------");
        System.out.println("-");

        
        for (int t : times) System.out.printf("%-6d", t);
        System.out.println("\n");
    }
}
