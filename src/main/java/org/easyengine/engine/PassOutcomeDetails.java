package org.easyengine.engine;

import org.easyengine.engine.space.PitchPosition;

import static org.easyengine.engine.ActionType.PASS;

public class PassOutcomeDetails extends ActionOutcomeDetails {

    public PassOutcomeDetails(ActionType actionType, PitchPosition initialPosition, PitchPosition targetPosition, ActionOutcome actionOutcome) {
        this.actionType = PASS;

    }
}
