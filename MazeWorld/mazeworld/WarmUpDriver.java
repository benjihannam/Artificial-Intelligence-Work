package mazeworld;

import java.util.List;

public class WarmUpDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//maze_parameters
		int height = 10;
		int width = 10;
		int goal_x = 0;
		int goal_y = height -1;
		
		
		//################################################################################################################
		//MAZE0: An empty maze
		int new_maze0[][] = new int[width][height];
		
		//MAZE1: A maze with walls at all x = y positions except (0,0); Start is top right, Goal is bottom left;
		int new_maze1[][] = new int[width][height];
		for(int x = 2; x < width; x++){
			for(int y = 2; y < height; y++){
				new_maze1[x][y] = -1;
			}
		}
		
		//MAZE2: A maze with a wall down a column, opening in the middle, Start is top right, Goal is bottom left;
		int new_maze2[][] = new int[width][height];
		int column = width/2;
		for(int y = 0; y < height; y++){
			if(y != height/2){
				new_maze2[column][y] = -1;
			}
		}
		
		//MAZE3: A maze forcing it to take a non-manhattan path, Start is top right, Goal is bottom left;
		int new_maze3[][] = new int[width][height];
		//wall in row right above the goal with an opening at opposite end
		for(int x = 0; x < width - 1; x++ ){
			new_maze3[x][height - 2] = -1;
		}
		//wall in row right below the start with an opening at opposite end
		for(int x = 1; x < width; x++ ){
			new_maze3[x][1] = -1;
		}
		//################################################################################################################		
		
		//LOOK HERE: change the new_maze variable here to choose the maze
		MazeworldWarmUp mwProb = new MazeworldWarmUp(new_maze3, height, width, goal_x, goal_y, width - 1 , 0);
		List<SearchProblem.UUSearchNode> path;
		
		
		path = mwProb.breadthFirstSearch();
		if(path != null){
			System.out.println("bfs path length:  " + path.size() + " " + path);
			mwProb.startNode.robot_draw();
			for(int i = path.size() - 1 ; i >= 0; i--){
				System.out.println(path.get(i));
				path.get(i).robot_draw();
				System.out.println("\n \n \n \n \n \n");
			}
		}
		else{
			System.out.println("No solution found.");
		}
		mwProb.printStats();
		System.out.println("--------");	
	}
}