package assignment1;

import java.util.List;

public class CannibalDriver {
	public static void main(String args[]) {
	
		final int MAXDEPTH = 5000;

		// interesting starting state:  
		//  8, 5, 1  (IDS slow, but uses least memory.)


		// set up the "standard" 331 problem:
		CannibalProblem mcProblem = new CannibalProblem(3, 3, 1, 0, 0, 0);
	
		List<UUSearchProblem.UUSearchNode> path;
		
		
		path = mcProblem.breadthFirstSearch();
		if(path != null){
			System.out.println("bfs path length:  " + path.size() + " " + path);
		}
		else{
			System.out.println("No solution found.");
		}
		mcProblem.printStats();
		System.out.println("--------");
		
	
		path = mcProblem.depthFirstMemoizingSearch(MAXDEPTH);
		if(path != null){
		System.out.println("dfs memoizing path length:" + path.size() + path);
		}
		else{
			System.out.println("No solution found.");
		}
		mcProblem.printStats();
		System.out.println("--------");
		
		path = mcProblem.depthFirstPathCheckingSearch(MAXDEPTH);
		if(path != null){
		System.out.println("dfs path checking path length:" + path.size() + path);
		}
		else{
			System.out.println("No solution found.");
		}
		mcProblem.printStats();
		
		System.out.println("--------");
		path = mcProblem.IDSearch(MAXDEPTH);
		if(path != null){
		System.out.println("Iterative deepening (path checking) path length:" + path.size() + path);
		}
		else{
			System.out.println("No solution found.");
		}
		mcProblem.printStats();
		
	}
}
