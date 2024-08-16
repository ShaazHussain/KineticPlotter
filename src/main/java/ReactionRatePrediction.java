import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ReactionRatePrediction {

    // Method to calculate the rate of the reaction at any random point
    public static double[] calculateReactionRates(double[] concentration, double[] time) {
        double[] rates = new double[concentration.length];

        // Calculate the rate of reaction using centered differences
        for (int i = 1; i < concentration.length - 1; i++) {
            double deltaConcentration = concentration[i + 1] - concentration[i - 1];
            double deltaTime = time[i + 1] - time[i - 1];
            double rate = -(deltaConcentration / deltaTime);
            rates[i] = rate;
        }

        // Handle boundaries with forward and backward differences
        rates[0] = -(concentration[1] - concentration[0]) / (time[1] - time[0]);
        rates[concentration.length - 1] = -(concentration[concentration.length - 1] - concentration[concentration.length - 2]) / (time[concentration.length - 1] - time[concentration.length - 2]);

        return rates;
    }

    // Method to predict the order of the reaction
    public static double predictReactionOrder(double[] concentration, double[] rates) {
        int n = rates.length - 2; // Exclude boundaries
        double sumLogRate = 0.0;
        double sumLogConcentration = 0.0;
        double sumLogRateLogConcentration = 0.0;
        double sumLogConcentrationSquared = 0.0;

        for (int i = 1; i < n + 1; i++) {
            double logRate = Math.log(rates[i]);
            double logConcentration = Math.log(concentration[i]);

            sumLogRate += logRate;
            sumLogConcentration += logConcentration;
            sumLogRateLogConcentration += logRate * logConcentration;
            sumLogConcentrationSquared += logConcentration * logConcentration;
        }

        // Calculate the order of the reaction (n) using linear regression
        double numerator = n * sumLogRateLogConcentration - sumLogRate * sumLogConcentration;
        double denominator = n * sumLogConcentrationSquared - sumLogConcentration * sumLogConcentration;
        return numerator / denominator;
    }

    // Method to save a plot as an image
    public static void savePlotAsImage(double[] xData, double[] yData, String xLabel, String yLabel, String title, String filePath) throws IOException {
        XYSeries series = new XYSeries(title);
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartUtilities.saveChartAsPNG(new File(filePath), chart, 800, 600);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: number of data points
        System.out.print("Enter the number of data points: ");
        int n = scanner.nextInt();

        double[] concentration = new double[n];
        double[] time = new double[n];

        // Input: concentrations and corresponding times
        System.out.println("Enter the concentration and time values:");
        for (int i = 0; i < n; i++) {
            System.out.print("Concentration at point " + (i + 1) + ": ");
            concentration[i] = scanner.nextDouble();
            System.out.print("Time at point " + (i + 1) + ": ");
            time[i] = scanner.nextDouble();
        }

        // Calculate the reaction rates
        double[] rates = calculateReactionRates(concentration, time);

        // Predict the order of the reaction
        double order = predictReactionOrder(concentration, rates);

        // Display the predicted order of the reaction
        System.out.printf("The predicted order of the reaction is: %.4f\n", order);

        try {

            String fp="C:/Users/cutes.DESKTOP-8304MKF/Downloads/proj/proj/src/main/resources/static/";
            // Save Concentration vs. Time plot as an image
            savePlotAsImage(time, concentration, "Time (s)", "Concentration (M)", "Concentration vs Time", fp+"conc_vs_time.png");

            // Save Rate vs. Time plot as an image
            savePlotAsImage(time, rates, "Time (s)", "Reaction Rate (M/s)", "Reaction Rate vs. Time", fp+"rate_vs_time.png");
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }

        scanner.close();
    }
}