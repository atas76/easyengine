package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

public class Outcome {

    private ActionType actionType;
    private PitchPosition initialPosition;
    private PitchPosition targetPosition;
    private ActionOutcome actionOutcome;

    public Outcome(ActionType actionType, PitchPosition initialPosition, PitchPosition targetPosition, ActionOutcome actionOutcome) {
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

    public PitchPosition getInitialPosition() {
        return initialPosition;
    }

    public PitchPosition getTargetPosition() {
        return targetPosition;
    }

    public ActionOutcome getActionOutcome() {
        return actionOutcome;
    }
}
