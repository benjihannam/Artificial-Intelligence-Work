package assignment3;

import java.util.*;

public class PRMSearch {
	
	private Graph graph;
	private int num_arms;
	
	//constructor
	public PRMSearch(Graph PRM_graph, int number_arms){
		graph = PRM_graph;
		num_arms = number_arms;
	}
	
	public ArrayList<Vertex> A_star_search(Vertex start, Vertex end){
		
		//set up the path arrayList
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		
		//if the goal is not in the PRM then we need to change that to the nearest
		Vertex end_vertex = end;
		if(!graph.getVertices().contains(end)){
			System.out.println("end not in PRM");
			Vertex new_end = null;
			double min = 1000;
			//get the closes vertex to the goal and set it
			for(Vertex u : graph.getVertices()){
				Edge temp = new Edge(end, u, num_arms);
				if(temp.getWeight() < min){
					new_end = u;
					min = temp.getWeight();
				}
			}
			end_vertex = new_end;
		}
		System.out.println("end:" + end_vertex);
		
		//keep track of the backpointing and put start node in
		HashMap<Vertex, Vertex> visited = new HashMap<Vertex, Vertex>();
		
		//set up the priority queue
		PriorityQueue<Vertex> frontier = new PriorityQueue<Vertex>(10000, new Comparator<Vertex>(){
			//compare function to place vertices in the right place in the queue
			public int compare(Vertex v1, Vertex v2){
				//initally add the depth
				double diff_one = 50*v1.getDepth();
				double diff_two = 50*v2.getDepth();
				//loop through the arms
				for(int i = 0; i < num_arms; i++){
					//add the difference in the angles for v1
					if(end.getAngles()[i] > v1.getAngles()[i]){
						double temp = (end.getAngles()[i] - v1.getAngles()[i]);
						if(temp > 180){
							temp = 360 - temp;
						}
						diff_one += temp;
					}
					else{
						double temp = (v1.getAngles()[i] - end.getAngles()[i]);
						if(temp > 180){
							temp = 360 - temp;
						}
						diff_one += (v1.getAngles()[i] - end.getAngles()[i]);
					}
					//add the difference in the angles for v2
					if(end.getAngles()[i] > v2.getAngles()[i]){
						double temp = (end.getAngles()[i] - v2.getAngles()[i]);
						if(temp > 180){
							temp = 360 - temp;
						}
						diff_two += temp;
					}
					else{
						double temp = (v2.getAngles()[i] - end.getAngles()[i]);
						if(temp > 180){
							temp = 360 - temp;
						}
						diff_two += temp;
					}
				}
				double pri_one = diff_one;
				double pri_two = diff_two;
		
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
		
		//if we got the start off random sampling
		if(graph.getVertices().contains(start)){
			frontier.add(start);
			visited.put(start, null);
			System.out.println("Start was in PRM");
		}
		//otherwise move from the start to the closest vertex
		else{
			System.out.println("Start was not in PRM");
			Vertex new_start = null;
			double min = 1000;
			for(Vertex u : graph.getVertices()){
				Edge temp = new Edge(start, u, num_arms);
				if(temp.getWeight() < min){
					new_start = u;
					min = temp.getWeight();
				}
			}
			System.out.println("New Start:" + new_start);
			 frontier.add(new_start);
			 visited.put(new_start, null);
		}
		
		
		//create a set to keep track of visited nodes
		Set<Vertex> explored = new HashSet<Vertex>();
		
		//go through each new layer
				while(!frontier.isEmpty()){
					Vertex current = frontier.poll();
					
					//if we haven't already explored the current node
					if(!explored.contains(current)){
						
						//add the current node to explored
						explored.add(current);
						//check if its the goal node
						if(current.equals(end_vertex)){
							path = backchain(current, visited);
							path.add(end);
							return path;
						}
						
						//for each child
						for(Vertex adjacent : current.getAdjacent()){
							//check if they are in explored
							if(!explored.contains(adjacent)){
								//get the edge between them
								adjacent.setDepth(current.getDepth()+1); 
								//add the child to the HashMap pointing back to the current node
								visited.put(adjacent, current);
								//add the child to the frontier
								frontier.add(adjacent);
							}
						}
					}
				}
		
		return path;
	}
	
	//back chain should only be used by bfs, not the recursive dfs
	private ArrayList<Vertex> backchain(Vertex node,
			HashMap<Vertex, Vertex> visited) {
		
		//list to hold the backwards path
		ArrayList<Vertex> answer = new ArrayList<Vertex>();
		
		//list to hold the actual path
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		
		//Set our first node to be the startNode
		Vertex current = node;
		
		//as long as we don't point to null
		while(visited.get(current) != null){
			//add the node to the list
			answer.add(current);
			//change current to the node it points to
			current = visited.get(current);
		}	
		for(int i = answer.size() - 1; i > -1; i-- ){
			path.add(answer.get(i));
		}
		return path;
	}
}
