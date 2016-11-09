import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by benjihannam on 10/31/16.
 */
public class Constraint {
    private HashMap<Pair, HashSet<Pair>> constraints;

    public Constraint(HashMap<Pair, HashSet<Pair>> cons){
        constraints = cons;
    }

    //checks if a assignment for a pair of values is ok
    public boolean isSatisfied(Pair variables, Pair values){

        //System.out.println("variables = " + variables);
        if(constraints.containsKey(variables)){
            return constraints.get(variables).contains(values);
        }
        
        return true;
    }

    public HashMap<Pair, HashSet<Pair>> getTable(){
        return constraints;
    }
}
