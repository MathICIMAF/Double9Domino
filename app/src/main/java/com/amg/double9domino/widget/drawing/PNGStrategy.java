package com.amg.double9domino.widget.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.amg.double9domino.R;

public class PNGStrategy implements DrawingTileStrategy {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$lagunex$domino$widget$drawing$DrawingTileStrategy$Position;
    protected static PNGStrategy instance;
    private Context context;
    private int spriteHeight;
    private int spriteHeightStep;
    private int spriteWidth;
    private int spriteWidthStep;
    private Bitmap tilesBackHorizontal;
    private Bitmap tilesBackVertical;
    private Bitmap tilesHolder;
    private Bitmap tilesSprite;
    private int tilesStyle;

    static /* synthetic */ int[] $SWITCH_TABLE$com$lagunex$domino$widget$drawing$DrawingTileStrategy$Position() {
        int[] iArr = $SWITCH_TABLE$com$lagunex$domino$widget$drawing$DrawingTileStrategy$Position;
        if (iArr == null) {
            iArr = new int[Position.values().length];
            try {
                iArr[Position.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Position.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Position.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Position.UP.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$lagunex$domino$widget$drawing$DrawingTileStrategy$Position = iArr;
        }
        return iArr;
    }

    public static DrawingTileStrategy getInstance(Context context2) {
        if (instance == null) {
            instance = new PNGStrategy(context2);
            instance.initInstance();
        }
        return instance;
    }

    protected PNGStrategy(Context context2) {
        this.context = context2;
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy
    public int[] drawTile(Canvas g, int centerX, int centerY, int TILE_LONG, int TILE_SHORT, boolean horizontal, int value0, int value1) {
        int left;
        int top;
        int angle;
        if (horizontal) {
            left = centerX - (TILE_LONG >> 1);
            top = centerY - (TILE_SHORT >> 1);
        } else {
            left = centerX - (TILE_SHORT >> 1);
            top = centerY - (TILE_LONG >> 1);
        }
        Bitmap tile = getTile(value0, value1, TILE_SHORT, TILE_LONG);
        if (horizontal) {
            if (value0 > value1) {
                angle = 90;
            } else {
                angle = 270;
            }
        } else if (value0 < value1) {
            angle = 0;
        } else {
            angle = 180;
        }
        Matrix m = new Matrix();
        m.postRotate((float) angle);
        Bitmap finalTile = Bitmap.createBitmap(tile, 0, 0, tile.getWidth(), tile.getHeight(), m, true);
        g.drawBitmap(finalTile, (float) left, (float) top, (Paint) null);
        return new int[]{left, top, finalTile.getWidth(), finalTile.getHeight()};
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy
    public int[] drawTile(Canvas g, int centerX, int centerY, int TILE_LONG, int TILE_SHORT, boolean horizontal, int value0, int value1, Highlight highlight) {
        return drawTile(g, centerX, centerY, TILE_LONG, TILE_SHORT, horizontal, value0, value1);
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy
    public void drawTileBack(Canvas graphics, int left, int top, int width, int height, boolean horizontal) {
        Bitmap back = getTileBack(horizontal);
        if (width > 0 && height > 0) {
            back = Bitmap.createScaledBitmap(back, width, height, true);
        }
        graphics.drawBitmap(back, (float) left, (float) top, (Paint) null);
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy
    public void drawTileHolder(Canvas graphics, int left, int top, int width, int height, Position position) {
        Bitmap back = getTileHolder(position);
        if (width > 0 && height > 0) {
            back = Bitmap.createScaledBitmap(back, width, height, true);
        }
        graphics.drawBitmap(back, (float) left, (float) top, (Paint) null);
    }

    private Bitmap getTileHolder(Position position) {
        int angle = 0;
        switch ($SWITCH_TABLE$com$lagunex$domino$widget$drawing$DrawingTileStrategy$Position()[position.ordinal()]) {
            case 1:
                angle = 270;
                break;
            case 2:
                angle = 90;
                break;
        }
        Matrix m = new Matrix();
        m.postRotate((float) angle);
        return Bitmap.createBitmap(instance.tilesHolder, 0, 0, instance.tilesHolder.getWidth(), instance.tilesHolder.getHeight(), m, true);
    }

    private Bitmap getSprite() {
        /*if (this.tilesStyle != Options.getInstance().getTileStyle()) {

        }*/
        //updateSprites();
        return instance.tilesSprite;
    }

    @Override // com.lagunex.domino.widget.drawing.DrawingTileStrategy
    public Bitmap getTile(int value0, int value1, int shortSide, int longSide) {
        int min = Math.min(value0, value1);
        Bitmap tile = Bitmap.createBitmap(getSprite(), this.spriteWidthStep * Math.max(value0, value1), this.spriteHeightStep * min, this.spriteWidthStep, this.spriteHeightStep);
        if (shortSide <= 0 || longSide <= 0) {
            return tile;
        }
        return Bitmap.createScaledBitmap(tile, shortSide, longSide, true);
    }

    private Bitmap getTileBack(boolean horizontal) {
        /*if (Options.getInstance().getTileStyle() != instance.tilesStyle) {

        }*/
        //updateSprites();
        if (horizontal) {
            return this.tilesBackHorizontal;
        }
        return this.tilesBackVertical;
    }

    /* access modifiers changed from: protected */
    public void initInstance() {
        instance.tilesBackHorizontal = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile);
        instance.tilesHolder = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile_holder);
        instance.tilesSprite = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tiles9);
        instance.spriteWidth = instance.tilesSprite.getWidth();
        instance.spriteHeight = instance.tilesSprite.getHeight();
        instance.spriteWidthStep = instance.spriteWidth / 10;
        instance.spriteHeightStep = instance.spriteHeight / 10;
        initTileBackVertical();
    }

    private void initTileBackVertical() {
        Matrix m = new Matrix();
        m.postRotate(90.0f);
        instance.tilesBackVertical = Bitmap.createBitmap(instance.tilesBackHorizontal, 0, 0, instance.tilesBackHorizontal.getWidth(), instance.tilesBackHorizontal.getHeight(), m, true);
    }

    private void updateSprites() {
        this.tilesStyle = 0;//Options.getInstance().getTileStyle();
        switch (this.tilesStyle) {
            case 0:
                instance.tilesBackHorizontal = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile);
                instance.tilesSprite = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tiles9);
                break;
            case 1:
                instance.tilesBackHorizontal = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile);
                instance.tilesSprite = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tiles_color);
                break;
            case 2:
                instance.tilesBackHorizontal = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile_ivory);
                instance.tilesSprite = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tiles_ivory);
                break;
            case 3:
                instance.tilesBackHorizontal = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tile_black);
                instance.tilesSprite = BitmapFactory.decodeResource(instance.context.getResources(), R.drawable.tiles_black);
                break;
        }
        initTileBackVertical();
    }
}
