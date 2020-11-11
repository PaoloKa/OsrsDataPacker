package datapacker.datapackers;

import datapacker.IDataPacker;
import store.FileStore;
import store.codec.SpotDefinition;
import store.codec.util.Constants;
import store.codec.util.Utils;
import store.io.InputStream;

public class OsrsGfxPacker implements IDataPacker {

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        int length = sourceCache.getIndexes()[2].getValidFilesCount(13);
        for (int index = 0; index < length; index++) {
            byte[] data = sourceCache.getIndexes()[2].getFile(13, index);
            if(data == null)
                continue;
            SpotDefinition def = new SpotDefinition((index + getDataOffset()));
            def.decode(new InputStream(data));
            destinationCache.getIndexes()[21].putFile(Utils.getConfigArchive((index + getDataOffset()), 8),
                    Utils.getConfigFile((index + getDataOffset()), 8), Constants.GZIP_COMPRESSION, def.encode(), null, false, false, -1, -1);
            System.out.println("Packed " + (index + getDataOffset()) + " succesfully..");
        }
        System.out.println("Rewriting table..");
        destinationCache.getIndexes()[21].rewriteTable();
    }

    @Override
    public int getDataOffset() {
        return 5000;
    }
}
