import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by benjihannam on 10/30/16.
 */
public class MapColoringCSP extends ConstraintSatisfactionProblem{
    public String[] names;
    public String[] colors;
    public Constraint constraints;
    public HashMap<Pair, HashSet<Pair>> table;
    public int num_states = 8;

    public MapColoringCSP(){

        names = new String[num_states];
        colors = new String[3];
        table = new HashMap<Pair, HashSet<Pair>>();
        //set up the names
        names[0] = "WA";
        names[1] = "NT";
        names[2] = "SA";
        names[3] = "Q";
        names[4] = "NSW";
        names[5] = "V";
        names[6] = "T";
        names[7] = "extra";

        colors[0] = "red";
        colors[1] = "green";
        colors[2] = "blue";


        //set up the set of possible combinations
        HashSet<Pair> adjacent_combos = new HashSet<Pair>();
        //add r,g and g,r
        adjacent_combos.add(new Pair(0,1));
        adjacent_combos.add(new Pair(1,0));
        //add r,b and b,r
        adjacent_combos.add(new Pair(0,2));
        adjacent_combos.add(new Pair(2,0));
        //add b,g and b,r
        adjacent_combos.add(new Pair(2,1));
        adjacent_combos.add(new Pair(1,2));


        //set up the links beween states/territories
        //WA and NT
        table.put(new Pair(0,1), adjacent_combos);
        //WA and SA
        table.put(new Pair(0,2), adjacent_combos);
        //NT and SA
        table.put(new Pair(1,2), adjacent_combos);
        //NT and Q
        table.put(new Pair(1,3), adjacent_combos);
        //SA and Q
        table.put(new Pair(2,3), adjacent_combos);
        //SA and NSW
        table.put(new Pair(2,4), adjacent_combos);
        //SA and V
        table.put(new Pair(2,5), adjacent_combos);
        //Q and NSW
        table.put(new Pair(3,4), adjacent_combos);
        //NSW and V
        table.put(new Pair(4,5), adjacent_combos);
        //T and extra
        table.put(new Pair(6,7), adjacent_combos);
        constraints = new Constraint(table);

    }

    //gets the solution to the problem
    public void getSolution(){
        ConstraintSatisfactionProblem CSP = new ConstraintSatisfactionProblem(8, 3);
        int[] solution = CSP.BacktrackingSearch(constraints);
        if(solution == null){
            System.out.println("no solution found");
        }
        else {
            System.out.println("solution = " + Arrays.toString(solution));
            for (int i = 0; i < solution.length; i++) {
                int color = 0;
                if(solution[i] != -1){
                    color = solution[i];
                }

                System.out.println(names[i] + " is colored " + colors[color]);
            }
        }
        System.out.println("Number of values looked at: " + CSP.num_vals_looked_at);
    }


    public static void main(String[] args){
        MapColoringCSP test = new MapColoringCSP();
        test.getSolution();
    }

}
