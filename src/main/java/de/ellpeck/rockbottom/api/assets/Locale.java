/*
 * This file ("Locale.java") is part of the RockBottomAPI by Ellpeck.
 * View the source code at <https://github.com/RockBottomGame/>.
 * View information on the project at <https://rockbottom.ellpeck.de/>.
 *
 * The RockBottomAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The RockBottomAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the RockBottomAPI. If not, see <http://www.gnu.org/licenses/>.
 *
 * © 2018 Ellpeck
 */

package de.ellpeck.rockbottom.api.assets;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.util.ApiInternal;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.Collections;
import java.util.Map;

@ApiInternal
public final class Locale implements IAsset {

    public static final ResourceName ID = ResourceName.intern("loc");

    private final String name;
    private final Map<ResourceName, String> localization;

    public Locale(String name, Map<ResourceName, String> localization) {
        this.name = name;
        this.localization = localization;
    }

    public String localize(Locale fallback, ResourceName unloc, Object... format) {
        String loc = this.localization.get(unloc);

        if (loc == null) {
            if (fallback != null && fallback != this) {
                loc = fallback.localize(null, unloc, format);
            } else {
                loc = unloc.toString();
            }

            this.localization.put(unloc, loc);

            RockBottomAPI.logger().warn("Localization with name " + unloc + " is missing from locale with name " + this.name + '!');
        }

        if (format == null || format.length <= 0) {
            return loc;
        } else {
            return String.format(loc, format);
        }
    }

    @ApiInternal
    public void override(Map<ResourceName, String> newLocalization) {
        this.localization.putAll(newLocalization);
    }

    public String getName() {
        return this.name;
    }

    public Map<ResourceName, String> getLocalization() {
        return Collections.unmodifiableMap(this.localization);
    }

    @Override
    public void dispose() {

    }
}
