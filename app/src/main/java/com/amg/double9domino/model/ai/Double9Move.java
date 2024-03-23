package com.amg.double9domino.model.ai;

import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Play;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;

public class Double9Move {

    public Play move(Player player, Hand hand) {
        Play res = null;
        if (hand.getTilesOnTable().size() == 0){
            Tile pickDoubleStartCandidate = player.pickDoubleStartCandidate();
            if (pickDoubleStartCandidate == null) {
                pickDoubleStartCandidate = player.pickAnyStartCandidate();
            }
            if (pickDoubleStartCandidate != null) {
                player.weStarted = true;
                player.firstPlay = pickDoubleStartCandidate;
                return new Play(pickDoubleStartCandidate,-1);
                //this.firstPlayerPosition = this.gameData.getPlayerPosition(this);
                //play = new Play(pickDoubleStartCandidate, Table.Side.RIGHT);
            }
        }
        else {
            res = player.getBestPossiblePlay(hand);
        }

        return (res == null) ? new Play(Tile.PASS,-1): res;
    }


}
