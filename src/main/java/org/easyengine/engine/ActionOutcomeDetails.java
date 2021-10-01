package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

public class ActionOutcomeDetails {

    protected ActionType actionType;
    protected PitchPosition initialPosition;
    protected PitchPosition targetPosition;
    protected ActionOutcome actionOutcome;
    protected ShotOutcome shotOutcome;

    public ActionOutcomeDetails() {}

    public ActionOutcomeDetails(ActionType actionType, PitchPosition initialPosition, PitchPosition targetPosition, ActionOutcome actionOutcome) {
        this.actionType = actionType;
        this.initialPosition = initialPosition;
        this.targetPosition = targetPosition;
        this.actionOutcome = actionOutcome;
    }

    public ActionOutcomeDetails(ActionType actionType, PitchPosition initialPosition, ShotOutcome shotOutcome) {
        this.actionType = actionType;
        this.initialPosition = initialPosition;
        this.shotOutcome = shotOutcome;
    }

    @Override
    public String toString() {
        return "Initial position = " + initialPosition + ", Target position = " + targetPosition + ", Outcome = " + actionOutcome;
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

    public ShotOutcome getShotOutcome() {
        return shotOutcome;
    }
}
