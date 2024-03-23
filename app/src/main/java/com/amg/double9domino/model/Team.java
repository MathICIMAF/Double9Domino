package com.amg.double9domino.model;

public final class Team {
    Player player1;
    Player player2;

    public void setPlayer1(Player j1) {
        this.player1 = j1;
    }

    public void setPlayer2(Player j2) {
        this.player2 = j2;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public String getName() {
        return String.valueOf(this.player1.getName()) + " & " + this.player2.getName();
    }
}
