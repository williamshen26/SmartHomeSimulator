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

        JSONArray trainingData = new JSONArray();
        try {
            FileUtil.readDataRecursively(devicesDir, deviceFiles);
            for (Path file : deviceFiles) {
                if (file.toString().endsWith(".csv")) {
                    System.out.println("Found file: " + file.toString());
                    String data = FileUtil.readFileData(file.toString());
                    Set<String> availableAreas = getAvailableAreas(data);
                    String trainingInput = PROMPT_DATA_BEFORE_2 + data.replace("%", "%%");
                    trainingInput = String.format(trainingInput, getCurrentDateTime(), getCurrentDayOfWeek(), availableAreas.stream().findAny().get());
                    data = PROMPT_DATA_BEFORE + PROMPT_INSTRUCTION_1 + trainingInput + PROMPT_INSTRUCTION_2 + PROMPT_DATA_AFTER;
                    trainingInput = PROMPT_DATA_BEFORE + trainingInput + PROMPT_DATA_AFTER;
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

                        JSONObject trainingInputObject = JsonUtil.convertStringToJson(trainingInput);
                        trainingInputObject.getJSONArray("data").put(userCommand);
                        JSONObject trainingObject = getTrainingObject(trainingInputObject, response);
                        trainingData.put(trainingObject);
                    }

                    FileUtil.cutFile(file.toString(), file.toString().replace(DEVICES_DATA_DIR, DEVICES_DATA_ARCHIVE_DIR));
                }
            }
            FileUtil.saveDataToFile(String.format(TRAINING_DATA_FILE_PATH, getCurrentDateTime()), trainingData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // if any error occures, still save the currently collected training data.
            try {
                FileUtil.saveDataToFile(String.format(TRAINING_DATA_FILE_PATH, getCurrentDateTime()), trainingData.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
