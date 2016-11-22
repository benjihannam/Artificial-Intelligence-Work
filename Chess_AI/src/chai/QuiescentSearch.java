package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

/**
 * Created by benjihannam on 10/23/16.
 * Quiescent search is done based of the code from https://chessprogramming.wikispaces.com/Quiescence+Search;
 */
public class QuiescentSearch implements ChessAI{

    private int nodes_visited;
    //gets the best move
    public  short getMove(Position position) throws IllegalMoveException {
        //keeps track on number of nodes visited
        nodes_visited = 0;
        Position copy = new Position(position);
        short bestMove = QuiescentStart(copy, 2);
        System.out.println("quiesce nodes_visited = " + nodes_visited);
        if(position.isMate()){
            System.out.println("Last Move: " + position.getLastMove());
            System.out.println("GAME OVER: CHECKMATE");
        }
        System.out.println("Returning move: " + bestMove);
        return bestMove;
    }

    //minimax based off 164
    public short QuiescentStart(Position current, int max_depth) throws IllegalMoveException {
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
        System.out.println("returing best move");
        return bestMove;
    }

    //cut off test method
    public boolean CutOffTest(Position current, int depth){
        return depth == 0 || current.isMate() || current.isStaleMate();
    }

    //MinValue function
    public int MinValue(Position current, int depth, int alpha, int beta) throws IllegalMoveException{

        //if a final state or maxDepth
        if(CutOffTest(current, depth)){
            return Quiesce(current, alpha, beta, false);
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
                return v;
            }

            beta = Math.min(beta, v);
        }
        return  v;
    }

    //MaxValue funtion
    public int MaxValue(Position current, int depth, int alpha, int beta) throws IllegalMoveException {

        //if a final state or at Maxdepth
        if (CutOffTest(current, depth)) {
            //if checkmate and max
            return Quiesce(current, alpha, beta, true);
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
                return v;
            }
            alpha = Math.max(alpha, v);

        }
        return v;
    }

    //Evaluation function
    public int Evaluation(Position current){

        //random int used to prevent repetitive loops
        int rand = (int)(Math.random() * 30);

        return current.getMaterial() + rand;
    }

    //is it a quiet position
    public boolean isQuiet(Position current){
        return !(current.getLastMove().isCapturing() || current.getLastMove().isCheck());
    }

    //Quiescent search based off of code from https://chessprogramming.wikispaces.com/Quiescence+Search;
    public int Quiesce(Position current, int alpha, int beta, boolean max) throws IllegalMoveException{

        //if we are at a check/stale mate
        if(current.isMate() && max){
            return Integer.MIN_VALUE + 1;
        }
        else if (current.isMate()) {
            return Integer.MAX_VALUE - 1;
        }
        else if(current.isStaleMate()){
            return 0;
        }

        //base value to be used as the lower bound
        int base = Evaluation(current);

        //if we are bigger than a value we have already found from a different node, return the other value
        if(base >= beta){
            return beta;
        }

        //if we are less that the small, reset the lower bound
        if(alpha < base){
            alpha = base;
        }
        int score;
        //loop through all the capturing moves
        for(short move : current.getAllMoves()){
            //do the move
            current.doMove(move);
            //check if we are at a quiet state
            if(Move.isCapturing(move) || current.isCheck()){
                current.undoMove();
                continue;
            }
            //get the score from the recursive call
            score = -Quiesce(current, -beta, -alpha, !max);
            current.undoMove();

            //if greater than the upper bound
            if (score >= beta) {
                return beta;
            }
            //if greater than the lower bound
            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

}
