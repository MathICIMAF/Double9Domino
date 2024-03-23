package com.amg.double9domino.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
//import com.flurry.android.FlurryAgent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amg.double9domino.FragmentStatusCallback;
import com.amg.double9domino.GameActivity;
import com.amg.double9domino.R;
//import com.lagunex.domino.analytics.Flurry;
import com.amg.double9domino.analytics.Flurry;
import com.amg.double9domino.model.Game;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Player;
import com.amg.double9domino.model.Team;
import com.amg.double9domino.model.Tile;
import com.amg.double9domino.sound.SoundPlayer;
import com.amg.double9domino.util.Command;
import com.amg.double9domino.widget.BothSidesDialog;
import com.amg.double9domino.widget.EndHandDialog;
import com.amg.double9domino.widget.Table;
import com.amg.double9domino.widget.drawing.DrawingTileStrategy;
import java.util.List;

public class GameFragment extends Fragment implements BothSidesDialog.BothSidesCallback, EndHandDialog.EndHandDialogCallback {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$analytics$Flurry$Event = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$widget$EndHandDialog$EndHandDialogCallback$Action = null;
    public static final String NAME = "com.amg.double9domino.GameFragment";
    private static final String TAG = "GameFragment";
    protected EndHandDialog endHandDialog;
    protected boolean foreground;
    protected Game game;
    boolean newGame;
    private GameFragmentCallback gameFragmentCallback;
    protected Player me;
    protected LinearLayout pass;
    protected Button passButton;
    protected SoundPlayer sound;
    private FragmentStatusCallback statusCallback;
    protected Table table;
    protected LinearLayout tiles;

