package com.amg.double9domino.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import com.amg.double9domino.R;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Tile;

public class BothSidesDialog {
    BothSidesCallback callback;
    Dialog dialog;

    public interface BothSidesCallback {
        void onChosenSide(Tile tile, View view);
    }

    public void setCallback(BothSidesCallback callback2) {
        this.callback = callback2;
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    public void setSides(Hand hand, final Tile tile, int left, int right, final View tileView, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.both_sides).setPositiveButton(String.valueOf(right), new DialogInterface.OnClickListener() {
            /* class com.lagunex.domino.widget.BothSidesDialog.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int id) {
                if (BothSidesDialog.this.callback != null) {
                    tile.setHalf(Hand.Corner.RIGHT);
                    BothSidesDialog.this.callback.onChosenSide(tile, tileView);
                }
            }
        }).setNegativeButton(String.valueOf(left), new DialogInterface.OnClickListener() {
            /* class com.lagunex.domino.widget.BothSidesDialog.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int id) {
                if (BothSidesDialog.this.callback != null) {
                    tile.setHalf(Hand.Corner.LEFT);
                    BothSidesDialog.this.callback.onChosenSide(tile, tileView);
                }
            }
        });
        this.dialog = builder.create();
    }
}
