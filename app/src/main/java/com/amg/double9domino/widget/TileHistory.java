package com.amg.double9domino.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amg.double9domino.R;
import com.amg.double9domino.model.Tile;
import com.amg.double9domino.widget.drawing.DrawingFactory;
import com.amg.double9domino.widget.drawing.DrawingTileStrategy;

public class TileHistory extends LinearLayout {
    DrawingTileStrategy drawing;
    private Tile tile;

    public Tile getTile() {
        return this.tile;
    }

    public void setTile(Tile tile2) {
        this.tile = tile2;
        if (Tile.PASS.equals(tile2)) {
            ((ImageView) findViewById(R.id.play_tile)).setVisibility(INVISIBLE);
        } else {
            ((ImageView) findViewById(R.id.play_tile)).setImageBitmap(this.drawing.getTile(tile2.getLeft(), tile2.getRight(), getResources().getDimensionPixelSize(R.dimen.tile_player_side_short), getResources().getDimensionPixelSize(R.dimen.tile_player_side_long)));
        }
        StringBuilder message = new StringBuilder(this.tile.getPlayer().getName());

        ((TextView) findViewById(R.id.play_name)).setText(message.toString());
    }

    public TileHistory(Context context) {
        this(context, null);
    }

    public TileHistory(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidget(context);
    }

    private void initWidget(Context context) {
        this.drawing = DrawingFactory.getInstance().getDrawingTileStrategy(context);
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_history_tile, this);
    }
}
