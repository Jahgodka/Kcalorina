import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class MainFrame extends JFrame {
    private UserData userData;
    private CalorieCalculator calorieCalculator;

    // GUI Components
    private JTextField tfFood, tfFoodGrams;
    private JLabel lblFoodResult;
    private JLabel lblFoodIcon; // New label for food image

    private JTextField tfWeight, tfHeight, tfAge, tfGender;
    private JLabel lblBMI, lblBMR;

    private double totalCalories = 0.0;
    private JLabel lblTotalCalories;
    private JButton btnResetTotal;

    public MainFrame() {
        super("Kcalorina - Calorie Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500); // Increased size slightly to fit icon
        setLayout(new GridLayout(10, 2, 5, 5));

        // --- Icon Setup (App Icon) ---
        // Looks for logo.png in the project root
        ImageIcon appIcon = new ImageIcon("logo.png");
        setIconImage(appIcon.getImage());

        // Default Data
        userData = new UserData(70, 175, 25, 'M');
        calorieCalculator = new CalorieCalculator();

        // --- Food Section ---
        add(new JLabel("Food Item (Eng):"));
        tfFood = new JTextField();
        add(tfFood);

        add(new JLabel("Weight [g]:"));
        tfFoodGrams = new JTextField();
        add(tfFoodGrams);

        JButton btnCalcFood = new JButton("Calculate Calories");
        add(btnCalcFood);

        // Result Panel (Text + Image)
        JPanel pResult = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFoodResult = new JLabel("-");
        lblFoodIcon = new JLabel(); // Icon placeholder
        lblFoodIcon.setPreferredSize(new Dimension(50, 50));
        pResult.add(lblFoodResult);
        pResult.add(lblFoodIcon);
        add(pResult);

        // --- Total Section ---
        add(new JLabel("Total Kcal:"));
        lblTotalCalories = new JLabel("0.0 kcal");
        add(lblTotalCalories);

        btnResetTotal = new JButton("Reset Total");
        add(btnResetTotal);
        add(new JLabel()); // Spacer

        // --- User Data Section ---
        add(new JLabel("Weight [kg]:"));
        tfWeight = new JTextField(String.valueOf(userData.getWeightKg()));
        add(tfWeight);

        add(new JLabel("Height [cm]:"));
        tfHeight = new JTextField(String.valueOf(userData.getHeightCm()));
        add(tfHeight);

        add(new JLabel("Age [years]:"));
        tfAge = new JTextField(String.valueOf(userData.getAgeYears()));
        add(tfAge);

        add(new JLabel("Gender [M/F]:"));
        tfGender = new JTextField(String.valueOf(userData.getGender()));
        add(tfGender);

        JButton btnUpdateUser = new JButton("Update Stats");
        add(btnUpdateUser);

        // Panel for results
        JPanel pRight = new JPanel(new GridLayout(2,1));
        lblBMI = new JLabel("BMI: -");
        lblBMR = new JLabel("BMR: -");
        pRight.add(lblBMI);
        pRight.add(lblBMR);
        add(pRight);

        // --- Action Listeners ---
        btnCalcFood.addActionListener(e -> onCalculateFood());
        btnUpdateUser.addActionListener(e -> onUpdateUser());

        btnResetTotal.addActionListener(e -> {
            totalCalories = 0.0;
            lblTotalCalories.setText(String.format("%.1f kcal", totalCalories));
            lblFoodIcon.setIcon(null);
        });

        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }

    private void onCalculateFood() {
        try {
            String food = tfFood.getText();
            double grams = Double.parseDouble(tfFoodGrams.getText());

            // Get result (calories + image url)
            FoodResult result = calorieCalculator.calculateCalories(food, grams);

            // Display text result
            lblFoodResult.setText(String.format("%.1f kcal", result.calories));

            // Display image result
            if (result.imageUrl != null && !result.imageUrl.isEmpty()) {
                try {
                    URL url = URI.create(result.imageUrl).toURL();
                    ImageIcon icon = new ImageIcon(url);
                    // Resize image to 50x50 smoothly
                    Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    lblFoodIcon.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    System.err.println("Failed to load image: " + e.getMessage());
                    lblFoodIcon.setIcon(null);
                }
            } else {
                lblFoodIcon.setIcon(null); // No image available
            }

            // Update total
            totalCalories += result.calories;
            lblTotalCalories.setText(String.format("%.1f kcal", totalCalories));

        } catch (NumberFormatException ex) {
            lblFoodResult.setText("Invalid weight!");
            lblFoodIcon.setIcon(null);
        } catch (IllegalArgumentException ex) {
            lblFoodResult.setText("Not found.");
            lblFoodIcon.setIcon(null);
        }
    }

    private void onUpdateUser() {
        try {
            double w = Double.parseDouble(tfWeight.getText());
            double h = Double.parseDouble(tfHeight.getText());
            int age = Integer.parseInt(tfAge.getText());
            String genderText = tfGender.getText().trim();

            if (genderText.isEmpty()) throw new IllegalArgumentException();
            char g = genderText.toUpperCase().charAt(0);

            userData.setWeightKg(w);
            userData.setHeightCm(h);
            userData.setAgeYears(age);
            userData.setGender(g);

            double bmi = userData.calculateBMI();
            double bmr = userData.calculateBMR();
            String bmiCategory = userData.calculateBMICategory();

            lblBMI.setText(String.format("BMI: %.1f - %s", bmi, bmiCategory));
            lblBMR.setText(String.format("BMR: %.0f kcal/day", bmr));
        } catch (Exception ex) {
            lblBMI.setText("Invalid Data!");
            lblBMR.setText("");
        }
    }
}