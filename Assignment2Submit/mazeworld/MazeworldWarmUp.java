package mazeworld;

// AUTHOR: BENJI HANNAM
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MazeworldWarmUp extends SearchProblem{
	
	//hold the maze layout
	private int maze[][];
	private int height;
	private int width;
	private int start_x;
	private int start_y;
	
	//holds the goal location
	private int goal_x;
	private int goal_y;
	
	//constructor
	public MazeworldWarmUp(int maze_layout[][], int init_height, int init_width, int gx, int gy, int init_x, int init_y){
		
		//initialize values
		maze = maze_layout;
		goal_x = gx;
		goal_y = gy;
		start_x = init_x;
		start_y = init_y;
		height = init_height;
		width = init_width;
		startNode = new MazeNode(init_x, init_y, 0);
	}
	
	//a node class for each state of the game
	private class MazeNode implements UUSearchNode{
		
		//hold the current location
		private int x;
		private int y;
		private int depth;
		
		public MazeNode(int start_x, int start_y, int depth){
			
			this.x = start_x;
			this.y = start_y;
			this.depth = depth;
		}
		
		//gets all the child nodes of a state
		public ArrayList<UUSearchNode> getSuccessors(){
			
			//the array to hold the successors
			ArrayList<UUSearchNode> children = new ArrayList<UUSearchNode>();
			
			//move up x
			MazeNode up_x = new MazeNode(this.x + 1, this.y, depth + 1);
			//check it
			if(up_x.isSafeState()){
				children.add(up_x);
			}
			
			//move down x
			MazeNode down_x = new MazeNode(this.x - 1, this.y, depth + 1);
			//check it
			if(down_x.isSafeState()){
				children.add(down_x);
			}
			
			//move up y
			MazeNode up_y= new MazeNode(this.x, this.y + 1, depth + 1);
			//check it
			if(up_y.isSafeState()){
				children.add(up_y);
			}
			
			//move down y
			MazeNode down_y = new MazeNode(this.x, y - 1, depth + 1);
			//check it
			if(down_y.isSafeState()){
				children.add(down_y);
			}				
			return children;	
		}
		
		//checks if a state is valid
		public boolean isSafeState(){
			
			//check if the location is within bounds
			if( !(this.x >= 0 && this.x < width && this.y >= 0 && this.y < height) ){
				return false;
			}
			
			//check if the location is a wall, walls are marked in the maze as -1 at maze[x][y]
			if( maze[this.x][this.y] == -1 ){
				return false;
			}
			else{
				return true;
			}
		}
		
		//check if a state is the goal state
		public boolean goalTest(){
			// check if the coordinates match
			return( this.x == goal_x && this.y == goal_y);
		}
		
		//get the priority of a node (depth + manhattan distance)
		@Override
		public int getPriority(){
			//get the x and y distances
			int man_x = Math.abs(this.x - goal_x);
			int man_y = Math.abs(this.y - goal_y);
	
			return man_x + man_y + this.depth;
		}

		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return (this.x == ((MazeNode) other).x && this.y == ((MazeNode) other).y);
		}
		
		@Override
		public String toString() {
			// you write this method
			return "[" + this.x + "," + this.y + "]";
		}
		
		@Override
		public int hashCode(){
			return x*10 + y*10;
		}

		@Override
		public int getDepth() {
			return this.depth;
		}

		@Override
		public void robot_draw() {
			
			for (int y = 0; y < height; y++){
				System.out.printf("\n");
				for(int x = 0; x < width; x++){
					if(this.x == x && this.y == y){
						System.out.printf("R");
					}
					else if(maze[x][y] == -1){
						System.out.printf("#");
					}
					else{
						System.out.printf(".");
					}	
				}
			}
			System.out.println("");
			System.out.println("---------------");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void getDirection() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//updates the current position of the robot
	public void updateCurrent(int new_x, int new_y){	
		start_x = new_x;
		start_y = new_y;
	}
	
	//draws the maze
	public void drawMaze(){

		for (int y = 0; y < this.height; y++){
			System.out.printf("\n");
			for(int x = 0; x < this.width; x++){
				if(this.maze[x][y] == -1){
					System.out.printf("#");
				}
				else if(x == this.start_x && y == this.start_y){
					System.out.printf("S");
				}
				else if(x == this.goal_x && y == this.goal_y){
					System.out.printf("G");
				}
				else{
					System.out.printf(".");
				}
			}
		}
		System.out.println("");
		System.out.println("");
	}
	
	
}