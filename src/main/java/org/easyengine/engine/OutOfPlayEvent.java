package org.easyengine.engine;

public class OutOfPlayEvent extends MatchEvent {

    public OutOfPlayEvent(BallPlayState playState) {
        super(playState);
    }

    @Override
    public String toString() {
        return "{ " + this.ballPlayState + " }";
    }
}
