package assignment3;

import java.util.*;

public class Graph {
	
	private Set<Vertex> vertices;
	private HashMap<Vertex, Set<Edge>> edges;
	private int num_arms;
	
	//constructor
	public Graph(int num_samples, int number_arms){
		
		//set up the vertices array and the edge map
		vertices = new HashSet<Vertex>();
		edges = new HashMap<Vertex, Set<Edge>>();
		num_arms = number_arms;
	}
	
	//adds a vertex
	public void addVertex(Vertex v){
		//add the vertex
		vertices.add(v);
		Set<Edge> vert_set = new HashSet<Edge>();
		edges.put(v, vert_set);
		//add the edges for all the adjacent vertices
		for(Vertex adj : v.getAdjacent()){
			Edge temp = new Edge(v, adj, num_arms);
			edges.get(v).add(temp);
		}
	}
	
	//add an edge
	public void addEdge(Vertex v, Edge e){
		edges.get(v).add(e);
	}
	
	//get the vertex set
	public Set<Vertex> getVertices(){
		return vertices;
	}
	
}
