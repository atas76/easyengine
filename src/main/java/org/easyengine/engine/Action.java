package org.easyengine.engine;

import org.easyengine.engine.space.Position;

public class Action {

    private ActionType type;
    public Position target;

    public Action(ActionType type, Position target) {
        this.type = type;
        this.target = target;
    }
}
