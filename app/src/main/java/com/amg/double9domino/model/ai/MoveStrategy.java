package com.amg.double9domino.model.ai;

import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;

public interface MoveStrategy {
    Tile move(Player player, Hand hand);
}
