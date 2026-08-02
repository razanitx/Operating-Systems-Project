package com.mycompany.projectos;

import java.util.*;

public class Process {
    
    String id; 
    int arrivalTime;
    int CPUburst;
    int priority;
    int timeLeft; 
    int start, finish; 

    
    int turnaroundTime;
    int waitingTime;

    public Process(String id, int arrival, int burst, int pr){
        this.id = id;
        arrivalTime = arrival;
        CPUburst = burst;
        priority = pr;
        timeLeft = burst;
    }

    @Override
    public String toString(){
        return id + " arrival: " + arrivalTime + " burst: " + CPUburst + " priority: " + priority;
    }
}
