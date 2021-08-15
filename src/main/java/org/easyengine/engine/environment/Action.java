package org.easyengine.engine.environment;

import org.easyengine.engine.ActionType;
import org.easyengine.engine.space.PitchPosition;

import java.util.Objects;

public class Action extends org.easyengine.engine.Action implements Comparable<Action> {

    private PitchPosition source;

    public Action(ActionType type, PitchPosition source, PitchPosition target) {
        super(type, target);
        this.source = source;
    }

    public PitchPosition getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Action action = (Action) o;
        return source == action.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source);
    }


    @Override
    public int compareTo(Action a) {
        if (this.getType() != a.getType()) {
            return this.getType().compareTo(a.getType());
        }
        if (this.getSource() != a.getSource()) {
            return this.getSource().compareTo(a.getSource());
        }
        return this.getTarget().compareTo(a.getTarget());
    }
}
