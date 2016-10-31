package mazeworld;

import java.util.List;

public class BlindDriver {

	public static void main(String[] args) {
		
		//Set up the maze
		int width = 10;
		int height = 10;
		int goal_x = width -1;
		int goal_y = 0;
		
		
		//################################################################################################################	
		//MAZE0: An empty maze
		int new_maze0[][] = new int[width][height];
		
		int new_maze3[][] = new int[width][height];
		for(int x = 1; x < width; x++){
			new_maze3[x][height - 2] = -1;
			new_maze3[x][1] = -1;
		}
		
		//create a multi robot maze
		//MAZE1: A maze with walls at all x = y positions except (0,0); Start is top right, Goal is bottom left;
		int new_maze1[][] = new int[width][height];
		for(int x = 1; x < width; x++){
				new_maze1[x][x] = -1;
		}
		
		//MAZE2: A maze with a wall down a column, opening in the middle, Start is top right, Goal is bottom left;
		int new_maze2[][] = new int[width][height];
		int column = width/2;
		for(int y = 0; y < height; y++){
			if(y != height/2){
				new_maze2[column][y] = -1;
			}
		}
		//################################################################################################################	
		
		
		//LOOK HERE:change the new_maze variable here to choose the maze
		BlindRobot br_problem = new BlindRobot(new_maze2, height, width, goal_x, goal_y);
				
		List<SearchProblem.UUSearchNode> path;
		path = br_problem.a_star_search();
		if(path != null){
			System.out.println("a* path length:  " + path.size() + " " + path);
			for(int i = path.size() - 1 ; i >= 0; i--){
				System.out.println("\n ----------- \n");
				path.get(i).getDirection();
				System.out.println(path.get(i));
				path.get(i).getDirection();
				path.get(i).robot_draw();
				System.out.println("\n ----------- \n");
				
			}
		}
		else{
			System.out.println("No solution found.");
		}
		
	}

}
