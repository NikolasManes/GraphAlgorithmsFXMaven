package com.nikolas;

public class GPath {
    // The path connects 2 nodes - has direction and weight
    private GNode mStart;
    private GNode mEnd;
    private int mWeight;
    // Basic constructor
    public GPath(GNode start, GNode end, int weight) {
        this.mStart = start;
        this.mEnd = end;
        this.mWeight = weight;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GPath path = (GPath) o;
        return mStart.getId() == path.getStart().getId() && mEnd.getId() == path.getEnd().getId() && mWeight == path.getWeight();
    }

    public GNode getStart() {
        return mStart;
    }

    public GNode getEnd() {
        return mEnd;
    }

    public int getWeight() {
        return mWeight;
    }

    public boolean hasNode(int nodeID) {
        return nodeID == mStart.getId() || nodeID == mEnd.getId();
    }

    // For debugging
    public void printPath(){
        System.out.println("| START: " + this.getStart().getId() + " | END: " + this.getEnd().getId() + " | WEIGHT: " + this.getWeight() + " |");
    }
}