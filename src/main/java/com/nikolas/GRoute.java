package com.nikolas;

import java.util.List;

public class GRoute {
    private GNode mStartPoint;
    private GNode mEndPoint;
    private List<GPath> mGPaths;
    private int mTotalWeight = Integer.MAX_VALUE;

    public GRoute(GNode startPoint, GNode endPoint, List<GPath> GPaths) {
        this.mStartPoint = startPoint;
        this.mEndPoint = endPoint;
        this.mGPaths = GPaths;
    }

    public GNode getStartPoint() {
        return mStartPoint;
    }

    public GNode getEndPoint() {
        return mEndPoint;
    }

    public List<GPath> getPaths() {
        return mGPaths;
    }

    public void addPathToRoute(GPath GPath) throws PathCannotConnectToRouteException {
        if (this.mEndPoint.getId() == GPath.getStart().getId()){
            this.mGPaths.add(GPath);
            this.mEndPoint = GPath.getEnd();
            this.calcTotalWeight();
        } else {
            throw new PathCannotConnectToRouteException("Route and Path cannot be connected!");
        }
    }

    // For debugging
    public void printRoute(){
        System.out.println("-ROUTE:\n FROM: " + this.getStartPoint().getId() + "\tTO: "   + this.getEndPoint().getId());
        System.out.println(" TOTAL WEIGHT: " + this.getTotalWeight());
        System.out.println(" PATHS: ");
        if(mGPaths.isEmpty()){
            System.out.println("Route is empty");
        }
        for (GPath GPath : mGPaths){
            GPath.printPath();
        }
        System.out.println("-----");
    }

    public void setTotalWeight(int totalWeight) {
        this.mTotalWeight = totalWeight;
    }

    public void calcTotalWeight(){
        int totalWeight = 0;
        for (GPath GPath : mGPaths){
            totalWeight = totalWeight + GPath.getWeight();
        }
        mTotalWeight = totalWeight;
    }

    public int getTotalWeight() {
        return mTotalWeight;
    }
}

class PathCannotConnectToRouteException extends Exception{
    public PathCannotConnectToRouteException(String message){
        super(message);
    }
}
