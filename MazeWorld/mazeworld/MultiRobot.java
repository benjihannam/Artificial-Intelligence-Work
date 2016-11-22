package mazeworld;
//AUTHOR: BENJI HANNAM
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MultiRobot extends SearchProblem{

	//hold the maze layout
	private int maze[][];
	private int goal_locations[];
	private int height;
	private int width;
	private int num_bots;
	
	
	//constructor
	public MultiRobot(int maze_layout[][], int start_locs[], int goal_locs[], int init_height, int init_width, int k){
		
		//initialise the variables
		height = init_height;
		width = init_width;
		maze = maze_layout;
		num_bots = k;
		goal_locations = goal_locs;
		startNode = new MultiNode(start_locs, 0);
		
	}
	
	private class MultiNode implements UUSearchNode{
		//variables to hold the state and depth
		private int[] state;
		private int depth;
		
		public MultiNode(int robot_locations[], int new_depth){
			//initialise the variables
			depth = new_depth;
			state = new int[2*num_bots + 1];
			//input the locations and whose turn it is
			for(int i = 0; i < 2*num_bots + 1; i++){
				state[i] = robot_locations[i];
			}
		}
		
		//the goal test
		//loop through the state array and check that the coords match those of the goal array
		public boolean goalTest(){
			for(int i = 0; i < 2*num_bots; i++){
				//if the two slots in the array at spot i don't match
				if(state[i] != goal_locations[i]){	
					return false;
				}
			}
			return true;
		}
		
		//get the next possible state
		public ArrayList<UUSearchNode> getSuccessors(){
			
			//the array to hold the successors
			ArrayList<UUSearchNode> children = new ArrayList<UUSearchNode>();
			
			//whose turn it is and who is next
			int whose_turn = state[2 * num_bots];
			int next = (whose_turn + 1) % num_bots;
			
			//the arrays to hold the new states
			int north[] = new int[2*num_bots + 1];
			int south[] = new int[2*num_bots + 1];
			int east[] = new int[2*num_bots + 1];
			int west[] = new int[2*num_bots + 1];
			int stay[] = new int[2*num_bots + 1];
			System.arraycopy(state, 0, north, 0, state.length);
			System.arraycopy(state, 0, south, 0, state.length);
			System.arraycopy(state, 0, east, 0, state.length);
			System.arraycopy(state, 0, west, 0, state.length);
			System.arraycopy(state, 0, stay, 0, state.length);
			
			//update the north array
			north[2 * whose_turn + 1] = north[2 * whose_turn + 1] - 1;
			north[2 * num_bots] = next;
			MultiNode north_node = new MultiNode(north, depth + 1);
			if(north_node.isSafeState()){
				children.add(north_node);
			}
			
			//update the south array
			south[2 * whose_turn + 1] += 1;
			south[2 * num_bots] = next;
			MultiNode south_node = new MultiNode(south, depth + 1);
			if(south_node.isSafeState()){
				children.add(south_node);
			}
			
			//update the east array
			east[2 * whose_turn] = east[2 * whose_turn] + 1;
			east[2 * num_bots] = next;
			MultiNode east_node = new MultiNode(east, depth + 1);
			if(east_node.isSafeState()){
				children.add(east_node);
			}
			
			//update the west array
			west[2 * whose_turn] = (west[2 * whose_turn] - 1);
			west[2 * num_bots] = next;
			MultiNode west_node = new MultiNode(west, depth + 1);
			if(west_node.isSafeState()){
				children.add(west_node);
			}
			
			//update the stay array
			stay[2 * num_bots] = next;
			MultiNode stay_node = new MultiNode(stay, depth + 1);
			if(stay_node.isSafeState()){
				children.add(stay_node);
			}
			return children;
		}
		
		//check if a state is safe
		public boolean isSafeState(){
			// get each pair of x and y
				int i = state[2*num_bots];
				int x = state[2*i];
				int y = state[2*i + 1];
				//check they are in bounds
				if(x < 0 || x >= width || y < 0 || y >= height){
					return false;
				}
				//check they are not a wall
				else if(maze[x][y] == -1){
					return false;
				}
				//check if there is not a robot already there
				else{
					for(int j = 0; j < num_bots; j++){
						if(j != i){
							if(x == state[2*j] && y == state[2*j +1]){
								return false;
							}
						}
					}
				}
			return true;
		}

		@Override
		public int getDepth() {
			return depth;
		}

		@Override
		public int getPriority() {
			int total = depth;
			
			for(int i = 0; i < num_bots; i++){
				// get the current and goal coords
				int goal_x = goal_locations[2*i];
				int goal_y = goal_locations[2*i + 1];
				int bot_x = state[2*i];
				int bot_y = state[2*i + 1];
				
				//get the manhattan distance and add it to the total for each robot
				int man_x = Math.abs(bot_x - goal_x);
				int man_y = Math.abs(bot_y - goal_y);
				total += man_x + man_y;
			}
			return total;
		}
		
		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			for(int i = 0; i < 2*num_bots; i++){
				if(state[i] != ((MultiNode) other).state[i]){
					return false;
				}
			}
			return true;
		}
		
		//hashfunction
		@Override
		public int hashCode(){
			int total = 0;
			for(int i = 0; i < 2*num_bots + 1; i++){
				total += state[i];
				total = total * 10;
			}
			return Arrays.hashCode(state);
			//return total;
		}
		
		//string display
		@Override
		public String toString() {
			
			String answer = "[";
			for(int i = 0; i < 2 * num_bots + 1; i++){
				answer += state[i];
				answer += ", ";
			}
			answer += "]";
			return answer;
		}
		
		@Override
		//draw the maze for the robot
		public void robot_draw(){
			//loop through rows
			for (int y = 0; y < height; y++){
				System.out.printf("\n");
				//loop through columns
				for(int x = 0; x < width; x++){
					//should we draw as empty
					boolean drawn = true;
					//check for a wall
					if(maze[x][y] == -1){
						System.out.printf("#");
						drawn = false;
					}
					for(int i = 0; i < num_bots; i++){
						//check for a robot
						if(state[2*i] == x && state[2*i + 1] == y){
							System.out.printf("%d" , i);
							drawn = false;
						}
					}
					for(int i = 0; i < num_bots; i++){
						//check if its a goal
						if(goal_locations[2*i] == x && goal_locations[2*i + 1] == y && drawn){
							System.out.printf("G");
							drawn = false;
						}
					}
					if(drawn){
						System.out.printf(".");
					}
				}
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void getDirection() {
			// TODO Auto-generated method stub
			
		}
	}
}
