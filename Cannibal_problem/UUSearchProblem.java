
// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (BENJI HANNAM),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).
// OTHER SOURCES: CS76 Week 1 Lectures: http://www.cs.dartmouth.edu/~devin/cs76/slides-week-01/#/20

package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


public abstract class UUSearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<UUSearchNode> breadthFirstSearch(){
		resetStats();
		
		//create the queue and add the start node
		Queue<UUSearchNode> frontier = new LinkedList<UUSearchNode>();
		frontier.add(startNode);
		
		//keep track of the backpointing and put start node in
		HashMap<UUSearchNode, UUSearchNode> visited = new HashMap<UUSearchNode, UUSearchNode>();
		visited.put(startNode, null);
		
		//List containing answer path
		List<UUSearchNode> answer = new ArrayList<UUSearchNode>();
		
		//create a set to keep track of visited nodes
		Set<UUSearchNode> explored = new HashSet<UUSearchNode>();
		explored.add(startNode);
		incrementNodeCount();
		//go through each new layer
		while(!frontier.isEmpty()){
			UUSearchNode current = frontier.poll();
			
			//check if its the goal node
			if(current.goalTest()){
				answer = backchain(current, visited);
				
				// update stats
				updateMemory(visited.size() + frontier.size());
				incrementNodeCount();
				return answer;
			}
			
			//for each child
			for(UUSearchNode child : current.getSuccessors()){
				//check if they are in explored
				if(!explored.contains(child)){
					//if they aren't add them to explored
					explored.add(child);
					//add the child to the HashMap pointing back to the current node
					visited.put(child, current);
					//add the child to the frontier
					frontier.add(child);
					//increment the node count
					incrementNodeCount();
				}
			}
		}	
		return answer;
	}
	
	//back chain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {
		
		//list to return
		List<UUSearchNode> answer = new ArrayList<UUSearchNode>();
		
		//Set our first node to be the startNode
		UUSearchNode current = node;
		
		//as long as we don't point to null
		while(visited.get(current) != null){
			//add the node to the list
			answer.add(current);
			//change current to the node it points to
			current = visited.get(current);
		}	
		
		return answer;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats(); 
		
		//create the visited HashMap
		HashMap<UUSearchNode, Integer> visited = new HashMap<UUSearchNode, Integer>();
		
		//add the startNode to visited
		visited.put(this.startNode, 0);
		incrementNodeCount();
		//list to hold the answer
		return dfsrm(this.startNode, visited, 0, maxDepth);	
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {

		//check that we have not exceeded maxDepth (one form of a base case)
		if(depth >= maxDepth){
			return null;
		}
		
		//loop over all the children of the current node
		for(UUSearchNode child : currentNode.getSuccessors()){
			
			//if it has not been visited
			if(!visited.containsKey(child)){
				
				//check if we have a base case (we reached the goal)
				if(child.goalTest()){
					//put it in the path and return the path;
					List<UUSearchNode> path = new ArrayList<UUSearchNode>();
					path.add(child);
					updateMemory(visited.size());
					return path;
				}
				
				//otherwise go to the next depth
				else{
					// add it to visited
					visited.put(child, depth);
					incrementNodeCount();
					//get the result of the recursive case (the child node, with depth + 1)
					List<UUSearchNode> path = dfsrm(child, visited, depth + 1, maxDepth);
					// if the path is not null then add child to the path and return it
					if(path != null){
						path.add(child);
						updateMemory(visited.size());
						return path;
					}
					//otherwise the child is not on the path so move onto the next one													
				}
			}
		}
		//if we have no children or none on the path then return null
		return null;
	}
	
	
	// set up the iterative deepening search, and make use of dfsrpc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		
		// set up the answer list
		List<UUSearchNode> answer;
		
		//go through each depth
		for(int i = 0; i < maxDepth; i++){
			//get the result for the depth
			answer = depthFirstPathCheckingSearch(i);
			//if we found a path return it otherwise go to the next depth
			if(answer != null){
				return answer;
			}
		}
		//once we have exhausted up to maxDepth
		return null;	
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		
		currentPath.add(startNode);
		incrementNodeCount();

		return dfsrpc(startNode, currentPath, 0, maxDepth);

	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {
		
		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		
		//check that we have not exceeded maxDepth (one form of a base case)
		if(depth >= maxDepth){
			return null;
		}
		
		//loop over all the children of the current node
		for(UUSearchNode child : currentNode.getSuccessors()){
			
			//if it is not in our currentPath 
			if(!currentPath.contains(child)){
				
				//check if we have a base case (we reached the goal)
				if(child.goalTest()){
					//put it in the path and return the path;
					List<UUSearchNode> path = new ArrayList<UUSearchNode>();
					path.add(child);
					return path;
				}
				
				//otherwise go to the next depth
				else{
					// add it to visited
					currentPath.add(child);
					incrementNodeCount();
//					System.out.println("Adding a child to path, CurrentPath is: " + currentPath);
					//get the result of the recursive case (the child node, with depth + 1)
					List<UUSearchNode> path = dfsrpc(child, currentPath, depth + 1, maxDepth);
					// if the path is not null then add child to the path and return it
					if(path != null){
						path.add(child);
//						System.out.println("Solution Path is: " + path + "at depth: " + depth);
						return path;
					}
					//remove the child from the currentPath and move onto the next one
					else{
						currentPath.remove(child);
//						System.out.println("Removing a child from path, CurrentPath is: " + currentPath);
					}
				}
			}
		}
		//if we have no children or no valid did not hit the base case then return null
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
