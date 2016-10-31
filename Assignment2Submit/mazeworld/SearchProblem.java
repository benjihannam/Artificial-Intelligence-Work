
// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (BENJI HANNAM),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).
// OTHER SOURCES: CS76 Week 1 Lectures: http://www.cs.dartmouth.edu/~devin/cs76/slides-week-01/#/20

package mazeworld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


public abstract class SearchProblem {
	
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
		public int getPriority();
		public void robot_draw();
		public void getDirection();
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

	
	public List<UUSearchNode> a_star_search() {
		resetStats();
		//set up the answer path
		List<UUSearchNode> path = new ArrayList<UUSearchNode>();
		
		//set up the priority queue
		PriorityQueue<UUSearchNode> frontier = new PriorityQueue<UUSearchNode>(10000, new Comparator<UUSearchNode>(){
			public int compare(UUSearchNode node1, UUSearchNode node2){
				int pri_one = node1.getPriority();
				int pri_two = node2.getPriority();
		
				if(pri_one < pri_two){
					return -1;
				}
				else if(pri_one > pri_two){
					return 1;
				}
				else{
					return 0;
				}
			}
		});
		frontier.add(startNode);
		
		//keep track of the backpointing and put start node in
		HashMap<UUSearchNode, UUSearchNode> visited = new HashMap<UUSearchNode, UUSearchNode>();
		visited.put(startNode, null);
		
		
		//create a set to keep track of visited nodes
		Set<UUSearchNode> explored = new HashSet<UUSearchNode>();
		//explored.add(startNode);
		incrementNodeCount();
		
		//go through each new layer
		while(!frontier.isEmpty()){
			UUSearchNode current = frontier.poll();
			//if we haven't already explored the current node
			if(!explored.contains(current)){
				//add the current node to explored
				explored.add(current);
				//check if its the goal node
				if(current.goalTest()){
					path = backchain(current, visited);
					
					// update stats
					updateMemory(visited.size() + frontier.size());
					incrementNodeCount();
					return path;
				}
				
				//for each child
				for(UUSearchNode child : current.getSuccessors()){
					//check if they are in explored
					if(!explored.contains(child)){
						//add the child to the HashMap pointing back to the current node
						visited.put(child, current);
						//add the child to the frontier
						frontier.add(child);
						//increment the node count
						incrementNodeCount();
					}
				}
			}
		}
		return path;
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


