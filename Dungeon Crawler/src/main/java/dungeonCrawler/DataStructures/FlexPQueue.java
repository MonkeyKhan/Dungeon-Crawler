package dungeonCrawler.DataStructures;


import java.util.Arrays;

public class FlexPQueue<T extends Comparable<? super T>>
{
    private int capacity;
    private int size = 0;
    private Object[] elements;
    
    public FlexPQueue(int initialCapacity){
        this.capacity = initialCapacity;
        if(this.capacity >= 0){
            elements = new Object[this.capacity+1];
        }else{
            throw new RuntimeException();
        }
        size = 0;
    }
    
    public FlexPQueue(){
        this(10);
    }
    
    
    @Override
    public String toString(){
       int rows = (int)Math.floor(Math.log10(size) / Math.log10(2.))+1;
        int cols = (int)Math.pow(2, rows)-1;

        String s = ""+size+" Nodes in "+rows+"x"+cols+"rxc\n";
        
        int[][] entries = new int[rows][];
        int entriesInThisRow = 1;
        entries[0] = new int[]{(int)Math.floor(0.5d*cols)};

        
        for(int i=1; i<rows; i++){
            entriesInThisRow *= 2;
            entries[i] = new int[entriesInThisRow];
            for(int j=0; j< entries[i-1].length; j++){
                int prev = entries[i-1][j];
                entries[i][j*2] = prev - (int)Math.ceil((double)cols / (entriesInThisRow*2));
                entries[i][(j*2)+1] = prev + (int)Math.ceil((double)cols / (entriesInThisRow*2));
            }
        }
        
        int current = 1;
        for(int[] i: entries){
            for(int k=0; k<cols; k++){
                String t = "          ";
                for(int j: i){
                    if(k == j){
                        if(current <= size){
                            
                            t = ((T)elements[current]).toString();
                            current++;
                        }
                    }
                }
                //s = s+(T)elements[current].toString();
                s = s + t;
            }
            s = s + "\n";
        }
        return s;
    }

    
    public void update(T o){
        /**Tries to add an object to the priorityQueue. If the item is not yet in the queue, this calls add().
         * If the object is in the queue already, it updates the priority of the object ONLY IF the new object's priority is higher than the existing one's.
         * If the item to add is known to not be inside the queue yet, add() is always faster.
         */
        if(o != null){
            int objIndex = find(o);
            if(objIndex == -1){
                //The item is not yet in queue, add normally
                add(o);
            }else{
                //The object is already in the queue. Check whether the new object has a higher priority
                if(o.compareTo((T)elements[objIndex])>0){
                    //Replace object in queue with new object
                    elements[objIndex] = o;
                    //New object might violate heap condition. It has a higher priority than the old object, so its children are definitely smaller than itself -> it might need to move up, but never down
                    heapifyUpwards(objIndex);
                }
            }
        }
    }
    
    
    public T poll(){
        /**Removes and returns the highest priority element
         * Ensures heap condition is satisfied after polling
         */
        if(size < 1){
            return null;
        }
        
        T highestPriorityElement = (T)elements[1];
        
        //Keep moving the higher-priority child up until a leaf is reached
        //Index of childs are 2*k and 2*k +1, that means if k is <= (size-1)/2, then the node k has at least 1 child
       
        elements[1] = elements[size];
        elements[size] = null;
        size--;
        heapify();
        
        return highestPriorityElement;
    }
    
    public T peek() {
    	/**
    	 * Returns the highest priority element without removing it
    	 */
    	
    	if(size<1) {
    		return null;
    	}else {
    		return (T)elements[1];
    	}
    }
    
    public boolean contains(T o){
        /**Returns whether an object is part of the PriorityQueue
         * 
         */
        if(find(o) == -1){
            return false;
        }else{
            return true;
        }
    }
        
    private void add(T element){
        /**Adds an element to the PriorityQueue. The method doesnt check if theres a copy of the element already inside the queue, so duplicates are allowed.
         * Ensures heap condition is satisfied after adding
         * 
         */
        if(!(size < capacity-1)){  
            //Array is full, create an array of doubled capacity and transfer elements to new array
            //This can be avoided by choosing a correct initialCapacit
            capacity *= 2;
            elements = Arrays.copyOf(elements, capacity +1);
        }
        size++;
        elements[size] = element;
        
        int current = size;
        int parent = current/2;
        //Check new element against its parent. If its of higher priority (compareTo > 0), swap them
        //Do this until the parent is of higher priority or the element is in the root node
        while(current > 1 && ( ((T)elements[current]).compareTo((T)elements[parent]) > 0)){
            swap(current, parent);
            current /= 2;
            parent = current/2;    
        }
    }
    
    private int find(T o){
        //Search for an object based on equals() and return its array index
        //If object is not part of the queue, return -1
        if(o == null){
            return -1;
        }
        for(int i=1; i<=size; i++){
            if(elements[i].equals(o)){
                return i;
            }
        }
        return -1;
    }
    private void heapify(){
        /**Moves the top-most element down the heap until heap structure is achieved
         * This is achieved by swapping the element with its higher-priority child until both its children are of lower priority than itself
         */
        int current = 1;
        int higherPrioChild;
        boolean stop = false; 
       
        
        while (!stop){
            int child1 = current*2;
            int child2 = (current*2) +1;
            
            if(child1 <= size){
                //current has at least 1 child
                if(child2 <=size){
                    //current has 2 children, compare children
                    if( ((T)elements[child1]).compareTo((T)elements[child2]) > 0){ 
                        //child 1 is higher prio, check current against it
                        higherPrioChild = child1;
                        
                    }else{
                        //child 2 is higher prio, check current against it
                        higherPrioChild = child2;               
                    }
                }else{
                    //current only has 1 child (child1)
                    higherPrioChild = child1;
                }
                //Check current against the higher prio child
                if( ((T)elements[current]).compareTo((T)elements[higherPrioChild]) < 0){ 
                    //higher prio child has higher prio than current, swap and continue in that subheap
                    swap(current, higherPrioChild);   
                    current = higherPrioChild;
                }else{
                    //current has higher prio than higher prio child, heap structure is achieved
                    stop = true;
                }
            }else{
                //current has no childen, heap structure is achieved
                stop = true;
            }
        }

    }
    
    private void heapifyUpwards(int startIndex){
        //This method takes an index and moves the element at the given index up the heap
        //It assumes that the element's childen are each intact heaps and that the element's priority is higher than before
        
        int current = startIndex;
        int parent = current/2;
        //Check new element against its parent. If its of higher priority (compareTo > 0), swap them
        //Do this until the parent is of higher priority or the element is in the root node
        while(current > 1 && ( ((T)elements[current]).compareTo((T)elements[parent]) > 0)){
            swap(current, parent);
            current /= 2;
            parent = current/2;    
        }
    }
    
    private void swap(int a, int b){
        Object temp = elements[a];
        elements[a] = elements[b];
        elements[b] = temp;
    } 

}

