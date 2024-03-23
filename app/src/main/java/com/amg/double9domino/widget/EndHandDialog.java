package com.amg.double9domino.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//import com.facebook.widget.ProfilePictureView;
import com.amg.double9domino.R;
import com.amg.double9domino.model.Game;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Tile;
import com.amg.double9domino.widget.drawing.DrawingFactory;
import com.amg.double9domino.widget.drawing.DrawingTileStrategy;
import java.util.List;

public class EndHandDialog {
    private EndHandDialogCallback callback;
    private Context context;
    private Dialog dialog;
    private boolean endGame;
    private Game game;

    public interface EndHandDialogCallback {

        public enum Action {
            NEW_GAME,
            NEXT_HAND,
            SHARE_RESULT
        }

        void onNextHandAction(Action action);
    }

    public EndHandDialog(boolean endGame2, Game game2, Context context2) {
        this.endGame = endGame2;
        setGame(game2);
        setContext(context2);
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(initTitle());
        Context context = getContext();
        builder.setView(createDialogContent(getContext()));
        DialogInterface.OnClickListener onFinish = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        };
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
            /* class com.lagunex.domino.widget.EndHandDialog.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (EndHandDialog.this.callback != null) {
                    switch (which) {
                        case -2:
                        default:
                            return;
                        case -1:
                            if (EndHandDialog.this.endGame) {
                                EndHandDialog.this.callback.onNextHandAction(EndHandDialogCallback.Action.NEW_GAME);
                                return;
                            } else {
                                EndHandDialog.this.callback.onNextHandAction(EndHandDialogCallback.Action.NEXT_HAND);
                                return;
                            }
                    }
                }
            }
        };
        builder.setNegativeButton(R.string.game_view_table, onClick);
        if (this.endGame) {
            builder.setNeutralButton(R.string.exit, onFinish);
            builder.setPositiveButton(R.string.game_new_game, onClick);
        } else {
            builder.setPositiveButton(R.string.game_next_hand, onClick);
        }
        this.dialog = builder.create();
    }

    /* access modifiers changed from: protected */
    public CharSequence initTitle() {
        StringBuffer title = new StringBuffer(getContext().getResources().getString(R.string.game_hand_result));
        Hand h = getGame().getCurrentHand();
        if (h.getTotal1() > h.getTotal2()) {
            title.append(": ").append(getContext().getResources().getString(R.string.game_hand_won));
        } else if (h.getTotal1() < h.getTotal2()) {
            title.append(": ").append(getContext().getResources().getString(R.string.game_hand_lost));
        } else {
            title.append(": ").append(getContext().getResources().getString(R.string.game_hand_tie));
        }
        if (getGame().hasEnded()) {
            title.append(" ").append(getContext().getResources().getString(R.string.game_hand_endgame));
        } else {
            title.append(" ").append(getContext().getResources().getString(R.string.game_hand_endhand));
        }
        return title.toString();
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    /* access modifiers changed from: protected */
    public View createDialogContent(Context context2) {
        Hand hand = getGame().getCurrentHand();
        ScrollView layout = new ScrollView(context2);
        int horizontalPadding = context2.getResources().getDimensionPixelSize(R.dimen.dialog_horizontal_margins);
        layout.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        View content = ((LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_end_hand, layout);
        ((TextView) content.findViewById(R.id.game_team1)).setText(getGame().getTeam1().getName());
        ((TextView) content.findViewById(R.id.game_team1_score_hand)).setText(String.valueOf(hand.getTotal1()));
        ((TextView) content.findViewById(R.id.game_team1_score_game)).setText(String.valueOf(getGame().getTotal1()));
        ((TextView) content.findViewById(R.id.game_team2)).setText(getGame().getTeam2().getName());
        ((TextView) content.findViewById(R.id.game_team2_score_hand)).setText(String.valueOf(hand.getTotal2()));
        ((TextView) content.findViewById(R.id.game_team2_score_game)).setText(String.valueOf(getGame().getTotal2()));
        setPlayerInfo(content, R.id.game_team1_player1, getGame().getTeam1().getPlayer1());
        setPlayerInfo(content, R.id.game_team1_player2, getGame().getTeam1().getPlayer2());
        setPlayerInfo(content, R.id.game_team2_player1, getGame().getTeam2().getPlayer1());
        setPlayerInfo(content, R.id.game_team2_player2, getGame().getTeam2().getPlayer2());
        return content;
    }

    public void setCallback(EndHandDialogCallback callback2) {
        this.callback = callback2;
    }

    /* access modifiers changed from: protected */
    public void setPlayerInfo(View content, int playerId, Player player) {
        List<Tile> tiles = player.getTiles();
        if (tiles.size() > 0) {
            TableLayout table = (TableLayout) content.findViewById(playerId);
            TextView name = new TextView(getContext());
            name.setText(player.getName());
            table.addView(name);
            TableRow tilesRow = new TableRow(getContext());
            DrawingTileStrategy drawing = DrawingFactory.getInstance().getDrawingTileStrategy(getContext());
            for (Tile t : tiles) {
                ImageView tileImage = new ImageView(getContext());
                tileImage.setImageBitmap(drawing.getTile(t.getLeft(), t.getRight(), getContext().getResources().getDimensionPixelSize(R.dimen.tile_player_side_short), getContext().getResources().getDimensionPixelSize(R.dimen.tile_player_side_long)));
                tilesRow.addView(tileImage);
            }
            table.addView(tilesRow);
        }
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game2) {
        this.game = game2;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }
}
