package assignment3;

public class Edge {
	private Vertex one;
	private Vertex two;
	private double weight;
	
	//constructor
	public Edge(Vertex first, Vertex second, int num_arms){
		one = first;
		two = second;
		
		//get the cumultive difference between the angles
		double diff = 0;
		//loop through the arms
		for(int i = 0; i < num_arms; i++){
			//do the bigger angle take away the smaller angle and make sure it is less than 180
			if(one.getAngles()[i] > second.getAngles()[i]){
				double temp = (one.getAngles()[i] - two.getAngles()[i]);
				if(temp > 180){
					temp = 360 - temp;
				}
				diff += temp;
			}
			else{
				double temp = (two.getAngles()[i] - one.getAngles()[i]);
				if(temp > 180){
					temp = 360 - temp;
				}
				diff += temp;
			}
		}
		//set the weight to the cumulutive distance between the angles
		weight = diff;
	}
	
	//get the weight
	public double getWeight(){
		return weight;
	}
	
	//get the second vertex
		public Vertex getAdjacent(Vertex v){
			if(v.equals(one)){
				return two;
			}
			else{
				return null;
			}
		}
		
	// A compare function
	public int compareTo(Edge other){
		if(weight < other.weight){
			return -1;
		}
		else if( weight == other.weight){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	//the equals function
	public boolean equals(Edge other){
		if(one.equals(other.one) && two.equals(other.two) || one.equals(other.two) && two.equals(other.one)){
			return true;
		}
		else{
			return false;
		}
	}
	
	//create the string representation
	public String toString() {
        String answer = "[" + one + "-------" + two + "]";
        return answer;
    }
	
	//hash function
	public int hashCode(){
		int total = 0;
		total = (int)weight;
		return total;
	}

}
