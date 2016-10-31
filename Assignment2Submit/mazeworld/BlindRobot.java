package mazeworld;
//AUTHOR: BENJI HANNAM
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BlindRobot extends SearchProblem{
	
	//hold the maze layout
	private int maze[][];
	private int height;
	private int width;
	
	//holds the goal location
	private int goal_x;
	private int goal_y;
	
	//constructor
	public BlindRobot(int maze_layout[][], int init_height, int init_width, int gx, int gy){
		
		//set for the start state
		Set<Pair> start_set = new HashSet<Pair>();
		
		//initialize values
		maze = maze_layout;
		goal_x = gx;
		goal_y = gy;
		height = init_height;
		width = init_width;
		
		//set up the start state
		//loop through all the spots in the maze
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				Pair possible_location = new Pair(x, y);
				//if its not a wall add it to the start set
				if(maze[x][y] != -1){
					start_set.add(possible_location);
				}
			}
		}
		startNode = new BlindNode(start_set, 0, 0);
				
	}
	
	// a class to hold a x,y pair
	private class Pair{
		private int x;
		private int y;
		
		public Pair(int nx, int ny){
			x = nx;
			y = ny;
		}
		
		public boolean equals(Object other) {
			return (x == ((Pair) other).x) && (y == ((Pair) other).y);
		}
		
		//string display
		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
		
		//hashfunction
		@Override
		public int hashCode(){
			int total = 0;
			total += x*10 + y*8;
			return total;
		}
	}
	
	//the node class for a state
	
	private class BlindNode implements UUSearchNode{
		
		private Set<Pair> state_set;
		private int depth;
		private int last_direction;
		
		public BlindNode(Set<Pair> state, int new_depth, int direction){
			state_set = state;
			depth = new_depth;
			last_direction = direction;
		}
		
		//get the child nodes
		public ArrayList<UUSearchNode> getSuccessors(){
			
			//set up the array
			ArrayList<UUSearchNode> children = new ArrayList<UUSearchNode>();
			
			//loop through set and go north in each node
			Set<Pair> north_set = new HashSet<Pair>();
			for(Pair child: state_set){
				Pair north = go_direction(1, child);
				if(isSafePair(north)){
					north_set.add(north);
				}
			}
			BlindNode north_child = new BlindNode(north_set, depth + 1, 1);
			
			//loop through set and go east in each node
			Set<Pair> east_set = new HashSet<Pair>();
			for(Pair child: state_set){
				Pair east = go_direction(2, child);
				if(isSafePair(east)){
					east_set.add(east);
				}
			}
			BlindNode east_child = new BlindNode(east_set, depth + 1, 2);
		
			//loop through set and go south in each node
			Set<Pair> south_set = new HashSet<Pair>();
			for(Pair child: state_set){
				Pair south = go_direction(3, child);
				if(isSafePair(south)){
					south_set.add(south);
				}
			}
			BlindNode south_child = new BlindNode(south_set, depth + 1, 3);
			
			//loop through set and go west in each node
			Set<Pair> west_set = new HashSet<Pair>();
			for(Pair child: state_set){
				Pair west = go_direction(4, child);
				if(isSafePair(west)){
					west_set.add(west);
				}
			}
			BlindNode west_child = new BlindNode(west_set, depth + 1, 4);
		
			//add the new states
			children.add(north_child);
			children.add(east_child);
			children.add(south_child);
			children.add(west_child);
			
			return children;
		}
		
		//check if we are have a safe Pair
		public boolean isSafePair(Pair test){
			if( (test.x < 0) || (test.x >= width)){
				return false;
			}
			else if( (test.y < 0) || (test.y >= height)){
				return false;
			}
			else{
				return (maze[test.x][test.y] != -1);
			}
		}
		
		//go a direction
		public Pair go_direction(int direction, Pair child){
			
			//go north one spot
			if(direction == 1){	
				if(child.y > 0 && maze[child.x][child.y - 1] != -1){
					Pair north_spot = new Pair(child.x, child.y - 1);
					return north_spot;
				}
				else{
					return child;
				}
			}
			//go east one spot
			else if(direction == 2){
				if(child.x < width - 1 && maze[child.x + 1][child.y] != -1){
					Pair east_spot = new Pair(child.x + 1, child.y);
					return east_spot;
				}
				else{
					return child;
				}
			}
			//go south one spot
			else if(direction == 3){
				if(child.y < height - 1 && maze[child.x][child.y + 1] != -1){
					Pair south_spot = new Pair(child.x, child.y + 1);
					return south_spot;
				}
				else{
					return child;
				}
			}
			//go west one spot
			else{
				if(child.x > 0 && maze[child.x - 1][child.y] != -1){
					Pair west_spot = new Pair(child.x - 1, child.y);
					return west_spot;
				}
				else{
					return child;
				}
			}
		}

		@Override
		public boolean goalTest() {
			if(state_set.size() == 1){
				for(Pair temp : state_set){
					return temp.x == goal_x && temp.y == goal_y;
				}
				return false;
			}
			else{
				return false;
			}
		}
		
		//hashfunction
		@Override
		public int hashCode(){
			int total = 0;
			for(Pair coords : state_set){
				total += coords.x + coords.y;
				total = total * 10;
			}
			return total;
		}
		
		@Override
		public int getDepth() {
			return depth;
		}
		
		// an equality test is required so that visited lists in searches
		// can check for containment of states
		//@Override
		public boolean equals(Object other) {
			//if everything in one set is in the other and vice versa
			if (state_set.containsAll(((BlindNode)other).state_set) && ((BlindNode)other).state_set.containsAll(state_set)) {
					return true;
				}
			else{
				return false;
			}
		}

		@Override
		public int getPriority() {
			int total = depth + state_set.size() * 10;
			for(Pair coords: state_set){
				//get the manhattan distance and add it to the total for each robot
				int man_x = Math.abs(coords.x - goal_x);
				int man_y = Math.abs(coords.y - goal_y);
				total += man_x + man_y;
			}
			return total;
		}
		
		//string display
		@Override
		public String toString() {
			// you write this method
			String answer = "[";
			String last_dir;
			//get the string for the last_direction
			if(last_direction == 1){
				last_dir = "north";
			}
			else if(last_direction == 2){
				last_dir = "east";
			}
			else if(last_direction == 3){
				last_dir = "south";
			}
			else if(last_direction == 4){
				last_dir = "west";
			}
			else{
				last_dir = "start";
			}
			for(Pair coords : state_set){
				String temp = ", " + coords;
				answer += temp;
			}
			answer += "] "+ last_dir;
			return answer;
		}
		
		// a method to draw out all the possible locations of the robot in a maze
		@Override
		public void robot_draw() {
			System.out.println("Possible locations");
			int temp_maze[][] = new int[width][height];
			for(Pair coords : state_set){
				temp_maze[coords.x][coords.y] = 1;
			}
			System.out.println(" ");
			for(int y = 0; y < height; y++){
				System.out.println("");
				for(int x = 0; x < width; x++){
					if(temp_maze[x][y] == 1){
						System.out.printf("R");
					}
					else if(x == goal_x && y == goal_y){
						System.out.printf("G");
					}
					else if( maze[x][y] == -1){
						System.out.printf("#");
					}
					else{
						System.out.printf(".");
					}
				}
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void getDirection(){
			String last_dir;
			if(last_direction == 1){
				last_dir = "north";
			}
			else if(last_direction == 2){
				last_dir = "east";
			}
			else if(last_direction == 3){
				last_dir = "south";
			}
			else if(last_direction == 4){
				last_dir = "west";
			}
			else{
				last_dir = "start";
			}
			System.out.println("Last Direction:" + last_dir);;
		}
	}
		
}
