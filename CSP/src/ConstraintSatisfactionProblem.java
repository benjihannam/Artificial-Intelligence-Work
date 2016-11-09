import java.util.*;

/**
 * Created by benjihannam on 10/30/16.
 */
public class ConstraintSatisfactionProblem {
    public int size;
    public int values;
    public int num_vals_looked_at;

    public ConstraintSatisfactionProblem(){}

    public ConstraintSatisfactionProblem(int arr_size, int vals){
        size = arr_size;
        values = vals;

    }

    public int[] BacktrackingSearch(Constraint constraints){
        if(constraints.getTable().size() == 0){
            return null;
        }
        int assignment[] = new int[size];
        for(int i = 0; i < assignment.length; i++){
            assignment[i] = -1;
        }
        //return assignment;
        num_vals_looked_at = 0;
        return Backtrack(assignment, constraints);
    }

    public int[] Backtrack(int[] assign, Constraint constraints){

        //if the assignment is a solution return it
        if(isComplete(constraints, assign)){
            System.out.println("returning " + Arrays.toString(assign));
            return assign;
        }
        int var = 0;
        if(assign[0] != -1){
            var = SelectUnassignedVariable(assign, constraints);
        }

        //for each value that it can take
        for(int value : OrderDomainValues()){
            //set it to the value
            assign[var] = value;
            num_vals_looked_at++;

            if(isConsistent(assign, constraints)){
                //set this equal to true if inferences should not be used
                //boolean inferences = AC_three(constraints, assign);
                boolean inferences = true;
                if(inferences){
                    int[] result = Backtrack(assign, constraints);
                    if(isComplete(constraints, result)){
                        return result;
                    }
                }

            }

            //remove the value assignment
            assign[var] = -1;
        }
        return null;
    }

    //returns the order to look at the values
    public int[] OrderDomainValues(){
        int[] sol = new int[values];
        for(int i = 0; i < values; i++){
            sol[i] = i;
        }
        return sol;

    }

    //chooses the next variable to look at
    public int SelectUnassignedVariable(int[] assignment, Constraint constraints){

//        for(int i = 0; i < assignment.length; i++){
//            if(assignment[i] == -1){
//                return i;
//            }
//        }
//        return 0;
        //return MinimumRemainingValue(assignment, constraints);
        return DegreeHeuristic(assignment, constraints);
    }

