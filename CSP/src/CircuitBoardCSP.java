import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by benjihannam on 11/5/16.
 */
public class CircuitBoardCSP extends ConstraintSatisfactionProblem {

        public BoardPiece[] pieces;
        public int[] corners;
        public Constraint constraints;
        public HashMap<Pair, HashSet<Pair>> table;
        public int num_states = 4;
        public int board_height = 4;
        public int board_width = 4;


        public CircuitBoardCSP(){


            pieces = new BoardPiece[num_states];
            corners = new int[num_states];
            table = new HashMap<Pair, HashSet<Pair>>();

            //the board is set up so that from the top left the spots are labeled as follows for a 10X3 board
            // 20,21,22,23,24,25,26,27,28,29
            // 10,11,12,13,14,15,16,17,18,19
            //  0, 1, 2, 3, 4, 5, 6, 7, 8, 9

            //therefore a spot of (x,y) in coords will be height*y + x

            //set up pieces
            BoardPiece zero = new BoardPiece(2,2, "a");
            BoardPiece one = new BoardPiece(2,2, "b");
            BoardPiece two = new BoardPiece(2,2, "c");
            BoardPiece three = new BoardPiece(2,2, "d");
            pieces[0] = zero;
            pieces[1] = one;
            pieces[2] = two;
            pieces[3] = three;

            //now set up the constrains
            //for each pair of pieces
            boolean has_values = false;
            for(int p1 = 0; p1 < num_states; p1++){
                for(int p2 = 0; p2 < num_states; p2++){

                    if(p1 < p2){
                        //get each possible location for each
                        for(int x1 = 0; x1 < board_width; x1++){

                            //check x1 for early termination
                            if( (x1 > -1 && (x1 + pieces[p1].width -1) < board_width)){
                                for(int y1 = 0; y1 < board_height; y1++){

                                    //check y1 for early termination and loop through x2
                                    if( (y1 > -1 && (y1 + pieces[p1].height -1) < board_height)){
                                        for(int x2 = 0; x2 < board_width; x2++){

                                            //check x2 and loop through y2
                                            if((x2 > -1 && (x2 + pieces[p2].width -1) < board_width)){

                                                for(int y2 = 0; y2 < board_height; y2++){

                                                    if((y2 > -1 && (y2 + pieces[p2].height -1) < board_height)){

                                                        int w1 = pieces[p1].width, w2 = pieces[p2].width;
                                                        int h1 = pieces[p1].height, h2 = pieces[p2].height;
                                                        boolean coord_check = CoordCheck(x1, y1, x2, y2, w1, h1, w2, h2);

                                                        //if they are both fine
                                                        if(coord_check){

                                                            //get the coords
                                                            int coord1 = y1 * board_width + x1;
                                                            int coord2 = y2 * board_width + x2;

                                                            //create the new pair
                                                            Pair pieces = new Pair(p1, p2);
                                                            Pair values = new Pair(coord1, coord2);
                                                            //put them into the constraint
                                                            if(table.containsKey(pieces)){
                                                                table.get(pieces).add(values);
                                                            }
                                                            else{
                                                                HashSet<Pair> vals = new HashSet<Pair>();
                                                                vals.add(values);
                                                                table.put(pieces, vals);
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //there must be a pair of values for every one otherwise it's invalid, n! pairs.
            int pair_numbers = NChoosek(num_states, 2);
            if(table.keySet().size() == pair_numbers){
                constraints = new Constraint(table);
            }
            else{
                constraints = new Constraint(new HashMap<>());
            }

            System.out.println("table = " + table);


    }

    //check the coords of both
    public boolean CoordCheck(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2){

        //if y1 comfortably below
        if(y2 >= y1 + h1){
            return true;
        }
        //if comfortable above
        else if(y1 >= y2 + h2){
            return true;
        }
        //if comfortable to the left
        if(x2 >= x1 + w1){
            return true;
        }
        //if comfortable to the right
        else if(x1 >= x2 + w2){
            return true;
        }
        return false;
    }

    //n choose k, based off code from http://stackoverflow.com/questions/15301885/calculate-value-of-n-choose-k
    public int NChoosek(int n, int k){
        if(k == 0){
            return 1;
        }
        else{
            return (n* NChoosek(n-1, k-1)) / k;
        }
    }

    //print the board
    public void printBoard(int[] solution){
        for(int y = board_height -1; y > -1; y--){
            for(int x = 0; x < board_width; x++){
                boolean print = true;
                for(int i = 0; i < solution.length; i++){
                    int piece_x = solution[i] % board_width;
                    int piece_y = (solution[i] - piece_x) / board_width;
//                    System.out.println("solution = " + solution[i]);
//                    System.out.println("piece_x = " + piece_x);
//                    System.out.println("piece_y = " + piece_y);
                    if( (x >= piece_x && x < piece_x + pieces[i].width) &&
                            (y >= piece_y && y < piece_y + pieces[i].height)){
                        print = false;
                        System.out.printf("%s", pieces[i].name);
                    }
                }
                if(print){
                    System.out.printf(".");
                }
            }
            System.out.printf("\n");
        }

    }

    //gets the solution to the problem
    public void getSolution(){
        ConstraintSatisfactionProblem CSP = new ConstraintSatisfactionProblem(num_states, board_height*board_width);
        int[] solution = CSP.BacktrackingSearch(constraints);
        if(solution == null){
            System.out.println("no solution found");
        }
        else {
            //put print solution code here
            printBoard(solution);
            System.out.println("solution found");
            System.out.println("Number of values looked at: " + CSP.num_vals_looked_at);
        }
    }


    public static void main(String[] args){
        CircuitBoardCSP test = new CircuitBoardCSP();
        test.getSolution();
    }
}
