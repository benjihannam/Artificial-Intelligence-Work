AUTHOR: BENJI HANNAM

ReadMe file for CS76 Assignment 2: Mazeworld

There are three drivers set up inside of the code folder that run the programs. They are WarmUpDriver.java, MultiDriver.java and BlindDriver.java. Each of them has within them some preloaded mazes that have been hard coded. 

WarmUpDriver.java:
	- height and width are defined at the top, set those to desired values. All the mazes scale.
	- Set the goal location using the goal_x and goal_y variables at the top.
	- Inside of the mwProb set up constructor select desired maze by either inputting new_maze0, new_maze1,... into the first paramater.
	- The graphics are on a one second delay


MultiDriver.java:
	- height and width are defined at the top, set those to desired values. All the mazes scale.
	- Also at the top you can set the number of robots(k) and the start/goal locations for each robots
	- Inside of the new_problem set up constructor select desired maze by either inputting new_maze0, new_maze1,... into the first paramater.
	- The graphics are on a one second delay

BlindDriver.java:
	- height and width are defined at the top, set those to desired values. All the mazes scale.
	- Goal_x and goal_y are also set at the top
	- Inside of the br_problem set up constructor select desired maze by either inputting new_maze0, new_maze1,... into the first paramater.
	- The graphics are on a one second delay