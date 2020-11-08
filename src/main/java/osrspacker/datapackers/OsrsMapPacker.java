package osrspacker.datapackers;

import osrspacker.OSRSPacker;
import osrspacker.PackerUtils;
import store.FileStore;
import store.codec.util.Constants;
import store.codec.util.Utils;
import utility.XTEASManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class OsrsMapPacker implements DataPacker{

    @Override
    public void packAllData(FileStore sourceCache, FileStore destinationCache) {
        double percentage;
        try {
            dump_maps(sourceCache);
        } catch (Exception ex){
            System.out.println("There was an error dumping the map files: "+ex.getMessage());
        }
        for (int index = 0; index < regionIds.length; index++) {
            pack_region(destinationCache, regionIds[index], regionIds[index]);
            percentage = (double) index / (double) (regionIds.length) * 100D;
            System.out.println("Packing regions: " + OSRSPacker.format.format(percentage) + "%");
        }
        System.out.println("Done packing regions.");
    }

    public static void pack_region(FileStore cache, int fromRegion, int toRegion) {
        try {
            int fromRegionId = fromRegion;
            int fromRegionX = (fromRegionId >> 8);
            int fromRegionY = (fromRegionId & 0xff);

            int toRegionId = toRegion;
            int toRegionX = (toRegionId >> 8);
            int toRegionY = (toRegionId & 0xff);

            int[] xteas = XTEASManager.lookup(fromRegionId);

            int nameHash;
            int archiveId;
            byte[] data;

            //Map data
            File file = new File("./maps/m" + fromRegionX + "_" + fromRegionY + ".dat");
            if (file.exists()) {
                String name = PackerUtils.stripDat(file.getName());
                data = Files.readAllBytes(file.toPath());
                nameHash = Utils.getNameHash(name);
                archiveId = cache.getIndexes()[5].getArchiveId(nameHash);
                if (archiveId == -1) {
                    archiveId = cache.getIndexes()[5].getLastArchiveId() + 1;
                }
                cache.getIndexes()[5].putFile(archiveId, 0, Constants.GZIP_COMPRESSION, data, null, false, false, nameHash, -1);
            }
            //Location data
            file = new File("./maps/l" + fromRegionX + "_" + fromRegionY + ".dat");
            if (file.exists()) {
                String name = PackerUtils.stripDat(file.getName());
                data = Files.readAllBytes(file.toPath());
                nameHash = Utils.getNameHash(name);
                archiveId = cache.getIndexes()[5].getArchiveId(nameHash);
                if (archiveId == -1) {
                    archiveId = cache.getIndexes()[5].getLastArchiveId() + 1;
                }
                cache.getIndexes()[5].putFile(archiveId, 0, Constants.GZIP_COMPRESSION, data, xteas, false, false, nameHash, -1);
            }
            System.out.println("Packed region " + toRegionId);
        } catch (Exception e) {
            System.out.println("Error occurred while packing the given regions: "+e.getMessage());
        }
    }

    private static void dump_maps(FileStore cache) throws IOException {
        for (int region = 0; region < 32768; region++) {

            int x = (region >> 8);
            int y = (region & 0xff);

            byte[] data;
            File file;
            DataOutputStream dos;

            //Map
            int map = cache.getIndexes()[5].getArchiveId("m" + x + "_" + y);
            if (map != -1) {
                data = cache.getIndexes()[5].getFile(map);
                file = new File("./maps/", "m" + x + "_" + y + ".dat");
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write(data);
                dos.close();
                System.out.println("Dumped map " + file.getName());
            }

            //Locations
            int location =  cache.getIndexes()[5].getArchiveId("l" + x + "_" + y);
            if (location != -1) {
                int[] xteas = XTEASManager.lookup(region);
                data = cache.getIndexes()[5].getFile(location, 0, xteas);
                if (data == null)
                    continue;
                file = new File("./maps/", "l" + x + "_" + y + ".dat");
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write(data);
                dos.close();
                System.out.println("Dumped landscape " + file.getName());
            }

        }
    }

    @Override
    public int getDataOffset() {
        //throw new Exception("Maps should not have a data offset, these are packed to regions.");
        return -1;
    }

    private int[] regionIds = new int[] {
            //Theater of blood lobby
            14385, 14386, 14642, 14641,
            //Zulrah
            8751,
            8752,
            9007,
            9008,
            //Zeah
            4668, 4924, 5180, 5436,
            4667, 4923, 5179, 5435,
            4666, 4922, 5178, 5434,
            4665, 4921, 5177, 5433,
            5695, 5951, 6207, 6463, 6719, 6975, 7231, 7487, 7743, 5694, 5950, 6206, 6462, 6718, 6974, 7230, 7486,
            7742, 5693, 5949, 6205, 6461, 6717, 6973, 7229, 7485, 7741, 5692, 5949, 6204, 6460, 6716, 6972, 7228,
            7484, 7740, 5691, 5948, 6203, 6459, 6715, 6971, 7227, 7483, 7739, 5690, 5947, 6202, 6458, 6714, 6970,
            7226, 7482, 7738, 5689, 5946, 6201, 6457, 6713, 6969, 7225, 7481, 7737, 4664, 4920, 5176, 5432, 5688,
            5945, 6200, 6457, 6712, 6968, 7224, 7480, 7736, 4663, 4919, 5176, 5431, 5687, 5944, 6199, 6456, 6711,
            6967, 7223, 7479, 7735, 4662, 6918, 5174, 5430, 5686, 5943, 6198, 6455, 6710, 6966, 7222, 7478, 7734, 5942,
            5685, 5941, 6197, 6453, 6709, 6965, 7221, 7477, 7733, 5684, 5940, 6196, 6452, 6708, 6964, 7220, 7476, 6454, 6709,
            7732, 5175,
            //Cerberus
            4883,
            //Skotizo
            6810,
            //Vorkath
            9023,
            //Adamant & rune dragons
            6223,
            //Khorend dungeon
            6556,
            6557,
            6812,
            6813,
            7069,
            6301,
            6299,
            6555,
            6811,
            7067,
            7068,
            //Keymaster
            5139,
            //Inferno
            9043,
            //Myths guild
            9772,
            //Demonic gorillas
            8536, 8280,
            //Raids
            12889, 13145, 13401,
            13141, 13397,
            13140, 13396,
            13139, 13395,
            13138, 13394,
            13137, 13393,
            13136, 13392,
            9043,
    };
}
