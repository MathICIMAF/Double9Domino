package com.amg.double9domino.model;

import com.amg.double9domino.model.Hand;

public class Tile {
    public static final Tile[] DOUBLE = new Tile[10];
    public static final Tile PASS = new Tile(-1, -1);
    private Hand.Corner half;
    private Player player;
    int[] sides;
    private boolean thought;

    static {
        for (int i = 9; i >= 0; i--) {
            DOUBLE[i] = new Tile(i, i);
        }
    }

    public static Tile getPassTile(Player p) {
        Tile pass = new Tile(-1, -1);
        pass.setPlayer(p);
        return pass;
    }

    public Tile() {
        this.half = Hand.Corner.NONE;
        this.sides = new int[2];
    }

    public Tile(int i, int j) {
        this.half = Hand.Corner.NONE;
        this.sides = new int[]{i, j};
    }

    public boolean equals(Object o) {
        if (!(o instanceof Tile)) {
            return false;
        }
        Tile f = (Tile) o;
        if ((this.sides[0] == f.sides[0] && this.sides[1] == f.sides[1]) || (this.sides[0] == f.sides[1] && this.sides[1] == f.sides[0])) {
            return true;
        }
        return false;
    }

    public Hand.Corner getHalf() {
        return this.half;
    }

    public int getLeft() {
        return this.sides[0];
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getRight() {
        return this.sides[1];
    }

    public int getSum() {
        return this.sides[0] + this.sides[1];
    }

    public int hashCode() {
        return this.sides[0] ^ this.sides[1];
    }

    public boolean hasThought() {
        return this.thought;
    }

    public boolean isA(int number) {
        return this.sides[0] == number || this.sides[1] == number;
    }

    public boolean isDouble() {
        return this.sides[0] == this.sides[1] && this.sides[0] != -1;
    }

    public void setHalf(Hand.Corner half2) {
        this.half = half2;
    }

    public void setPlayer(Player player2) {
        this.player = player2;
    }

    public void setThought(boolean thought2) {
        this.thought = thought2;
    }

    public String toString() {
        return String.valueOf(this.sides[0]) + ":" + this.sides[1];
    }
}
