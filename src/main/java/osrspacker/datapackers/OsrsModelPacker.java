package osrspacker.datapackers;

import osrspacker.OSRSPacker;
import store.FileStore;
import store.codec.util.Constants;

public class OsrsModelPacker implements DataPacker {

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        double percentage;
        int valid_models = sourceCache.getIndexes()[7].getValidArchivesCount() + 1;
        for (int index = 0; index < valid_models; index++) {
            byte[] data = sourceCache.getIndexes()[7].getFile(index);
            if (data == null)
                continue;
            destinationCache.getIndexes()[7].putFile((index + getDataOffset()), 0, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            percentage = (double) index / (double) valid_models * 100D;
            System.out.println("Packing models: " + OSRSPacker.format.format(percentage) + "%");
        }
        System.out.println("Rewriting table..");
        destinationCache.getIndexes()[7].rewriteTable();
    }

    @Override
    public int getDataOffset() {
        return 100_000;
    }
}
