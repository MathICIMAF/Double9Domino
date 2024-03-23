package com.amg.double9domino.model;

public class Play {
    Tile tile;
    int side;

    public Play(Tile tile, int side){
        this.tile = tile;
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    public Tile getTile() {
        return tile;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
