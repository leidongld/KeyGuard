package com.example.leidong.keyguard.listeners;

import android.support.v7.widget.RecyclerView;

/**
 * Created by leidong on 2017/10/15
 */

public class OnCardsScrollListener extends RecyclerView.OnScrollListener{
    private RecyclerView recyclerView;

    public OnCardsScrollListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        int first;
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){

    }
}
