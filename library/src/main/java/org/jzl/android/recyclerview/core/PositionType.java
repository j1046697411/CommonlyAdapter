package org.jzl.android.recyclerview.core;

public enum PositionType {
    HEADER(1), CONTENT(2), FOOTER(3);

    private int sequence;

    PositionType(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return sequence;
    }
}
