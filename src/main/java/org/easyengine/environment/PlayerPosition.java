package org.easyengine.environment;

import java.util.Objects;

public class PlayerPosition {

    public enum PositionX {
        GK, D, M, F
    }

    public enum PositionY {
        R, L, C, C_R, C_L, RC, LC
    }

    private PositionX x;
    private PositionY y;

    public PlayerPosition(PositionX x) {
        this.x = x;
    }

    public PlayerPosition(PositionX x, PositionY y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerPosition)) return false;
        PlayerPosition that = (PlayerPosition) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
