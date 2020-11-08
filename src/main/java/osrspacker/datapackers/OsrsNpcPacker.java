package osrspacker.datapackers;

import lombok.var;
import store.FileStore;
import store.Indices;
import store.codec.BASDefinition;
import store.codec.osrs.NPCDefinition;

import java.util.HashMap;

public class OsrsNpcPacker implements DataPacker{


    private HashMap<Integer, BASDefinition> packedRenderDefinitions;

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        int osrsNpcIndexSize = sourceCache.getIndexes()[2].getValidFilesCount(9);
        packedRenderDefinitions = new HashMap<Integer, BASDefinition>();
        for (int index = 0; index < osrsNpcIndexSize; index++) {
            byte[] data = sourceCache.getIndexes()[2].getFile(9, index);
            var osrsDef = new store.codec.osrs.NPCDefinition(data);
            var newNpcId = index + getDataOffset();
            var def = new store.codec.NPCDefinition(newNpcId);

            def.name = osrsDef.name;
            def.size = osrsDef.size;
            def.modelIds = osrsDef.models;
            def.options = osrsDef.options;
            def.originalModelColors = osrsDef.recolorToFind;
            def.modifiedModelColors = osrsDef.recolorToReplace;
            def.originalTextureColors = osrsDef.retextureToFind;
            def.modifiedTextureColors = osrsDef.retextureToReplace;
            def.npcChatHeads = osrsDef.chatHeads;
            def.invisibleOnMap = !osrsDef.invisable;
            def.combatLevel = osrsDef.combatLevel;
            def.npcWidth = osrsDef.resizeX;
            def.npcHeight = osrsDef.resizeY;
            def.renderPriority = osrsDef.renderPriority;
            def.ambient = osrsDef.ambient;
            def.contrast = osrsDef.contrast;
            def.headIcon = osrsDef.headIcon;
//            def.anInt853 = osrsDef.anInt6181;
//            def.bConfig = osrsDef.bConfig;
//            def.config = osrsDef.config;
            def.aBoolean852 = osrsDef.aBoolean6164;
            def.aBoolean857 = osrsDef.aBoolean6165;
            def.renderEmote = (index + 3000);

            def.save(destinationCache);
            System.out.println("Saving npc definition: " +index+" to "+newNpcId);
            //Todo find a way to use the same bas for npcs with the same values, EG: man
            var bas = createBAS(index, osrsDef);
            bas.save(destinationCache);
            System.out.println("Created new BAS " + bas.id +" for npc "+osrsDef.name);
        }
        destinationCache.getIndexes()[Indices.NPC_DEFINITION.getIndex()].rewriteTable();
        destinationCache.getIndexes()[Indices.CONFIG.getIndex()].rewriteTable();
    }

    private BASDefinition createBAS(int id, store.codec.osrs.NPCDefinition osrsDef){
        var bas = new BASDefinition(id + getBasOffset());
        if (osrsDef.idleAnimation > 0)
            bas.idle = osrsDef.idleAnimation;
        if (osrsDef.walkAnimation > 0)
            bas.walking = osrsDef.walkAnimation;
        if (osrsDef.anInt2165 > 0)
            bas.running = osrsDef.anInt2165;
        if (osrsDef.rotate90LeftAnimation > 0)
            bas.strafe_left = osrsDef.rotate90LeftAnimation;
        if (osrsDef.rotate90RightAnimation > 0)
            bas.strafe_right = osrsDef.rotate90RightAnimation;
        if (osrsDef.rotate180Animation > 0)
            bas.backwards = osrsDef.rotate180Animation;
        return bas;
    }

    public int getBasOffset(){
        return 3000;
    }

    @Override
    public int getDataOffset() {
        return 20_000;
    }
}
