package assignment1;

import java.util.ArrayList;
import java.util.Arrays;


// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem {

	// the following are the only instance variables you should need.
	//  (some others might be inherited from UUSearchProblem, but worry
	//  about that later.)

	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 

	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		// I (djb) wrote the constructor; nothing for you to do here.

		startNode = new CannibalNode(sm, sc, 1, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;
		
	}
	
	// node class used by searches.  Searches themselves are implemented
	//  in UUSearchProblem.
	private class CannibalNode implements UUSearchNode {

		// do not change BOAT_SIZE without considering how it affect
		// getSuccessors. 
		
		private final static int BOAT_SIZE = 2;
	
		// how many missionaries, cannibals, and boats
		// are on the starting shore
		private int[] state; 
		
		// how far the current node is from the start.  
		// Not strictly required for search, but useful 
		// information for debugging, and for comparing paths
		private int depth;  

		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;
			
			depth = d;

		}

		public ArrayList<UUSearchNode> getSuccessors() {
			
			//the array to hold the Successor nodes
			ArrayList<UUSearchNode> list = new ArrayList<UUSearchNode>();
			
			//ints holding values of current state
			int missionaries = this.state[0];
			int cannibals = this.state[1];
			int boat = this.state[2];
			
			
			//if the current state has the boat then 
			//take away missionaries/cannibals
			if(boat == 1){
				//try taking away up to BOAT_SIZE missionaries or cannibals
				for(int i = 0; i <= BOAT_SIZE; i++){
					for(int j = 0; j <= BOAT_SIZE - i; j++){
						//check for the i = 0 and j = 0 state
						if( !(i == 0 && j == 0)){
							//create the new node
							CannibalNode new_node = 
									new CannibalNode(missionaries - i, cannibals - j, 0, depth + 1);
							
							// if the node is safe add it to the list
							if(new_node.isSafeState()){
								
								list.add(new_node);
							}
						}
						
					}				
				}	
			}
			//otherwise add missionaries/cannibals
			else if(boat == 0){
				
				//try adding BOAT_SIZE missionaries or cannibals
				for(int i = 0; i <= BOAT_SIZE; i++){
					for(int j = 0; j <= BOAT_SIZE - i; j++){
						//check for the i = 0 and j = 0 state
						if( !(i == 0 && j == 0)){
							//create the new node
							CannibalNode new_node = 
									new CannibalNode(missionaries + i, cannibals + j, 1, depth + 1);
							
							// if the node is safe add it to the list
							if(new_node.isSafeState()){
								list.add(new_node);
							}
						}
						
					}				
				}	
			}
			return list;
		}
		
		@Override
		public boolean goalTest() {
			
			//check that we have the goal amounts of missionaries, cannibals and boats
			return ((this.state[0] == goalm) && (this.state[1] == goalc) && (this.state[2] == goalb)); 
		}
		
		//a test to check if a node is a safe state
		private boolean isSafeState(){
			
			
			//check if the missionaries are within bounds
			if(this.state[0] < 0 || this.state[0] > totalMissionaries){
				return false;
			}
			
			//check if the cannibals are within bounds
			if(this.state[1] < 0 || this.state[1] > totalCannibals){
				return false;
			}
			
			//if there are less missionaries than number of cannibals on this bank
			if( (this.state[0] < this.state[1]) && (this.state[0] > 0) ){
				return false;
			}
			
			//if there are less missionaries on the other bank
			if( ((totalMissionaries - this.state[0]) < (totalCannibals - this.state[1])) && ((totalMissionaries - this.state[0]) > 0) ){
				return false;
			}
			
			//what if the boat is on the start bank with no one
			if(this.state[0] == 0 && this.state[1] == 0 && this.state[2] == 1){
				return false;
			}
			//what if the boat is on the end bank with no one
			else if(this.state[0] == totalMissionaries && this.state[1] == totalCannibals && this.state[2] == 0){
				return false;
			}
			else{
				return true;
			}		
		}
		

		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[2];
		}

		@Override
		public String toString() {
			// you write this method
			return "[" + this.state[0] + this.state[1] + this.state[2] + "]";
		}

		/*
        You might need this method when you start writing 
        (and debugging) UUSearchProblem.
        */
		@Override
		public int getDepth() {
			return depth;
		}
		
		
	}
}