    public boolean isComplete(Constraint constraints, int[] assign){

        if(assign == null){
            return false;
        }
        else{
            for(int i = 0; i < assign.length; i++){
                if(assign[i] == -1){
                    return false;
                }
            }
        }

        //check all the combos
        for(int i = 0; i < assign.length; i++){
            for(int j = 0; j < assign.length; j++){
                // The pairs are set up so that the smaller one is always first
                if(i < j){
                    //create the pairs
                    Pair variables = new Pair(i, j);
                    Pair values = new Pair(assign[i], assign[j]);
                    if(!constraints.isSatisfied(variables, values)){
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public boolean isConsistent(int[] assign, Constraint constraints){

        //for each pair
        for(Pair variables : constraints.getTable().keySet()){
            //check if the assigned values are valid
            //get the current value of each
            int first = assign[variables.one];
            int second = assign[variables.two];
            //make sure they have both been assigned, otherwise its fine
            if(first != -1 && second != -1){

                Pair temp = new Pair(first, second);
                //if the assignment is consistent
                if(!constraints.isSatisfied(variables, temp)){
                    return false;
                }

            }
        }

        return true;
    }


    //the MRV heuristic
    public int MinimumRemainingValue(int[] assignment, Constraint constraints){

        //set up the initial possible domains
        HashSet<Integer> possible_vals = new HashSet<Integer>();
        for(int i = 0; i < values; i++){
            possible_vals.add(i);
        }

        //insert unassigned values into a hashmap
        HashMap<Integer, HashSet<Integer>> domains = new HashMap<Integer, HashSet<Integer>>();
        for(int i = 0; i < assignment.length; i++){
            //if unassigned
            if(assignment[i] == -1){
                //put into the hashmap with all possible values
                domains.put(i, possible_vals);
            }
        }

        //for each unassigned
        for(int current : domains.keySet()) {

            //assign it a value
            for(int i = 0; i < values; i++){
                assignment[current] = i;

                //check if all the pairs it is involved with are satisfied
                for(Pair variables : constraints.getTable().keySet()){
                    //if it is within the current pair
                    if(variables.contains(current)){
                        //get the current color of each
                        int first = assignment[variables.one];
                        int second = assignment[variables.two];
                        //make sure they have both been assigned
                        if(first != -1 && second != -1){
                            Pair temp = new Pair(first, second);
                            //if the assignment is not consistent
                            if(!constraints.isSatisfied(variables, temp)){
                                //remove the value from the set
                                domains.get(current).remove(i);
                            }
                        }
                    }
                }
            }

            //unassign it
            assignment[current] = -1;
        }

        //now get the MRV
        boolean first = true;
        int best_var = 0, num_vals = 1000;
        for(int variable : domains.keySet()){
            if(first){
                best_var = variable;
                num_vals = domains.get(variable).size();
                first = false;
            }
            else if(domains.get(variable).size() < num_vals){
                best_var = variable;
                num_vals = domains.get(variable).size();
            }
        }

        return best_var;
    }

    //Degree Heuristic
    public int DegreeHeuristic(int[] assignment, Constraint constraints){

        //set up the array
        int[] best = new int[size];
        for(int i = 0; i < best.length; i++){
            if(assignment[i] == -1){
                best[i] = 0;
            }
            else{
                best[i] = -1000000000;
            }

        }

        for(Pair variables : constraints.getTable().keySet()){
            for(int i = 0; i < best.length; i++){
                if(variables.contains(i)){
                    best[i] += 1;
                }
            }
        }

        //get the biggest degree
        int biggest = 0;
        int size = -1;
        for(int i = 0; i < best.length; i++){
            if(best[i] > size){
                size = best[i];
                biggest = i;
            }
        }

        return biggest;

    }


    //MAC-3 inference method
    public boolean AC_three(Constraint constraints, int[] assignment){

        //set up the domains
        HashSet<Integer> possible_vals = new HashSet<Integer>();
        for(int i = 0; i < values; i++){
            possible_vals.add(i);
        }

        //insert into the hashmap
        HashMap<Integer, HashSet<Integer>> domains = new HashMap<Integer, HashSet<Integer>>();
        for(int i = 0; i < size; i++){
            if(assignment[i] == -1){
                domains.put(i, possible_vals);
            }
            else{
                HashSet<Integer> assign_set = new HashSet<>();
                assign_set.add(assignment[i]);
                domains.put(i, assign_set);
            }
        }

        //set up the set of all possible combos
        Queue<Pair> combos = new LinkedList<Pair>();
        combos.addAll(constraints.getTable().keySet());

        //get the head of the queue
        Pair current;

        //while we still are checking pairs
        while(!combos.isEmpty()){
            //move to the next thing in the queue
            current = combos.remove();
            //if we edit any domains, checking either way
            if(ReviseOne(constraints, current, domains)){
                //check if the edited domain is empty
                if(domains.get(current.one).isEmpty()){
                    return false;
                }
                //otherwise add all connecting pairs
                else{
                    for(Pair vars : constraints.getTable().keySet()){
                        if((vars.contains(current.one) || vars.contains(current.two)) && vars != current){
                            combos.add(vars);
                        }
                    }

                }
            }
        }
        return true;
    }

    public boolean ReviseOne(Constraint constraints, Pair current,  HashMap<Integer, HashSet<Integer>> domains) {

        //do (0,1)
        int first = current.one;
        int second = current.two;

        boolean revised = false;
        HashSet<Integer> to_remove = new HashSet<>();
        //for all the values in the first
        for(int val1 : domains.get(first)){
            boolean safe = false;

            //check each pair of values in two
            for(Pair values : constraints.getTable().get(current)){
                if(values.one == val1 && domains.get(second).contains(values.two)){
                    safe = true;
                }
            }

            //if the value did not have any possible values in the other domains
            if(!safe){
                revised = true;
                to_remove.add(val1);

            }

        }
        //if we revised then remove the values
        if(revised){
            for(int val : to_remove){
                domains.get(first).remove(val);
            }
        }

        return revised;
    }
}
