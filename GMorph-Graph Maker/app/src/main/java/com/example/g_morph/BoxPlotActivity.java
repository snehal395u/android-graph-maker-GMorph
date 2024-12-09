package com.example.g_morph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BoxPlotActivity extends AppCompatActivity {
    EditText quartilesInput;
    EditText medianInput;
    EditText outlierInput;
    Button drawButton;
    LinearLayout chartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_plot);

        // Initialize views
        quartilesInput = findViewById(R.id.quartiles_input);
        medianInput = findViewById(R.id.median_input);
        outlierInput = findViewById(R.id.outlier_input);
        drawButton = findViewById(R.id.draw_button);
        chartLayout = findViewById(R.id.box_plot_layout);

        // Set button click listener
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    drawBoxPlot();
                } else {
                    Toast.makeText(BoxPlotActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to draw the box plot
    private void drawBoxPlot() {
        String quartilesText = quartilesInput.getText().toString();
        String[] medianValues = medianInput.getText().toString().split(",");
        String outliersText = outlierInput.getText().toString();

        int[] quartiles = parseValues(quartilesText);
        int[] outliers = parseValues(outliersText);

        // Remove previous views
        chartLayout.removeAllViews();

        // Add the box plot view
        chartLayout.addView(new BoxPlotView(this, quartiles, medianValues, outliers));
    }

    // Validate user inputs
    private boolean validateInputs() {
        String quartilesText = quartilesInput.getText().toString();
        String medianText = medianInput.getText().toString();
        String outliersText = outlierInput.getText().toString();

        return !TextUtils.isEmpty(quartilesText) && !TextUtils.isEmpty(medianText) && !TextUtils.isEmpty(outliersText);
    }

    // Custom view for drawing box plot
    class BoxPlotView extends View {
        private Paint paint = new Paint();
        private int[] quartiles;
        private String[] medians;
        private int[] outliers;
        private static final float LINE_LENGTH = 100; // Increase this value to increase the length of quartile and median lines

        public BoxPlotView(Context context, int[] quartiles, String[] medians, int[] outliers) {
            super(context);
            this.quartiles = quartiles;
            this.medians = medians;
            this.outliers = outliers;
            paint.setStrokeWidth(5); // Set line width for box plot
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Draw axes
            drawAxes(canvas, width, height);

            // Draw quartiles
            drawQuartiles(canvas, width, height);

            // Draw outliers
            drawOutliers(canvas, width, height);

            // Draw median lines and boxes
            drawMedians(canvas, width, height);
        }

        // Draw X and Y axes
        private void drawAxes(Canvas canvas, int width, int height) {
            paint.setColor(Color.BLACK);
            canvas.drawLine(100, height - 100, width - 100, height - 100, paint); // X axis
            canvas.drawLine(100, 100, 100, height - 100, paint); // Y axis

            // Draw axis labels
            paint.setTextSize(40);
            canvas.drawText("Frequency", width / 2 - 100, height - 20, paint);
            canvas.drawText("Values", 20, height / 2 + 50, paint);

            // Draw x-axis values
            paint.setTextSize(40);
            String[] xValues = getXAxisValues();
            float xInterval = (width - 200) / (float) xValues.length;
            for (int i = 0; i < xValues.length; i++) {
                float x = 100 + i * xInterval;
                canvas.drawText(xValues[i], x, height - 70, paint);
            }

            // Draw y-axis values
            String[] yValues = getYAxisValues();
            float yInterval = (height - 200) / (float) yValues.length;
            for (int i = 0; i < yValues.length; i++) {
                float y = height - 100 - i * yInterval;
                canvas.drawText(yValues[i], 70, y, paint);
            }
        }

        // Get x-axis values from user input
        private String[] getXAxisValues() {
            String quartilesText = quartilesInput.getText().toString();
            return quartilesText.split(",");
        }

        // Get y-axis values from user input
        private String[] getYAxisValues() {
            String outliersText = outlierInput.getText().toString();
            return outliersText.split(",");
        }

        // Draw quartiles
        private void drawQuartiles(Canvas canvas, int width, int height) {
            paint.setColor(Color.GREEN);
            float medianY = height / 2; // Y-coordinate for quartiles and outliers

            for (int quartile : quartiles) {
                float quartileX = (float) (100 + quartile / 100.0 * (width - 200));
                canvas.drawRect(quartileX - 20, medianY - LINE_LENGTH / 2, quartileX + 20, medianY + LINE_LENGTH / 2, paint);
            }
        }

        // Draw outliers
        private void drawOutliers(Canvas canvas, int width, int height) {
            paint.setColor(Color.RED);
            float medianY = height / 2; // Y-coordinate for quartiles and outliers

            for (int outlier : outliers) {
                float outlierX = (float) (100 + outlier / 100.0 * (width - 200));
                canvas.drawCircle(outlierX, medianY, 10, paint);
            }
        }

        // Draw median lines and boxes
        private void drawMedians(Canvas canvas, int width, int height) {
            paint.setColor(Color.BLUE);
            for (String medianStr : medians) {
                int medianValue = Integer.parseInt(medianStr.trim());
                float medianX = (float) (100 + medianValue / 100.0 * (width - 200));

                // Draw median line
                canvas.drawLine(medianX, height / 2 - LINE_LENGTH / 2, medianX, height / 2 + LINE_LENGTH / 2, paint);
            }
        }
    }

    // Parse input string to int array
    private int[] parseValues(String input) {
        String[] valuesArray = input.split(",");
        int[] values = new int[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Integer.parseInt(valuesArray[i].trim());
        }
        return values;
    }
}
