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

public class BubbleChartActivity extends AppCompatActivity {
    EditText xValuesInput;
    EditText yValuesInput;
    EditText radiusInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_chart);

        // EditText fields for entering x, y values, and radius
        xValuesInput = findViewById(R.id.x_values_input);
        yValuesInput = findViewById(R.id.y_values_input);
        radiusInput = findViewById(R.id.radius_input);

        // Get the LinearLayout where we will add the bubbles
        LinearLayout chartLayout = findViewById(R.id.bubble_chart_layout);

        // Draw bubbles with default values
        double[] defaultXValues = {0, 40, 60, 80, 100};
        double[] defaultYValues = {0, 50, 40, 70, 100};
        double[] defaultRadius = {1, 20, 30, 40, 100};
        chartLayout.addView(new BubbleChartView(this, defaultXValues, defaultYValues, defaultRadius));
    }

    // Custom view for drawing bubble chart
    class BubbleChartView extends View {
        private Paint paint = new Paint();
        private double[] xValues;
        private double[] yValues;
        private double[] radiusValues;

        public BubbleChartView(Context context, double[] xValues, double[] yValues, double[] radiusValues) {
            super(context);
            this.xValues = xValues;
            this.yValues = yValues;
            this.radiusValues = radiusValues;
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
                canvas.drawText(String.valueOf(xValues[i]), x, y, paint);
            }

            // Draw Y axis labels
            for (int i = 0; i < yValues.length; i++) {
                float x = 50; // Position to the left of Y axis
                float y = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                canvas.drawText(String.valueOf(yValues[i]), x, y, paint);
            }

            // Draw bubbles
            paint.setColor(Color.RED); // Set bubble color to red
            for (int i = 0; i < xValues.length; i++) {
                float x = (float) (xValues[i] / 100.0 * (width - 200)) + 100; // Adjust x position based on width
                float y = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                float radius = (float) (radiusValues[i] / 100.0 * width); // Adjust radius to fit screen size
                canvas.drawCircle(x, y, radius, paint);
            }
        }
    }

    // Method to handle button click for generating bubble chart
    public void generateBubbleChart(View view) {
        String xValuesText = xValuesInput.getText().toString();
        String yValuesText = yValuesInput.getText().toString();
        String radiusText = radiusInput.getText().toString();

        // Split x and y values input by commas
        String[] xValuesArray = xValuesText.split(",");
        String[] yValuesArray = yValuesText.split(",");
        String[] radiusArray = radiusText.split(",");

        // Convert string arrays to double arrays for x, y values, and radius
        double[] xValues = new double[xValuesArray.length];
        double[] yValues = new double[yValuesArray.length];
        double[] radiusValues = new double[radiusArray.length];
        for (int i = 0; i < xValuesArray.length; i++) {
            xValues[i] = Double.parseDouble(xValuesArray[i].trim());
        }
        for (int i = 0; i < yValuesArray.length; i++) {
            yValues[i] = Double.parseDouble(yValuesArray[i].trim());
        }
        for (int i = 0; i < radiusArray.length; i++) {
            radiusValues[i] = Double.parseDouble(radiusArray[i].trim());
        }

        // Draw bubbles with user input values
        LinearLayout chartLayout = findViewById(R.id.bubble_chart_layout);
        chartLayout.removeAllViews(); // Remove any previous views
        chartLayout.addView(new BubbleChartView(this, xValues, yValues, radiusValues));
    }
}
