package com.amg.double9domino.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.amg.double9domino.model.Tile;

public class HistoryAdapter<T> extends ArrayAdapter<T> {
    public HistoryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Tile t = (Tile) getItem(position);
        TileHistory th = null;
        if (convertView != null && (convertView instanceof TileHistory) && t.equals(((TileHistory) convertView).getTile())) {
            th = (TileHistory) convertView;
        }
        if (th != null) {
            return th;
        }
        TileHistory th2 = new TileHistory(getContext());
        th2.setTile(t);
        return th2;
    }
}
