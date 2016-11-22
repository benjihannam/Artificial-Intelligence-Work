package chai;

import chai.ChessAI;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import javafx.geometry.Pos;

/**
 * Created by benjihannam on 10/20/16.
 */
public class Minimax implements ChessAI{

    private int nodes_visited;
    //gets the best move
    public  short getMove(Position position) throws  IllegalMoveException{
        //keeps track on number of nodes visited
        nodes_visited = 0;
        short bestMove = MiniMaxDecision(position, 2);
        System.out.println("Minimax nodes_visited = " + nodes_visited);
        if(position.isMate()){
            System.out.println("Last Move: " + position.getLastMove());
        }
        return bestMove;
    }

    //minimax based off the pseudocode in the book
    public short MiniMaxDecision(Position current, int max_depth) throws IllegalMoveException {
       short bestMove = 0;
        int bestScore = Integer.MIN_VALUE;
        //loop over all the moves
       for(short move : current.getAllMoves()){

           //increment the count
            nodes_visited++;
           //do the move
           current.doMove(move);

           //if more than best score then set bestmove
           int temp = MinValue(current, max_depth);
           if (temp > bestScore) {
               bestScore = temp;
               System.out.println(move);
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
    public int MinValue(Position current, int depth) throws IllegalMoveException{

        //if a final state or maxDepth
        if(CutOffTest(current, depth)){
            return Utility(current, false);

        }
        int v = Integer.MAX_VALUE;

        //loop over moves
        for(short move : current.getAllMoves()){
            //do the move and increment the count
            nodes_visited ++;
            current.doMove(move);

            //get the MaxValue for the next level
            int temp = MaxValue(current, depth - 1);
            v = Math.min(temp, v);

            //undo the math
            current.undoMove();
        }
        return  v;
    }

    //MaxValue funtion
    public int MaxValue(Position current, int depth) throws IllegalMoveException {

        //if a final state or at Maxdepth
        if (CutOffTest(current, depth)) {
            return Utility(current, true);
        }
        int v = Integer.MIN_VALUE;

        //loop over the moves
        for (short move : current.getAllMoves()) {

            //do the move and increment count
            nodes_visited++;
            current.doMove(move);

            //get the minvalue
            int temp = MinValue(current, depth);
            //get the max of the two
            v = Math.max(temp, v);
            //undo
            current.undoMove();
        }
        return v;
    }

    //Evaluation function
    public int Evaluation(Position current){

        //random int used to prevent repetitive loops
        int rand = (int)(Math.random() * 20);

        return current.getMaterial() + rand;
    }
}
