package datapacker.datapackers;

import datapacker.IDataPacker;
import store.FileStore;
import store.Indices;
import store.codec.ItemDefinition;
import store.codec.util.Constants;
import store.codec.util.Utils;
import store.io.InputStream;

public class OsrsItemPacker implements IDataPacker {

    @Override
    public void packAllData(FileStore osrsCache, FileStore destinationCache) {
        int osrsItemIndexSize = Utils.getItemDefinitionsSize(osrsCache);
        int packed = 0;
        for (int currentOsrsItemIndex = 0; currentOsrsItemIndex < osrsItemIndexSize; currentOsrsItemIndex++) {
            byte[] osrsItemData = osrsCache.getIndexes()[Indices.ITEMS.getIndex()].getFile(Utils.getConfigArchive(currentOsrsItemIndex, 8), Utils.getConfigFile(currentOsrsItemIndex, 8));
            if (osrsItemData == null) {
                System.out.println("No osrs data found for item " + currentOsrsItemIndex + ".");
                continue;
            }
            ItemDefinition definition = new ItemDefinition(currentOsrsItemIndex);
            definition.decode(new InputStream(osrsItemData));
            definition.notedID+= getDataOffset();
            definition.noteTemplate += getDataOffset();
            int newItemId = getDataOffset() + currentOsrsItemIndex;
            destinationCache.getIndexes()[Indices.ITEMS.getIndex()].putFile(Utils.getConfigArchive(newItemId, 8), Utils.getConfigFile(newItemId, 8), Constants.GZIP_COMPRESSION, definition.encode(), null, false, false, -1, -1);
            System.out.println("Repacked item " + currentOsrsItemIndex + " into new id "+newItemId);
            packed++;
        }
        System.out.println("Repacked " + packed + " new item configs.");
        System.out.println("Rewritting reference table...");
        destinationCache.getIndexes()[Indices.ITEMS.getIndex()].rewriteTable();
        System.out.println("Done packing osrs items.");
    }

    @Override
    public int getDataOffset() {
        return 30_000;
    }
}
