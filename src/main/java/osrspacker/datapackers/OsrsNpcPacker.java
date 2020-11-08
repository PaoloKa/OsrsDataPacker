package osrspacker.datapackers;

import store.FileStore;

public class OsrsNpcPacker implements DataPacker{
    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {

    }

    @Override
    public int getDataOffset() {
        return 20_000;
    }
}
