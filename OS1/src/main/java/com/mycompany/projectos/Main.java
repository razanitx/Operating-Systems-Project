package com.mycompany.projectos;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Process> processes = new ArrayList<>();
        System.out.println("Enter # of processes:");
        int num = readInt(scanner);

        for (int i = 0; i < num; i++) { 
            int arrival, burst, priority; 

            while (true) { 
                System.out.println("Enter arrival time for P" + (i + 1) + ":");
                arrival = readInt(scanner);
                if (arrival >= 0) break;
                System.out.println("Invalid input, arrival time must be >= 0 ");
            }

            while (true) {
                System.out.println("Enter burst value for P" + (i + 1) + ":");
                burst = readInt(scanner);
                if (burst > 0) break;
                System.out.println("Invalid input, burst must be > 0 ");
            }

            while (true) {
                System.out.println("Enter priority value for P" + (i + 1) + ":");
                priority = readInt(scanner);
                if (priority >= 0) break;
                System.out.println("Invalid input, priority must be >= 0 ");
            }

            Process p = new Process("P" + (i + 1), arrival, burst, priority);
            processes.add(p);
        }

        
        int quantum;
        while (true) {
            System.out.println("Enter Quantum value for RR:");
            quantum = readInt(scanner);
            if (quantum > 0) break;
            System.out.println("Invalid input [must be > 0]");
        }

        System.out.println("\nAll processes entered:");
        for (Process p : processes) {
            System.out.println(p);
        }

       
        List<Process> forPriority = deepCopyProcessList(processes);
        PriorityRR.runPriorityRR(forPriority, quantum); 

       
        List<Process> forFCFS = deepCopyProcessList(processes);
        FCFS.runFCFS(forFCFS);

        scanner.close();
    }

    
    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.next());
            } catch (Exception ex) {
                System.out.println("Invalid number, try again:");
                sc.nextLine();
            }
        }
    }

    
    private static List<Process> deepCopyProcessList(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            Process np = new Process(p.id, p.arrivalTime, p.CPUburst, p.priority);
            
            np.timeLeft = p.CPUburst;
            np.start = -1;
            np.finish = -1;
            np.turnaroundTime = 0;
            np.waitingTime = 0;
            copy.add(np);
        }
        return copy;
    }
}
