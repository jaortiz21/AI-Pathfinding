import java.util.*;
import java.io.*;
/**
 * The Runner class allows a user to test three
 * search methods on a text file.
 *
 * @author Jose Ortiz
 * @version 1.0
 */
public class Runner{
    public static void main(String[] args) throws FileNotFoundException{
        Scanner input = new Scanner(getFile());
        Grid g = createGrid(input);
        if(g == null){return;}
        searchInfo(g,0);
        g.resetPassed(g.places);
        searchInfo(g,1);
        g.resetPassed(g.places);
        searchInfo(g,2);
    }
    
    /**
     * This function calls all search functions
     * and provides information of each search
       */
    public static void searchInfo(Grid g,int n){
        if(n == 0){
            long start = System.currentTimeMillis();
            g.breadthFirst();
            long end = System.currentTimeMillis();
            if(g.breadth == null){return;}
            System.out.println("Breadt-first Search:");
            System.out.println("\tCost of path found: " + g.cost[0]);
            System.out.println("\tNumber of nodes expanded " + g.breadth.size());
            System.out.println("\tTime of search in milliseconds: " + (end-start));
            Iterator<Place> i = g.breadth.listIterator();
            System.out.print("\tSequence of coordinates: ");
            while(i.hasNext()){
                Place t = i.next();
                System.out.print("{" + t.row + "," + t.column + "}");
                if(i.hasNext()){System.out.print(", ");}
            }
            System.out.println();
            System.out.println("\tMaximum number of nodes held in memory: " + g.maxNodeCount[0]);
        }
        if(n == 1){
            long start = System.currentTimeMillis();
            g.iterativeDeep(30);
            long end = System.currentTimeMillis();
            if(g.itrList == null){
                System.out.println("\tTime of search in milliseconds:" + (end-start));
                System.out.println();
                return;
            }
            System.out.println("Iterative Deepening Search:");
            System.out.println("\tCost of path found: " + g.cost[1]);
            System.out.println("\tNumber of nodes expanded " + g.itrList.size());
            System.out.println("\tTime of search in milliseconds: " + (end-start));
            Iterator<Place> i = g.itrList.listIterator();
            System.out.print("\tSequence of coordinates: ");
            while(i.hasNext()){
                Place t = i.next();
                System.out.print("{" + t.row + "," + t.column + "}");
                if(i.hasNext()){System.out.print(", ");}
            }
            System.out.println();
            System.out.println("\tMaximum number of nodes held in memory: "  + g.maxNodeCount[1]);
        }
        if(n == 2){
            long start = System.currentTimeMillis();
            g.aStar();
            long end = System.currentTimeMillis();
            if(g.aList == null){return;}
            System.out.println("A* Search: ");
            System.out.println("\tCost of path found: " + g.cost[2]);
            System.out.println("\tNumber of nodes expanded " + g.aList.size());
            System.out.println("\tTime of search in milliseconds: " + (end - start));
            Iterator<Place> i = g.aList.listIterator();
            System.out.print("\tSequence of coordinates: ");
            while(i.hasNext()){
                Place t = i.next();
                System.out.print("{" + t.row + "," + t.column + "}");
                if(i.hasNext()){System.out.print(", ");}
            }
            System.out.println();
            System.out.println("\tMaximum number of nodes held in memory: " + g.maxNodeCount[2]);
        }
        System.out.println();
    }
    
    /**
     * Retrieves valid text file to be read
       */
    public static File getFile()throws FileNotFoundException{
        Scanner console = new Scanner(System.in);
        System.out.print("Which file would you like to scan? ");
        String newFile = console.nextLine();
        System.out.println();
        File f = new File(newFile);
        while(!f.exists()){
            System.out.println();
            System.out.println("That is not a valid file. Try again.");
            System.out.print("Which file would you like to scan? ");
            newFile = console.nextLine();
            f = new File(newFile);
            System.out.println();
        }
        console.close();
        return f;
    }
    
    /**
     * Creates a Grid object with all places mapped
     * by the given text file
       */
    public static Grid createGrid(Scanner input){
        int height = input.nextInt();
        int width = input.nextInt();
        int startRow = input.nextInt();
        int startCol = input.nextInt();
        int endRow = input.nextInt();
        int endCol = input.nextInt();
        if(height < 1 || width < 1 || startRow < 1 || startCol < 1 || endRow < 1 || endCol < 1){
            System.out.println("There is an input error with the text file. Be sure to provide valid dimensions\nas well as start and end spaces that comply with those dimensions.");
            return null;
        }
        Place st = null;
        Place end =  null;
        int[] costArray = new int[height*width];
        int count = 0; // to help keep track of which row and column each place is in
        LinkedList<Place> pl = new LinkedList();
        while(input.hasNext()){ //create a list of Place objects for the Grid
            int r = (count / width) + 1;
            int c = (count % width) + 1;
            int cost = input.nextInt();
            costArray[count] = cost;
            Place t = new Place(r,c,cost);
            if(r == startRow && c == startCol){st = t;}
            if(r == endRow && c == endCol){end = t;}
            pl.add(t);
            count++;
        }
        Grid g = new Grid(st,end);
        g.places = pl;
        g.height = height;
        g.width = width;
        return g;
    }
}
