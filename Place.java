import java.util.LinkedList;
/**
 * The Place represents a passable/impassable
 * piece of terrain that is in a grid system.
 *
 * @author Jose
 * @version 1.0
 */
public class Place{
    protected int row;
    protected int column;
    protected int cost;
    protected int g;
    protected int f;
    protected boolean beenPassed;
    protected LinkedList<Place> neighbors = new LinkedList();
    
    Place(int row, int column, int cost){
        this.row = row;
        this.column = column;
        this.cost = cost;
        beenPassed = false;
    }
    
    /**
     *  @return A boolean that determines if this is a passable terrain
     *  
     */
    protected boolean canPass(){
        if(cost == 0 || beenPassed) return false;
        return true;
    }
    
    /**
     *  Notifies that a Place can no longer be passed through.
       */
    protected void passed(){
        beenPassed = true;
    }
}
