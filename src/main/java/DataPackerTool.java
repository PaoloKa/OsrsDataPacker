
import java.io.File;
import java.io.IOException;

import datapacker.IDataPacker;
import lombok.var;
import store.FileStore;
import utility.ClassFinder;

public class DataPackerTool {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Starting packer");
        FileStore destinationCache = new FileStore(Settings.CACHE_DIR);
        FileStore sourceCache = new FileStore(Settings.OSRS_CACHE);
        var classFinder = new ClassFinder();
        var packers = classFinder.findClasses(new File("./out/production/OsrsDataPacker/datapacker/datapackers"),"datapacker.datapackers");
        System.out.println("Packers found: "+packers.size());
        packers.forEach(x -> {
            try {
                System.out.println("Starting: "+x.getSimpleName());
                 var packer = ((IDataPacker)x.newInstance());
                  packer.packAllData(sourceCache,destinationCache);
            } catch (InstantiationException e) {
                      e.printStackTrace();
            } catch (IllegalAccessException e) {
                      e.printStackTrace();
            }
        });
        System.out.println("Packer finished");
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