package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

import java.util.Objects;

public class Action {

    private ActionType type;
    public PitchPosition target;

    public Action(ActionType type) {
        this.type = type;
    }

    public Action(ActionType type, PitchPosition target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public String toString() {
        return "type = " + type + ", target = " + target;
    }

    public ActionType getType() {
        return type;
    }

    public PitchPosition getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return type == action.type && target == action.target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, target);
    }
}
