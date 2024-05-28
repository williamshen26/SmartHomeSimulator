package com.ascii.simulator.device;

import com.ascii.simulator.device.openAI.OpenAIClient;
import com.ascii.simulator.device.util.FileUtil;
import com.ascii.simulator.device.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ascii.simulator.device.util.Commands.COMMANDS;
import static com.ascii.simulator.device.util.Constants.*;
import static com.ascii.simulator.device.util.DateUtil.getCurrentDateTime;
import static com.ascii.simulator.device.util.DateUtil.getCurrentDayOfWeek;

public class GenerateTrainingDataMain {
    public static void main(String[] args) {
        // recursively read data from devices directory and generate training data
        Path devicesDir = Paths.get(DEVICES_DATA_DIR);
        List<Path> deviceFiles = new ArrayList<>();
        OpenAIClient client = new OpenAIClient();

        try {
            JSONArray trainingData = new JSONArray();
            FileUtil.readDataRecursively(devicesDir, deviceFiles);
            for (Path file : deviceFiles) {
                System.out.println("Found file: " + file.toString());
                String data = FileUtil.readFileData(file.toString());
                Set<String> availableAreas = getAvailableAreas(data);
                data = PROMPT_DATA_BEFORE + data.replace("%", "%%") + PROMPT_DATA_AFTER;
                data = String.format(data, getCurrentDateTime(), getCurrentDayOfWeek(), availableAreas.stream().findAny().get());
                // get the JSON from data
                for (String command : COMMANDS) {
                    JSONObject json = JsonUtil.convertStringToJson(data);
                    JSONObject userCommand = new JSONObject();
                    userCommand.put("role", "user");
                    userCommand.put("content", command);
                    json.getJSONArray("data").put(userCommand);
                    System.out.println(json.toString());
                    JSONObject response = client.makeChatCompletionRequest(json.getJSONArray("data"));
                    System.out.println(response.toString());

                    JSONObject trainingObject = getTrainingObject(json, response);
                    trainingData.put(trainingObject);
                }
            }
            FileUtil.saveDataToFile(String.format(TRAINING_DATA_FILE_PATH, getCurrentDateTime()), trainingData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Set<String> getAvailableAreas(String data) {
        Set<String> areas = new HashSet<>();
        String[] lines = data.split("\\\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            String area = parts[parts.length - 1];
            if (!"area_id".equals(area)) {
                areas.add(area);
            }
        }
        return areas;
    }

    private static JSONObject getTrainingObject(JSONObject input, JSONObject output) {
        JSONObject trainingObject = new JSONObject();
        trainingObject.put("input", input);
        trainingObject.put("output", output.getJSONArray("choices"));
        return trainingObject;
    }
}