    /* access modifiers changed from: protected */
    public enum Move {
        BOTH_SIDES,
        END,
        INVALID,
        VALID,
        WRONG_TURN
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$analytics$Flurry$Event() {
        int[] iArr = $SWITCH_TABLE$com$amg$double9domino$analytics$Flurry$Event;
        if (iArr == null) {
            iArr = new int[Flurry.Event.values().length];
            try {
                iArr[Flurry.Event.NEW_GAME.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Flurry.Event.NEW_HAND.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Flurry.Event.SHARE_RESULT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$amg$double9domino$analytics$Flurry$Event = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$widget$EndHandDialog$EndHandDialogCallback$Action() {
        int[] iArr = $SWITCH_TABLE$com$amg$double9domino$widget$EndHandDialog$EndHandDialogCallback$Action;
        if (iArr == null) {
            iArr = new int[EndHandDialog.EndHandDialogCallback.Action.values().length];
            try {
                iArr[EndHandDialog.EndHandDialogCallback.Action.NEW_GAME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EndHandDialog.EndHandDialogCallback.Action.NEXT_HAND.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EndHandDialog.EndHandDialogCallback.Action.SHARE_RESULT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$amg$double9domino$widget$EndHandDialog$EndHandDialogCallback$Action = iArr;
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public class ClickAsync extends AsyncTask<Object, Void, Object[]> {
        private /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$fragments$GameFragment$Move;

        /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$fragments$GameFragment$Move() {
            int[] iArr = $SWITCH_TABLE$com$amg$double9domino$fragments$GameFragment$Move;
            if (iArr == null) {
                iArr = new int[Move.values().length];
                try {
                    iArr[Move.BOTH_SIDES.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Move.END.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Move.INVALID.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Move.VALID.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Move.WRONG_TURN.ordinal()] = 5;
                } catch (NoSuchFieldError e5) {
                }
                $SWITCH_TABLE$com$amg$double9domino$fragments$GameFragment$Move = iArr;
            }
            return iArr;
        }

        protected ClickAsync() {
        }

        /* access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Object[] doInBackground(Object... params) {
            Move move;
            Tile tile = (Tile) params[0];
            View tileView = (View) params[1];
            Hand h = GameFragment.this.game.getCurrentHand();
            if (h.hasEnded()) {
                move = Move.END;
            } else if (h.isTurn(GameFragment.this.me)) {
                boolean pass = GameFragment.this.me.pass(h);
                boolean passTile = Tile.PASS.equals(tile);
                if (!pass || !passTile) {
                    move = ((!pass || passTile) && (pass || !passTile)) ? (tile.getHalf() != Hand.Corner.NONE || !h.playBothCorners(tile)) ? h.play(tile) ? Move.VALID : Move.INVALID : Move.BOTH_SIDES : Move.INVALID;
                } else {
                    move = h.play(tile) ? Move.VALID : Move.INVALID;
                }
            } else {
                move = Move.WRONG_TURN;
            }
            return new Object[]{tile, tileView, move};
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Object[] result) {
            String action;
            super.onPostExecute( result);
            if (GameFragment.this.foreground) {
                Tile tile = (Tile) result[0];
                View button = (View) result[1];
                switch ($SWITCH_TABLE$com$amg$double9domino$fragments$GameFragment$Move()[((Move) result[2]).ordinal()]) {
                    case 1:
                        BothSidesDialog bothSides = GameFragment.this.createBothSidesDialog();
                        bothSides.setCallback(GameFragment.this);
                        bothSides.setSides(GameFragment.this.game.getCurrentHand(), tile, GameFragment.this.game.getCurrentHand().getLeftCorner(), GameFragment.this.game.getCurrentHand().getRightCorner(), button, GameFragment.this.getActivity());
                        bothSides.getDialog().show();
                        break;
                    case 2:
                        GameFragment.this.handEnded();
                        break;
                    case 3:
                        GameFragment.this.sound.play(SoundPlayer.Sound.WRONG);
                        Toast.makeText(GameFragment.this.getActivity(), (int) R.string.no, Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        if (Tile.PASS.equals(tile)) {
                            GameFragment.this.sound.play(SoundPlayer.Sound.PASS);
                            action = GameFragment.this.getResources().getString(R.string.player_pass);
                        } else {
                            GameFragment.this.sound.playMove();
                            button.setClickable(false);
                            button.setOnClickListener(null);
                            button.setVisibility(View.INVISIBLE);
                            action = GameFragment.this.getResources().getString(R.string.player_move);
                        }
                        ((GameActivity) GameFragment.this.getActivity()).getSupportActionBar().setTitle(String.valueOf(tile.getPlayer().getName()) + " " + action);
                        GameFragment.this.createContinueAsync().execute(new Void[0]);
                        break;
                    case 5:
                        GameFragment.this.sound.play(SoundPlayer.Sound.WRONG);
                        Toast.makeText(GameFragment.this.getActivity(), (int) R.string.wrong_turn, Toast.LENGTH_LONG).show();
                        break;
                }
                GameFragment.this.table.invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public class ContinueAsync extends AsyncTask<Void, Tile, Boolean> {
        private /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$model$Game$Think = null;
        private static final int LONG_TIME = 3500;
        private static final int SHORT_TIME = 2500;

        /* synthetic */ int[] $SWITCH_TABLE$com$amg$double9domino$model$Game$Think() {
            int[] iArr = $SWITCH_TABLE$com$amg$double9domino$model$Game$Think;
            if (iArr == null) {
                iArr = new int[Game.Think.values().length];
                try {
                    iArr[Game.Think.LONG.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Game.Think.SHORT.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Game.Think.THINK.ordinal()] = 1;
                } catch (NoSuchFieldError e3) {
                }
                $SWITCH_TABLE$com$amg$double9domino$model$Game$Think = iArr;
            }
            return iArr;
        }

        protected ContinueAsync() {
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... params) {
            Hand hand = GameFragment.this.game.getCurrentHand();
            int time = SHORT_TIME;
            while (!hand.isTurn(GameFragment.this.me) && !hand.hasEnded()) {
                Player player = hand.getCurrentPlayer();
                Tile tile = player.forcePlay(hand);
                try {
                    switch ($SWITCH_TABLE$com$amg$double9domino$model$Game$Think()[GameFragment.this.game.getTimeThinking().ordinal()]) {
                        case 1:
                            if (!tile.hasThought()) {
                                time = SHORT_TIME;
                                break;
                            } else {
                                time = LONG_TIME;
                                break;
                            }
                        case 2:
                            time = SHORT_TIME;
                            break;
                        case 3:
                            time = LONG_TIME;
                            break;
                    }
                    Thread.sleep((long) time);
                } catch (Exception e) {
                }
                hand.play(tile);
                Log.i(GameFragment.TAG, "player " + player + " tile " + tile);
                publishProgress(tile);
            }
            return Boolean.valueOf(hand.hasEnded());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            super.onPostExecute( result);
            if (GameFragment.this.foreground) {
                Log.d(GameFragment.TAG, "game ended? " + result.toString());
                if (result) {
                    GameFragment.this.handEnded();
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Tile... tiles) {
            String move;
            super.onProgressUpdate( tiles);
            if (GameFragment.this.foreground) {
                Tile tile = tiles[0];
                if (Tile.PASS.equals(tile)) {
                    GameFragment.this.sound.play(SoundPlayer.Sound.PASS);
                    move = GameFragment.this.getResources().getString(R.string.player_pass);
                } else {
                    GameFragment.this.sound.playMove();
                    move = GameFragment.this.getResources().getString(R.string.player_move);
                }
                GameFragment.this.setActionBarTitle(String.valueOf(tile.getPlayer().getName()) + " " + move);
                GameFragment.this.table.invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public class TileClickListener implements View.OnClickListener {
        private static final String TAG = "TileClickListener";
        protected Tile tile;
        protected Vibrator vibrator;

        public TileClickListener(Tile tile2) {
            this.vibrator = (Vibrator) GameFragment.this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            this.tile = tile2;
        }

        public void onClick(View v) {
            Log.d(TAG, this.tile.toString());
            this.vibrator.vibrate(25);
            GameFragment.this.createClickAsync().execute(this.tile, v);
        }
    }

    public GameFragment() {
        newGame = true;
    }


    /* access modifiers changed from: protected */
    public BothSidesDialog createBothSidesDialog() {
        return new BothSidesDialog();
    }

    /* access modifiers changed from: protected */
    public ClickAsync createClickAsync() {
        return new ClickAsync();
    }

    /* access modifiers changed from: protected */
    public ContinueAsync createContinueAsync() {
        return new ContinueAsync();
    }

    /* access modifiers changed from: protected */

    /* access modifiers changed from: protected */
    public Player createPlayer(String string, boolean human) {
        return new Player(string, human);
    }

    public Fragment createScoreFragment(FragmentStatusCallback callback) {
        ScoreFragment fragment = new ScoreFragment();
        fragment.setGame(getGame());
        fragment.setStatusCallback(callback);
        return fragment;
    }

    /* access modifiers changed from: protected */
    public TileClickListener createTileClickListener(Tile tile) {
        return new TileClickListener(tile);
    }

    /* access modifiers changed from: protected */
    public boolean didIWin(Game game2) {
        return game2.getTotal1() > game2.getTotal2();
    }

    public Game getGame() {
        return this.game;
    }

    public GameFragmentCallback getGameFragmentCallback() {
        return this.gameFragmentCallback;
    }

    /* access modifiers changed from: protected */
    public String getLogEventName(Flurry.Event event) {
        switch ($SWITCH_TABLE$com$amg$double9domino$analytics$Flurry$Event()[event.ordinal()]) {
            case 1:
                return Flurry.NEW_HAND_CLASSIC;
            case 2:
                return Flurry.NEW_GAME_CLASSIC;
            case 3:
                return Flurry.SHARE_RESULT_CLASSIC;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public String getShareGameResultMessage(Game game2) {
        int team1 = game2.getTotal1();
        int team2 = game2.getTotal2();
        StringBuilder result = new StringBuilder();
        if (team1 > team2) {
            result.append(String.format(getResources().getString(R.string.game_share_text_win), Integer.valueOf(team1), Integer.valueOf(team2)));
        } else {
            result.append(String.format(getResources().getString(R.string.game_share_text_lose), Integer.valueOf(team1), Integer.valueOf(team2)));
        }
        return result.toString();
    }

    /* access modifiers changed from: protected */
    public void handEnded() {
        Hand h = this.game.getCurrentHand();
        int total1 = h.getTotal1();
        int total2 = h.getTotal2();
        if (total1 > total2) {
            this.sound.play(SoundPlayer.Sound.WIN);
        } else if (total1 < total2) {
            this.sound.play(SoundPlayer.Sound.LOSE);
        }
        logEvent(Flurry.Event.NEW_HAND, false);
        if (this.game.hasEnded() && getGameFragmentCallback() != null) {
            logEvent(Flurry.Event.NEW_GAME, false);
            if (getGameFragmentCallback() != null) {
                getGameFragmentCallback().onEndGameResult(this.game, this.game.getTotal1(), this.game.getTotal2(), this.game.getScoreLimit(), 0);
            }
        }
        showEndHandDialog();
    }

    /* access modifiers changed from: protected */
    public void initBackground(View view) {
        view.findViewById(R.id.game_fragment).setBackgroundResource(R.drawable.table_green);
    }

    public void initGame() {
        this.game = new Game(getActivity());
        initGameOptions();
        Player[] players = new Player[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = createPlayer(String.valueOf(i), i == 0);
            players[i].setLocation("");
        }
        Team t1 = new Team();
        t1.setPlayer1(players[0]);
        t1.setPlayer2(players[2]);
        Team t2 = new Team();
        t2.setPlayer1(players[1]);
        t2.setPlayer2(players[3]);
        players[0].setPartner(players[2]);
        players[0].setRightOponent(players[1]);
        players[0].setLeftOponent(players[3]);

        players[1].setPartner(players[3]);
        players[1].setRightOponent(players[2]);
        players[1].setLeftOponent(players[0]);

        players[2].setPartner(players[0]);
        players[2].setRightOponent(players[3]);
        players[2].setLeftOponent(players[1]);

        players[3].setPartner(players[1]);
        players[3].setRightOponent(players[0]);
        players[3].setLeftOponent(players[2]);

        this.game.setTeam1(t1);
        this.game.setTeam2(t2);
        this.me = this.game.getTeam1().getPlayer1();
        this.game.startNextHand(true);
    }

    /* access modifiers changed from: protected */
    public void initGameOptions() {
        String limit = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("limit_score","100");
        game.setScoreLimit(Integer.parseInt(limit));
        boolean first_double = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("first_hand",false);
        game.setFirstHandDouble(first_double);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initMainLoop() {
        if (newGame) {
            initGame();
            newGame = false;
        }
        initPlayerNames();
        initTable();
        initTiles();
        initPass();
        if (game.isHuman_start() && game.getCurrentHand().getTilesOnTable().size() == 0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            if (game.isPatner_wants()){
                dialog.setTitle(getContext().getString(R.string.start));
            }
            else
                dialog.setTitle(getContext().getString(R.string.no_start));
            dialog.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            game.selectedStart(true);
                            if (endHandDialog == null || !endHandDialog.getDialog().isShowing())
                                createContinueAsync().execute(new Void[0]);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            game.selectedStart(false);
                            if (endHandDialog == null || !endHandDialog.getDialog().isShowing())
                                createContinueAsync().execute(new Void[0]);
                        }
                    }).create().show();

        }
        else {
            if ( (this.endHandDialog == null || !this.endHandDialog.getDialog().isShowing())) {
                createContinueAsync().execute(new Void[0]);
            }
        }

    }


    /* access modifiers changed from: protected */
    public void initPass() {
        this.passButton = new Button(getActivity());
        this.passButton.setText(R.string.pass_button);
        this.passButton.getBackground().setColorFilter(Color.rgb(194, 39, 45), PorterDuff.Mode.SRC_IN);
        this.passButton.setTextColor(-1);
        this.passButton.setOnClickListener(createTileClickListener(Tile.getPassTile(this.me)));
        this.pass.removeAllViews();
        this.pass.addView(this.passButton);
    }

    /* access modifiers changed from: protected */
    public void initPlayerNames() {
        int i;
        String title;
        int i2;
        if (this.game != null) {
            Team team = this.game.getTeam1();
            Resources res = getResources();
            team.getPlayer1().setName(res.getString(R.string.player_you));
            team.getPlayer2().setName(res.getString(R.string.player_teammate));
            Team team2 = this.game.getTeam2();
            team2.getPlayer1().setName(res.getString(R.string.player_rival1));
            team2.getPlayer2().setName(res.getString(R.string.player_rival2));
            Hand h = this.game.getCurrentHand();
            Player p = h.getCurrentPlayer();
            if (h.getPlayCount() > 0) {
                if (p.equals(this.me)) {
                    i2 = R.string.game_hand_continue_you;
                } else {
                    i2 = R.string.game_hand_continue_them;
                }
                title = res.getString(i2, p.getName());
            } else {
                if (p.equals(this.me)) {
                    i = R.string.game_hand_start_you;
                } else {
                    i = R.string.game_hand_start_them;
                }
                title = res.getString(i, p.getName());
            }
            setActionBarTitle(title);
        }
    }

    /* access modifiers changed from: protected */
    public void initTable() {
        this.table.setMe(this.me);
        this.table.setHand(this.game.getCurrentHand());
        this.table.invalidate();
    }

    /* access modifiers changed from: protected */
    public void initTiles() {
        initTiles(getResources().getDimensionPixelSize(R.dimen.tile_player_side_short), getResources().getDimensionPixelSize(R.dimen.tile_player_side_long));
    }

    /* access modifiers changed from: protected */
    public void initTiles(int shortSide, int longSide) {
        if (this.tiles.getChildCount() > 0) {
            this.tiles.removeAllViews();
        }
        List<Tile> playerTiles = this.game.getTeam1().getPlayer1().getTiles();
        DrawingTileStrategy draw = this.table.getDrawingTileStrategy();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2, 1.0f);
        for (Tile t : playerTiles) {
            ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(lp);
            iv.setImageBitmap(draw.getTile(t.getLeft(), t.getRight(), shortSide, longSide));
            iv.setOnClickListener(createTileClickListener(t));
            this.tiles.addView(iv);
        }
    }

    /* access modifiers changed from: protected */
    public void logEvent(Flurry.Event event, boolean begin) {

    }

    @Override // com.lagunex.domino.widget.BothSidesDialog.BothSidesCallback
    public void onChosenSide(Tile tile, View tileView) {
        createClickAsync().execute(tile, tileView);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.sound = new SoundPlayer();
        this.sound.initSounds(getActivity());
        View view = inflater.inflate(R.layout.layout_game, container, false);
        initBackground(view);
        this.table = (Table) view.findViewById(R.id.game_table);
        this.tiles = (LinearLayout) view.findViewById(R.id.game_tiles);
        this.pass = (LinearLayout) view.findViewById(R.id.game_pass);
        return view;
    }

    @Override // com.lagunex.domino.widget.EndHandDialog.EndHandDialogCallback
    public void onNextHandAction(EndHandDialog.EndHandDialogCallback.Action command) {
        switch ($SWITCH_TABLE$com$amg$double9domino$widget$EndHandDialog$EndHandDialogCallback$Action()[command.ordinal()]) {
            case 1:
                //initGame();
                if (getGameFragmentCallback() != null) {
                    getGameFragmentCallback().setSendScore(true);
                }
                newGame = true;
                initMainLoop();
                return;
            case 2:
                logEvent(Flurry.Event.NEW_HAND, true);
                startNewHand();
                return;
            case 3:
                if (getGameFragmentCallback() != null) {
                    //logEvent(Flurry.Event.SHARE_RESULT);
                    getGameFragmentCallback().onShareGameResult(didIWin(this.game), getShareGameResultMessage(this.game));
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.foreground = false;
        this.sound.setForeground(this.foreground);
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.foreground = true;
        this.sound.setForeground(this.foreground);
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        this.sound.setForeground(true);
        if (this.statusCallback != null) {
            this.statusCallback.onFragmentVisible(NAME);
        }
        setActionBarTitle("");
        initMainLoop();
    }

    /* access modifiers changed from: protected */
    public void setActionBarTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    public void setGameFragmentCallback(GameFragmentCallback gameFragmentCallback2) {
        this.gameFragmentCallback = gameFragmentCallback2;
    }

    public void setStatusCallback(FragmentStatusCallback statusCallback2) {
        this.statusCallback = statusCallback2;
    }

    /* access modifiers changed from: protected */
    public void showEndHandDialog() {
        this.endHandDialog = new EndHandDialog(this.game.hasEnded(), this.game, getActivity());
        this.endHandDialog.setCallback(this);
        this.endHandDialog.getDialog().show();
    }

    private void startNewHand() {
        Command startNextHand = new Command() {
            /* class com.lagunex.domino.fragments.GameFragment.AnonymousClass1 */

            @Override // com.lagunex.domino.util.Command
            public void execute() {
                newGame = false;
                GameFragment.this.game.startNextHand(newGame);
                GameFragment.this.initMainLoop();
            }
        };
        if (getGameFragmentCallback() != null) {
            getGameFragmentCallback().onStartNextHand(startNextHand);
        } else {
            startNextHand.execute();
        }
    }
}
