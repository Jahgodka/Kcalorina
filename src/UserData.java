public class UserData {
    private double weightKg;
    private double heightCm;
    private int ageYears;
    private char gender;

    public UserData(double weightKg, double heightCm, int ageYears, char gender) {
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.ageYears = ageYears;
        this.gender = gender;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public int getAgeYears() {
        return ageYears;
    }

    public void setAgeYears(int ageYears) {
        this.ageYears = ageYears;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public double calculateBMI() {
        double h = heightCm / 100.0;
        return weightKg / (h * h);
    }

    public String calculateBMICategory() {
        double bmi = calculateBMI();
        if (bmi < 16.0) {
            return "Severe Thinness";
        } else if (bmi >= 16.0 && bmi <= 16.9) {
            return "Moderate Thinness";
        } else if (bmi >= 17.0 && bmi <= 18.5) {
            return "Mild Thinness";
        } else if (bmi >= 18.6 && bmi <= 24.9) {
            return "Normal Weight";
        } else if (bmi >= 25.0 && bmi <= 29.9) {
            return "Overweight";
        } else if (bmi >= 30.0 && bmi <= 34.9) {
            return "Obesity Class I";
        } else if (bmi >= 35.0 && bmi <= 39.9) {
            return "Obesity Class II";
        } else if (bmi >= 40) {
            return "Obesity Class III";
        }
        return "Error";
    }

    // Basal Metabolic Rate (BMR) using Mifflin-St Jeor equation
    public double calculateBMR() {
        double base = 10 * weightKg + 6.25 * heightCm - 5 * ageYears;
        if (gender == 'M' || gender == 'm') {
            return base + 5;
        } else {
            return base - 161;
        }
    }
}