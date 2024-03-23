package com.amg.double9domino.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amg.double9domino.FragmentStatusCallback;
import com.amg.double9domino.GameActivity;
import com.amg.double9domino.R;
import com.amg.double9domino.model.Game;
import com.amg.double9domino.model.Hand;

public class ScoreFragment extends Fragment {
    public static String NAME = ScoreFragment.class.getName();
    private Typeface font;
    private Game game;
    private LinearLayout scoresLayout;
    private FragmentStatusCallback statusCallback;
    private TextView team1;
    private TextView team2;

    public void setGame(Game game2) {
        this.game = game2;
    }

    public void setStatusCallback(FragmentStatusCallback statusCallback2) {
        this.statusCallback = statusCallback2;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_score, container, false);
        this.scoresLayout = (LinearLayout) v.findViewById(R.id.game_scores);
        this.team1 = (TextView) v.findViewById(R.id.game_score_team1);
        this.team2 = (TextView) v.findViewById(R.id.game_score_team2);
        this.font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/WalterTurncoat.ttf");
        return v;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.statusCallback != null) {
            this.statusCallback.onFragmentVisible(NAME);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.game_score));
        initScore();
    }

    private void initScore() {
        if (this.game != null) {
            this.team1.setText(this.game.getTeam1().getName());
            this.team1.setTypeface(this.font);
            this.team2.setText(this.game.getTeam2().getName());
            this.team2.setTypeface(this.font);
            int total1 = 0;
            int total2 = 0;
            for (int i = 0; i < this.game.getHandsCount(); i++) {
                Hand h = this.game.getHand(i);
                if (h.hasEnded()) {
                    total1 += h.getTotal1();
                    total2 += h.getTotal2();
                    this.scoresLayout.addView(getViewPartials(h.getTotal1(), total1, h.getTotal2(), total2));
                }
            }
        }
    }

    private View getViewPartials(int partial1, int total1, int partial2, int total2) {
        LinearLayout row = new LinearLayout(getActivity());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(getViewPartial(partial1, total1));
        row.addView(getViewPartial(partial2, total2));
        return row;
    }

    private View getViewPartial(int partial, int total) {
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        tv.setGravity(17);
        tv.setTypeface(this.font);
        tv.setText(String.valueOf(partial) + " - " + total);
        tv.setTextColor(-1);
        tv.setTextSize(20.0f);
        return tv;
    }
}
