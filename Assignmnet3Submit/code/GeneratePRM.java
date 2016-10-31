package assignment3;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;


public class GeneratePRM extends JPanel{
	
	//variables
	private int num_samples;
	public int num_arms;
	private int arm_l;
	private int k;
	private Rectangle obstacles[];
	private Graph graph;
	public GraphicsDriver graphics;
	
	GeneratePRM(int number_arms, int arm_length, Rectangle obs[], int sample_count, int num_neighbors){
		//initiate variables
		num_arms = number_arms;
		num_samples = sample_count;
		arm_l = arm_length;
		obstacles = obs;
		k = num_neighbors;
		graph = new Graph(num_samples, num_arms);
	}
	
	//sets up this graphics
	public void setGraphics(ArrayList<Vertex> path){
		graphics = new GraphicsDriver(path);
	}
	
	//Generates the random points
	public void GeneratePoints(){
		
		//for each sample
		for(int i = 0; i < num_samples; i++){
			//get random angles
			double sample_spot[] = new double[num_arms];
			for(int j = 0; j < num_arms; j++){
				sample_spot[j] = Math.random() * 360;
			}
			//create a new vertex
			Vertex new_vertex = new Vertex(sample_spot, arm_l);
			//if it is a safe point
			if(testVertex(new_vertex)){
				//add it to the graph
				graph.addVertex(new_vertex);
			}
		}
	}
	
	//check if a vertex is valid
	public boolean testVertex(Vertex v){
		
		//get each arm
		for(int i = 0; i < num_arms; i++){
			
			double x1,x2,y1,y2;
			if(i == 0){
				//if its the first arm start from the center
				x1 = 0;
				y1 = 0;
				x2 = v.getX(0);
				y2 = v.getY(0);				
			}
			else{
				//otherwise get the segment from each
				x1 = v.getX(i - 1);
				y1 = v.getY(i - 1);
				x2 = v.getX(i);
				y2 = v.getY(i);	
			}

			//loop through all the obstacles and see if they contain the x,y point
			for(int o = 0; o < obstacles.length; o++){
				if(obstacles[o].intersectsLine(x1, y1, x2, y2)){
					return false;
				}
			}
		}
		return true;
	}
	
	//A function to generate the k nearest neighbors for each vertex in the graph
	// and insert the edges into the graph
	public void getNeighbors(){
		//loop through each vertex
		for(Vertex v : graph.getVertices()){
			//create a set of ones already seen
			ArrayList<Edge> neighbors = new ArrayList<Edge>();
			for(Vertex u : graph.getVertices()){
				//if not the same vertex
				if(!u.equals(v)){
					 
					//create an edge
					Edge e = new Edge(v, u, num_arms);
					
					//if we have less than k members automatically add it
					if(neighbors.size() < k){
						neighbors.add(e);				
					}
					//otherwise compare it to the largest one
					else{
						if(e.getWeight() < neighbors.get(k - 1).getWeight()){
							neighbors.set(k - 1, e);
						}
						
					}
					//sort the list
					neighbors.sort(new Comparator<Edge>(){
						public int compare(Edge e1, Edge e2){
							return e1.compareTo(e2);
						}
					});
				}
			}
			//now add all the edges to the graph
			for(int i = 0; i < neighbors.size(); i++){
				//check that we can move from the vertex to the neighbor
				if(moveCheck(v, neighbors.get(i).getAdjacent(v))){
					graph.addEdge(v, neighbors.get(i));
					Vertex adjacent = neighbors.get(i).getAdjacent(v);
					v.addNeighbor(adjacent);
				}
			}
		}
	}
	
	//a function to get the graph
	public Graph getGraph(){
		return graph;
	}
	
