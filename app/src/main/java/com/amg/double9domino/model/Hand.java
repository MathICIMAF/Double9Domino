package com.amg.double9domino.model;

import com.amg.double9domino.model.Game;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Hand {
    protected boolean completed;
    boolean firstHand;
    protected Tile firstTilePlayed = null;
    protected Game game;
    protected int leftCorner = Corner.NONE.value;
    protected Tile leftCornerTile = null;
    ArrayList<Player> order = new ArrayList<>();
    protected int rightCorner = Corner.NONE.value;
    protected Tile rightCornerTile = null;
    ArrayList<Tile> tilesInOrder = new ArrayList<>();
    ArrayList<Tile> tilesOnTable = new ArrayList<>();
    int total1 = 0;
    int total2 = 0;
    int turn = -1;
    int[] used = new int[10];

    public enum Corner {
        NONE(-1),
        LEFT(0),
        RIGHT(1),
        UP(2),
        DOWN(3);
        
        int value;

        private Corner(int index) {
            this.value = index;
        }
    }

    public Hand(Game game2) {
        this.game = game2;
        Team team1 = game2.team1;
        Team team2 = game2.team2;
        this.order.add(team1.player1);
        this.order.add(team2.player1);
        this.order.add(team1.player2);
        this.order.add(team2.player2);
    }

    /* access modifiers changed from: protected */
    public void countPoints(boolean blocked) {
        int compare1;
        int compare2;
        Team team1 = this.game.team1;
        Team team2 = this.game.team2;
        int total12 = team1.player1.getSum() + team1.player2.getSum();
        int total22 = team2.player1.getSum() + team2.player2.getSum();
        if (blocked) {

            compare1 = Math.min(team1.player1.getSum(), team1.player2.getSum());
            compare2 = Math.min(team2.player1.getSum(), team2.player2.getSum());

            if (compare1 < compare2) {
                winTeam1(total12, total22);
            } else if (compare2 < compare1) {
                winTeam2(total12, total22);
            } else {
                this.total2 = 0;
                this.total1 = 0;
            }
        } else if (team1.player1.getTiles().size() == 0 || team1.player2.getTiles().size() == 0) {
            winTeam1(total12, total22);
        } else {
            winTeam2(total12, total22);
        }
        if (this.game.total1 == 0 && this.game.total2 == 0 && game.getFirstHandDouble()){
            this.game.total1 += this.total1*2;
            this.total1*=2;
            this.game.total2 += this.total2*2;
            this.total2*=2;
        }
        else {
            this.game.total1 += this.total1;
            this.game.total2 += this.total2;
        }
    }

    /* access modifiers changed from: protected */
    public Tile createTile(int i, int j) {
        return new Tile(i, j);
    }

    /* access modifiers changed from: protected */
    public void deal(Player player, int ini, int fin, List<Tile> fichas) {
        player.reset();
        for (int i = ini; i <= fin; i++) {
            player.add(fichas.get(i));
        }
    }

    /* access modifiers changed from: protected */
    public List<Tile> fillBag() {
        List<Tile> bolsa = new ArrayList<>();
        for (short i = 0; i <= 9; i = (short) (i + 1)) {
            for (short j = i; j <= 9; j = (short) (j + 1)) {
                bolsa.add(createTile(i, j));
            }
        }
        return bolsa;
    }

    public int getCornerSize(Corner corner) {
        int centerPos = getFirstTilePos();
        if (corner == Corner.LEFT) {
            return centerPos;
        }
        return (getTilesOnTable().size() - centerPos) - 1;
    }

    public Player getCurrentPlayer() {
        if (this.turn >= 0) {
            return this.order.get(this.turn);
        }
        return null;
    }

    public int getDownCorner() {
        return Corner.NONE.value;
    }

    public Game.FirstPlay getFirstStart() {
        return this.game.getFirstStart();
    }

    public Tile getFirstTile() {
        return this.firstTilePlayed;
    }

    public int getFirstTilePos() {
        return getTilesOnTable().indexOf(getFirstTile());
    }

    public Game getGame() {
        return this.game;
    }

    public int getLeftCorner() {
        return this.leftCorner;
    }

    public Tile getLeftCornerTile() {
        return this.leftCornerTile;
    }

    public int getMaxPoints() {
        return Math.max(this.total1, this.total2);
    }

    public int getOrder(Player me) {
        return this.order.indexOf(me);
    }

    public int getPlayCount() {
        return this.tilesInOrder.size();
    }

    /* access modifiers changed from: protected */
    public Corner getPlayHalf(Tile f) {
        return (f.getLeft() == this.leftCorner || f.getRight() == this.leftCorner) ? Corner.LEFT : Corner.RIGHT;
    }

    public Tile getPlayInTurn(int index) {
        return this.tilesInOrder.get(index);
    }

    public List<List<Tile>> getRemainingTitles(Player me) {
        List<List<Tile>> result = new ArrayList<>();
        int begin = getOrder(me);
        for (int i = 0; i < this.order.size(); i++) {
            result.add(this.order.get((begin + i) % 4).getTiles());
        }
        return result;
    }

    public int getRightCorner() {
        return this.rightCorner;
    }

    public Tile getRightCornerTile() {
        return this.rightCornerTile;
    }

    public Corner getShorterCorner() {
        Corner corner = Corner.RIGHT;
        if (this.firstTilePlayed == null || this.tilesOnTable.size() - 1 <= (this.tilesOnTable.indexOf(getFirstTile()) << 1)) {
            return corner;
        }
        return Corner.LEFT;
    }

    public Tile getSpinner() {
        return null;
    }

    public int getSumTilesPlayed() {
        int sum = 0;
        for (Tile t : this.tilesInOrder) {
            if (!Tile.PASS.equals(t)) {
                sum += t.getSum();
            }
        }
        return sum;
    }

    public Player getTeammate(Player player) {
        return this.order.get((getOrder(player) + 2) % 4);
    }

    public List<Tile> getTilesOnTable() {
        return this.tilesOnTable;
    }

    public int getTotal1() {
        return this.total1;
    }

    public int getTotal2() {
        return this.total2;
    }

    public int getUpCorner() {
        return Corner.NONE.value;
    }

    public Team getWinner() {
        if (!hasEnded()) {
            return null;
        }
        if (this.total1 > this.total2) {
            return this.game.team1;
        }
        if (this.total2 > this.total1) {
            return this.game.team2;
        }
        return null;
    }

    public boolean hasEnded() {
        if (!this.completed) {
            Team team1 = this.game.team1;
            Team team2 = this.game.team2;
            if (team1.getPlayer1().getTiles().size() == 0 || team1.getPlayer2().getTiles().size() == 0 || team2.getPlayer1().getTiles().size() == 0 || team2.getPlayer2().getTiles().size() == 0) {
                countPoints(false);
                this.completed = true;
            } else {
                /*int rightCorner2 = getRightCorner();
                int leftCorner2 = getLeftCorner();
                if (rightCorner2 != Corner.NONE.value && rightCorner2 == leftCorner2 && this.used[rightCorner2] == 7) {

                }*/
                if (tilesInOrder.size() >=4) {
                    int count = 0;
                    for (int i = 1; i <= 4; i++) {
                        if (tilesInOrder.get(tilesInOrder.size() - i).equals(Tile.PASS))
                            count++;
                        else break;
                    }
                    if (count >= 4) {
                        countPoints(true);
                        this.completed = true;
                    }
                }
            }
        }
        return this.completed;
    }

    public int howMany(int number) {
        return this.used[number];
    }

    public boolean isFirstHand() {
        return this.firstHand;
    }

    public boolean isTurn(Player player) {
        return player.equals(getCurrentPlayer());
    }

    public boolean play(Tile f) {
        boolean valid;
        if (f.equals(Tile.PASS)) {
            this.turn = (this.turn + 1) % this.order.size();
            this.tilesInOrder.add(f);
            valid = true;
        } else {
            int rightCorner2 = getRightCorner();
            if (rightCorner2 == getLeftCorner()) {
                f.setHalf(getShorterCorner());
            } else if (f.getHalf() == Corner.NONE) {
                f.setHalf(getPlayHalf(f));
            }
            valid = updateHand(f);
            if (!valid) {
                f.setHalf(Corner.NONE);
            }
        }
        return valid;
    }

    public boolean playBothCorners(Tile tile) {
        boolean both;
        int rightCorner2 = getRightCorner();
        int leftCorner2 = getLeftCorner();
        int rightValue = tile.getRight();
        int leftValue = tile.getLeft();
        if ((rightCorner2 == rightValue && leftCorner2 == leftValue) || (leftCorner2 == rightValue && rightCorner2 == leftValue)) {
            both = true;
        } else {
            both = false;
        }
        return both && leftCorner2 != rightCorner2 && tile.getPlayer().getTiles().size() > 1;
    }

    public void setFirstHand(boolean firstHand2) {
        this.firstHand = firstHand2;
    }

    /* access modifiers changed from: package-private */
    public void setFirstTile(Tile primera) {
        this.firstTilePlayed = primera;
    }

    public void setLeftCorner(int leftCorner2) {
        this.leftCorner = leftCorner2;
    }

    public void setLeftCornerTile(Tile leftCornerTile2) {
        this.leftCornerTile = leftCornerTile2;
    }

    public void setRightCorner(int rightCorner2) {
        this.rightCorner = rightCorner2;
    }

    public void setRightCornerTile(Tile rightCornerTile2) {
        this.rightCornerTile = rightCornerTile2;
    }

    public void setTotal1(int total12) {
        this.total1 = total12;
    }

    public void setTotal2(int total22) {
        this.total2 = total22;
    }

    public void setTurn(int turno) {
        this.turn = turno;
    }

    public List<Tile> shuffle() {
        List<Tile> bag = fillBag();
        List<Tile> tiles = new ArrayList<>();
        Random r = new Random();
        while (bag.size() > 0) {
            int pos = r.nextInt(bag.size());
            tiles.add(bag.get(pos));
            bag.remove(pos);
        }
        return tiles;
    }

    public void deal(List<Tile> tilesShuffled) {
        Team team1 = this.game.team1;
        Team team2 = this.game.team2;
        deal(team1.player1, 0, 9, tilesShuffled);
        deal(team2.player1, 10, 19, tilesShuffled);
        deal(team1.player2, 20, 29, tilesShuffled);
        deal(team2.player2, 30, 39, tilesShuffled);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Team team1 = this.game.team1;
        Team team2 = this.game.team2;
        sb.append("Players:\n").append(team1.player1).append("\n").append(team1.player2).append("\n").append(team2.player1).append("\n").append(team2.player2).append("\n").append("\nTurn:\n").append(this.turn).append("\nTable:").append(this.tilesOnTable).append("\n");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public boolean updateHand(Tile tile) {
        boolean valid = false;
        if (isTurn(tile.getPlayer())) {
            int rightCorner2 = getRightCorner();
            int leftCorner2 = getLeftCorner();
            int tileSide0 = tile.getLeft();
            int tileSide1 = tile.getRight();
            if (rightCorner2 == leftCorner2 && rightCorner2 == Corner.NONE.value) {
                valid = true;
                int[] iArr = this.used;
                iArr[tileSide0] = iArr[tileSide0] + 1;
                if (!tile.isDouble()) {
                    int[] iArr2 = this.used;
                    iArr2[tileSide1] = iArr2[tileSide1] + 1;
                }
                this.tilesInOrder.add(tile);
                this.tilesOnTable.add(tile);
                setLeftCorner(tileSide0);
                setLeftCornerTile(tile);
                setRightCorner(tileSide1);
                setRightCornerTile(tile);
                tile.getPlayer().substract(tile);
            } else if ((tile.getHalf() == Corner.RIGHT && (tileSide0 == rightCorner2 || tileSide1 == rightCorner2)) || (tile.getHalf() == Corner.LEFT && (tileSide0 == leftCorner2 || tileSide1 == leftCorner2))) {
                valid = true;
                int[] iArr3 = this.used;
                iArr3[tileSide0] = iArr3[tileSide0] + 1;
                if (!tile.isDouble()) {
                    int[] iArr4 = this.used;
                    iArr4[tileSide1] = iArr4[tileSide1] + 1;
                }
                this.tilesInOrder.add(tile);
                if (tile.getHalf() == Corner.LEFT) {
                    this.tilesOnTable.add(0, tile);
                    setLeftCornerTile(tile);
                    if (tileSide0 == leftCorner2) {
                        setLeftCorner(tileSide1);
                    } else {
                        setLeftCorner(tileSide0);
                    }
                } else {
                    this.tilesOnTable.add(tile);
                    setRightCornerTile(tile);
                    if (tileSide0 == rightCorner2) {
                        setRightCorner(tileSide1);
                    } else {
                        setRightCorner(tileSide0);
                    }
                }
                tile.getPlayer().substract(tile);
            }
        }
        if (valid) {
            this.turn = (this.turn + 1) % this.order.size();
            if (this.tilesInOrder.size() == 1) {
                setFirstTile(tile);
            }
        }
        return valid;
    }

    private void winTeam1(int total12, int total22) {
        if (this.game.getScoreCount() == Game.Count.TEAM) {
            this.total1 = total22;
        } else {
            this.total1 = total12 + total22;
        }
        this.total2 = 0;
    }

    private void winTeam2(int total12, int total22) {
        if (this.game.getScoreCount() == Game.Count.TEAM) {
            this.total2 = total12;
        } else {
            this.total2 = total12 + total22;
        }
        this.total1 = 0;
    }
}
