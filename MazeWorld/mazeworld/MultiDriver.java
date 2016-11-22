package mazeworld;

import java.util.Arrays;
import java.util.List;

public class MultiDriver {

	public static void main(String[] args) {
		
		//Set up the maze
		int width = 10;
		int height = 10;
		//set the number of robots: 1,2 or 3
		int k = 3;
		
		//robot 1
		int x1 = 0;
		int y1 = height/2;
		int gx1 = width/2;
		int gy1 = 0;
		
		//robot 2
		int x2 = width - 1;
		int y2 = height/2;
		int gx2 = 0;
		int gy2 = height/2;
		
		//robot 3
		int x3 = width/2;
		int y3 = 0;
		int gx3 = width -1;
		int gy3 = height/2;
		
		//################################################################################################################	
		//MAZE0: An empty maze
		int new_maze0[][] = new int[width][height];
		
		//create a multi robot maze
		//MAZE1: A maze with walls at all x = y positions except (0,0); Start is top right, Goal is bottom left;
		int new_maze1[][] = new int[width][height];
		for(int x = 2; x < width - 2; x++){
				new_maze1[x][x] = -1;
				new_maze1[width - x][x] = -1;
		}
		
		//MAZE2: A maze with a wall down a column, opening in the middle, Start is top right, Goal is bottom left;
		int new_maze2[][] = new int[width][height];
		int column = width/2;
		for(int y = 0; y < height; y++){
			if(y != height/2){
				new_maze2[column][y] = -1;
			}
		}
		
		//corridor test
		int new_maze3[][] = new int[width][height];
		// make corners walls
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(x == width/2 && y == height/2 -1){
					new_maze3[x][y] = -10;
				}
				else if(y == height/2){
					new_maze3[x][y] = -10;
				}
				else{
					new_maze3[x][y] = -1;
				}
			}
		}
		
		//T-shaped Maze
		int new_maze4[][] = new int[width][height];
		// make corners walls
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(x == width/2 && y <= height/2 -1){
					new_maze4[x][y] = -10;
				}
				else if(y == height/2){
					new_maze4[x][y] = -10;
				}
				else{
					new_maze4[x][y] = -1;
				}
			}
		}
		//################################################################################################################	
			
		
		//the start positions and goal positions
		int goal_locs[] = new int[2*k];
		int start_locs[] = new int[2*k + 1];
		if(k > 0){
		start_locs[0] = x1;
		start_locs[1] = y1;
		goal_locs[0] =  gx1;
		goal_locs[1] = gy1;
		}
		if(k > 1){
			start_locs[2] = x2;
			start_locs[3]= y2;
			goal_locs[2] = gx2;
			goal_locs[3] = gy2;
		}
		if(k > 2){
			start_locs[4] = x3;
			start_locs[5] = y3;
			goal_locs[4] = gx3;
			goal_locs[5] = gy3;
		}
		System.out.println("Start: " + Arrays.toString(start_locs));
		System.out.println("goal:" + Arrays.toString(goal_locs));
		
				
		//LOOK HERE:change the new_maze variable here to choose the maze
		MultiRobot new_problem = new MultiRobot(new_maze4, start_locs, goal_locs, height, width, k);
		new_problem.startNode.robot_draw();
		System.out.println("");
		List<SearchProblem.UUSearchNode> path;
		
		path = new_problem.a_star_search();
		if(path != null){
			System.out.println("a* path length:  " + path.size() + " " + path);
			for(int i = path.size() - 1 ; i >= 0; i--){
				System.out.println("\n ----------- \n");
				System.out.println(path.get(i));
				path.get(i).robot_draw();
				System.out.println("\n ----------- \n \n \n");
				
			}
		}
		else{
			System.out.println("No solution found.");
		}
	}

}
