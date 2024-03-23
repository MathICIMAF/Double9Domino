package com.amg.double9domino.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentActivity;

import com.amg.double9domino.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class Game {
    public static final int DEFAULT_SCORE_LIMIT = 100;
    public static final int GAME_CLASSIC = 0;
    public static final int[] SCORE_LIMITS = {100, 200};
    Block blockCount = Block.TEAM;
    int firstPlayer = 0;
    boolean human_start,patner_wants;
    FirstPlay firstStart = FirstPlay.RANDOM;
    protected int gameType;
    Activity context;
    ArrayList<Hand> hands = new ArrayList<>();
    NextPlay nextStart = NextPlay.NEXT;
    private Player[] playersInArray;
    Count scoreCount = Count.TEAM;
    int scoreLimit;
    Team team1;
    Team team2;
    Think timeThinking = Think.THINK;
    int total1 = 0;
    int total2 = 0;
    Hand newHand;
    Team start_team;
    private boolean firstHandDouble,isFirstHand;

    public enum Block {
        TEAM,
        SOLO
    }

    public enum Count {
        TEAM,
        ALL,
        MUGGINS_ROUND,
        MUGGINS_DIVIDED
    }

    public enum FirstPlay {
        DOUBLE_SIX,
        RANDOM,
        MUGGINS_HIGHEST_DOUBLE,
        MUGGINS_HIGHEST_TILE
    }

    public enum NextPlay {
        NEXT,
        WINNER,
        FIRST_PLAY
    }

    public enum Think {
        THINK,
        SHORT,
        LONG
    }

    public Game(FragmentActivity activity){
        isFirstHand = true;
        this.context = activity;
    }

    /* access modifiers changed from: protected */
    public Hand createHand() {
        return new Hand(this);
    }

    public Block getBlockCount() {
        return this.blockCount;
    }

    public boolean isHuman_start() {
        return human_start;
    }

    public boolean isPatner_wants(){
        return patner_wants;
    }

    public void selectedStart(boolean human){
        if (start_team.player1.isHuman() && human){
            this.firstPlayer = getIndex(start_team.player1);
        }
        else if (start_team.player1.isHuman() && !human)
            this.firstPlayer = getIndex(start_team.player2);
        else if (!start_team.player1.isHuman() && !human)
            this.firstPlayer = getIndex(start_team.player1);
        else
            this.firstPlayer = getIndex(start_team.player2);
        getCurrentHand().setTurn(firstPlayer);
    }

    public Hand getCurrentHand() {
        return this.hands.get(this.hands.size() - 1);
    }

    public int getFirstPlayer() {
        return this.firstPlayer;
    }

    public FirstPlay getFirstStart() {
        return this.firstStart;
    }

    public int getGameType() {
        return this.gameType;
    }

    public Hand getHand(int i) {
        return this.hands.get(i);
    }

    public int getHandsCount() {
        if (this.hands != null) {
            return this.hands.size();
        }
        return 0;
    }


    public  void  playersInArray() {
        if (this.playersInArray == null) {
            this.playersInArray = new Player[]{this.team1.player1, this.team2.player1, this.team1.player2, this.team2.player2};
        }
    }

    public Count getScoreCount() {
        return this.scoreCount;
    }

    public int getScoreLimit() {
        return this.scoreLimit;
    }

    public Team getTeam1() {
        return this.team1;
    }

    public Team getTeam2() {
        return this.team2;
    }

    public Think getTimeThinking() {
        return this.timeThinking;
    }

    public int getTotal1() {
        return this.total1;
    }

    public int getTotal2() {
        return this.total2;
    }

    public boolean hasEnded() {
        return getTotal1() >= getScoreLimit() || getTotal2() >= getScoreLimit();
    }

    public boolean isGameInTheMiddle() {
        return !hasEnded() && (getHandsCount() > 1 || getCurrentHand().getPlayCount() >= 4);
    }

    public void setBlockCount(Block blockCount2) {
        this.blockCount = blockCount2;
    }

    public void setFirstPlayer() {
        setFirstPlayer(null);
    }

    public void setFirstPlayer(List<Tile> tilesShuffled) {

            int starter;
            playersInArray();
            if (getHandsCount() != 0) {
                Hand lastOne = getCurrentHand();

                Team winner = lastOne.getWinner();
                if (winner == null) {
                    if(team1.getPlayer1().equals(playersInArray[firstPlayer]) || team1.getPlayer2().equals(playersInArray[firstPlayer] )  ){
                        int rand = new Random().nextInt(2);
                        start_team = team2;
                        if (rand == 0)
                            starter = lastOne.getOrder(team2.getPlayer1());
                        else
                            starter = lastOne.getOrder(team2.getPlayer2());
                    }
                    else {
                        start_team = team1;
                        int rand = new Random().nextInt(2);
                        if (rand == 0)
                            starter = lastOne.getOrder(team1.getPlayer1());
                        else
                            starter = lastOne.getOrder(team1.getPlayer2());
                    }
                    human_start = !human_start;
                }
                else {
                    start_team = winner;
                    if (winner.player1.isHuman() || winner.player2.isHuman()) {
                        Player patner = (winner.player1.isHuman()) ? winner.player2 : winner.player1;
                        Player human = (winner.player1.isHuman()) ? winner.player1 : winner.player2;
                        human_start = true;
                        if (patner.pickDoubleStartCandidate() != null) {
                            patner_wants = true;
                        } else {
                            patner_wants = false;
                        }
                        starter = (human_start) ? getIndex(human) : getIndex(patner);
                    } else {
                        human_start = false;
                        if (winner.player1.pickDoubleStartCandidate() != null)
                            starter = getIndex(winner.player1);
                        else if (winner.player2.pickDoubleStartCandidate() != null)
                            starter = getIndex(winner.player2);
                        else {
                            int rand = new Random().nextInt(2);
                            starter = getIndex(rand == 0 ? winner.player1 : winner.player2);
                        }
                    }
                }

            } else {
                int team_no = new Random().nextInt(2);
                start_team = (team_no == 0)?team1:team2;
                if (start_team.player1.isHuman() || start_team.player2.isHuman()){
                    Player patner = (start_team.player1.isHuman())? start_team.player2: start_team.player1;
                    Player human = (start_team.player1.isHuman())? start_team.player1:start_team.player2;
                    human_start = true;
                    if (patner.pickDoubleStartCandidate() != null){
                        patner_wants = true;
                    }
                    else{
                        patner_wants = false;
                    }
                        //humanStarts(context.getString(R.string.no_start));
                    starter = (human_start)?getIndex(human):getIndex(patner);
                }
                else {
                    human_start = false;
                    if (start_team.player1.pickDoubleStartCandidate() != null )
                        starter = getIndex(start_team.player1);
                    else if (start_team.player2.pickDoubleStartCandidate() != null)
                        starter = getIndex(start_team.player2);
                    else {
                        int rand = new Random().nextInt(2);
                        starter = getIndex(rand == 0?start_team.player1:start_team.player2);
                    }
                }
            }
            Game.this.firstPlayer = starter;

    }

    public void setFirstStart(FirstPlay firstStart2) {
        this.firstStart = firstStart2;
    }

    public void setNextStart(NextPlay nextStart2) {
        this.nextStart = nextStart2;
    }

    public void setScoreCount(Count scoreCount2) {
        this.scoreCount = scoreCount2;
    }

    private int getIndex(Player player){
        for (int i = 0; i < playersInArray.length; i++){
            if (player.equals(playersInArray[i]))
                return i;
        }
        return -1;
    }

    public void setScoreLimit(int scoreLimit2) {
        this.scoreLimit = scoreLimit2;
    }

    public boolean getFirstHandDouble(){
        return firstHandDouble;
    }

    public void setFirstHandDouble(boolean value){
        this.firstHandDouble = value;
    }

    public void setTeam1(Team equipo1) {
        this.team1 = equipo1;
    }

    public void setTeam2(Team equipo2) {
        this.team2 = equipo2;
    }

    public void setTimeThinking(Think timeThinking2) {
        this.timeThinking = timeThinking2;
    }

    public void setTotal1(int i) {
        this.total1 = i;
    }

    public void setTotal2(int i) {
        this.total2 = i;
    }

    public void startNextHand(boolean newGame) {
        newHand = createHand();
        newHand.setFirstHand(newGame);
        List<Tile> tilesShuffled = newHand.shuffle();
        newHand.deal(tilesShuffled);
        setFirstPlayer(tilesShuffled);
        newHand.setTurn(this.firstPlayer);
        this.hands.add(newHand);
    }
}
