package com.amg.double9domino.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.internal.view.SupportMenu;

import com.amg.double9domino.R;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;
import com.amg.double9domino.widget.drawing.DrawingFactory;
import com.amg.double9domino.widget.drawing.DrawingTileStrategy;
import java.util.List;

public class Table extends View {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction;
    protected static int NONE = -1;
    protected final int HEIGHT;
    protected final int TABLE_BORDER;
    protected final int TILE_LONG_SIDE;
    protected final int TILE_SHORT_SIDE;
    protected final int TILE_THICKNESS;
    protected final int WIDTH;
    protected DrawingTileStrategy drawingTileStrategy;
    protected Hand hand;
    private Player me;
    private final int[] players;

    /* access modifiers changed from: protected */
    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    /* access modifiers changed from: protected */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction() {
        int[] iArr = $SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction;
        if (iArr == null) {
            iArr = new int[Direction.values().length];
            try {
                iArr[Direction.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Direction.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Direction.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Direction.UP.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction = iArr;
        }
        return iArr;
    }

    /* access modifiers changed from: private */
    public class TileMovement {
        Direction direction;
        int dx;
        int dy;
        Orientation orientation;

        TileMovement(Direction direction2, Orientation orientation2, int dx2, int dy2) {
            this.direction = direction2;
            this.orientation = orientation2;
            this.dx = dx2;
            this.dy = dy2;
        }
    }

    /* access modifiers changed from: private */
    public class TilePosition {
        Direction direction;
        Orientation orientation;
        int value;
        int value0;
        int value1;
        int x;
        int y;

        public TilePosition(Direction direction2, Orientation orientation2, int x2, int y2, int value2, int value02, int value12) {
            this.direction = direction2;
            this.x = x2;
            this.y = y2;
            this.orientation = orientation2;
            this.value = value2;
            this.value0 = value02;
            this.value1 = value12;
        }
    }

    public Table(Context context) {
        this(context, null);
    }

    public Table(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TILE_SHORT_SIDE = getResources().getDimensionPixelSize(R.dimen.tile_side_short);
        this.TILE_LONG_SIDE = getResources().getDimensionPixelSize(R.dimen.tile_side_long);
        this.TILE_THICKNESS = this.TILE_SHORT_SIDE >> 1;
        this.TABLE_BORDER = (this.TILE_SHORT_SIDE * 3) / 2;
        this.players = new int[]{-1, -16776961, SupportMenu.CATEGORY_MASK, -65281};
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.WIDTH = displaymetrics.widthPixels;
        this.HEIGHT = getHeight(displaymetrics.heightPixels);
        this.drawingTileStrategy = DrawingFactory.getInstance().getDrawingTileStrategy(context);
    }

    /* access modifiers changed from: protected */
    public int computeTableDownBorder(Direction half) {
        return half == Direction.RIGHT ? this.HEIGHT -20: this.HEIGHT / 2 -20;
    }

    /* access modifiers changed from: protected */
    public int computeTableLeftBorder(Direction half) {
        return this.TABLE_BORDER +20;
    }

    /* access modifiers changed from: protected */
    public int computeTableRightBorder(Direction half) {
        return this.WIDTH - this.TABLE_BORDER -20;
    }

    /* access modifiers changed from: protected */
    public int computeTableUpBorder(Direction half) {
        return half == Direction.LEFT ? this.TABLE_BORDER +20: this.HEIGHT / 2 + 20;
    }

    private TileMovement computeTileMovement(Tile tile, Direction dir, int lastX, int lastY, Orientation lastOrientation, Direction half) {
        boolean horizontal;
        int i = 0;
        if (tile.isDouble()) {
            horizontal = false;
        } else {
            horizontal = true;
        }
        if (dir == Direction.DOWN || dir == Direction.UP) {
            if (horizontal) {
                horizontal = false;
            } else {
                horizontal = true;
            }
        }
        int dx = 0;
        int dy = 0;
        switch ($SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction()[dir.ordinal()]) {
            case 1:
                int border = computeTableLeftBorder(half);
                if ((lastX - this.TILE_SHORT_SIDE) - this.TILE_LONG_SIDE <= border && (lastOrientation != Orientation.VERTICAL || (lastX - (this.TILE_SHORT_SIDE / 2)) - this.TILE_LONG_SIDE <= border)) {
                    if (lastX - this.TILE_LONG_SIDE <= border && (lastOrientation != Orientation.VERTICAL || lastX - ((this.TILE_SHORT_SIDE * 3) / 2) <= border)) {
                        dx = lastOrientation == Orientation.HORIZONTAL ? (-this.TILE_SHORT_SIDE) / 2 : 0;
                        dy = lastOrientation == Orientation.HORIZONTAL ? ((-this.TILE_SHORT_SIDE) * 3) / 2 : -this.TILE_LONG_SIDE;
                        horizontal = false;
                        dir = Direction.UP;
                        break;
                    } else {
                        if (lastOrientation == Orientation.HORIZONTAL) {
                            dx = ((-this.TILE_SHORT_SIDE) * 3) / 2;
                        } else {
                            dx = -this.TILE_SHORT_SIDE;
                        }
                        dy = (-this.TILE_SHORT_SIDE) / 2;
                        horizontal = false;
                        dir = Direction.UP;
                        break;
                    }
                } else {
                    int dx2 = lastOrientation == Orientation.HORIZONTAL ? -this.TILE_LONG_SIDE : ((-this.TILE_SHORT_SIDE) * 3) / 2;
                    if (!horizontal) {
                        i = this.TILE_SHORT_SIDE / 2;
                    }
                    dx = dx2 + i;
                    dy = 0;
                    break;
                }
            case 2:
                int border2 = computeTableRightBorder(half);
                if (this.TILE_SHORT_SIDE + lastX + this.TILE_LONG_SIDE >= border2 && (lastOrientation != Orientation.VERTICAL || (this.TILE_SHORT_SIDE / 2) + lastX + this.TILE_LONG_SIDE >= border2)) {
                    if (this.TILE_LONG_SIDE + lastX >= border2 && (lastOrientation != Orientation.VERTICAL || ((this.TILE_SHORT_SIDE * 3) / 2) + lastX >= border2)) {
                        dx = lastOrientation == Orientation.HORIZONTAL ? this.TILE_SHORT_SIDE / 2 : 0;
                        dy = lastOrientation == Orientation.HORIZONTAL ? (this.TILE_SHORT_SIDE * 3) / 2 : this.TILE_LONG_SIDE;
                        horizontal = false;
                        dir = Direction.DOWN;
                        break;
                    } else {
                        dx = lastOrientation == Orientation.HORIZONTAL ? (this.TILE_SHORT_SIDE * 3) / 2 : this.TILE_SHORT_SIDE;
                        dy = this.TILE_SHORT_SIDE / 2;
                        horizontal = false;
                        dir = Direction.DOWN;
                        break;
                    }
                } else {
                    int dx3 = lastOrientation == Orientation.HORIZONTAL ? this.TILE_LONG_SIDE : (this.TILE_SHORT_SIDE * 3) / 2;
                    if (!horizontal) {
                        i = (-this.TILE_SHORT_SIDE) / 2;
                    }
                    dx = dx3 + i;
                    dy = 0;
                    break;
                }
            case 3:
                int border3 = computeTableUpBorder(half);
                if ((lastY - this.TILE_SHORT_SIDE) - this.TILE_LONG_SIDE <= border3 && (lastOrientation != Orientation.HORIZONTAL || (lastY - (this.TILE_SHORT_SIDE / 2)) - this.TILE_LONG_SIDE <= border3)) {
                    if (lastY - this.TILE_LONG_SIDE <= border3 && (lastOrientation != Orientation.HORIZONTAL || lastY - ((this.TILE_SHORT_SIDE * 3) / 2) <= border3)) {
                        dy = (-this.TILE_SHORT_SIDE) / 2;
                        dx = lastOrientation == Orientation.HORIZONTAL ? this.TILE_LONG_SIDE / 2 : (this.TILE_SHORT_SIDE * 3) / 2;
                        horizontal = true;
                        dir = Direction.RIGHT;
                        break;
                    } else {
                        if (lastOrientation == Orientation.HORIZONTAL) {
                            dy = -this.TILE_SHORT_SIDE;
                        } else {
                            dy = ((-this.TILE_SHORT_SIDE) * 3) / 2;
                        }
                        dx = this.TILE_SHORT_SIDE / 2;
                        horizontal = true;
                        dir = Direction.RIGHT;
                        break;
                    }
                } else {
                    int dy2 = lastOrientation == Orientation.HORIZONTAL ? ((-this.TILE_SHORT_SIDE) * 3) / 2 : -this.TILE_LONG_SIDE;
                    if (horizontal) {
                        i = this.TILE_SHORT_SIDE / 2;
                    }
                    dy = dy2 + i;
                    dx = 0;
                    break;
                }
            case 4:
                int border4 = computeTableDownBorder(half);
                if (this.TILE_LONG_SIDE + lastY + this.TILE_SHORT_SIDE >= border4 && (lastOrientation != Orientation.HORIZONTAL || (this.TILE_SHORT_SIDE / 2) + lastY + this.TILE_LONG_SIDE >= border4)) {
                    if (this.TILE_LONG_SIDE + lastY >= border4 && (lastOrientation != Orientation.HORIZONTAL || ((this.TILE_SHORT_SIDE * 3) / 2) + lastY >= border4)) {
                        dy = this.TILE_SHORT_SIDE / 2;
                        dx = lastOrientation == Orientation.HORIZONTAL ? -this.TILE_LONG_SIDE : ((-this.TILE_SHORT_SIDE) * 3) / 2;
                        horizontal = true;
                        dir = Direction.LEFT;
                        break;
                    } else {
                        dy = lastOrientation == Orientation.HORIZONTAL ? this.TILE_SHORT_SIDE : (this.TILE_SHORT_SIDE * 3) / 2;
                        dx = (-this.TILE_SHORT_SIDE) / 2;
                        horizontal = true;
                        dir = Direction.LEFT;
                        break;
                    }
                } else {
                    int dy3 = lastOrientation == Orientation.HORIZONTAL ? (this.TILE_SHORT_SIDE * 3) / 2 : this.TILE_LONG_SIDE;
                    if (horizontal) {
                        i = (-this.TILE_SHORT_SIDE) / 2;
                    }
                    dy = dy3 + i;
                    dx = 0;
                    break;
                }
        }
        return new TileMovement(dir, horizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, dx, dy);
    }

    private TilePosition computeTilePosition(Tile tile, int value, Direction lastDirection, int lastX, int lastY, Orientation lastOrientation, Direction half) {
        int value0;
        int value1;
        int value2;
        TileMovement moves = computeTileMovement(tile, lastDirection, lastX, lastY, lastOrientation, half);
        int x = lastX + moves.dx;
        int y = lastY + moves.dy;
        Direction direction = moves.direction;
        Orientation orientation = moves.orientation;
        if (((direction == Direction.RIGHT || direction == Direction.LEFT) && orientation == Orientation.VERTICAL) || ((direction == Direction.UP || direction == Direction.DOWN) && orientation == Orientation.HORIZONTAL)) {
            x = Math.max(Math.min(x, this.WIDTH - this.TILE_SHORT_SIDE), this.TILE_SHORT_SIDE);
            y = Math.max(Math.min(y, this.HEIGHT - this.TILE_SHORT_SIDE), this.TILE_SHORT_SIDE);
        }
        if (tile.isDouble()) {
            value0 = tile.getLeft();
            value1 = tile.getRight();
        } else {
            switch ($SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction()[direction.ordinal()]) {
                case 1:
                case 3:
                    if (tile.getLeft() != value) {
                        value0 = tile.getLeft();
                        break;
                    } else {
                        value0 = tile.getRight();
                        break;
                    }
                case 2:
                case 4:
                    if (tile.getLeft() != value) {
                        value0 = tile.getRight();
                        break;
                    } else {
                        value0 = tile.getLeft();
                        break;
                    }
                default:
                    value0 = tile.getLeft();
                    break;
            }
            value1 = tile.getLeft() == value0 ? tile.getRight() : tile.getLeft();
        }
        if (value0 == value) {
            value2 = value1;
        } else {
            value2 = value0;
        }
        return new TilePosition(direction, orientation, x, y, value2, value0, value1);
    }

    private boolean continueLoop(int begin, int end, boolean lowerThan) {
        return lowerThan ? begin < end : begin >= end;
    }

    private void drawCenterTile(Canvas graphics, int centerX, int centerY, Tile tile, boolean leftSideEmpty, boolean rightSideEmpty) {
        drawTile(graphics, centerX, centerY, tile.isDouble() ? Orientation.VERTICAL : Orientation.HORIZONTAL, tile.getLeft(), tile.getRight(), leftSideEmpty || rightSideEmpty, rightSideEmpty ? Direction.RIGHT : Direction.LEFT);
    }

    /* access modifiers changed from: protected */
    public void drawHalf(Canvas graphics, int centerX, int centerY, int value, int centerPos, Direction half) {
        loopHalf(graphics, half == Direction.RIGHT, this.hand.getTilesOnTable(), centerPos, value, centerX, centerY, half);
    }

    private void drawOtherPlayerTiles(Canvas graphics) {
        if (this.hand != null) {
            List<List<Tile>> remainingTiles = this.hand.getRemainingTitles(this.me);
            drawRemainingTiles(graphics, Direction.RIGHT, remainingTiles.get(1), this.players[1]);
            drawRemainingTiles(graphics, Direction.UP, remainingTiles.get(2), this.players[2]);
            drawRemainingTiles(graphics, Direction.LEFT, remainingTiles.get(3), this.players[3]);
        }
    }

    private void drawRemainingTiles(Canvas g, Direction position, List<Tile> tiles, int color) {
        int left = this.WIDTH / 2;
        int top = this.HEIGHT / 2;
        int dx = 0;
        int dy = 0;
        int width = 0;
        int height = 0;
        int leftHolder = left;
        int topHolder = top;
        int widthHolder = 0;
        int heightHolder = 0;
        DrawingTileStrategy.Position positionHolder = DrawingTileStrategy.Position.UP;
        int quantity = tiles.size();
        switch ($SWITCH_TABLE$com$amg$double9domino$widget$Table$Direction()[position.ordinal()]) {
            case 1:
                left = this.TILE_THICKNESS;
                top -= (this.TILE_SHORT_SIDE * quantity) / 2;
                dy = this.TILE_SHORT_SIDE;
                width = this.TILE_THICKNESS;
                height = this.TILE_SHORT_SIDE;
                leftHolder = 0;
                topHolder -= this.TILE_SHORT_SIDE * 4;
                widthHolder = this.TILE_THICKNESS << 1;
                heightHolder = this.TILE_SHORT_SIDE * 8;
                positionHolder = DrawingTileStrategy.Position.LEFT;
                break;
            case 2:
                left = this.WIDTH - (this.TILE_THICKNESS << 1);
                top -= (this.TILE_SHORT_SIDE * quantity) / 2;
                dy = this.TILE_SHORT_SIDE;
                width = this.TILE_THICKNESS;
                height = this.TILE_SHORT_SIDE;
                leftHolder = this.WIDTH - (this.TILE_THICKNESS << 1);
                topHolder -= this.TILE_SHORT_SIDE * 4;
                widthHolder = this.TILE_THICKNESS << 1;
                heightHolder = this.TILE_SHORT_SIDE * 8;
                positionHolder = DrawingTileStrategy.Position.RIGHT;
                break;
            case 3:
                left -= (this.TILE_SHORT_SIDE * quantity) / 2;
                top = this.TILE_THICKNESS;
                dx = this.TILE_SHORT_SIDE;
                height = this.TILE_THICKNESS;
                width = this.TILE_SHORT_SIDE;
                leftHolder -= this.TILE_SHORT_SIDE * 4;
                topHolder = 0;
                widthHolder = this.TILE_SHORT_SIDE * 8;
                heightHolder = this.TILE_THICKNESS << 1;
                positionHolder = DrawingTileStrategy.Position.UP;
                break;
        }
        boolean horizontal = position == Direction.RIGHT || position == Direction.LEFT;
        this.drawingTileStrategy.drawTileHolder(g, leftHolder, topHolder, widthHolder, heightHolder, positionHolder);
        for (int i = quantity; i > 0; i--) {
            this.drawingTileStrategy.drawTileBack(g, left, top, width, height, horizontal);
            left += dx;
            top += dy;
        }
    }

    /* access modifiers changed from: protected */
    public void drawTile(Canvas graphics, int x, int y, Orientation orientation, int value0, int value1, boolean cornerTile, Direction direction) {
        this.drawingTileStrategy.drawTile(graphics, x, y, this.TILE_LONG_SIDE, this.TILE_SHORT_SIDE, orientation == Orientation.HORIZONTAL, value0, value1);
    }

    /* access modifiers changed from: protected */
    public void drawTiles(Canvas graphics) {
        boolean z;
        boolean z2 = true;
        int centerX = this.WIDTH / 2;
        int centerY = this.HEIGHT / 2;
        int centerPos = NONE;
        if (this.hand != null) {
            centerPos = this.hand.getFirstTilePos();
        }
        if (centerPos >= 0) {
            int tilesPlayed = this.hand.getTilesOnTable().size();
            Tile centerTile = this.hand.getTilesOnTable().get(centerPos);
            if (centerPos == 0) {
                z = true;
            } else {
                z = false;
            }
            if (centerPos + 1 != tilesPlayed) {
                z2 = false;
            }
            drawCenterTile(graphics, centerX, centerY, centerTile, z, z2);
            drawHalf(graphics, centerX, centerY, centerTile.getRight(), centerPos, Direction.RIGHT);
            drawHalf(graphics, centerX, centerY, centerTile.getLeft(), centerPos, Direction.LEFT);
        }
    }

    public DrawingTileStrategy getDrawingTileStrategy() {
        return this.drawingTileStrategy;
    }

    private int getHeight(int heightPixels) {
        int playerTileHeight = getResources().getDimensionPixelSize(R.dimen.tile_player_side_long);
        float density = getResources().getDisplayMetrics().density;
        //int advertisement = getResources().getDimensionPixelSize(R.dimen.advertisement);
        return (((heightPixels - 2*playerTileHeight)) - ((int) (10.0f * density))) - getResources().getDimensionPixelSize(R.dimen.actionbar);
    }

    public int getPreferredHeight() {
        return this.HEIGHT;
    }

    public int getPreferredWidth() {
        return this.WIDTH;
    }

    /* access modifiers changed from: protected */
    public int getZoomOutLimit() {
        return 10;
    }

    /* access modifiers changed from: protected */
    public void loopHalf(Canvas graphics, boolean forward, List<Tile> tiles, int centerPos, int value, int x, int y, Direction half) {
        boolean z;
        Direction direction = half;
        int limit = forward ? tiles.size() : 0;
        int delta = forward ? 1 : -1;
        Orientation orientation = tiles.get(centerPos).isDouble() ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        for (int i = forward ? centerPos + 1 : centerPos - 1; continueLoop(i, limit, forward); i += delta) {
            TilePosition tilePosition = computeTilePosition(tiles.get(i), value, direction, x, y, orientation, half);
            direction = tilePosition.direction;
            x = tilePosition.x;
            y = tilePosition.y;
            orientation = tilePosition.orientation;
            value = tilePosition.value;
            int value0 = tilePosition.value0;
            int value1 = tilePosition.value1;
            if (continueLoop(i + delta, limit, forward)) {
                z = false;
            } else {
                z = true;
            }
            drawTile(graphics, x, y, orientation, value0, value1, z, direction);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawOtherPlayerTiles(canvas);
        drawTiles(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.WIDTH, this.HEIGHT);
    }

    public void setHand(Hand m) {
        this.hand = m;
        invalidate();
    }

    public void setMe(Player me2) {
        this.me = me2;
    }

    public void setPlayer(Player me2) {
        this.me = me2;
    }
}
