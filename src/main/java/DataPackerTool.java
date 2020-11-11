
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
}