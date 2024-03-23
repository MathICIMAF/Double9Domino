package com.amg.double9domino.model.ai;

import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;
import java.util.List;

public final class DummyMove implements MoveStrategy {
    @Override // com.lagunex.domino.model.ai.MoveStrategy
    public Tile move(Player player, Hand hand) {
        Tile f = Tile.getPassTile(player);
        int rightCorner = hand.getRightCorner();
        int leftCorner = hand.getLeftCorner();
        List<Tile> tiles = player.getTiles();
        if (rightCorner == leftCorner && rightCorner == -1) {
            int pos = tiles.indexOf(Tile.DOUBLE[6]);
            if (pos != -1) {
                return tiles.get(pos);
            }
            return tiles.get(0);
        }
        int size = tiles.size();
        for (int i = 0; i < size; i++) {
            Tile nextTile = tiles.get(i);
            int left = nextTile.getLeft();
            int right = nextTile.getRight();
            if (left == rightCorner || right == rightCorner || left == leftCorner || right == leftCorner) {
                return nextTile;
            }
        }
        return f;
    }
}
