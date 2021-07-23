package org.easyengine.engine;

import org.easyengine.engine.space.Position;

public class Outcome {

    private ActionType actionType;
    private Position initialPosition;
    private Position targetPosition;
    private ActionOutcome actionOutcome;

    public Outcome(ActionType actionType, Position initialPosition, Position targetPosition, ActionOutcome actionOutcome) {
        this.actionType = actionType;
        this.initialPosition = initialPosition;
        this.targetPosition = targetPosition;
        this.actionOutcome = actionOutcome;
    }

    public enum ActionOutcome {
        SUCCESS, FAIL;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Position getInitialPosition() {
        return initialPosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public ActionOutcome getActionOutcome() {
        return actionOutcome;
    }
}
