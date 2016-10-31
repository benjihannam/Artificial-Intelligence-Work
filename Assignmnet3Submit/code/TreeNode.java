package assignment3;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.*;

public class TreeNode {

	public TreeNode parent;
	private ArrayList<TreeNode> children;
	public double x, y, theta;
	
	//Constructor
	public TreeNode(TreeNode par, double nx, double ny, double th){
		x = nx;
		y = ny;
		theta = th;
		parent = par;
	}
	
	// add a child to the children list
	public void addChild(TreeNode child){
		children.add(child);
	}
	
	//is the node the goal
	public boolean goalTest(TreeNode goalNode, double radius){
		//get the distance between the x,y values
		double diff_x = Math.abs(this.x - goalNode.x);
		double diff_y = Math.abs(this.y - goalNode.y);
		//see if they are both within the radius
		return(diff_x <= radius && diff_y <= radius);
	}
	
	//the equals function
	public boolean equals(TreeNode other){
		return(this.x == other.x && this.y == other.y && this.theta == other.theta);
	}
	
	public ArrayList<TreeNode> getSuccessors(double v, double omega, Rectangle obstacles[], double radius, ArrayList<Line2D> lines, ArrayList<Arc2D> arcs){
		ArrayList<TreeNode> children = new ArrayList<TreeNode>();
		double new_x, new_y;
		//get the forward move
		new_x = this.x + v * Math.cos(Math.toRadians(this.theta));
		new_y = this.y + v * Math.sin(Math.toRadians(this.theta));
		//create the new node and check
		TreeNode forward = new TreeNode(this, new_x, new_y, this.theta);
		if(straightLineCheck(forward, obstacles, radius, v, omega, lines)){
			// now add it to the array
			children.add(forward);
		}
		
		//get the backward move
		new_x = this.x - v * Math.cos(Math.toRadians(this.theta));
		new_y = this.y - v * Math.sin(Math.toRadians(this.theta));
		//create the new node and check
		TreeNode backward = new TreeNode(this, new_x, new_y, this.theta);
		if(straightLineCheck(backward, obstacles, radius, v, omega, lines)){
			// now add it to the array
			children.add(backward);
		}
		
		//variables to hold rotation center
		double rot_x, rot_y;
		
		//get the left turn rotation center
		rot_x = this.x  - (v / Math.toRadians(omega)) * Math.sin(Math.toRadians(theta));
		rot_y = this.y  + (v / Math.toRadians(omega)) * Math.cos(Math.toRadians(theta));
		//get the forward left turn
		//rotate around the origin by +omega
		new_x = (this.x - rot_x) *Math.cos(Math.toRadians(omega)) - (this.y - rot_y) * Math.sin(Math.toRadians(omega));
		new_y = (this.x - rot_x) *Math.sin(Math.toRadians(omega)) + (this.y - rot_y) * Math.cos(Math.toRadians(omega));
		//translate back
		new_x += rot_x;
		new_y += rot_y;
		//create the new node and check
		TreeNode forw_left = new TreeNode(this, new_x, new_y, (theta + omega));
		if(checkArc(forw_left, 0, obstacles, radius, v, omega, rot_x, rot_y, arcs)){
			// now add it to the array
			children.add(forw_left);
		}
		
		// get the backward left turn (uses the same rot_x, rot_y as forward)
		//rotate around the origin by -omega
		new_x = (this.x - rot_x) *Math.cos(Math.toRadians(-omega)) - (this.y - rot_y) * Math.sin(Math.toRadians(-omega));
		new_y = (this.x - rot_x) *Math.sin(Math.toRadians(-omega)) + (this.y - rot_y) * Math.cos(Math.toRadians(-omega));
		//translate back
		new_x += rot_x;
		new_y += rot_y;
		//create the new node and check
		TreeNode back_left = new TreeNode(this, new_x, new_y, (theta - omega));
		if(checkArc(back_left, 1, obstacles, radius, v, -omega, rot_x, rot_y, arcs)){
			// now add it to the array
			children.add(back_left);
		}
		
		//get the right turn rotation center
		rot_x = this.x  - (v / Math.toRadians(-omega)) * Math.sin(Math.toRadians(theta));
		rot_y = this.y  + (v / Math.toRadians(-omega)) * Math.cos(Math.toRadians(theta));
		
		// get the forward right turn
		//rotate around the origin by omega
		new_x = (this.x - rot_x) *Math.cos(Math.toRadians(-omega)) - (this.y - rot_y) * Math.sin(Math.toRadians(-omega));
		new_y = (this.x - rot_x) *Math.sin(Math.toRadians(-omega)) + (this.y - rot_y) * Math.cos(Math.toRadians(-omega));
		//translate back
		new_x += rot_x;
		new_y += rot_y;
		//create the new node and check
		TreeNode forw_right = new TreeNode(this, new_x, new_y, (theta - omega));
		if(checkArc(forw_right, 2, obstacles, radius, v, -omega, rot_x, rot_y, arcs)){
			// now add it to the array
			children.add(forw_right);
		}
		
		// get the backward right turn
		new_x = (this.x - rot_x) *Math.cos(Math.toRadians(omega)) - (this.y - rot_y) * Math.sin(Math.toRadians(omega));
		new_y = (this.x - rot_x) *Math.sin(Math.toRadians(omega)) + (this.y - rot_y) * Math.cos(Math.toRadians(omega));
		//translate back
		new_x += rot_x;
		new_y += rot_y;
		//create the new node and check
		TreeNode back_right = new TreeNode(this, new_x, new_y, (theta + omega));
		if(checkArc(back_right, 3, obstacles, radius, v, omega, rot_x, rot_y, arcs)){
			// now add it to the array
			children.add(back_right);
		}	
		return children;
	}
	
