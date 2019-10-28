/*
 * This file ("MultiTileRenderer.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.render.tile;

import com.google.gson.JsonElement;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.IRenderer;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.texture.ITexture;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.MultiMetaTile;
import de.ellpeck.rockbottom.api.tile.MultiTile;
import de.ellpeck.rockbottom.api.tile.state.TileState;
import de.ellpeck.rockbottom.api.util.Pos2;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.util.HashMap;
import java.util.Map;

public class MultiMetaTileRenderer<T extends MultiMetaTile> extends DefaultTileRenderer<T> {

    protected final ResourceName texItem;
    protected final Map<Pos2, ResourceName> textures = new HashMap<>();

    public MultiMetaTileRenderer(ResourceName texture, T tile) {
        super(texture);
        this.texItem = this.texture.addSuffix(".item");

        for (int x = 0; x < tile.getWidth(); x++) {
            for (int y = 0; y < tile.getHeight(); y++) {
                if (tile.isStructurePart(x, y)) {
                    this.textures.put(new Pos2(x, y), this.texture.addSuffix("." + x + '.' + y));
                }
            }
        }
    }

    @Override
    public void renderItem(IGameInstance game, IAssetManager manager, IRenderer g, T tile, ItemInstance instance, float x, float y, float scale, int filter) {
        this.setTextureResource(tile, instance.getMeta());
        super.renderItem(game, manager, g, tile, instance, x, y, scale, filter);
    }

    @Override
    public void renderInMainMenuBackground(IGameInstance game, IAssetManager manager, IRenderer g, T tile, TileState state, float x, float y, float scale) {
        this.setTextureResource(tile, state.get(tile.metaProp));
        super.renderInMainMenuBackground(game, manager, g, tile, state, x, y, scale);
    }

    @Override
    public ITexture getParticleTexture(IGameInstance game, IAssetManager manager, IRenderer g, T tile, TileState state) {
        this.setTextureResource(tile, state.get(tile.metaProp));
        Pos2 innerCoord = tile.getInnerCoord(state);
        return manager.getTexture(this.textures.get(innerCoord));
    }

    protected ResourceName getTextureResource(T tile, int meta) {
        if (meta >= 0 && meta < tile.subResourceNames.size()) {
            return tile.subResourceNames.get(meta);
        } else {
            return null;
        }
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, IRenderer g, IWorld world, T tile, TileState state, int x, int y, TileLayer layer, float renderX, float renderY, float scale, int[] light) {
        this.setTextureResource(tile, state.get(tile.metaProp));
        manager.getTexture(this.texItem).draw(x, y, scale, scale, light);
        super.render(game, manager, g, world, tile, state, x, y, layer, renderX, renderY, scale, light);
    }

    private void setTextureResource(T tile, int meta) {
        this.texture = getTextureResource(tile, meta);
    }

    @Override
    public JsonElement getAdditionalTextureData(IGameInstance game, IAssetManager manager, IRenderer g, T tile, ItemInstance instance, AbstractEntityPlayer player, String name) {
        return manager.getTexture(this.getTextureResource(tile, instance.getMeta())).getAdditionalData(name);
    }

}
