package com.amg.double9domino.widget.drawing;

import android.content.Context;

public final class DrawingFactory {
    private static DrawingFactory instance = new DrawingFactory();

    public enum Mode {
        BLOCK,
        MUGGINS
    }

    public static DrawingFactory getInstance() {
        return instance;
    }

    private DrawingFactory() {
    }

    public DrawingTileStrategy getDrawingTileStrategy(Context context) {
        return getDrawingTileStrategy(context, Mode.BLOCK);
    }

    public DrawingTileStrategy getDrawingTileStrategy(Context context, Mode mode) {
        if (mode == Mode.BLOCK) {
            return PNGStrategy.getInstance(context);
        }
        return PNGMugginsStrategy.getInstance(context);
    }
}
