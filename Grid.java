import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Grid represents a map that will implement search
 * methods to find the most cost-effective path.
 *
 * @author Jose Ortiz
 * @version 1.0
 */
public class Grid
{
    protected LinkedList<Place> places;
    protected LinkedList<Place> breadth;
    protected LinkedList<Place> itrList;
    protected LinkedList<Place> aList;
    protected int[] cost = new int[3];
    protected int[] maxNodeCount = new int[3];
    protected Place start;
    protected Place end;
    protected int[] costArray; // array that stores cost of each space in the grid
    protected int height;
    protected int width;
    
    Grid(Place start, Place end){
        this.start = start;
        this.end = end;
    }
    
    /**
     * Fills out the list of neighbors for the given space
       */
    protected void fillNeighbors(Place n){
        //System.out.println("Adding neighbors");
        int index = (width * (n.row-1)) + n.column - 1;
        if(n.row != 1 && n.row != height && n.column != 1 && n.column != width){ //this space will have four neighbors
            //System.out.println("Add four neighbors");
            n.neighbors.add(places.get(index(n.row-1,n.column))); //place above space in grid
            n.neighbors.add(places.get(index(n.row+1,n.column))); //place below space in grid
            n.neighbors.add(places.get(index(n.row,n.column-1))); //place before space in grid
            n.neighbors.add(places.get(index(n.row,n.column+1))); //place after grid
        }
        if(n.row == 1 || n.row != height){ //add neighbors below space
            //System.out.println("Add below");
            n.neighbors.add(places.get(index(n.row+1,n.column)));
        }
        if(n.row == height || n.row != 1){ //add neighbor above space
            //System.out.println("Add above");
            n.neighbors.add(places.get(index(n.row-1,n.column))); 
        }
        if(n.column == 1 || n.column != width){// add neighbor to right
            //System.out.println("Add right");
            n.neighbors.add(places.get(index(n.row,n.column+1))); 
        }
        if(n.column == width || n.column != 1){//add neighbor to the left
            //System.out.println("Add left");
            n.neighbors.add(places.get(index(n.row,n.column-1))); 
        }
    }
    
    /**
     * Finds the index of a Place object in the List according 
     * to its row and column index.
       */
    private int index(int row, int column){
        //System.out.println("Width, row, column: " + width + " " + row + " " + column);
        int i = (width * (row-1)) + column - 1;
        //System.out.println("Index added: " + i );
        return i;
    }
    
    /**
     * Finds a path from a start position to end position
     * using breadth-first search.
       */
    protected void breadthFirst(){
        long s = System.currentTimeMillis();
        LinkedList<Place> queue = new LinkedList(); //think of this as frontier
        breadth = new LinkedList();
        queue.add(start);
        int count = 1;
        
        while(queue.size() != 0){
            long e = System.currentTimeMillis();
            if((e - s) > 3000){
                System.out.println("The Breadth-first search has exceeeded a max time of three minutes.");
                break;
            }
            //System.out.println("Inside queue");
            Place t = queue.poll();
            breadth.add(t);
            t.passed();
            cost[0] += t.cost; //update to current cost of path
            //System.out.println("{" + t.row + "," + t.column + "} added to breadth. Current cost to path: " + (cost[0] - start.cost));
            fillNeighbors(t);
            Iterator<Place> i = t.neighbors.listIterator();
            while(i.hasNext()){
                Place n = i.next();
                //System.out.println("Adding to queue");
                if(n.canPass()){
                    //System.out.println("{" + n.row + "," + n.column + "} added to queue.");
                    n.passed(); //ensure that we do not go through this spot again
                    queue.add(n); //add to queue for getting neighbors
                    count++;
                    if(count > maxNodeCount[0]){maxNodeCount[0] = count;}
                    if(n.row == end.row && n.column == end.column){ // we have reached the end
                        breadth.add(n);
                        cost[0] += (n.cost - start.cost);
                        return; 
                    }
                }
            }
        }
        if(!breadth.contains(end)){
            System.out.println("\tCost of path: -1\n\tSequence of coordinates: ");
            Iterator<Place> i = breadth.listIterator();
            while(i.hasNext()){
                Place t = i.next();
                System.out.print("{" + t.row + "," + t.column + "}");
                if(i.hasNext()){System.out.print(", ");}
            }
            System.out.println();
            System.out.println("\tMaximum number of nodes held in memeory: " + maxNodeCount[0]);
            breadth = null;
        }
    }
    
