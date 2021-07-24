package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

public class ActionOutcomeDetails {

    protected ActionType actionType;
    protected PitchPosition initialPosition;
    protected PitchPosition targetPosition;
    protected ActionOutcome actionOutcome;

    public ActionOutcomeDetails() {}

    public ActionOutcomeDetails(ActionType actionType, PitchPosition initialPosition, PitchPosition targetPosition, ActionOutcome actionOutcome) {
        this.actionType = actionType;
        this.initialPosition = initialPosition;
        this.targetPosition = targetPosition;
        this.actionOutcome = actionOutcome;
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