//package com.mycompany.projectos;

import java.util.*;

public class FCFS {

    public static void runFCFS(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        double totalTurnaround = 0;
        double totalWaiting = 0;
        int totalBurst = 0;

        List<String> order = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        System.out.println("\n===== First-Come First-Served (FCFS) =====");
        System.out.println("Time\tProcess");

        for (Process p : processes) {
            if (currentTime < p.arrivalTime)
                currentTime = p.arrivalTime;

            p.start = currentTime;
            p.finish = p.start + p.CPUburst;
            p.turnaroundTime = p.finish - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.CPUburst;

            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
            totalBurst += p.CPUburst;

            System.out.println(p.start + "-" + p.finish + "\t" + p.id);

            
            order.add(p.id);
            if (times.isEmpty()) times.add(p.start);
            times.add(p.finish);

            currentTime = p.finish;
        }

        
        printGanttChart(order, times);

        double avgTurnaround = totalTurnaround / processes.size();
        double avgWaiting = totalWaiting / processes.size();
        double cpuUtilization = ((double) totalBurst / currentTime) * 100.0;

        System.out.println("\nPerformance Metrics");
        System.out.printf("Average Turnaround Time: %.2f%n", avgTurnaround);
        System.out.printf("Average Waiting Time: %.2f%n", avgWaiting);
        System.out.printf("CPU Utilization: %.0f%%%n", cpuUtilization);
    }

    
    public static void printGanttChart(List<String> processOrder, List<Integer> timePoints) {
        System.out.println("\nGantt Chart:");
        
        
        for (String p : processOrder) System.out.print("------");
        System.out.println("-");
        
       
        for (String p : processOrder) System.out.print("| " + p + " ");
        System.out.println("|");
        
        
        for (String p : processOrder) System.out.print("------");
        System.out.println("-");
        
        
        for (int t : timePoints) System.out.printf("%-6d", t);
        System.out.println("\n");
    }
}
