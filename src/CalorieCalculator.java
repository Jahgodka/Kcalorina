import java.util.HashMap;
import java.util.Map;

public class CalorieCalculator {
    private Map<String, Double> foodMap;
    private NutritionixApiClient apiClient;

    public CalorieCalculator() {
        foodMap = new HashMap<>();
        // Local fallback database (used if API fails or item not found)
        foodMap.put("apple", 52.0);
        foodMap.put("chicken", 165.0);
        foodMap.put("potato", 77.0);

        apiClient = new NutritionixApiClient();
    }

    public void addFood(String name, double kcalPer100g) {
        foodMap.put(name.toLowerCase(), kcalPer100g);
    }

    public FoodResult calculateCalories(String foodName, double grams) {
        // 1) Try API first
        try {
            return apiClient.fetchCalories(foodName, grams);
        } catch (Exception e) {
            System.err.println("API Nutritionix failed: " + e.getMessage());
        }

        // 2) Fallback logic
        String key = foodName.toLowerCase();
        if (!foodMap.containsKey(key)) {
            throw new IllegalArgumentException("Unknown food item: " + foodName);
        }
        // Calculate proportionally (no image for local items)
        double kcal = foodMap.get(key) * grams / 100.0;
        return new FoodResult(kcal, null);
    }
}

// Helper class to store result data
class FoodResult {
    public final double calories;
    public final String imageUrl;

    public FoodResult(double calories, String imageUrl) {
        this.calories = calories;
        this.imageUrl = imageUrl;
    }
}