	// A function to check if you can move safely between two vertices
	public boolean moveCheck(Vertex v, Vertex u){
		
		//check ten spots between the two vertices
		for(int i = 0; i < 10; i++){
			//new vertex array
			double new_vert_array[] = new double[num_arms];
			for(int j = 0; j < num_arms; j ++){
				double angle_diff = (u.getAngles()[j] - v.getAngles()[j])/10.0;;
				new_vert_array[j] = v.getAngles()[j] + i*angle_diff;
			}
			Vertex new_vert = new Vertex(new_vert_array, arm_l);
			if(!testVertex(new_vert)){
				return false;
			}
		}
		return true;
	}
	
	//the graphics class
	public class GraphicsDriver extends JPanel{
		
		private ArrayList<Vertex> path;
		
		//constructor
		GraphicsDriver(ArrayList<Vertex> answer){
			path = answer;
		}
		
		public void paintComponent(Graphics g) {
		  g.translate(400, 400);  
		  super.paintComponent(g);
		  g.setColor(Color.black);
		  drawObstacles(g);
		  g.fillOval(-5, -5, 10, 10);
		  for(int i = 0; i < path.size(); i++){
			  g.setColor(Color.red);
			  if(i < 3){
				  g.setColor(Color.green);
			  }
			  if (i > path.size()-4){
				  g.setColor(Color.black);
			  }
			  paintArm(g, path.get(i));
			  g.setColor(Color.black);
			  drawObstacles(g);
			  g.fillOval(-5, -5, 10, 10);
		  }
		}
		
		//draw in the obstacles
		public void drawObstacles(Graphics g){
			for(Rectangle obs : obstacles){
				g.fillRect(obs.x, obs.y, obs.width, obs.height);
			}
		}
		
		//draws an arm
		public void paintArm(Graphics g, Vertex v){
			for(int i = 0; i < num_arms; i++){
				double x1,x2,y1,y2;
				if(i == 0){
					//if its the first arm start from the center
					x1 = 0;
					y1 = 0;
					x2 = v.getX(0);
					
					y2 = v.getY(0);
				}
				else{
					//otherwise get the segment from each
					x1 = v.getX(i - 1);
					y1 = v.getY(i - 1);
					x2 = v.getX(i);
					y2 = v.getY(i);
				}
				g.drawLine((int) x1, (int)y1,(int) x2, (int)y2);
			}
		}		
	}
	
	
	public static void main(String[] args) {

		int arm_length = 50;
		int num_arms = 4;
		
		//set up the obstacles
		Rectangle test1 = new Rectangle(30, 30, 10, 10);
		Rectangle test2 = new Rectangle(0, -90, 10, 10);
		Rectangle obstacles[] = new Rectangle[2];
		obstacles[0] = test1;
		obstacles[1] = test2;
		
		//Generate the PRM
		GeneratePRM PRM = new GeneratePRM(num_arms, arm_length, obstacles, 100000, 5);
		PRM.GeneratePoints();
		PRM.getNeighbors();
		
		//Set up the Search
		PRMSearch search = new PRMSearch(PRM.getGraph(), PRM.num_arms);
		//Set up the start
		double start_array[] = new double[num_arms];
		start_array[0] = 270;
		start_array[1] = 0;
		start_array[2] = 0;
		start_array[3] = 0;
		Vertex start = new Vertex(start_array, arm_length);
		start.setDepth(0);
		//Set up the goal
		double end_array[] = new double[num_arms];
		end_array[0] = 95;
		end_array[1] = 270;
		end_array[2] = 0;
		end_array[3] = 180;
		Vertex end = new Vertex(end_array, arm_length);
		end.setDepth(0);
		
		//Get the path and output it in the graphics
		ArrayList<Vertex> path = search.A_start_search(start, end);
		System.out.println(path);
		PRM.setGraphics(path);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
           public void run() {
        	 //Create and set up the window.
   	        JFrame frame = new JFrame("Driver");
   	        frame.setSize(800, 800);
   	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   	        frame.setLocationRelativeTo(null);
   	        frame.add( PRM.graphics);
   	 
   	        //Display the window.
   	        frame.setVisible(true);
           }
       });
	}
}
