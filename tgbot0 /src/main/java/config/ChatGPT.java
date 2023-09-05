package config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Objects;

public class ChatGPT {
    private final String API_KEY;

    public ChatGPT(String apiKey) {
        API_KEY = apiKey;
    }

    public String askChatGPT(String text) throws IOException, URISyntaxException {
        var con = (HttpURLConnection) new URI("https://api.openai.com/v1/chat/completions").toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);
        con.setDoOutput(true);
        con.getOutputStream().write(new JSONObject() {{
            put("model", "gpt-3.5-turbo-0613");
            put("messages", new JSONArray(List.of(new JSONObject() {{
                put("role", "user");
                put("content", text);
            }})));
            put("max_tokens", 4000);
            put("temperature", 1.0);
        }}.toString().getBytes());
        return new JSONObject(Objects.requireNonNull(new BufferedReader(new InputStreamReader(con.getInputStream())).lines().reduce((a, b) -> a + b).orElse(null))).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}