    /**
     * Finds a path from a start positon to end position
     * using iterative deepening search.
       */
    protected void iterativeDeep(int maxDepth){
        long s = System.currentTimeMillis();
        //System.out.println("Enter iterative deepening");
        for(int i = 0; i <= maxDepth; i++){
            long e = System.currentTimeMillis();
            if((e - s) > 3000){
                System.out.println("The Iterative Deepening Search has exceeded the max time of a three minute search.");
                break;
            }
            resetPassed(places);
            start.passed();
            itrList = new LinkedList();
            cost[1] = 0;
            itrList.add(start);
            //System.out.println("Depth search: " + i);
            if(DLS(start,i,1)){
                return;
            }
        }
        //no path found
        System.out.println("Iterative Deepening Search: ");
        System.out.print("\tCost of path: -1\n\tSequence of coordinates: ");
        Iterator<Place> i = itrList.listIterator();
        while(i.hasNext()){
            Place t = i.next();
            System.out.print("{" + t.row + "," + t.column + "}");
            if(i.hasNext()){System.out.print(", ");}
        }
        System.out.println();
        System.out.println("\tMaximum number of nodes held in memory: " + maxNodeCount[1]);
        itrList = null;
    }
    
    /**
     * Helper method for Iterative Deepening Search
     * that searches a certain depth
     * @param Place object whose children will be searched
     * @param How deep the search will go
       */
    
    protected boolean DLS(Place n, int limit,int count){
        //System.out.println("Entering DLS of {" + n.row + "," + n.column + "}"  );
        if(n.row == end.row && n.column == end.column){
            //System.out.println("Found end");
            maxNodeCount[1] = count;
            return true;
        }
        if(limit <= 0){
            //System.out.println("Leaving DLS of {" + n.row + "," + n.column + "} due to limit reached");
            return false;
        }
        //System.out.println("Entering neighbors for {" + n.row + "," + n.column + "}");
        fillNeighbors(n);
        for(int i = 0; i < n.neighbors.size();i++){
            Place t = n.neighbors.get(i);
            if(t.canPass()){ //state check if we have passed already
               t.passed();
               itrList.add(t);
               //System.out.println("{" + t.row + "," + t.column + "} added to itr");
               cost[1] += t.cost;
               if(DLS(t,limit-1,count+1)){
                   return true;
               }
            }
        }
        //System.out.println("Leaving DLS of {" + n.row + "," + n.column + "}");
        return false;
    }
    
    /**
     * Helper method to reset all Place objects that
     * were passed in previous searches.
      */
    protected void resetPassed(LinkedList<Place> list){
        Iterator<Place> i = list.listIterator();
        while(i.hasNext()){
            Place t = i.next();
            t.beenPassed = false;
        }
    }
    
    /**
     * Finds a path from a start position to end position
     * using A* search.
       */
    protected void aStar(){
        long s = System.currentTimeMillis();
        Comparator<Place> comparator = new PlaceComparator();
        PriorityQueue<Place> queue = new PriorityQueue<Place>(comparator);//open list
        aList = new LinkedList(); //closed list
        queue.add(start);//open list
        int c = 1;
        start.g = 0;
        while(queue.size() != 0){
            long e = System.currentTimeMillis();
            if((e - s) > 3000){
                System.out.println("The A* search has exceeded the max time of a three minute search.");
                break;
            }
            Place q = queue.poll();
            aList.add(q);
            q.passed();
            fillNeighbors(q);
            for(int i = 0; i < q.neighbors.size(); i++){
                Place t = q.neighbors.get(i);
                if(t.row == end.row && t.column == end.column){ //if is the goal
                    aList.add(t);
                    t.g = q.g + t.cost;
                    t.f = t.g + manhattanDistance(t);
                    cost[2] = t.f;
                    return;
                }
                //if is not in closed list and can pass
                else if(!aList.contains(t) && t.canPass()){
                    int gNew = q.g + t.cost;
                    int fNew = t.g + manhattanDistance(t);
                    if(!queue.contains(t) || fNew < t.f){
                        queue.add(t);
                        c++;
                        if(c > maxNodeCount[2]){maxNodeCount[2] = c;}
                        //update new path
                        t.f = fNew;
                        t.g = gNew;
                    }
                }
            }
        }
        //if we're here then no path was found
        System.out.println("A* Search:");
        System.out.print("\tCost of path: -1\n\tSequence of coordinates: ");
        Iterator<Place> i = aList.listIterator();
        while(i.hasNext()){
            Place t = i.next();
            System.out.print("{" + t.row + "," + t.column + "}");
            if(i.hasNext()){System.out.print(", ");}
        }
        System.out.println();
        System.out.println("\tMaximum number of nodes held in memory: " + maxNodeCount[2]);
        aList = null;
    }
    
    /**
     * Computes the Manhattan distance heuristic
     * to help calculate f in A* search
       */
    private int manhattanDistance(Place n){
        //Add absolute values of difference between rows and columns
        return Math.abs(n.row - end.row) + Math.abs(n.column - end.column);
    }
    
    public class PlaceComparator implements Comparator<Place>{
        public int compare(Place x,Place y){
            //Compares x and y by computing each f value
            //f = h + g where h = manhattan heuristic and g is distance
            //from start point to 
            if((x.g + manhattanDistance(x)) < (y.g + manhattanDistance(y))){
                return -1;
            }
            if((x.g + manhattanDistance(x)) > (y.g + manhattanDistance(y))){
                return 1;
            }
            return 0;
        }
    }
}
