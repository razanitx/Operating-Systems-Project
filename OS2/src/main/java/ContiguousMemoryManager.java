/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;
/**
 *
 * @author Leena
 */
public class ContiguousMemoryManager {
   

    static LinkedList<block> memory = new LinkedList<>();
     static Scanner input = new Scanner(System.in);
    static int size; //statics for main
    
    // ==================== REPLACE YOUR MAIN METHOD WITH THIS ====================
    public static void main(String[] args) {
        System.out.println("Enter the total memory size:");
        size = input.nextInt();
        input.nextLine(); // consume newline
        
        block freeBlock = new block(0, size - 1, "free");
        memory.add(freeBlock);
        
        System.out.println("\nMemory initialized successfully!");
        
        while (true) {
            displayMenu();
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            switch (choice) {
                case 1: // Allocate memory
                    allocateMemoryMenu();
                    break;
                case 2: // Release memory
                    releaseMemoryMenu();
                    break;
                case 3: // Compact memory
                    compactMemory();
                    break;
                case 4: // Display memory status
                    displayMemory();
                    break;
                case 5: // Exit
                    System.out.println("Exiting Memory Allocation Simulator. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void displayMenu() {
        System.out.println("\nMemory Allocation Simulator");
        System.out.println("---");
        System.out.println("1. Request (Allocate) memory");
        System.out.println("2. Release memory");
        System.out.println("3. Compact memory");
        System.out.println("4. Display memory status");
        System.out.println("5. Exit");
        System.out.println("---");
        System.out.print("Enter your choice: ");
    }

    static void allocateMemoryMenu() {
        System.out.print("Enter process name: ");
        String processName = input.nextLine();
        
        System.out.print("Enter size: ");
        int size = input.nextInt();
        input.nextLine(); // consume newline
        
        System.out.print("Enter strategy (F/B/W): ");
        char strategy = input.nextLine().charAt(0);
        
        allocateMemory(processName, size, strategy);
    }

    static void releaseMemoryMenu() {
        System.out.print("Enter process name to release: ");
        String processName = input.nextLine();
        releaseProcess(processName);
    }
    
    // ==================== YOUR TASK 2 - ALLOCATION ALGORITHMS ====================
    
    // Allocation Algorithms - Student 2
    static boolean allocateMemory(String processName, int size, char strategy) {
        switch (Character.toUpperCase(strategy)) {
            case 'F': return firstFit(processName, size);
            case 'B': return bestFit(processName, size);
            case 'W': return worstFit(processName, size);
            default: 
                System.out.println("Invalid strategy. Use F, B, or W.");
                return false;
        }
    }

   static boolean firstFit(String processName, int size) {
    
    for (int i = 0; i < memory.size(); i++) {
        block current = memory.get(i);
        if (current.process.equals("free") && current.getSize() >= size) {
            splitBlock(i, current, processName, size);
            
            return true;
        }
    }
    System.out.println("Error: Not enough contiguous memory for process " + processName);
    return false;
}

   static boolean bestFit(String processName, int size) {
    block bestBlock = null;
    int bestIndex = -1;
    
    for (int i = 0; i < memory.size(); i++) {
        block current = memory.get(i);
        if (current.process.equals("free") && current.getSize() >= size) {
            if (bestBlock == null || current.getSize() < bestBlock.getSize()) {
                bestBlock = current;
                bestIndex = i;
            }
        }
    }
    
    if (bestBlock != null) {
        splitBlock(bestIndex, bestBlock, processName, size);
        return true;  // No print statement here!
    }
    
    System.out.println("Error: Not enough contiguous memory for process " + processName);
    return false;
}

    static boolean worstFit(String processName, int size) {
    block worstBlock = null;
    int worstIndex = -1;
    
    for (int i = 0; i < memory.size(); i++) {
        block current = memory.get(i);
        if (current.process.equals("free") && current.getSize() >= size) {
            if (worstBlock == null || current.getSize() > worstBlock.getSize()) {
                worstBlock = current;
                worstIndex = i;
            }
        }
    }
    
    if (worstBlock != null) {
        splitBlock(worstIndex, worstBlock, processName, size);
        return true;  // No print statement here!
    }
    
    System.out.println("Error: Not enough contiguous memory for process " + processName);
    return false;
}

 // Helper method to split a block into allocated and free parts
static void splitBlock(int index, block currentBlock, String processName, int size) {
    // Save the original values before modifying
    int originalStart = currentBlock.start;
    int originalEnd = currentBlock.end;
    
    // If the block size exactly matches, just mark it as allocated
    if (currentBlock.getSize() == size) {
        currentBlock.process = processName;
        System.out.println("Process " + processName + " allocated from " + originalStart + " to " + originalEnd);
    } 
    // If block is larger, split it
    else {
        // Create new allocated block
        block allocatedBlock = new block(
            originalStart, 
            originalStart + size - 1, 
            processName
        );
        
        // Create new free block with remaining space
        block freeBlock = new block(
            originalStart + size, 
            originalEnd, 
            "free"
        );
        
        // Replace the current block with allocated block and insert free block after it
        memory.set(index, allocatedBlock);
        memory.add(index + 1, freeBlock);
        
        System.out.println("Process " + processName + " allocated from " + allocatedBlock.start + " to " + allocatedBlock.end);
    }
}

    // ==================== EXISTING METHODS (KEEP THESE) ====================
    
    static void displayMemory() {
        System.out.println("\nMemory status:");
        System.out.println("-----------------------------------------------");

        for (block b : memory) {
            String status;
            if (b.process.equals("free")) {
                status = "Unused (Free)";
            } else {
                status = "Process " + b.process;
            }

            System.out.printf("Addresses [%d : %d]\t%s%n", b.start, b.end, status);
        }

        System.out.println("-----------------------------------------------");
    }
    
    static void releaseProcess(String processName) {
        block prev = null;
        block curr = null;

        for (block b : memory) {
            if (b.process.equals(processName)) {
                curr = b;
                break;
            }
            prev = b;
        }

        if (curr == null) {
            System.out.println(" Process not found.");
            return;
        }

        curr.process = "free";
        System.out.println("Process " + processName + " released.");

        mergeWithNext(curr);

        if (prev != null && prev.process.equals("free")) {
            mergeWithPrevious(prev, curr);
        }
    }

    // Merge with next free block
    static void mergeWithNext(block curr) {
        int index = memory.indexOf(curr);

        if (index != -1 && index + 1 < memory.size()) {
            block next = memory.get(index + 1);

            if (next.process.equals("free")) {
                curr.end = next.end;
                memory.remove(index + 1);
            }
        }
    }

    // Merge with previous free block
    static void mergeWithPrevious(block prev, block curr) {
        prev.end = curr.end;
        memory.remove(curr);
    }
    
    static void compactMemory() {
        LinkedList<block> newMemory = new LinkedList<>();

        int currentStart = 0; 

        for (block b : memory) {
            if (!b.process.equals("free")) { 
                int blockSize = b.getSize();

                block newBlock = new block(
                        currentStart,
                        currentStart + blockSize - 1,
                        b.process
                );
                newMemory.add(newBlock);

                currentStart += blockSize; 
            }
        }

        if (currentStart < size) {
            block freeBlock = new block(
                    currentStart,
                    size - 1,
                    "free"
            );
            newMemory.add(freeBlock);
        }

        memory.clear();
        memory.addAll(newMemory);

        System.out.println("Memory compacted successfully.");
    }
}

class block { //for memory blockk
    int start,end;  //initilization
    String process;
    
    block(int s,int e, String p){ //constructor
            start =s;
            end=e;
            process=p;
    }
    
    int getSize(){
        return end-start+1;
    }
}
