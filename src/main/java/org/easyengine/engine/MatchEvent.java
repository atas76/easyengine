package org.easyengine.engine;

public class MatchEvent extends ActionOutcomeDetails {

    public MatchEvent(ActionOutcomeDetails actionOutcomeDetails) {
        this.actionType = actionOutcomeDetails.actionType;
        this.initialPosition = actionOutcomeDetails.initialPosition;
        this.targetPosition = actionOutcomeDetails.targetPosition;
        this.actionOutcome = actionOutcomeDetails.actionOutcome;
    }
}
