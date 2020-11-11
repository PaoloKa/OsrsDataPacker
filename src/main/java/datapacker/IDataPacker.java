package datapacker;

import store.FileStore;

public interface IDataPacker {

    void packAllData(FileStore sourceCache, FileStore destinationCache);
    int getDataOffset();
}
