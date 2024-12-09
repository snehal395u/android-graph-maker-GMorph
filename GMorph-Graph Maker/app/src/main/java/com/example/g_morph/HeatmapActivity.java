package com.example.g_morph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HeatmapActivity extends AppCompatActivity {
    EditText xValuesInput;
    EditText yValuesInput;
    EditText colorInput;
    EditText xAxisTitle;
    EditText yAxisTitle;
    Button drawButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        // EditText fields for entering x, y values, color, and axis titles
        xValuesInput = findViewById(R.id.x_values_input);
        yValuesInput = findViewById(R.id.y_values_input);
        colorInput = findViewById(R.id.color_input);
        xAxisTitle = findViewById(R.id.x_axis_title_input);
        yAxisTitle = findViewById(R.id.y_axis_title_input);

        // Button to draw heatmap
        drawButton = findViewById(R.id.draw_button);

        // Set button click listener
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateHeatmap();
            }
        });
    }

    // Method to generate heatmap
    private void generateHeatmap() {
        String xValuesText = xValuesInput.getText().toString();
        String yValuesText = yValuesInput.getText().toString();
        String colorText = colorInput.getText().toString();
        String xAxisTitleText = xAxisTitle.getText().toString();
        String yAxisTitleText = yAxisTitle.getText().toString();

        // Split x and y values input by commas
        String[] xValuesArray = xValuesText.split(",");
        String[] yValuesArray = yValuesText.split(",");
        String[] colorArray = colorText.split(",");

        // Convert string arrays to double arrays for x, y values, and colors
        double[] xValues = new double[xValuesArray.length];
        double[] yValues = new double[yValuesArray.length];
        int[] colors = new int[colorArray.length];
        for (int i = 0; i < xValuesArray.length; i++) {
            xValues[i] = Double.parseDouble(xValuesArray[i].trim());
        }
        for (int i = 0; i < yValuesArray.length; i++) {
            yValues[i] = Double.parseDouble(yValuesArray[i].trim());
        }
        for (int i = 0; i < colorArray.length; i++) {
            // Parse color names to corresponding color codes
            String colorName = colorArray[i].trim().toLowerCase(); // Convert to lowercase for case-insensitive comparison
            switch (colorName) {
                case "red":
                    colors[i] = Color.RED;
                    break;
                case "green":
                    colors[i] = Color.GREEN;
                    break;
                case "black":
                    colors[i] = Color.BLACK;
                    break;
                case "blue":
                    colors[i] = Color.BLUE;
                    break;
                default:
                    // Parse as hexadecimal color code if not a recognized color name
                    colors[i] = Color.parseColor(colorArray[i].trim());
                    break;
            }
        }

        // Get the LinearLayout where we will add the heatmap
        LinearLayout chartLayout = findViewById(R.id.heatmap_layout);

        // Remove any previous views
        chartLayout.removeAllViews();

        // Add the heatmap view
        chartLayout.addView(new HeatmapView(this, xValues, yValues, colors, xAxisTitleText, yAxisTitleText));
    }

    // Custom view for drawing heatmap
    class HeatmapView extends View {
        private Paint paint = new Paint();
        private double[] xValues;
        private double[] yValues;
        private int[] colors;
        private int maxX;
        private int maxY;
        private String xAxisTitle;
        private String yAxisTitle;

        public HeatmapView(Context context, double[] xValues, double[] yValues, int[] colors, String xAxisTitle, String yAxisTitle) {
            super(context);
            this.xValues = xValues;
            this.yValues = yValues;
            this.colors = colors;
            this.maxX = getMaxValue(xValues);
            this.maxY = getMaxValue(yValues);
            this.xAxisTitle = xAxisTitle;
            this.yAxisTitle = yAxisTitle;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Draw grid rectangle frame
            drawGrid(canvas, width, height);

            // Draw heatmap
            for (int i = 0; i < xValues.length; i++) {
                float x = (float) (xValues[i] / (double) maxX * (width - 200)) + 100; // Adjust x position based on width
                float y = height - ((float) (yValues[i] / (double) maxY * (height - 200)) + 100); // Adjust y position based on height

                paint.setColor(colors[i]);
                canvas.drawRect(x - 50, y - 50, x + 50, y + 50, paint); // Draw pixel rectangles
                // Write values
                paint.setColor(Color.BLACK);
                paint.setTextSize(30);
                canvas.drawText(String.valueOf(xValues[i]), x - 10, y + 5, paint);
                canvas.drawText(String.valueOf(yValues[i]), x - 30, y + 25, paint);
            }

            // Draw x-axis title
            paint.setTextSize(40);
            canvas.drawText(xAxisTitle, width / 2 - 100, height - 20, paint);

            // Draw y-axis title
            canvas.rotate(-90);
            canvas.drawText(yAxisTitle, -height / 2 - 50, 40, paint);
        }

        // Draw grid rectangle frame
        private void drawGrid(Canvas canvas, int width, int height) {
            paint.setColor(Color.BLACK); // Change grid color to black
            paint.setStrokeWidth(5);

            // Draw horizontal grid lines
            for (int i = 0; i <= 10; i++) {
                float y = (float) (i / 10.0 * (height - 200)) + 100;
                canvas.drawLine(100, y, width - 100, y, paint);
            }

            // Draw vertical grid lines
            for (int i = 0; i <= 10; i++) {
                float x = (float) (i / 10.0 * (width - 200)) + 100;
                canvas.drawLine(x, 100, x, height - 100, paint);
            }

            // Draw x-axis
            paint.setColor(Color.BLACK);
            canvas.drawLine(100, height - 100, width - 100, height - 100, paint);

            // Draw y-axis
            paint.setColor(Color.BLACK);
            canvas.drawLine(100, 100, 100, height - 100, paint);
        }

        // Find the maximum value in an array
        private int getMaxValue(double[] array) {
            int max = Integer.MIN_VALUE;
            for (double value : array) {
                max = Math.max(max, (int) value);
            }
            return max;
        }
    }
}
