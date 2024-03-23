package com.amg.double9domino;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
//import com.flurry.android.FlurryAgent;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.games.Games;
//import com.google.example.games.basegameutils.GameHelper;
//import com.lagunex.domino.analytics.Flurry;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amg.double9domino.fragments.GameFragment;
import com.amg.double9domino.fragments.GameFragmentCallback;
import com.amg.double9domino.fragments.HistoryFragment;
import com.amg.double9domino.model.Game;
//import com.lagunex.domino.net.PostScoreController;
import com.amg.double9domino.util.Command;
//import com.amg.double9domino.model.ShareResultDialog;

public class GameActivity extends AppCompatActivity implements FragmentStatusCallback, GameFragmentCallback {
    private static final String PENDING_GAME = "PENDING_GAME_v2";
    private static final String TAG = GameActivity.class.getSimpleName();
    //private AdEngine adEngine;
    private AlertDialog exitGame;
    private Game game;
    private GameFragment gameFragment;
    private String gameMode;
    private boolean gameVisible;
    //private GameHelper googleGameHelper;
    private HistoryFragment historyFragment;
    private Menu menu;
    private Fragment scoreFragment;
    private boolean sendScore;
    //private UiLifecycleHelper uiHelper;


    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Game getGame() {
        if (this.game == null) {
            this.game = this.gameFragment.getGame();
        }
        return this.game;
    }


    private void initExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_exit);
        builder.setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
            /* class com.lagunex.domino.GameActivity.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                GameActivity.this.sendGameScore(-GameActivity.this.getGame().getScoreLimit(), GameActivity.this.getGame().getScoreLimit(), GameActivity.this.getGame().getGameType());
                GameActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener() {
            /* class com.lagunex.domino.GameActivity.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int which) {
                GameActivity.this.exitGame.dismiss();
            }
        });
        this.exitGame = builder.create();
    }

    private void initGameFragment() {
        this.gameVisible = true;
        this.gameFragment = new GameFragment();
        this.gameFragment.setStatusCallback(this);
        this.gameFragment.setRetainInstance(true);
        setSendScore(true);
        this.gameFragment.setGameFragmentCallback(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.game_fragment, this.gameFragment);
        ft.commit();
    }


    private void initGameMode() {
        this.gameMode = Game.class.getSimpleName();//getIntent().getStringExtra(Game.class.getName());
    }

    /* access modifiers changed from: protected */

    @Override // android.support.v7.app.ActionBarActivity, android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        if (!this.gameVisible || !getGame().isGameInTheMiddle()) {
            super.onBackPressed();
        } else {
            this.exitGame.show();
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.ActionBarActivity, android.support.v4.app.FragmentActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
      //  this.uiHelper = new UiLifecycleHelper(this, this);
        //this.uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGameMode();
        initExitDialog();
        initGameFragment();
        //initAd();
        int pendingGame = getPreferences(0).getInt(PENDING_GAME, -1);
        if (pendingGame != -1) {
            Log.d(TAG, "Updating previous forced loss " + pendingGame);
            sendGameScore(-100, 100, pendingGame);
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    protected void onDestroy() {
        super.onDestroy();
        //this.uiHelper.onDestroy();
    }

    @Override // com.lagunex.domino.fragments.GameFragmentCallback
    public void onEndGameResult(Game game2, int total1, int total2, int maximum, int gameType) {
        /*if (this.googleGameHelper.isSignedIn()) {
            AchievementController.getInstance().unlockAchievements(game2, this.googleGameHelper.getApiClient(), this);
        }

        sendGameScore(total1 - total2, maximum, gameType);
         */
    }

    @Override // com.lagunex.domino.FragmentStatusCallback
    public void onFragmentVisible(String fragmentName) {
        this.gameVisible = GameFragment.NAME.equals(fragmentName);
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.game, menu2);
        this.menu = menu2;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId() == R.id.action_settings) {
                Intent intent = new Intent(this, MySettingsActivity.class);
                startActivity(intent);
                Toast.makeText(this,getString(R.string.changes),Toast.LENGTH_LONG).show();
                return true;
            }
            else if (item.getItemId() == R.id.action_share){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Aplicación de Dominó Doble 9\n"+"disponible en Apklis...\n"+"https://www.apklis.cu/application/com.amg.double9domino";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
            return super.onOptionsItemSelected(item);
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    protected void onPause() {
        super.onPause();
        //this.uiHelper.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    protected void onResume() {
        super.onResume();
        //this.uiHelper.onResume();
        //AppEventsLogger.activateApp(this, getResources().getString(R.string.facebook_app_id));
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //this.uiHelper.onSaveInstanceState(outState);
    }

    @Override // com.lagunex.domino.fragments.GameFragmentCallback
    public void onShareGameResult(boolean win, String result) {
        //new ShareResultDialog(win, result, this, this.uiHelper).share();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    protected void onStart() {
        super.onStart();
        //this.googleGameHelper.onStart(this);
        //FlurryAgent.onStartSession(this, Flurry.ID);
    }


    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.ActionBarActivity, android.support.v4.app.FragmentActivity
    protected void onStop() {
        boolean pending;
        super.onStop();
        //this.googleGameHelper.onStop();
        //this.uiHelper.onStop();
        if (!getGame().isGameInTheMiddle() || !sendScore()) {
            pending = false;
        } else {
            pending = true;
        }
        Log.d(TAG, "onStop pending game=" + pending);
        //FlurryAgent.onEndSession(this);
        SharedPreferences.Editor editor = getPreferences(0).edit();
        if (pending) {
            editor.putInt(PENDING_GAME, getGame().getGameType());
        } else {
            editor.putInt(PENDING_GAME, -1);
        }
        editor.commit();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendGameScore(int score, int maximum, int gameType) {
        /*if (sendScore()) {
            Log.d(TAG, "sendGameScore " + score);
            PostScoreController post = new PostScoreController();
            post.setResult(score);
            post.setMaximum(maximum);
            post.setContext(this);
            if (gameType == 0) {
                post.setGameType(0);
            } else if (gameType == 1) {
                post.setGameType(1);
            }
            if (this.googleGameHelper.isSignedIn()) {
                GoogleApiClient apiClient = this.googleGameHelper.getApiClient();
                post.setGoogleApiClient(apiClient);
                post.setGoogleId(Games.getCurrentAccountName(apiClient));
            }
            new Thread(post).start();
            setSendScore(false);
        }*/
    }

    @Override // com.lagunex.domino.fragments.GameFragmentCallback
    public boolean sendScore() {
        return this.sendScore;
    }

    @Override // com.lagunex.domino.fragments.GameFragmentCallback
    public void setSendScore(boolean send) {
        Log.d(TAG, "setSendScore " + send);
        this.sendScore = send;
    }
    @Override // com.lagunex.domino.fragments.GameFragmentCallback
    public void onStartNextHand(Command callback) {
        //this.adEngine.showAd(callback);
        callback.execute();
    }
}