	//checks if it is safe to move to a node
	public boolean straightLineCheck(TreeNode other, Rectangle obs[], double radius, double v, double omega, ArrayList<Line2D> lines){
		
		//are we in bound
		if(other.x < -400 + radius || other.x > 400 - radius || other.y < -400 + radius || other.y > 400 - radius ){
			return false;
		}
		//shift the points positive radius
		double pos_x1 = this.x + radius * Math.cos(Math.toRadians(this.theta));
		double pos_y1 = this.y + radius * Math.sin(Math.toRadians(this.theta));
		double pos_x2 = other.x + radius * Math.cos(Math.toRadians(this.theta));
		double pos_y2 = other.y + radius * Math.sin(Math.toRadians(this.theta));
		
		//shift the points negative radius
		double neg_x1 = this.x - radius * Math.cos(Math.toRadians(this.theta));
		double neg_y1 = this.y - radius * Math.sin(Math.toRadians(this.theta));
		double neg_x2 = other.x - radius * Math.cos(Math.toRadians(this.theta));
		double neg_y2 = other.y - radius * Math.sin(Math.toRadians(this.theta));
		
		//create a line
		Line2D line = new Line2D.Double(this.x, this.y, other.x, other.y);
		Line2D neg_line = new Line2D.Double(neg_x1, neg_y1, neg_x2, neg_y2);
		Line2D pos_line = new Line2D.Double(pos_x1, pos_y1, pos_x2, pos_y2);
		
		//create a circle around the end points
		Ellipse2D end_circle = new Ellipse2D.Double(other.x - radius, other.y - radius, radius*2, radius*2);
		
		for(Rectangle ob : obs){
			//check if it intersects the lines
			if(line.intersects(ob) || neg_line.intersects(ob) || pos_line.intersects(ob)){
				return false;
			}
			//the end point circles
			else if(end_circle.intersects(ob)){
				return false;
			}
		}
		lines.add(line);
		return true;
	}
	
	//check a right arc
	public boolean checkArc(TreeNode other, int type, Rectangle obs[], double radius, double v, double omega, double rot_x, double rot_y, ArrayList<Arc2D> arcs){
		
		//are we in bounds
		if(other.x < -400 + radius || other.x > 400 - radius || other.y < -400 + radius || other.y > 400 - radius ){
			return false;
		}
		
		//double start_angle = Math.toDegrees(Math.atan(diff_y/diff_x));
		double start_angle;
		if(type == 0){
			 start_angle = 90 - theta;
		}
		else if(type == 1){
			 start_angle = 360 - (90 - theta);
		}
		else if(type == 2){
			 start_angle = 90 + theta;
		}
		else{
			start_angle = 360 - (90 + theta);
		}
		
		
		//get the half the side length of the rectangle
		double length = v / Math.toRadians(omega);
		double top_x = rot_x - length;
		double top_y = rot_y - length;
		
		//shift the points positive radius
		double pos_x1 = top_x + radius * Math.cos(Math.toRadians(this.theta));
		double pos_y1 = top_y + radius * Math.sin(Math.toRadians(this.theta));
		
		//shift the points negative radius
		double neg_x1 = top_x - radius * Math.cos(Math.toRadians(this.theta));
		double neg_y1 = top_y - radius * Math.sin(Math.toRadians(this.theta));

		//the arcs to check
		Arc2D new_arc1 = new Arc2D.Double(top_x, top_y, length*2, length*2, start_angle, omega, Arc2D.OPEN);
		Arc2D up_arc = new Arc2D.Double(pos_x1, pos_y1, length*2, length*2, start_angle, omega, Arc2D.OPEN);
		Arc2D down_arc = new Arc2D.Double(neg_x1, neg_y1, length*2, length*2, start_angle, omega, Arc2D.OPEN);
		
		//create a circle around the end points
		Ellipse2D end_circle = new Ellipse2D.Double(other.x - radius, other.y - radius, radius*2, radius*2);

		for(Rectangle ob : obs){
			if(new_arc1.intersects(ob) || up_arc.intersects(ob) || down_arc.intersects(ob) || end_circle.intersects(ob)){
				return false;
			}

			if(ob.contains(other.x, other.y)){
				return false;
			}
		}
		arcs.add(new_arc1);
		//arcs.add(new_arc2);
		return true;
	}
	
	public String toString() {
        String answer = "[" + (int)x + "," + (int)y + ", " + ((int)theta %360) + "]";
        return answer;
    }
	
	//hash function
	public int hashCode(){
		return (int)(this.x * 20 + this.y *40);
	}
}
