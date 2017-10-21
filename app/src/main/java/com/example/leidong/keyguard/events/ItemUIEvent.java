package com.example.leidong.keyguard.events;

/**
 * Created by leidong on 2017/10/15
 */

public class ItemUIEvent {
    public static final int LIST_SCROLLED = 0xE1;
    public static final int LIST_STOPPED  = 0xE2;

    private int type;
    private int dy;

    public ItemUIEvent(int type, int dy) {
        this.type = type;
        this.dy = dy;
    }

    public int getType() {
        return type;
    }

    public int getDy() {
        return dy;
    }
}
