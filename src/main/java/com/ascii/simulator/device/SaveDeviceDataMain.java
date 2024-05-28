package com.ascii.simulator.device;

import com.ascii.simulator.device.util.FileUtil;

import java.io.IOException;
import java.util.UUID;

import static com.ascii.simulator.device.util.Constants.DEVICES_DATA_DIR;

public class SaveDeviceDataMain {

    public static void main(String[] args) {
	// write your code here
        String data = "entity_id,name,state,aliases,area_id\n" +
                "light.office_ceiling,Office Ceiling Light,off,main_light/central_light,office\n" +
                "lock.office_door,Office Door Lock,locked,main_door_lock,office\n";

        // generate a random UUID for csv file name
        String fileName = UUID.randomUUID().toString() + ".csv";
        String filePath = DEVICES_DATA_DIR + "/" + fileName;

        // save data into devices directory located in the root of the project, use fileName as the name of the file. do it with Java io
        try {
            FileUtil.saveDataToFile(filePath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
