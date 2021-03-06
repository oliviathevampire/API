/*
 * This file ("ICompendiumRecipe.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.construction.compendium;

import de.ellpeck.rockbottom.api.Registries;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.content.IContent;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentConstruct;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentIngredient;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentPolaroid;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.entity.TileEntity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.ArrayList;
import java.util.List;

// TODO 0.4 Rename to IRecipe (There is no distinction between a Compendium and normal Recipe)
public interface ICompendiumRecipe extends IContent {

    ResourceName getName();

    List<IUseInfo> getInputs();

    List<ItemInstance> getOutputs();

    default boolean isKnown(AbstractEntityPlayer player) {
        return true;
    }

    default boolean isKnowledge() {
        return false;
    }

    default boolean canConstruct(IInventory inputInventory, IInventory outputInventory) {
        for (IUseInfo info : this.getActualInputs(inputInventory)) {
            if (!inputInventory.containsResource(info)) {
                return false;
            }
        }
        return true;
    }

    static ICompendiumRecipe forName(ResourceName name) {
    	return Registries.ALL_RECIPES.get(name);
	}

    default List<IUseInfo> getActualInputs(IInventory inventory) {
        return this.getInputs();
    }

    default List<ItemInstance> getActualOutputs(IInventory inputInventory, IInventory outputInventory, List<ItemInstance> inputs) {
        return this.getOutputs();
    }

    default List<ComponentIngredient> getIngredientButtons(Gui gui, AbstractEntityPlayer player, ResourceName tex) {
        List<ComponentIngredient> ingredients = new ArrayList<>();
		for (IUseInfo info : this.getInputs()) {
			ingredients.add(new ComponentIngredient(gui, player.getInv().containsResource(info), info.getItems(), tex));
		}
		return ingredients;
    }

    default ComponentConstruct getConstructButton(Gui gui, AbstractEntityPlayer player, TileEntity machine, boolean canConstruct) {
        return new ComponentConstruct(gui, this, true, canConstruct, null);
    }

    default ComponentPolaroid getPolaroidButton(Gui gui, AbstractEntityPlayer player, boolean canConstruct, ResourceName tex) {
        return new ComponentPolaroid(gui, this, canConstruct, tex);
    }
}
