package com.example.g_morph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ScatterPlotActivity extends AppCompatActivity {
    EditText xValuesInput;
    EditText yValuesInput;
    EditText xAxisTitle;
    EditText yAxisTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter_plot);

        // EditText fields for entering x, y values and axis titles
        xValuesInput = findViewById(R.id.x_values_input);
        yValuesInput = findViewById(R.id.y_values_input);
        xAxisTitle = findViewById(R.id.x_axis_title);
        yAxisTitle = findViewById(R.id.y_axis_title);

        // Get the LinearLayout where we will add the scatter plot
        LinearLayout chartLayout = findViewById(R.id.scatter_plot_layout);

        // Draw scatter plot with default values
        double[] defaultXValues = {0, 40, 60, 80, 100};
        double[] defaultYValues = {0, 50, 40, 70, 100};
        String defaultXAxisTitle = "X Axis Title";
        String defaultYAxisTitle = "Y Axis Title";
        chartLayout.addView(new ScatterPlotView(this, defaultXValues, defaultYValues, defaultXAxisTitle, defaultYAxisTitle));
    }

    // Custom view for drawing scatter plot
    class ScatterPlotView extends View {
        private Paint paint = new Paint();
        private double[] xValues;
        private double[] yValues;
        private String xAxisTitle;
        private String yAxisTitle;

        public ScatterPlotView(Context context, double[] xValues, double[] yValues, String xAxisTitle, String yAxisTitle) {
            super(context);
            this.xValues = xValues;
            this.yValues = yValues;
            this.xAxisTitle = xAxisTitle;
            this.yAxisTitle = yAxisTitle;
            paint.setStrokeWidth(5); // Set line width for axes
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Draw X and Y axes
            paint.setColor(Color.BLACK);
            canvas.drawLine(100, height - 100, width - 100, height - 100, paint); // X axis
            canvas.drawLine(100, 100, 100, height - 100, paint); // Y axis

            // Draw X axis labels
            paint.setTextSize(30);
            for (int i = 0; i < xValues.length; i++) {
                float x = (float) (xValues[i] / 100.0 * (width - 200)) + 100; // Adjust x position based on width
                float y = height - 60; // Position below X axis
                canvas.drawText(String.valueOf(xValues[i]), x, height - 40, paint); // Draw x-axis values on the axis lines
            }

            // Draw Y axis labels
            for (int i = 0; i < yValues.length; i++) {
                float x = 50; // Position to the left of Y axis
                float y = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                canvas.drawText(String.valueOf(yValues[i]), 20, y, paint); // Draw y-axis values on the axis lines
            }

            // Draw x-axis title
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText(xAxisTitle, (width / 2) - 100, height - 10, paint);

            // Draw y-axis title
            canvas.save();
            canvas.rotate(-90);
            canvas.drawText(yAxisTitle, -height / 2, 60, paint);
            canvas.restore();

            // Draw scatter plot points
            paint.setColor(Color.GREEN); // Set point color to blue
            for (int i = 0; i < xValues.length; i++) {
                float x = (float) (xValues[i] / 100.0 * (width - 200)) + 100; // Adjust x position based on width
                float y = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                canvas.drawCircle(x, y, 10, paint); // Draw circle for each data point
            }
        }
    }

    // Method to handle button click for generating scatter plot
    public void generateScatterPlot(View view) {
        String xValuesText = xValuesInput.getText().toString();
        String yValuesText = yValuesInput.getText().toString();
        String xAxisTitleText = xAxisTitle.getText().toString();
        String yAxisTitleText = yAxisTitle.getText().toString();

        // Split x and y values input by commas
        String[] xValuesArray = xValuesText.split(",");
        String[] yValuesArray = yValuesText.split(",");

        // Convert string arrays to double arrays for x, y values
        double[] xValues = new double[xValuesArray.length];
        double[] yValues = new double[yValuesArray.length];
        for (int i = 0; i < xValuesArray.length; i++) {
            xValues[i] = Double.parseDouble(xValuesArray[i].trim());
        }
        for (int i = 0; i < yValuesArray.length; i++) {
            yValues[i] = Double.parseDouble(yValuesArray[i].trim());
        }

        // Draw scatter plot with user input values
        LinearLayout chartLayout = findViewById(R.id.scatter_plot_layout);
        chartLayout.removeAllViews(); // Remove any previous views
        chartLayout.addView(new ScatterPlotView(this, xValues, yValues, xAxisTitleText, yAxisTitleText));
    }
}
