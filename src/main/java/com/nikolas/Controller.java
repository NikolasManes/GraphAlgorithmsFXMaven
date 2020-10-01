package com.nikolas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private Graph graph;
    private boolean nodeIsSelected;
    private GNode startNode;
    private GNode endNode;
    private int pathWeight;
    private int destinationNodeID;
    private int listItem = 0;
    private GNode lastNode;
    private List<GRoute> dijkstraRoutes;
    private Color backgroundColor = Color.LIGHTGRAY;
    private Color strokeColorDefault = Color.RED;
    private Color fillColorDefault = Color.BLACK;
    private Color nodeColorDefault = Color.BLUE;
    private Color pathColorDefault = Color.PURPLE;
    private Color routeColor = Color.ORANGERED;
    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    @FXML
    private RadioButton addNodes;
    @FXML
    private RadioButton addPaths;
    @FXML
    private CheckBox delete;
    @FXML
    private ChoiceBox chooseAlgorithmBox;
    @FXML
    private ChoiceBox clearChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new Graph();
        dijkstraRoutes = new ArrayList<>();
        nodeIsSelected = false;
        graphicsContext = canvas.getGraphicsContext2D();
        setScene();
    }

    public void setScene(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(backgroundColor);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(fillColorDefault);
        graphicsContext.setStroke(strokeColorDefault);
    }

    public void canvasClick(MouseEvent mouseEvent) {
        if (addNodes.isSelected()){
            if (delete.isSelected()){
                List<GNode> nodesDummy = new ArrayList<>(graph.getNodes());
                for (GNode gNode: nodesDummy) {
                    if (gNode.nodeSelect(mouseEvent.getX(), mouseEvent.getY())) {
                        graph.deleteNode(gNode);
                        drawGraph();
                        return;
                    }
                }
            }
            // Create new Node
            for (GNode gNode: graph.getNodes()) {
                if (gNode.nodeSelect(mouseEvent.getX(), mouseEvent.getY())) {   // Safe distance :P
                    return;
                }
            }
            GNode node = new GNode(mouseEvent.getX(), mouseEvent.getY());
            graph.addNode(node);
            drawGraph();
        } else if (addPaths.isSelected()){
            // Create new Path
            if(nodeIsSelected){
                // Start Node is selected
                for (GNode gNode: graph.getNodes()){
                    // Get End Node
                    if (gNode.nodeSelect(mouseEvent.getX(), mouseEvent.getY())){
                        nodeIsSelected = false;
                        endNode = gNode;
                        if (delete.isSelected()){
                            List<GPath> dummyPaths =new ArrayList<>(graph.getPaths());
                            for (GPath gPath: dummyPaths){
                                if (gPath.getStart().getId() == startNode.getId() && gPath.getEnd().getId() == endNode.getId()){
                                    graph.deletePath(gPath);
                                }
                            }
                            drawGraph();
                            break;
                        }
                        try {
                            // Get the weight
                            String weightText = createInputDialog("1", "Path Weight", "Please enter path's weight: ");
                            if (weightText == "cancel"){
                                drawGraph();
                                break;
                            }
                            pathWeight = Integer.parseInt(weightText);
                            GPath path = new GPath(startNode, endNode, pathWeight);
                            graph.addPathToGraph(path);
                            drawGraph();
                            break;
                        } catch (NumberFormatException numberFormatException){
                            createAlertDialog("Invalid input!", "Please enter an integer!");
                            drawGraph();
                            break;
                        }
                    }
                }
            } else {
                // Select the start Node
                for (GNode gNode: graph.getNodes()){
                    if (gNode.nodeSelect(mouseEvent.getX(), mouseEvent.getY())){
                        graphicsContext.strokeOval(gNode.getCordX()-15, gNode.getCordY()-15, 30, 30);
                        nodeIsSelected = true;
                        startNode = gNode;
                        break;
                    }
                }
            }
        }
    }
    public void clear(MouseEvent mouseEvent) {
        switch (clearChoiceBox.getValue().toString()){
            case "GRAPH":
                graph = new Graph();
                dijkstraRoutes = new ArrayList<>();
                nodeIsSelected = false;
                setScene();
                drawGraph();
                break;
            case "ROUTES":
                setScene();
                drawGraph();
                break;
        }
    }

    public void nextRoute(MouseEvent mouseEvent) {
        setScene();
        drawGraph();
        if(listItem < dijkstraRoutes.size()-1){
            listItem++;
        }
        drawRoute(dijkstraRoutes.get(listItem));
    }

    public void previousRoute(MouseEvent mouseEvent) {
        setScene();
        drawGraph();
        if(listItem > 0){
            listItem--;
        }
        drawRoute(dijkstraRoutes.get(listItem));

    }
    // Execute the algorithms
    public void runAlgorithm(MouseEvent mouseEvent) {
        switch (chooseAlgorithmBox.getValue().toString()){
            case "BFS":
                // Ask destination Node for BFS
                try {
                    // Get the weight
                    String destinationText = createInputDialog("0", "Destination Node", "Please enter the destination node: ");
                    if (destinationText == "cancel"){
                        drawGraph();
                        break;
                    }
                    destinationNodeID = Integer.parseInt(destinationText);
                    drawRoute(runBFS(graph, 0, graph.getSingleNode(destinationNodeID).getId()));
                } catch (NodeNotInGraphException nodeNotInGraphException) {
                    createAlertDialog("Node not in Graph!", "Please enter an integer from 0 to " + (graph.getNodeNumber() - 1)+ "!");
                    drawGraph();
                    break;
                } catch (NumberFormatException numberFormatException) {
                    createAlertDialog("Invalid input!", "Please enter an integer!");
                    drawGraph();
                    break;
                }
                break;
            case "DFS":
                // Ask destination Node for DFS
                try {
                    // Get the weight
                    String destinationText = createInputDialog("0", "Destination Node", "Please enter the destination node: ");
                    if (destinationText == "cancel"){
                        drawGraph();
                        break;
                    }
                    destinationNodeID = Integer.parseInt(destinationText);
                    drawRoute(runDFS(graph, 0, graph.getSingleNode(destinationNodeID).getId()));
                } catch (NodeNotInGraphException nodeNotInGraphException) {
                    createAlertDialog("Node not in Graph!", "Please enter an integer from 0 to " + (graph.getNodeNumber() - 1)+ "!");
                    drawGraph();
                    break;
                } catch (NumberFormatException numberFormatException) {
                    createAlertDialog("Invalid input!", "Please enter an integer!");
                    drawGraph();
                    break;
                }
                break;
            case "Prim":
                runPrim(graph);
                break;
            case "Dijkstra":
                listItem = 0;
                dijkstraRoutes = runDijkstra(graph);
                drawRoute(dijkstraRoutes.get(listItem));
                break;
        }
    }
    // Draw the graph
    private void drawGraph() {
        setScene();
        graphicsContext.setStroke(nodeColorDefault);
        // Draw every node
        for (GNode gNode: graph.getNodes()){
            graphicsContext.strokeOval(gNode.getCordX()-10, gNode.getCordY()-10, 20, 20);
            if (gNode.getId() <10){
                graphicsContext.strokeText(String.valueOf(gNode.getId()), gNode.getCordX()-3, gNode.getCordY()+4);
            } else {
                graphicsContext.strokeText(String.valueOf(gNode.getId()), gNode.getCordX()-6.5, gNode.getCordY()+4);
            }
        }
        graphicsContext.setStroke(pathColorDefault);
        graphicsContext.setFill(pathColorDefault);
        // Draw every path
        for (GPath gPath: graph.getPaths()){
            drawPath(graphicsContext, gPath.getStart().getCordX(), gPath.getStart().getCordY(), gPath.getEnd().getCordX(), gPath.getEnd().getCordY(), gPath.getWeight());
            graphicsContext.setStroke(pathColorDefault);
        }
        graphicsContext.setStroke(strokeColorDefault);
        graphicsContext.setFill(fillColorDefault);
    }
    // Draw the path as an arrow with a circle on it (--0-->)
    private void drawPath(GraphicsContext gc, double x1, double y1, double x2, double y2, int weight) {
        // Do the maths
        double dx = x2 - x1;
        double dy = y2 - y1;
        double midX = x1+(dx/2);
        double midY = y1+(dy/2);
        double angle = Math.atan2(dy, dx);
        double lineEndX = x2-10*Math.cos(angle);
        double lineEndY = y2-10*Math.sin(angle);
        double arrowAngle = Math.PI/6;
        double point1X = lineEndX+15*Math.cos(Math.PI-arrowAngle+angle);
        double point1Y = lineEndY+15*Math.sin(Math.PI-arrowAngle+angle);
        double point2X = lineEndX+15*Math.cos(Math.PI+angle+arrowAngle);
        double point2Y = lineEndY+15*Math.sin(Math.PI+angle+arrowAngle);
        // Drawing
        gc.strokeLine(x1+10*Math.cos(angle), y1+10*Math.sin(angle), lineEndX, lineEndY);
        gc.fillPolygon(new double[]{lineEndX, point1X, point2X, lineEndX}, new double[]{lineEndY, point1Y, point2Y, lineEndY}, 4);
        gc.fillOval(midX-8, midY-8, 16, 16);
        gc.setStroke(backgroundColor);
        if (weight<10){
            gc.strokeText(String.valueOf(weight), midX-3, midY+4);
        } else {
            gc.strokeText(String.valueOf(weight), midX-7, midY+4);
        }
    }
    // Draw the route
    public void drawRoute(GRoute route){
        setScene();
        drawGraph();
        try {
            lastNode = route.getEndPoint();
        } catch (NullPointerException nullPointerException) {
            createInfoDialog("There is no route!");
            return;
        }
        graphicsContext.setFill(routeColor);
        graphicsContext.fillRect(canvas.getWidth()-100, 0, canvas.getWidth(),20);
        graphicsContext.setStroke(backgroundColor);
        // Show the target Node
        graphicsContext.strokeText("Target Node: " + route.getEndPoint().getId(), canvas.getWidth()-90, 15);
        graphicsContext.setStroke(routeColor);
        // Color the paths
        for (GPath path: route.getPaths()){
            drawPath(graphicsContext, path.getStart().getCordX(), path.getStart().getCordY(), path.getEnd().getCordX(), path.getEnd().getCordY(), path.getWeight());
            graphicsContext.setStroke(routeColor);
        }
        graphicsContext.fillOval(lastNode.getCordX()-15, lastNode.getCordY()-15, 30, 30);
        graphicsContext.setStroke(backgroundColor);
        // Show total weight
        if (route.getTotalWeight()<10){
            graphicsContext.strokeText(String.valueOf(route.getTotalWeight()), lastNode.getCordX()-3, lastNode.getCordY()+4);
        } else {
            graphicsContext.strokeText(String.valueOf(route.getTotalWeight()), lastNode.getCordX()-7, lastNode.getCordY()+4);
        }
        graphicsContext.setStroke(strokeColorDefault);
        graphicsContext.setFill(fillColorDefault);
    }

    public void drawMinTree(List<GPath> paths){
        setScene();
        drawGraph();
        graphicsContext.setFill(routeColor);
        graphicsContext.setStroke(routeColor);
        // Color the paths
        for (GPath path: paths){
            drawPath(graphicsContext, path.getStart().getCordX(), path.getStart().getCordY(), path.getEnd().getCordX(), path.getEnd().getCordY(), path.getWeight());
            graphicsContext.setStroke(routeColor);
        }
        graphicsContext.setStroke(strokeColorDefault);
        graphicsContext.setFill(fillColorDefault);
    }

    private String createInputDialog(String prompt, String tittle, String content) {
        TextInputDialog dialog = new TextInputDialog(prompt);
        dialog.setTitle(tittle);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        } else {
            return "cancel";
        }
    }

    private void createInfoDialog(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    private void createAlertDialog(String cause, String solution) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(cause);
        alert.setContentText(solution);
        alert.showAndWait();
    }
    /***
     *      Breadth First Search Algorithm
     * */
    public GRoute runBFS(Graph graph, int startID, int endID){
        // Create necessary lists
        List<GNode> frontier = new ArrayList<>();
        List<GNode> exploredNodes = new ArrayList<>();
        List<GRoute> routes = new ArrayList<>();
        List<GRoute> routesToAdd = new ArrayList<>();
        try {
            GRoute startRoute = new GRoute(graph.getSingleNode(startID), graph.getSingleNode(startID), new ArrayList<>());
            routes.add(startRoute);
        } catch (NodeNotInGraphException e) {
            System.out.println(e.getMessage());
        }
        // Add the first Node in the frontier
        try {
            frontier.add(graph.getSingleNode(startID));
        } catch (NodeNotInGraphException e) {
            System.out.println(e.getMessage());
        }
        // If the frontier is empty there is no route
        while (!frontier.isEmpty()) {
            List<GNode> dummyFrontierList = new ArrayList<>(frontier);
            // Check every Node in the frontier if is the destination
            for (GNode node : dummyFrontierList) {
                GNode toCheck = node;
                frontier.remove(node);
                if (toCheck.getId() == endID) {
                    // When the destination reached return the Route
                    for (GRoute route : routes) {
                        if (route.getEndPoint().getId() == endID) {
                            return route;
                        }
                    }
                }
                // Add Node to explored list
                exploredNodes.add(toCheck);
                // Search the paths of the graph
                for (GPath path : graph.getPaths()) {
                    // Check the paths that start from the Node we exploring
                    if (path.getStart().getId() == toCheck.getId()) {
                        // Check the end Node, if not explored yet, added to frontier
                        if (frontier.contains(path.getEnd()) || exploredNodes.contains(path.getEnd())) {
                            continue;
                        }
                        frontier.add(path.getEnd());
                        // To every route that end to the Node we exploring append the path
                        for (GRoute r : routes) {
                            if (r.getEndPoint().getId() == toCheck.getId()) {
                                GRoute routeTemp = new GRoute(r.getStartPoint(), r.getEndPoint(), new ArrayList<>(r.getPaths()));
                                try {
                                    routeTemp.addPathToRoute(path);
                                } catch (PathCannotConnectToRouteException e) {
                                    e.printStackTrace();
                                }
                                routesToAdd.add(routeTemp);
                            }
                        }
                        // Add the routes created to the list
                        routes.addAll(routesToAdd);
                        routesToAdd.clear();
                    }
                }
            }
        }
        // When there is no route
        return null;
    }
    /***
     *      Depth First Search Algorithm
     * */
    public GRoute runDFS(Graph graph, int startID, int endID){
        // Create the stack and the lists we need
        Deque<GNode> nodesToCheck = new ArrayDeque<>();
        List<Integer> exploredNodes = new ArrayList<>();
        List<GRoute> routesCreated = new ArrayList<>();
        List<GRoute> routesToAdd = new ArrayList<>();
        try {
            GRoute dummyRoute = new GRoute(graph.getSingleNode(startID), graph.getSingleNode(startID), new ArrayList<>());
            routesCreated.add(dummyRoute);
            // Add the first Node
            nodesToCheck.push(graph.getSingleNode(startID));
            while (!nodesToCheck.isEmpty()){
                // Get the first Node from stack
                GNode currentNode = nodesToCheck.pop();
                // Check if the Node is explored
                if (exploredNodes.contains(currentNode.getId())){
                    continue;
                }
                exploredNodes.add(currentNode.getId());
                // Check all the paths of the graph that start from that Node
                for (GPath path: graph.getPaths()){
                    if (path.getStart().getId() == currentNode.getId()){
                        // Append the path to the routes that are created and have the same end
                        for (GRoute route: routesCreated){
                            if (route.getEndPoint().getId() == currentNode.getId()){
                                GRoute routeCreated = new GRoute(route.getStartPoint(), route.getEndPoint(), new ArrayList<>(route.getPaths()));
                                routeCreated.addPathToRoute(path);
                                // Check if reached the destination
                                if (routeCreated.getEndPoint().getId() == endID){
                                    return routeCreated;
                                }
                                routesToAdd.add(routeCreated);
                            }
                        }
                        // Add the routes to the list
                        routesCreated.addAll(routesToAdd);
                        routesToAdd.clear();
                        // Add the next Node to the stack
                        nodesToCheck.push(path.getEnd());
                    }
                }
            }
        } catch (NodeNotInGraphException e) {
            e.printStackTrace();
        } catch (PathCannotConnectToRouteException e) {
            e.printStackTrace();
        }
        // When there is no route
        return null;
    }
    /***
     *      Prim Algorithm
     * */
    public void runPrim(Graph graph){
        List<GNode> nodes = new ArrayList<>(graph.getNodes());
        List<GPath> paths = new ArrayList<>(graph.getPaths());
        // Tree variables
        List<GNode> treeNodes = new ArrayList<>();
        List<GPath> treePaths = new ArrayList<>();
        List<GPath> pathsToCheck = new ArrayList<>();

        treeNodes.add(nodes.remove(0));

        while (!nodes.isEmpty()) {
            // Get the available paths for each node
            for (GNode node: treeNodes){
                for (GPath path: paths){
                    if (path.hasNode(node.getId())){
                        if (treeNodes.contains(path.getEnd()) && treeNodes.contains(path.getStart())){
                            continue;
                        }
                        pathsToCheck.add(path);
                    }
                }
            }
            // Add the path with the minimum weight to the tree
            pathsToCheck.sort(Comparator.comparing(GPath::getWeight));
            treePaths.add(pathsToCheck.remove(0));
            // Remove it from the list
            paths.removeIf(path -> path.equals(treePaths.get(treePaths.size() - 1)));
            // Add node to treeNodes and remove it from nodes
            for (GNode node: treeNodes){
                if (treePaths.get(treePaths.size()-1).getStart().equals(node)){
                    treeNodes.add(treePaths.get(treePaths.size()-1).getEnd());
                    break;
                }
                if (treePaths.get(treePaths.size()-1).getEnd().equals(node)){
                    treeNodes.add(treePaths.get(treePaths.size()-1).getStart());
                    break;
                }
            }
            nodes.removeIf(node -> node.equals(treeNodes.get(treeNodes.size()-1)));
            pathsToCheck.clear();
        }
        drawMinTree(treePaths);
    }
    /***
     *      Dijkstra Shortest Paths Algorithm
     * */
    public List<GRoute> runDijkstra(Graph graph) {
        // Declare a list to store the routes
        List<GRoute> routes = new ArrayList<>();
        // Now add all the others the weight is MAX
        for (GNode node: graph.getNodes()){
            GRoute dummy = new GRoute(node, node, new ArrayList<>());
            routes.add(dummy);
        }
        // Set the first route for the first node weight is 0
        // - all others had been set to MAX-int value
        routes.get(0).setTotalWeight(0);
        // Declare a list to store the best routes
        List<GRoute> bestRoutes = new ArrayList<>();
        // Run Dijkstra
        int step = 0;
        while(bestRoutes.size()<graph.getNodeNumber()){
            step++;
            int pathCount = 0;
            /* Get the shortest route of all to stabilize it */
            // Declare a route to store the temporary value
            GRoute shortestRoute;
            // Sort the routes according their weight
            routes.sort(Comparator.comparingInt(GRoute::getTotalWeight));
            // Remove the shortest route and add it to bestRoutes
            shortestRoute = routes.remove(0);
            bestRoutes.add(shortestRoute);
            // Find all paths starting from the end of that route
            for (GPath path: graph.getPaths()){
                if (shortestRoute.getEndPoint().getId() == path.getStart().getId()){
                    pathCount++;
                    // For every path we find check if there is a shorter route to the Node
                    for (GRoute route: routes){
                        if(route.getEndPoint().getId() == path.getEnd().getId()){
                            // Check if there is a shortest way to go to that node(end)
                            if(route.getTotalWeight() > shortestRoute.getTotalWeight() + path.getWeight()){
                                // Create a dummy route to store the route
                                GRoute dummyRoute = new GRoute(shortestRoute.getStartPoint(), shortestRoute.getEndPoint(), new ArrayList<>(shortestRoute.getPaths()));
                                try {
                                    dummyRoute.addPathToRoute(path);
                                } catch (PathCannotConnectToRouteException e) {
                                    System.out.println(e.getMessage());
                                }
                                routes.set(routes.indexOf(route), dummyRoute);
                            }
                        }
                    }
                }
            }
        }
        // When a route has infinite weight means the node is unreachable
        bestRoutes.removeIf(route -> route.getTotalWeight() == Integer.MAX_VALUE);
        return bestRoutes;
    }
}