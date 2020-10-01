package com.nikolas;

import java.util.ArrayList;

public class Graph {

    private ArrayList<GNode> mGNodes = new ArrayList<>();
    private ArrayList<GPath> mGPaths = new ArrayList<>();

    public Graph(){
        GNode.autoIndex = 0;
    }

    public Graph(ArrayList<GNode> GNodes, ArrayList<GPath> GPaths){
        GNode.autoIndex = 0;
        this.mGNodes = GNodes;
        this.mGPaths = GPaths;
    }

    public int getNodeNumber() {
        return mGNodes.size();
    }

    public void addNode(GNode GNode) {
        mGNodes.add(GNode);
    }

    public int getPathNumber() {
        return mGPaths.size();
    }

    public void addPathToGraph (GPath GPath) {
        mGPaths.add(GPath);
    }

    public ArrayList<GNode> getNodes() {
        return mGNodes;
    }

    public void setNodes(ArrayList mNodes) {
        this.mGNodes = mNodes;
    }

    public ArrayList<GPath> getPaths() {
        return mGPaths;
    }

    public void setPaths(ArrayList<GPath> mGPaths) {
        this.mGPaths = mGPaths;
    }

    public boolean pathIsValid(GPath GPath){
        return nodeInGraph(GPath.getStart()) && nodeInGraph(GPath.getEnd());
    }

    public boolean nodeInGraph(GNode GNode){
        for (GNode n : mGNodes) {
            if(GNode.getId() == n.getId()) {
                return true;
            }
        }
        return false;
    }

    public void deletePath(GPath path){
        mGPaths.remove(path);
    }

    public void deleteNode(GNode node){
        // Delete the paths connected to that Node first
        ArrayList<GPath> pathsDummy = new ArrayList<>(mGPaths);
        for (GPath path: pathsDummy){
            if (path.getStart().getId() == node.getId() || path.getEnd().getId() == node.getId()){
                mGPaths.remove(path);
            }
        }
        // Delete the Node
        mGNodes.remove(node);
    }

    public GNode getSingleNode(int id) throws NodeNotInGraphException {
        for (GNode GNode : mGNodes){
            if(GNode.getId() == id) {
                return GNode;
            }
        }
        throw new NodeNotInGraphException("There is not a node with id " + id + " in the graph!");
    }
    // For debugging
    public void printGraph(){
        System.out.println("No of Nodes: " + mGNodes.size());
        for (GPath GPath : mGPaths){
            GPath.printPath();
        }
    }
}

class NodeNotInGraphException extends Exception{
    public NodeNotInGraphException(String message){
        super(message);
    }
}
