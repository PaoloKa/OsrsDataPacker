package osrspacker.datapackers;

import store.FileStore;

public interface DataPacker {

    void packAllData(FileStore sourceCache, FileStore destinationCache);
    int getDataOffset();
}
