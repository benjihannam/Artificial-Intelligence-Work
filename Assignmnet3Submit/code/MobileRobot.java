package assignment3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MobileRobot {
	public TreeNode goalNode;
	public TreeNode startNode;
	public double radius;
	public double v, omega;
	public Rectangle obstacles[];
	public GraphicsDriver graphics;
	public ArrayList<Line2D> lines;
	public ArrayList<Arc2D> arcs;
	public ArrayList<TreeNode> final_path;
	
	//constructor
	public MobileRobot(TreeNode start, TreeNode goal, double rad, double velocity, double angular_v, Rectangle obs[]){
		startNode = start;
		goalNode = goal;
		radius = rad;
		omega = angular_v;
		v = velocity;
		obstacles = obs;
		lines = new ArrayList<Line2D>();
		arcs = new ArrayList<Arc2D>();
	}
	
	//build the tree and get the path
	public ArrayList<TreeNode> buildTree(){
		
		//A set of leaf nodes
		Set<TreeNode> leaves = new HashSet<TreeNode>();
		leaves.add(startNode);
		//while we are not at the goal
		while(true){	
			
			//get the random point
			double rand_x = Math.random();
			double rand_y = Math.random();
			double nx = rand_x * 800 - 400;
			double ny = rand_y * 800 - 400;
			double min = 10000;
			
			//get the leaf node closest to the random poin
			TreeNode current_smallest = null;
			for(TreeNode leaf : leaves){
				double test_min = Math.abs(leaf.x - nx) + Math.abs(leaf.y - ny);
				if(test_min < min){
					current_smallest = leaf;
					min = test_min;
				}
			}
			//remove the leaf
			leaves.remove(current_smallest);
			
			//get the children
			ArrayList<TreeNode> children = current_smallest.getSuccessors(v, omega, obstacles, radius, lines, arcs);
			
			//check its children
			for(TreeNode child : children){
				//if they are not the goal test, add them as leave
				if(!child.goalTest(goalNode, radius)){
					leaves.add(child);
				}
				//otherwise get the path
				else{
					final_path = getPath(child);
					return final_path;
				}
			}
		}
	}
	
	//gets the path
	public ArrayList<TreeNode> getPath(TreeNode goal){
		ArrayList<TreeNode> answer = new ArrayList<TreeNode>();
		TreeNode current = goal;
		while(current.parent != null){
			answer.add(current);
			current = current.parent;
		}
		return answer;
	}
	//create the graphics
	//sets up this graphics
	public void setGraphics(){
		graphics = new GraphicsDriver();
	}
	
	//the graphics class
	public class GraphicsDriver extends JPanel{
			
		//constructor
		GraphicsDriver(){
		}
			
		public void paintComponent(Graphics g) {
		  g.translate(400, 400);  
		  super.paintComponent(g);
		  g.setColor(Color.black);
		  drawObstacles(g);
		  drawLine(g);
		  drawPath(g);
		  drawStartandGoal(g);
		  
		}
		
		//draw in the obstacles
		public void drawObstacles(Graphics g){
			for(Rectangle obs : obstacles){
				g.fillRect(obs.x, obs.y, obs.width, obs.height);
			}
		}
		
		//draw the arcs
		public void drawLine(Graphics g){
			g.setColor(Color.blue);
			for(Arc2D arc : arcs){
				g.drawArc((int)arc.getX(), (int)arc.getY(), (int)arc.getWidth(), (int)arc.getWidth(), (int)arc.getAngleStart(), (int)arc.getAngleExtent());
				
			}
			for(Line2D line : lines){
				g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
			}
		}
		
		//draw the start and end
		public void drawStartandGoal(Graphics g){
			g.setColor(Color.green);
			g.fillOval((int)(startNode.x - radius), (int)(startNode.y - radius), (int)(2*radius), (int)(2*radius));
			g.setColor(Color.red);
			g.fillOval((int)(goalNode.x - radius), (int)(goalNode.y - radius), (int)(2*radius), (int)(2*radius));
		}
		
		//draws the path
		public void drawPath(Graphics g){
			g.setColor(Color.orange);
			for(int i = 0; i < final_path.size(); i++){
				g.fillOval((int)(final_path.get(i).x - radius), (int)(final_path.get(i).y - radius), (int)radius*2, (int)radius*2);
			}
		}
		
	}

	public static void main(String[] args) {
		
		//set up the goal nodes
		TreeNode start = new TreeNode(null, -350, 350, 90);
		TreeNode goal = new TreeNode(null, 300, -270, 0);
		TreeNode goal2 = new TreeNode(null, -310, 350, 0);
		
		//set up the variable
		double radius = 10;
		double velocity = 30;
		double omega = 90;
		
		//set up the obstacles
		Rectangle obstacles[] = new Rectangle[6];
		Rectangle left_wall = new Rectangle(-400, -400,10, 800);
		Rectangle right_wall = new Rectangle(390, -400,10, 800);
		Rectangle top_wall = new Rectangle(-400, -400,800, 10);
		Rectangle bottom_wall = new Rectangle(-400, 390,800, 10);
		Rectangle new_obs = new Rectangle(-200, -100, 600, 50);
		Rectangle new_obs2 = new Rectangle(-400, 200, 600, 50);
		//the walls on the edge of the maze
		obstacles[0] = left_wall;
		obstacles[1] = right_wall;
		obstacles[2] = top_wall;
		obstacles[3] = bottom_wall;
		
		//new custom obstacles
		obstacles[4] = new_obs;
		obstacles[5] = new_obs2;
		
		//create the problem and solve
		MobileRobot problem = new MobileRobot(start, goal, radius, velocity, omega, obstacles);
		ArrayList<TreeNode> path = problem.buildTree();
		System.out.println(path);
		problem.setGraphics();
		
		//set up and run the graphics
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	           public void run() {
	        	 //Create and set up the window.
	   	        JFrame frame = new JFrame("Driver");
	   	        frame.setSize(800, 820);
	   	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   	        frame.setLocationRelativeTo(null);
	   	        frame.add(problem.graphics);
	   	        //Display the window.
	   	        frame.setVisible(true);
	           }
	       });
		
	}
		
}
