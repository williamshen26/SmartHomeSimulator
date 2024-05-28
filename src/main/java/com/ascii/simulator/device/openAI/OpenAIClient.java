package com.ascii.simulator.device.openAI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.ascii.simulator.device.util.Constants.*;

public class OpenAIClient {
    public JSONObject makeChatCompletionRequest(JSONArray messages) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Define function call instructions

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", OPENAI_API_MODEL);
        requestBody.put("messages", messages);
        requestBody.put("function_call", "auto");
        requestBody.put("functions", FUNCTIONS_DATA_JSON);
        requestBody.put("max_tokens", OPENAI_API_MAX_TOKENS);
        requestBody.put("temperature", OPENAI_API_TEMPERATURE);
        requestBody.put("n", 1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_COMPLETION_PATH))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new JSONObject(response.body());
        } else {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public String makeChatCompletionRequest(String userMessage) throws IOException, InterruptedException {
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", userMessage));

        JSONObject responseObject = makeChatCompletionRequest(messages);
        return responseObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
