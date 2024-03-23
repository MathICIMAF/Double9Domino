package com.amg.double9domino.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.amg.double9domino.model.ai.Double9Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Player {
    private static final String NULL = "null";
    private static final String TAG = "Player";
    public boolean weStarted;
    public Tile firstPlay;
    private boolean human;
    private Bitmap image;
    private String imageUrl;
    private String location;
    private int loss;
    private Double9Move moveStrategy;
    private String name;
    private int score;
    private int sum;
    private ArrayList<Tile> tiles;
    private int wins;
    List<Integer> myPasses,myPosiblePasses;
    Player partner,rightOponent,leftOponent;
    private HashMap<Short, Integer> myPlays;
    public Player() {
        this.tiles = new ArrayList<>();
        myPasses = new ArrayList<>();
        myPosiblePasses = new ArrayList<>();
        myPlays = new HashMap<>();
        this.moveStrategy = new Double9Move();
    }

    public Player(String name2) {
        this(name2, true);
    }

    public Player(String name2, boolean human2) {
        this(name2, human2, null);
    }

    public Player(String name2, boolean human2, Bitmap image2) {
        this.tiles = new ArrayList<>();
        this.moveStrategy = new Double9Move();
        this.human = human2;
        this.image = image2;
        myPasses = new ArrayList<>();
        myPlays = new HashMap<>();
        myPosiblePasses = new ArrayList<>();
        this.name = name2.replace('|', ' ').replace('[', ' ').replace(']', ' ').replace(',', ' ');
    }

    public List<Integer> getMyPosiblePasses() {
        return myPosiblePasses;
    }

    public void setMyPosiblePasses(List<Integer> myPosiblePasses) {
        this.myPosiblePasses = myPosiblePasses;
    }

    public List<Integer> getMyPasses(){
        return myPasses;
    }

    public void setPartner(Player player){
        partner = player;
    }

    public Player getPartner(){
        return partner;
    }

    public void setRightOponent(Player player){
        rightOponent = player;
    }

    public Player getRightOponent() {
        return rightOponent;
    }

    public void setLeftOponent(Player leftOponent) {
        this.leftOponent = leftOponent;
    }

    public Player getLeftOponent() {
        return leftOponent;
    }

    private void add(int i) {
        this.sum += i;
    }

    public void add(Tile f) {
        this.tiles.add(f);
        f.setPlayer(this);
        add(f.getSum());
    }

    private boolean equalObjects(Object s1, Object s2) {
        return s1 != null && s1.equals(s2);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }
        Player p = (Player) o;
        return equalObjects(this.name, p.name) && equalObjects(this.location, p.location) && this.wins == p.wins && this.loss == p.loss && this.score == p.score;
    }

    public Tile forcePlay(Hand hand) {
        //Tile res = this.moveStrategy.move(this, hand);
        Play play = this.moveStrategy.move(this,hand);
        Tile res = play.getTile();
        if (res == Tile.PASS)
            res.setPlayer(this);
        if(hand.getTilesOnTable().size() > 0) {
            if (res == Tile.PASS) {
                if (!myPasses.contains(hand.getLeftCorner()))
                    myPasses.add(hand.getLeftCorner());
                if (!myPasses.contains(hand.getRightCorner()))
                    myPasses.add(hand.getRightCorner());
            } else {
                int poss_pass;
                if (weStarted) {
                    poss_pass = posiblePass(play, hand);
                    if (poss_pass != -1) {
                        myPosiblePasses.add(poss_pass);
                    }
                }
                if (!bothSidesMeOrPartner(hand.getLeftCornerTile(), hand.getRightCornerTile())) {
                    if (hand.getLeftCorner() != hand.getRightCorner()) {
                        if (hand.getRightCornerTile().getPlayer() == this || hand.getRightCornerTile().getPlayer() == partner) {
                            if (hand.getRightCorner() == play.getSide())
                                myPosiblePasses.add(hand.getLeftCorner());
                        } else if (hand.getLeftCornerTile().getPlayer() == this || hand.getLeftCornerTile().getPlayer() == partner)
                            if (hand.getLeftCorner() == play.getSide())
                                myPosiblePasses.add(hand.getRightCorner());
                    }
                }
                if (myPosiblePasses.contains(play.getSide()))
                    myPosiblePasses.remove((Integer) play.getSide());
                if (res.getLeft() == play.getSide()) {
                    addToHash(myPlays, (short) res.getRight());
                } else
                    addToHash(myPlays, (short) res.getLeft());
            }
        }
        return res;
    }

    private boolean bothSidesMeOrPartner(Tile tile1, Tile tile2){
        return  (tile1.getPlayer() == this && tile2.getPlayer() == this) ||
                (tile1.getPlayer() == this && tile2.getPlayer() == partner)||
                (tile1.getPlayer() == partner && tile2.getPlayer() == this) ||
                (tile1.getPlayer() == partner && tile2.getPlayer() == partner);
    }

    private boolean sameBothSides(Tile first, Hand hand){
        return (first.getLeft() == hand.getLeftCorner() && first.getRight() == hand.getRightCorner())
                || (first.getLeft() == hand.getRightCorner() && first.getRight() == hand.getLeftCorner())
                || (first.getRight() == hand.getLeftCorner() && first.getLeft() == hand.getRightCorner())
                || (first.getRight() == hand.getRightCorner() && first.getLeft() == hand.getLeftCorner());
    }

    private int posiblePass(Play play, Hand hand) {
         Tile first = hand.getFirstTile();
         if ( sameBothSides(first,hand)  )
            return -1;
         else if (play.getSide() == hand.getLeftCorner()){
             return hand.getRightCorner();
         }
         else if (play.getSide() == hand.getRightCorner())
             return hand.getLeftCorner();
         return -1;
    }

    public Tile getBiggestAccompaniedDouble() {
        Tile biggest = null;
        int i = 9;
        while (true) {
            if (i < 0) {
                break;
            }
            Tile startTile = getTile(Tile.DOUBLE[i]);
            if (startTile != null && howManyOf(i, false) > 0) {
                biggest = startTile;
                break;
            }
            i--;
        }
        Log.d(TAG, "Player.biggestAccompaniedDouble " + biggest);
        return biggest;
    }

    public Tile getHighestTile(boolean onlyDouble) {
        int max = -1;
        Tile maxTile = null;
        for (int i = 0; i < getTiles().size(); i++) {
            Tile t = getTiles().get(i);
            if ((!onlyDouble || t.isDouble()) && t.getSum() > max) {
                max = t.getSum();
                maxTile = t;
            }
        }
        return maxTile;
    }

    public Tile getHighestTile(int number) {
        Tile t = null;
        List<Tile> tiles2 = getTiles();
        for (int i = tiles2.size() - 1; i >= 0; i--) {
            Tile next = tiles2.get(i);
            if (next.isA(number) && next.getSum() >= 0) {
                t = next;
            }
        }
        return t;
    }

    public Tile pickDoubleStartCandidate() {
        int countPieceOcurrence;
        ListIterator<Tile> listIterator = this.tiles.listIterator();
        int i = -1;
        Tile piece = null;
        while (listIterator.hasNext()) {
            Tile next = listIterator.next();
            if (next != null && next.isDouble() && (countPieceOcurrence = countPieceOcurrence((short) next.getLeft())) > i) {
                piece = next;
                i = countPieceOcurrence;
            }
        }
        if (i > 2) {
            return piece;
        }
        return null;
    }

    public int countPieceOcurrence(short s) {
        ListIterator<Tile> listIterator = this.tiles.listIterator();
        int i = 0;
        while (listIterator.hasNext()) {
            Tile next = listIterator.next();
            if (next != null && (next.getLeft() == s || next.getRight() == s)) {
                i++;
            }
        }
        return i;
    }

    public int getMostAccompaniedNumber() {
        int number = -1;
        int count = 0;
        for (int i = 6; i >= 0; i--) {
            int tmpCount = howManyOf(i, true);
            if (tmpCount >= count) {
                count = tmpCount;
                number = i;
            }
        }
        Log.d(TAG, "Player.getMostAccompaniedNumber " + getTiles());
        Log.d(TAG, "Player.getMostAccompaniedNumber " + number);
        return number;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public int getSum() {
        return this.sum;
    }

    public Tile getTile(Tile tile) {
        for (int i = this.tiles.size() - 1; i >= 0; i--) {
            Object tmp = this.tiles.get(i);
            if (tmp.equals(tile)) {
                return (Tile) tmp;
            }
        }
        return null;
    }

    public List<Tile> getTiles() {
        return this.tiles;
    }

    public List<Tile> getTiles(int number) {
        List<Tile> tiles2 = new ArrayList<>();
        List<Tile> myTiles = getTiles();
        for (int i = myTiles.size() - 1; i >= 0; i--) {
            Tile t = myTiles.get(i);
            if (t.getLeft() == number || t.getRight() == number) {
                tiles2.add(t);
            }
        }
        Log.d(TAG, "Player.getTiles " + number + "=" + tiles2);
        return tiles2;
    }

    public boolean hasDoubleSix() {
        for (int i = this.tiles.size() - 1; i >= 0; i--) {
            if (this.tiles.get(i).equals(Tile.DOUBLE[6])) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (this.wins ^ this.loss) ^ this.score;
    }

    public int howManyDoubles() {
        int count = 0;
        List<Tile> tiles2 = getTiles();
        for (int i = tiles2.size() - 1; i >= 0; i--) {
            if (tiles2.get(i).isDouble()) {
                count++;
            }
        }
        Log.d(TAG, "Player.howManyDoubles count=" + count);
        return count;
    }

    public int howManyOf(int number, boolean countDouble) {
        int count = 0;
        List<Tile> tiles2 = getTiles();
        for (int i = tiles2.size() - 1; i >= 0; i--) {
            Tile t = tiles2.get(i);
            if (t.isA(number)) {
                if (!t.isDouble()) {
                    count++;
                } else if (countDouble) {
                    count++;
                }
            }
        }
        Log.d(TAG, "Player.howManyOf " + number + " double?=" + countDouble + " count=" + count);
        return count;
    }

    public boolean pass(Hand hand) {
        int rightCorner = hand.getRightCorner();
        int leftCorner = hand.getLeftCorner();
        if (rightCorner == leftCorner && rightCorner == Hand.Corner.NONE.value) {
            return false;
        }
        List<Tile> tiles2 = getTiles();
        int upCorner = hand.getUpCorner();
        int downCorner = hand.getDownCorner();
        for (int i = tiles2.size() - 1; i >= 0; i--) {
            Tile tile = tiles2.get(i);
            int side0 = tile.sides[0];
            int side1 = tile.sides[1];
            if (side0 == leftCorner || side1 == leftCorner || side0 == rightCorner || side1 == rightCorner || (hand.getSpinner() != null && (side0 == upCorner || side1 == upCorner || side0 == downCorner || side1 == downCorner))) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        this.tiles.removeAll(this.tiles);
        this.myPosiblePasses.clear();
        this.myPasses.clear();
        this.myPlays.clear();
        weStarted = false;
        this.sum = 0;
    }

    public void setLocation(String location2) {
        if (location2 == null || NULL.equals(location2)) {
            location2 = "";
        }
        this.location = location2;
    }

    public void setLoss(int loss2) {
        this.loss = loss2;
    }

    public void setMuggins(boolean muggins) {
        /*if (muggins) {
            this.moveStrategy = new GreedyMoveMuggins();
        } else {
            this.moveStrategy = new RuleBasedMove();
        }*/
    }

    public void setName(String name2) {
        if (name2 == null || NULL.equals(name2)) {
            name2 = "";
        }
        this.name = name2;
    }

    public void setScore(int score2) {
        this.score = score2;
    }

    public void setTiles(ArrayList<Tile> newTiles) {
        this.tiles = new ArrayList<>();
        this.sum = 0;
        int size = newTiles.size();
        for (int i = 0; i < size; i++) {
            Tile tile = newTiles.get(i);
            add(tile);
            tile.setPlayer(this);
        }
    }

    public void setWins(int wins2) {
        this.wins = wins2;
    }

    public void substract(Tile f) {
        this.tiles.remove(f);
        add(-f.getSum());
    }

    public String toString() {
        return String.valueOf(this.name) + "|" + String.valueOf(this.human);
    }

    public boolean isHuman() {
        return human;
    }

    public HashMap<Short, Integer> getMyPlays() {
        return myPlays;
    }

    public Tile pickAnyStartCandidate() {
        ListIterator<Tile> listIterator = this.tiles.listIterator();
        int i = -1;
        Tile piece = null;
        while (listIterator.hasNext()) {
            Tile next = listIterator.next();
            if (next != null) {
                int countPieceOcurrence = countPieceOcurrence((short) next.getLeft());
                if (next.getLeft() != next.getRight()) {
                    countPieceOcurrence += countPieceOcurrence((short) next.getRight());
                }
                if (countPieceOcurrence > i) {
                    piece = next;
                    i = countPieceOcurrence;
                }
            }
        }
        return piece;
    }

    public Play getBestPossiblePlay(Hand hand) {
        Iterator<Tile> it = getAllPossibleTiles(hand).iterator();
        int i = -500000;
        Play res = null;
        while (it.hasNext()){
            Tile next = it.next();
            Play play = new Play(next,-1);
            int validatePlay = validatePlay(play,hand);
            if (validatePlay > i) {
                res = play;
                i = validatePlay;
            }
        }
        return res;
    }

    private int validatePlay(Play next, Hand hand) {
        int i = 0;
        Play aux = getNewHeads(next.getTile(),hand);
        Tile newHeads = aux.getTile();
        next.setSide(aux.getSide());
        if (this.firstPlay != null){
            int i2 = (newHeads.getLeft() == this.firstPlay.getLeft() || newHeads.getLeft() == this.firstPlay.getRight()) ? 300 : 0;
            if (newHeads.getRight() == this.firstPlay.getLeft() || newHeads.getRight() == this.firstPlay.getRight()) {
                i2 += 300;
            }
            i = this.weStarted ? i2 + 0 : 0 - i2;
        }
        if (this.rightOponent.getMyPasses().contains(newHeads.getLeft())) {
            i += 500;
        }
        if (this.rightOponent.getMyPasses().contains(newHeads.getRight())) {
            i += 500;
        }
        if (this.partner.getMyPasses().contains(newHeads.getLeft())) {
            i -= 400;
        }
        if (this.partner.getMyPasses().contains(newHeads.getRight())) {
            i -= 400;
        }
        if (this.leftOponent.getMyPasses().contains(newHeads.getLeft())) {
            i += 350;
        }
        if (this.leftOponent.getMyPasses().contains(newHeads.getRight())) {
            i += 350;
        }
        if (this.rightOponent.getMyPosiblePasses().contains(newHeads.getLeft())) {
            i += 250;
        }
        if (this.getMyPosiblePasses().contains(newHeads.getRight())) {
            i += 250;
        }
        if (this.partner.getMyPosiblePasses().contains(newHeads.getLeft())) {
            i -= 200;
        }
        if (this.partner.getMyPosiblePasses().contains(newHeads.getRight())) {
            i -= 200;
        }
        if (this.leftOponent.getMyPosiblePasses().contains(newHeads.getLeft())) {
            i += 150;
        }
        if (this.leftOponent.getMyPosiblePasses().contains(newHeads.getRight())) {
            i += 150;
        }
        if (isBlocked((short) newHeads.getLeft(),(short) newHeads.getRight())) {
            i = isItOKToBlock() ? i + 5000 : i - 5000;
        }
        int countMyPiecesMatching = i + (countMyPiecesMatching(newHeads.getLeft(), newHeads.getRight()) * 10);
        int timesPlayed = countMyPiecesMatching - (getTimesPlayed(this.leftOponent.getMyPlays(), (short) newHeads.getLeft()) * 3);
        int timesPlayed2 = timesPlayed - (getTimesPlayed(this.rightOponent.getMyPlays(), (short) newHeads.getLeft()) * 2);
        int timesPlayed3 = timesPlayed2 + (getTimesPlayed(this.partner.getMyPlays(), (short) newHeads.getLeft()) * 3);
        int timesPlayed4 = timesPlayed3 - (getTimesPlayed(this.leftOponent.getMyPlays(), (short) newHeads.getRight()) * 3);
        int timesPlayed5 = timesPlayed4 - (getTimesPlayed(this.rightOponent.getMyPlays(), (short) newHeads.getRight()) * 2);
        int timesPlayed6 = timesPlayed5 + (getTimesPlayed(this.partner.getMyPlays(), (short) newHeads.getRight()) * 3);
        int sum = timesPlayed6 + next.getTile().getSum();
        return sum;
    }

    private int getTimesPlayed(HashMap<Short, Integer> hashMap, short s) {
        if (hashMap.containsKey(Short.valueOf(s))) {
            return hashMap.get(Short.valueOf(s)).intValue();
        }
        return 0;
    }

    private void addToHash(HashMap<Short, Integer> hashMap, short s) {
        hashMap.put(Short.valueOf(s), Integer.valueOf((hashMap.containsKey(Short.valueOf(s)) ? hashMap.get(Short.valueOf(s)).intValue() : 0) + 1));
    }

    private boolean isItOKToBlock() {
        float f = 0.0f;
        float piecesCount = (float) (rightOponent.getTiles().size() * 2);
        f = (float) (leftOponent.getTiles().size() * 2);
        float piecesCount2 = (float) (partner.getTiles().size() * 4);
        float piecesSum = (float) getSum();
        if (piecesSum >= piecesCount || piecesSum >= f) {
            return piecesCount2 < f && piecesCount2 < piecesCount;
        }
        return true;
    }

    private boolean isBlocked(short s, short s2) {
        return this.rightOponent.getMyPasses().contains(Short.valueOf(s)) && this.rightOponent.getMyPasses().contains(Short.valueOf(s2)) && this.leftOponent.getMyPasses().contains(Short.valueOf(s)) && this.leftOponent.getMyPasses().contains(Short.valueOf(s2)) && this.partner.getMyPasses().contains(Short.valueOf(s)) && this.partner.getMyPasses().contains(Short.valueOf(s2)) && !hasPiece(s) && !hasPiece(s2);
    }

    public final boolean hasPiece(short s) {
        ListIterator<Tile> listIterator = this.tiles.listIterator();
        while (listIterator.hasNext()) {
            Tile next = listIterator.next();
            if (next != null && (next.getLeft() == s || next.getRight() == s)) {
                return true;
            }
        }
        return false;
    }

    private int countMyPiecesMatching(int left, int right) {
        return 0;
    }

    private Play getNewHeads(Tile next, Hand hand) {
        if (next.getLeft() == hand.getLeftCorner())
            return new Play( new Tile(next.getRight(),hand.rightCorner) , hand.getLeftCorner());
        else if (next.getLeft() == hand.getRightCorner())
            return new Play(  new Tile(hand.getLeftCorner(),next.getRight()), hand.getRightCorner() );
        else if (next.getRight() == hand.getLeftCorner())
            return new Play(  new Tile(next.getLeft(),hand.getRightCorner()), hand.getLeftCorner() );
        else
            return new Play(  new Tile(hand.getLeftCorner(),next.getLeft()) , hand.getRightCorner() );
    }


    private Iterable<Tile> getAllPossibleTiles(Hand hand) {
        short leftHead = (short) hand.leftCorner;
        short rightHead = (short) hand.rightCorner;
        ArrayList<Tile> arrayList = new ArrayList<>();
        ListIterator<Tile> pieces = getTiles().listIterator();
        while (pieces.hasNext()) {
            Tile next = pieces.next();
            if (next != null) {
                if (next.getLeft() == leftHead || next.getRight() == leftHead) {
                    arrayList.add(next);
                }
                if (leftHead != rightHead && (next.getLeft() == rightHead || next.getRight() == rightHead)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }
}
