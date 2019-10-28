package de.ellpeck.rockbottom.api.render;

import de.ellpeck.rockbottom.api.RockBottomAPI;

public interface Camera {
    double getLerpedX();
    double getLerpedY();
    default float getLerpedScale() {
        return RockBottomAPI.getGame().getSettings().renderScale;
    }
}