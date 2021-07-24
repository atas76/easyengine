package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

public class Action {

    private ActionType type;
    public PitchPosition target;

    public Action(ActionType type, PitchPosition target) {
        this.type = type;
        this.target = target;
    }

    public ActionType getType() {
        return type;
    }

    public PitchPosition getTarget() {
        return target;
    }
}
