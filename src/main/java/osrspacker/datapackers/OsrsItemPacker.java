package osrspacker.datapackers;

import store.FileStore;
import store.Indices;
import store.codec.ItemDefinition;
import store.codec.util.Constants;
import store.codec.util.Utils;
import store.io.InputStream;

public class OsrsItemPacker implements  DataPacker{

    @Override
    public void packAllData(FileStore osrsCache, FileStore destinationCache) {
        int osrsItemIndexSize = Utils.getItemDefinitionsSize(osrsCache);
        int packed = 0;
        for (int currentOsrsItemIndex = 0; currentOsrsItemIndex < osrsItemIndexSize; currentOsrsItemIndex++) {
            byte[] data = destinationCache.getIndexes()[Indices.ITEMS.getIndex()].getFile(Utils.getConfigArchive(currentOsrsItemIndex, 8), Utils.getConfigFile(currentOsrsItemIndex, 8));
            if (data == null) {
                System.out.println("No old data for " + currentOsrsItemIndex + ".");
                continue;
            }
            xd
            ItemDefinition definition = new ItemDefinition(currentOsrsItemIndex);
            definition.decode(new InputStream(data));
            data = osrsCache.getIndexes()[Indices.ITEMS.getIndex()].getFile(Utils.getConfigArchive(currentOsrsItemIndex, 8), Utils.getConfigFile(currentOsrsItemIndex, 8));
            if (data == null) {
                System.out.println("No osrs data found for item " + currentOsrsItemIndex + ".");
                continue;
            }
            ItemDefinition new_definition = new ItemDefinition(currentOsrsItemIndex);
            new_definition.decode(new InputStream(data));
            definition.equipSlot = new_definition.equipSlot;
            definition.equipType = new_definition.equipType;
            int newItemId = getDataOffset() + currentOsrsItemIndex;
            destinationCache.getIndexes()[Indices.ITEMS.getIndex()].putFile(Utils.getConfigArchive(newItemId, 8), Utils.getConfigFile(newItemId, 8), Constants.GZIP_COMPRESSION, definition.encode(), null, false, false, -1, -1);
            System.out.println("Repacked item " + currentOsrsItemIndex + " into new id "+newItemId);
            packed++;
        }
        System.out.println("Repacked " + packed + " new item configs.");
        System.out.println("Rewritting reference table...");
        destinationCache.getIndexes()[Indices.ITEMS.getIndex()].rewriteTable();
        System.out.println("Done.");
    }

    @Override
    public int getDataOffset() {
        return 30_000;
    }
}
