package com.amg.double9domino.widget.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.amg.double9domino.widget.drawing.DrawingTileStrategy;

public class PNGMugginsStrategy extends PNGStrategy {
    protected PNGMugginsStrategy(Context context) {
        super(context);
    }

    public static DrawingTileStrategy getInstance(Context context) {
        if (instance == null || !(instance instanceof PNGMugginsStrategy)) {
            instance = new PNGMugginsStrategy(context);
            instance.initInstance();
        }
        return instance;
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy, com.lagunex.domino.widget.drawing.PNGStrategy
    public int[] drawTile(Canvas g, int centerX, int centerY, int TILE_LONG, int TILE_SHORT, boolean horizontal, int value0, int value1, Highlight highlight) {
        int[] dimen = drawTile(g, centerX, centerY, TILE_LONG, TILE_SHORT, horizontal, value0, value1);
        if (highlight != Highlight.NONE) {
            drawHighlight(g, horizontal, highlight, dimen);
        }
        return dimen;
    }

    private void drawHighlight(Canvas g, boolean horizontal, Highlight highlight, int[] dimen) {
        int left;
        int top;
        int width;
        int height;
        Paint p = new Paint();
        p.setColor(-256);
        p.setAlpha(100);
        int shortSide = Math.min(dimen[2], dimen[3]);
        if (highlight == Highlight.BOTH) {
            left = dimen[0];
            top = dimen[1];
            width = dimen[2];
            height = dimen[3];
        } else if (horizontal) {
            int i = dimen[0];
            if (highlight == Highlight.LEFT) {
                shortSide = 0;
            }
            left = i + shortSide;
            top = dimen[1];
            width = dimen[2] / 2;
            height = dimen[3];
        } else {
            left = dimen[0];
            int i2 = dimen[1];
            if (highlight == Highlight.LEFT) {
                shortSide = 0;
            }
            top = i2 + shortSide;
            width = dimen[2];
            height = dimen[3] / 2;
        }
        g.drawRect((float) (left - 3), (float) (top - 3), (float) (left + width + 3), (float) (top + height + 3), p);
    }
}
