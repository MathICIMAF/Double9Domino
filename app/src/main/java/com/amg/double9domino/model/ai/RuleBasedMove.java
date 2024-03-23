package com.amg.double9domino.model.ai;

import android.util.Log;
import com.amg.double9domino.model.Game;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class RuleBasedMove implements MoveStrategy {
    private static final String TAG = RuleBasedMove.class.getSimpleName();

    /* access modifiers changed from: private */
    public class BestAlternative {
        boolean block;
        Tile tile;
        boolean winBlock;

        private BestAlternative() {
        }

        /* synthetic */ BestAlternative(RuleBasedMove ruleBasedMove, BestAlternative bestAlternative) {
            this();
        }
    }

    private BestAlternative bestPlayFor(Player player, Hand hand, int number) {
        Log.d(TAG, "RuleBasedMove.bestPlayFor BEGIN " + number);
        BestAlternative bestAlternative = new BestAlternative(this, null);
        bestAlternative.tile = player.getTile(new Tile(number, number));
        int opening = number;
        if (bestAlternative.tile == null) {
            List<Tile> tiles = player.getTiles(number);
            int[] options = getOptions(tiles, number);
            int max = 0;
            int which = 0;
            int option = 0;
            boolean block = true;
            boolean winBlock = false;
            for (int i = options.length - 1; i >= 0; i--) {
                int maxTmp = player.howManyOf(options[i], true);
                Tile tile = tiles.get(i);
                boolean blockTmp = willBlockIfPlayed(hand, tile, options[i]);
                boolean winBlockTmp = blockTmp && willWinBlock(hand, tile);
                if ((blockTmp && winBlockTmp) || (!blockTmp && maxTmp >= max && options[i] >= option)) {
                    max = maxTmp;
                    option = options[i];
                    which = i;
                    block = blockTmp;
                    winBlock = winBlockTmp;
                    if (winBlock) {
                        break;
                    }
                }
            }
            bestAlternative.tile = tiles.get(which);
            bestAlternative.block = block;
            bestAlternative.winBlock = winBlock;
            opening = option;
        }
        if (player.howManyOf(opening, true) > 1) {
            bestAlternative.tile.setThought(true);
        }
        if (number == hand.getLeftCorner()) {
            bestAlternative.tile.setHalf(Hand.Corner.LEFT);
        } else {
            bestAlternative.tile.setHalf(Hand.Corner.RIGHT);
        }
        Log.d(TAG, "RuleBasedMove.bestPlayFor END " + bestAlternative.tile);
        return bestAlternative;
    }

    private Tile bestPlayFor(Player player, Hand hand, int planA, int planB) {
        Log.d(TAG, String.format("RuleBasedMove.bestPlayFor BEGIN %s %d", player.getName(), Integer.valueOf(planA)));
        Log.d(TAG, String.format("RuleBasedMove.bestPlayFor %s", player.getTiles()));
        Tile t = null;
        if (player.howManyOf(planA, true) > 0) {
            BestAlternative best = bestPlayFor(player, hand, planA);
            if (!best.block || best.winBlock) {
                t = best.tile;
                if (!best.block && !t.isDouble() && planA != planB) {
                    Log.d(TAG, "RuleBasedMove.bestPlayFor double?");
                    Tile tmp = player.getTile(Tile.DOUBLE[planB]);
                    if (tmp != null) {
                        Log.d(TAG, "RuleBasedMove.bestPlayFor found " + tmp);
                        t = tmp;
                    }
                }
            } else if (player.howManyOf(planB, true) > 0) {
                t = bestPlayFor(player, hand, planB).tile;
            }
        } else if (player.howManyOf(planB, true) > 0) {
            t = bestPlayFor(player, hand, planB).tile;
        }
        Log.d(TAG, "RuleBasedMove.bestPlayFor END " + t);
        return t;
    }

    private int[] getOptions(List<Tile> tiles, int number) {
        int[] options = new int[tiles.size()];
        for (int i = options.length - 1; i >= 0; i--) {
            Tile t = tiles.get(i);
            if (number == t.getLeft()) {
                options[i] = t.getRight();
            } else {
                options[i] = t.getLeft();
            }
        }
        return options;
    }

    @Override // com.lagunex.domino.model.ai.MoveStrategy
    public Tile move(Player me, Hand hand) {
        Player playerToBlock;
        int planA;
        int planB;
        Player playerToBlock2;
        int which;
        Log.i(TAG, "RuleBasedMove.move BEGIN");
        Tile tile = Tile.getPassTile(me);
        int rightCorner = hand.getRightCorner();
        int leftCorner = hand.getLeftCorner();
        if (rightCorner == leftCorner && rightCorner == -1) {
            tile.setHalf(Hand.Corner.LEFT);
            if (!hand.isFirstHand() || hand.getFirstStart() != Game.FirstPlay.DOUBLE_SIX) {
                tile = me.getBiggestAccompaniedDouble();
                if (tile == null) {
                    which = me.getMostAccompaniedNumber();
                    tile = me.getHighestTile(which);
                } else {
                    which = tile.getLeft();
                }
                if (me.howManyOf(which, true) > 1) {
                    tile.setThought(true);
                }
            } else {
                tile = me.getTile(Tile.DOUBLE[6]);
                if (me.howManyOf(6, false) > 0) {
                    tile.setThought(true);
                }
            }
        } else {
            Player teammate = hand.getTeammate(me);
            Player playerLeftCornerTile = hand.getLeftCornerTile().getPlayer();
            boolean leftCornerOpponent = !teammate.equals(playerLeftCornerTile) && !me.equals(playerLeftCornerTile);
            Log.d(TAG, "RuleBasedMove.move leftCornerOpponent " + leftCornerOpponent);
            Player playerRightCornerTile = hand.getRightCornerTile().getPlayer();
            boolean rightCornerOpponent = !teammate.equals(playerRightCornerTile) && !me.equals(playerRightCornerTile);
            Log.d(TAG, "RuleBasedMove.move rightCornerOpponent " + rightCornerOpponent);
            List<List<Tile>> remainingTiles = hand.getRemainingTitles(me);
            if (leftCornerOpponent && rightCornerOpponent) {
                if (remainingTiles.get(3).size() < remainingTiles.get(1).size()) {
                    playerToBlock2 = remainingTiles.get(3).get(0).getPlayer();
                } else {
                    playerToBlock2 = remainingTiles.get(1).get(0).getPlayer();
                }
                Log.d(TAG, "RuleBasedMove.move opponent with least tiles " + playerToBlock2.getName());
                if (playerLeftCornerTile.equals(playerToBlock2)) {
                    Log.d(TAG, "RuleBasedMove.move he played left");
                    planA = leftCorner;
                    planB = rightCorner;
                } else {
                    Log.d(TAG, "RuleBasedMove.move he played right");
                    planA = rightCorner;
                    planB = leftCorner;
                }
            } else if (leftCornerOpponent) {
                planA = leftCorner;
                planB = rightCorner;
            } else if (rightCornerOpponent) {
                planA = rightCorner;
                planB = leftCorner;
            } else {
                if (remainingTiles.get(0).size() > remainingTiles.get(2).size()) {
                    playerToBlock = remainingTiles.get(0).get(0).getPlayer();
                } else {
                    playerToBlock = remainingTiles.get(2).get(0).getPlayer();
                }
                Log.d(TAG, "RuleBasedMove.move team-mate with least tiles " + playerToBlock.getName());
                if (playerLeftCornerTile.equals(playerToBlock)) {
                    Log.d(TAG, "RuleBasedMove.move he played left");
                    planA = leftCorner;
                    planB = rightCorner;
                } else {
                    Log.d(TAG, "RuleBasedMove.move he played right");
                    planA = rightCorner;
                    planB = leftCorner;
                }
            }
            Tile possibleMove = bestPlayFor(me, hand, planA, planB);
            if (possibleMove != null) {
                tile = possibleMove;
            }
        }
        Log.i(TAG, "RuleBasedMove.move END " + tile);
        return tile;
    }

    private boolean willBlockIfPlayed(Hand hand, Tile tile, int number) {
        return hand.howMany(number) == 6 && tile.getPlayer().getTiles().size() > 1;
    }

    private boolean willWinBlock(Hand hand, Tile tile) {
        Player player = tile.getPlayer();
        List<List<Tile>> remainingTiles = hand.getRemainingTitles(player);
        List<Tile> othersTiles = new ArrayList<>();
        for (int i = 1; i < remainingTiles.size(); i++) {
            othersTiles.addAll(remainingTiles.get(i));
        }
        int mySum = player.getSum() - tile.getSum();
        Collections.sort(othersTiles, new Comparator<Tile>() {
            /* class com.lagunex.domino.model.ai.RuleBasedMove.AnonymousClass1 */

            public int compare(Tile lhs, Tile rhs) {
                return lhs.getSum() - rhs.getSum();
            }
        });
        int worstCaseTeammate = 0;
        for (int i2 = 0; i2 < remainingTiles.get(2).size(); i2++) {
            worstCaseTeammate += othersTiles.get((othersTiles.size() - 1) - i2).getSum();
        }
        if (hand.getGame().getBlockCount() == Game.Block.TEAM) {
            int worstTeam = mySum + worstCaseTeammate;
            if (worstTeam < (168 - hand.getSumTilesPlayed()) - worstTeam) {
                return true;
            }
            return false;
        }
        int minIndividual = Math.min(mySum, worstCaseTeammate);
        int bestCaseOpponent1 = 0;
        int bestCaseOpponent2 = 0;
        int nextBest = 0;
        int i3 = 0;
        while (i3 < remainingTiles.get(1).size()) {
            bestCaseOpponent1 += othersTiles.get(nextBest).getSum();
            i3++;
            nextBest++;
        }
        int i4 = 0;
        while (i4 < remainingTiles.get(3).size()) {
            bestCaseOpponent2 += othersTiles.get(nextBest).getSum();
            i4++;
            nextBest++;
        }
        if (minIndividual >= bestCaseOpponent1 || minIndividual >= bestCaseOpponent2) {
            return false;
        }
        return true;
    }
}
