import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;

public class NutritionixApiClient {
    private static final String ENDPOINT = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    private final String appId = "YOUR_APP_ID";
    private final String appKey = "YOUR_APP_KEY";

    public FoodResult fetchCalories(String foodName, double grams) throws IOException {
        // Request Body
        JSONObject body = new JSONObject();
        body.put("query", String.format("%.1f g %s", grams, foodName));
        body.put("timezone", "US/Eastern");

        // Connection setup (using URI to avoid deprecated URL constructor)
        URL url = URI.create(ENDPOINT).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-app-id", appId);
        conn.setRequestProperty("x-app-key", appKey);
        conn.setDoOutput(true);

        // Send JSON
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes("UTF-8"));
        }

        // Handle Response
        int code = conn.getResponseCode();
        InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder resp = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                resp.append(line);
            }
        }

        if (code != 200) {
            throw new IOException("Nutritionix API error, HTTP " + code + ": " + resp);
        }

        // Parse JSON
        JSONObject json = new JSONObject(resp.toString());
        JSONArray foods = json.getJSONArray("foods");

        if (foods.length() == 0) {
            throw new RuntimeException("No data found in Nutritionix response");
        }

        JSONObject firstItem = foods.getJSONObject(0);
        double calories = firstItem.getDouble("nf_calories");

        // Extract image URL if available
        String thumbUrl = null;
        if (firstItem.has("photo")) {
            JSONObject photo = firstItem.getJSONObject("photo");
            if (photo.has("thumb")) {
                thumbUrl = photo.getString("thumb");
            }
        }

        // Return result object
        return new FoodResult(calories, thumbUrl);
    }
}