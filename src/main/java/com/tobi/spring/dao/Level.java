package com.tobi.spring.dao;

/**
 * Created by 김영환 on 2016-10-16.
 */
public enum Level {
    //declare Level enum in order
    GOLD(3, null), SILVER(2, GOLD), BASIC(1,SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return value;
    }

    public Level nextLevel() {
        return this.next;
    }

    public static Level valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknonw value:" + value);
        }
    }
}
