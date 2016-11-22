package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by benjihannam on 10/23/16.
 */
public class AlphaBetaMiniMax implements  ChessAI{

    private HashMap<Long, ArrayList<Integer>> transposition = new HashMap<Long, ArrayList<Integer>>();
    private int nodes_visited;
    //gets the best move
    public  short getMove(Position position) throws  IllegalMoveException{
        //keeps track on number of nodes visited
        Position copy = new Position(position);
        nodes_visited = 0;
        transposition = new HashMap<Long, ArrayList<Integer>>();
        short bestMove = ABMiniMaxDecision(copy, 2);
        System.out.println("Alpha-Beta nodes_visited = " + nodes_visited);
        if(position.isMate()){
            System.out.println("Last Move: " + position.getLastMove());
            System.out.println("GAME OVER: CHECKMATE");
        }
        return bestMove;
    }

    //minimax based off pseudocode in textbook
    public short ABMiniMaxDecision(Position current, int max_depth) throws IllegalMoveException {
        short bestMove = 0;
        int bestScore = Integer.MIN_VALUE;
        //loop over all the moves
        for(short move : current.getAllMoves()){

            //increment the count
            nodes_visited++;
            //do the move
            current.doMove(move);

            //if more than best score then set bestmove
            int temp = MinValue(current, max_depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (temp > bestScore) {
                bestScore = temp;
                bestMove = move;
            }
            //undo the move
            current.undoMove();
        }
        return bestMove;
    }

    //cut off test method
    public boolean CutOffTest(Position current, int depth){
        return depth == 0 || current.isMate() || current.isStaleMate();
    }

    //utility function
    public int Utility(Position current, boolean max) {

        //if checkmate and max
        if (current.isMate() && max) {
            return Integer.MIN_VALUE + 1;
        }
        else if(current.isMate()){
            return Integer.MAX_VALUE - 1;
        }
        else if(current.isStaleMate()){
            return 0;
        }
        else{
            return Evaluation(current);
        }
    }

    //MinValue function
    public int MinValue(Position current, int depth, int alpha, int beta) throws IllegalMoveException{

        //if the position is already in the transposition table
        if(transposition.containsKey(current.getHashCode())){
            //get the array
            ArrayList<Integer> arr_temp = transposition.get(current.getHashCode());
            //if the depths sync up
            if(arr_temp.get(1) >= depth){
                //get the value
                int temp_value = transposition.get(current.getHashCode()).get(0);
                //if the position was a max negate the value
                if(arr_temp.get(2) == 1){
                    temp_value = -temp_value;
                }
                return temp_value;
            }
        }
        //if a final state or maxDepth
         else if(CutOffTest(current, depth)){
            int value = Utility(current, false);
            return value;

        }

        int v = Integer.MAX_VALUE;

        //loop over moves
        for(short move : current.getAllMoves()){
            //do the move and increment the count
            nodes_visited ++;
            current.doMove(move);

            //get the MaxValue for the next level
            int temp = MaxValue(current, depth - 1, alpha, beta);
            //undo the math
            current.undoMove();
            v = Math.min(temp, v);

            if(v <= alpha){
                ArrayList<Integer> arr = new ArrayList<Integer>();
                arr.add(0, v);
                arr.add(1, depth);
                arr.add(2, 0);
                transposition.put(current.getHashCode(), arr);
                return v;
            }

            beta = Math.min(beta, v);
        }
        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(0, v);
        arr.add(1, depth);
        arr.add(2, 0);
        transposition.put(current.getHashCode(), arr);
        return  v;
    }

    //MaxValue funtion
    public int MaxValue(Position current, int depth, int alpha, int beta) throws IllegalMoveException {

        //if the position is already in the transposition table
        if(transposition.containsKey(current.getHashCode())){
            //get the array
            ArrayList<Integer> arr_temp = transposition.get(current.getHashCode());
            //if the depths sync up
            if(arr_temp.get(1) >= depth){
                //get the value
                int temp_value = transposition.get(current.getHashCode()).get(0);
                //if the position was a max negate the value
                if(arr_temp.get(2) == 0){
                    temp_value = -temp_value;
                }
                return temp_value;
            }
        }
        //if a final state or maxDepth
        else if(CutOffTest(current, depth)){
            int value = Utility(current, false);
            return value;

        }
        int v = Integer.MIN_VALUE;

        //loop over the moves
        for (short move : current.getAllMoves()) {

            //do the move and increment count
            nodes_visited++;
            current.doMove(move);

            //get the minvalue
            int temp = MinValue(current, depth, alpha, beta);
            //undo
            current.undoMove();
            //get the max of the two
            v = Math.max(temp, v);

            if(v >= beta){
                ArrayList<Integer> arr = new ArrayList<Integer>();
                arr.add(0, v);
                arr.add(1, depth);
                arr.add(2, 1);
                transposition.put(current.getHashCode(), arr);
                return v;
            }
            alpha = Math.max(alpha, v);

        }
        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(0, v);
        arr.add(1, depth);
        arr.add(2, 1);
        transposition.put(current.getHashCode(), arr);
        return v;
    }

    //Evaluation function
    public int Evaluation(Position current){

        //random int used to prevent repetitive loops
        int rand = (int)(Math.random() * 20);

        return current.getMaterial() + rand;
    }

}
