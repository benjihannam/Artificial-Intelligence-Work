package assignment3;

import java.util.*;

public class Vertex {
	
	// array that holds the angle values for the vertex
	private double angle_list[];
	private Set<Vertex> adjacent;
	private int arm_l;
	double depth;
	
	//constructor
	public Vertex(double angles[], int arm_length){
		angle_list = angles;
		adjacent = new HashSet<Vertex>();
		arm_l = arm_length;
	}
	
	//sets the depth
	public void setDepth(double input){
		depth = input;
	}
	
	//get the depth
	public double getDepth(){
		return depth;
	}
	
	//adds a neighbor to the set
	public void addNeighbor(Vertex v){
		if(!v.equals(this)){
			adjacent.add(v);
		}
	}
	
	//get the adjacent set
	public Set<Vertex> getAdjacent(){
		return adjacent;
	}
	
	//get the x location
	public double getX(int arm_number){
		//set to 0
		double x = arm_l * Math.cos(Math.toRadians(angle_list[0]));
		double angle = angle_list[0];
		//loop through the angles and compute the new x location
		for(int i = 1; i < arm_number+ 1; i++){
			 angle += angle_list[i];
			x += arm_l * Math.cos(Math.toRadians(angle));
		}
		return x;
	}
	
	//get the Y location
	public double getY(int arm_number){
		//set to 0
		double y = arm_l * -Math.sin(Math.toRadians(angle_list[0]));
		double angle = angle_list[0];
		//loop through the angles and get y
		for(int i = 1; i < arm_number + 1 ; i++){
			angle += angle_list[i];
			y += arm_l * -Math.sin(Math.toRadians(angle));
		}
		return y;
	}
	
	//get the list of angles
	public double[] getAngles(){
		return angle_list;
	}
	
	//equals function
	public boolean equals(Vertex other){
		for(int i = 0; i < angle_list.length; i++){
			if(angle_list[i] != other.getAngles()[i]){
				return false;
			}
		}
		return true;
	}
	
	//hash function
	public int hashCode(){
		int total = 0;
		//loop through the angles and compute the new x location
		for(int i = 0; i < angle_list.length ; i++){
			 total += angle_list[i];
			 total = total * 10;
		}
		return total;
	}
	
	public String toString() {
        String answer = "[";
        for(int i = 0; i < angle_list.length; i++){
        	answer += angle_list[i] + ", ";
        }
        answer += "]";
        return answer;
    }
}
