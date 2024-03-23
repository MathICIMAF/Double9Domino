package com.amg.double9domino.widget.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface DrawingTileStrategy {

    public enum Highlight {
        NONE,
        LEFT,
        RIGHT,
        BOTH
    }

    public enum Position {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    int[] drawTile(Canvas canvas, int i, int i2, int i3, int i4, boolean z, int i5, int i6);

    int[] drawTile(Canvas canvas, int i, int i2, int i3, int i4, boolean z, int i5, int i6, Highlight highlight);

    void drawTileBack(Canvas canvas, int i, int i2, int i3, int i4, boolean z);

    void drawTileHolder(Canvas canvas, int i, int i2, int i3, int i4, Position position);

    Bitmap getTile(int i, int i2, int i3, int i4);
}
