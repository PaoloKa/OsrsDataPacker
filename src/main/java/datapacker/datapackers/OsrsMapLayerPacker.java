package datapacker.datapackers;

import datapacker.IDataPacker;
import store.ConfigArchive;
import store.FileStore;
import store.Indices;

public class OsrsMapLayerPacker implements IDataPacker {

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        for (int file = 0; file < 200; file++) {
            byte[] data = sourceCache.getIndexes()[2].getFile(4, file);
            if (data == null)
                continue;
            int id = (file + getDataOffset());
            System.out.println("Packing overlay " + id + ", size: " + data.length);
            destinationCache.getIndexes()[2].putFile(4, id, data);
            System.out.println("Packed overlay " + file + " as overlay " + id + ".");
        }

        for (int file = 0; file < 200; file++) {
            byte[] data = sourceCache.getIndexes()[Indices.CONFIG.getIndex()].getFile(ConfigArchive.UNDERLAYS.getValue(), file);
            if (data == null)
                continue;
            int id = (file + getDataOffset());
            System.out.println("Packing underlay " + id + ", size: " + data.length);
            destinationCache.getIndexes()[Indices.CONFIG.getIndex()].putFile(ConfigArchive.UNDERLAYS.getValue(), id, data);
            System.out.println("Packed underlay " + file + " as underlay " + id + ".");
        }
    }

    @Override
    public int getDataOffset() {
        return 1000;
    }
}
