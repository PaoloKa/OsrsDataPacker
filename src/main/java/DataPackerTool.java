
import java.io.IOException;
import java.util.ServiceLoader;

import datapacker.datapackers.IDataPacker;
import store.FileStore;

public class DataPackerTool {

    public static void main(String[] args) throws IOException {
//        FileStore destinationCache = new FileStore(Settings.CACHE_DIR);
//        FileStore sourceCache = new FileStore(Settings.OSRS_CACHE);
        ServiceLoader<IDataPacker> loader = ServiceLoader.load(IDataPacker.class);
        for (IDataPacker implClass : loader) {
            System.out.println(implClass.getClass().getSimpleName());
        }

    }



//    private static final void transfer_flos(FileStore osrs_cache, FileStore cache) throws IOException {
//        for (int file = 0; file < 200; file++) {
//            byte[] data = osrs_cache.getIndexes()[2].getFile(4, file);
//            if (data == null)
//                continue;
//            int id = (file + OVERLAY_OFFSET);
//            System.out.println("Packing overlay " + id + ", size: " + data.length);
//            cache.getIndexes()[2].putFile(4, id, data);
//            System.out.println("Packed overlay " + file + " as overlay " + id + ".");
//        }
//    }
//
//    private static final void transfer_flus(FileStore osrs_cache, FileStore cache) throws IOException {
//        for (int file = 0; file < 200; file++) {
//            byte[] data = osrs_cache.getIndexes()[Indices.CONFIG.getIndex()].getFile(ConfigArchive.UNDERLAYS.getValue(), file);
//            if (data == null)
//                continue;
//            int id = (file + UNDERLAY_OFFSET);
//            System.out.println("Packing underlay " + id + ", size: " + data.length);
//            cache.getIndexes()[Indices.CONFIG.getIndex()].putFile(ConfigArchive.UNDERLAYS.getValue(), id, data);
//            System.out.println("Packed underlay " + file + " as underlay " + id + ".");
//        }
//    }
}