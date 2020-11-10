package datapacker.datapackers;

import datapacker.PackerUtils;
import store.FileStore;
import store.codec.util.Constants;

import static store.codec.util.Utils.getConfigArchive;
import static store.codec.util.Utils.getConfigFile;

public class OsrsObjectPacker implements IDataPacker{

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        double percentage;
        int osrsObjectIndexSize = sourceCache.getIndexes()[2].getLastFileId(6);
        for (int id = 0; id < osrsObjectIndexSize; id++) {
            byte[] data = sourceCache.getIndexes()[2].getFile(6, id);
            if (data == null)
                continue;
            destinationCache.getIndexes()[16].putFile(getConfigArchive(id + getDataOffset(), 8), getConfigFile(id + getDataOffset(), 8), Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            percentage = (double) id / (double) osrsObjectIndexSize * 100D;
            System.out.println("Packing objects: " + PackerUtils.format.format(percentage) + "%");
        }
        System.out.println("Rewriting table..");
        destinationCache.getIndexes()[16].rewriteTable();
    }

    @Override
    public int getDataOffset() {
        return 100000;
    }
}
