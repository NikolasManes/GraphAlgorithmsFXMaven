# GraphAlgorithmsFXMaven
Same project as GraphAlgorithmFX with Maven.
The project moved to Maven in order to create executable files independent from the JDK version.

About this application:

For educational use, shows how several graph algorithms run.

Project file:

https://www.dropbox.com/s/rreltb7h3630it8/GraphAlgorithms.zip?dl=0

To run the application: unzip the file and run the launcher (type: windows batch file) created in the bin folder

How to use the application:

First of all you have to create a graph.
- To create a new node: 
	(delete must be unchecked!)
	1. select Nodes on the top left of the window (if not selected)
	2. click on the screen 
- To create a new path: 
	(delete must be unchecked!)
	1. select Paths on the top left of the window (if not selected)
	2. click on any node to chose the start the node is marked with a red circle
	3. click on the node you want to be the end
	4. enter path's weight in the pop up window
- To delete a node or path, follow the same steps as in creation (for paths 1-3) with the delete choice checked
	*  if you delete a node the paths contain that node will be also deleted

When a graph is created you can run several algorithms on it.
- To run BFS algorithm:
	1. chose BFS on the bottom left of the window
	2. click run
	3. enter the destination node in the pop up window
- To run DFS algorithm same as the BFS but chose DFS
- To run Prim's algorithm:
	1. chose PRIM on the bottom left of the window
	2. click run
- To run Dijkstra's algorithm:
	1. chose DIJKSTRA on the bottom left of the window
	2. click run
	3. click NEXT or PREVIOUS to view all the routes
	
To clear the routes or the whole graph:
	1. chose ROUTES or GRAPH on the bottom right of the window
	2. click CLEAR
