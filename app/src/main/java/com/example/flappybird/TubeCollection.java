package com.example.flappybird;

public class TubeCollection {
    private int xTube;
    private int upTubeCollection_Y;


    public TubeCollection(int xTube, int upTubeCollection_Y) {
        this.xTube = xTube;
        this.upTubeCollection_Y = upTubeCollection_Y;
    }

    public int getXtube() {
        return xTube;
    }

    public void setXtube(int xTube) {
        this.xTube = xTube;
    }

    public int getUpTubeCollection_Y() {
        return upTubeCollection_Y;
    }

    public int getUpTube_Y() {
        return upTubeCollection_Y - AppHolder.getBitmapControl().getTubeHeight();
    }

    public int getDownTube_Y() {
        return upTubeCollection_Y + AppHolder.tubeGap;
    }

    public void setUpTubeCollection_Y(int upTubeCollection_Y) {
        this.upTubeCollection_Y = upTubeCollection_Y;
    }
}
