package com.amg.double9domino.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amg.double9domino.FragmentStatusCallback;
import com.amg.double9domino.GameActivity;
import com.amg.double9domino.R;
import com.amg.double9domino.model.Hand;
import com.amg.double9domino.model.Tile;
import com.amg.double9domino.widget.HistoryAdapter;

public class HistoryFragment extends Fragment {
    public static String NAME = HistoryFragment.class.getName();
    private HistoryAdapter<Tile> adapter;
    private Hand hand;
    private ListView history;
    private FragmentStatusCallback statusCallback;

    private void initTiles() {
        if (this.hand != null) {
            for (int i = this.hand.getPlayCount() - 1; i >= 0; i--) {
                this.adapter.add(this.hand.getPlayInTurn(i));
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.adapter = new HistoryAdapter<>(getActivity(), 0);
        this.history = new ListView(getActivity());
        this.history.setAdapter((ListAdapter) this.adapter);
        this.history.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        return this.history;
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.adapter.clear();
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.statusCallback != null) {
            this.statusCallback.onFragmentVisible(NAME);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.game_history));
        initTiles();
    }

    public void setHand(Hand currentHand) {
        this.hand = currentHand;
    }

    public void setStatusCallback(FragmentStatusCallback statusCallback2) {
        this.statusCallback = statusCallback2;
    }
}
