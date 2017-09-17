package de.ellpeck.rockbottom.api.net.chat.component;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.data.set.DataSet;

import java.util.logging.Level;

public abstract class ChatComponent{

    private ChatComponent child;

    public ChatComponent(){

    }

    public static ChatComponent createFromSet(DataSet set){
        int id = set.getInt("id");

        try{
            Class<? extends ChatComponent> theClass = RockBottomAPI.CHAT_COMPONENT_REGISTRY.get(id);

            ChatComponent component = theClass.newInstance();
            component.load(set);

            return component;
        }
        catch(Exception e){
            RockBottomAPI.logger().log(Level.SEVERE, "Couldn't read chat component with id "+id+" from data set "+set+"! Does it have a default constructor?", e);
            return null;
        }
    }

    public ChatComponent append(ChatComponent component){
        if(this.child != null){
            this.child.append(component);
        }
        else{
            this.child = component;
        }

        return this;
    }

    public ChatComponent getAppendage(){
        return this.child;
    }

    public abstract String getDisplayString(IGameInstance game, IAssetManager manager);

    public abstract String getUnformattedString();

    public String getDisplayWithChildren(IGameInstance game, IAssetManager manager){
        String s = this.getDisplayString(game, manager);
        if(this.child != null){
            s += this.child.getDisplayWithChildren(game, manager);
        }
        return s;
    }

    public String getUnformattedWithChildren(){
        String s = this.getUnformattedString();
        if(this.child != null){
            s += this.child.getUnformattedWithChildren();
        }
        return s;
    }

    public void save(DataSet set){
        set.addInt("id", RockBottomAPI.CHAT_COMPONENT_REGISTRY.getId(this.getClass()));

        if(this.child != null){
            DataSet subSet = new DataSet();
            this.child.save(subSet);
            set.addDataSet("child", subSet);
        }
    }

    public void load(DataSet set){
        if(set.hasKey("child")){
            DataSet subSet = set.getDataSet("child");
            this.child = createFromSet(subSet);
        }
    }

    @Override
    public String toString(){
        return this.getUnformattedWithChildren();
    }
}
