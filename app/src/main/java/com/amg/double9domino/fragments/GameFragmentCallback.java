package com.amg.double9domino.fragments;

import com.amg.double9domino.model.Game;
import com.amg.double9domino.util.Command;

public interface GameFragmentCallback {
    void onEndGameResult(Game game, int i, int i2, int i3, int i4);

    void onShareGameResult(boolean z, String str);

    void onStartNextHand(Command command);

    boolean sendScore();

    void setSendScore(boolean z);
}
