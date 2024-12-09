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

public class SpiderChartActivity extends AppCompatActivity {
    EditText xValuesInput;
    EditText yValuesInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_chart);

        // EditText fields for entering values
        xValuesInput = findViewById(R.id.x_values_input);
        yValuesInput = findViewById(R.id.y_values_input);

        // Get the LinearLayout where we will add the spider chart
        LinearLayout chartLayout = findViewById(R.id.spider_chart_layout);

        // Draw spider chart with default values and blue color
        double[] defaultValues = {0.6, 0.8, 0.7, 0.9, 0.5}; // Example values
        String defaultAxisTitles = "A,B,C,D,E"; // Example axis titles
        chartLayout.addView(new SpiderChartView(this, defaultValues, defaultAxisTitles));
    }

    // Custom view for drawing spider chart
    class SpiderChartView extends View {
        private final Paint paint = new Paint();
        private final double[] values;
        private final String[] axisTitles;

        public SpiderChartView(Context context, double[] values, String axisTitles) {
            super(context);
            this.values = values;
            this.axisTitles = axisTitles.split(",");
            paint.setStrokeWidth(5); // Set line width for spider chart
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            int numPoints = values.length;
            double angleStep = 2 * Math.PI / numPoints;
            float radius = Math.min(width, height) * 0.4f;

            // Draw X and Y axes
            paint.setColor(Color.BLACK);
            canvas.drawLine(centerX, centerY, centerX + radius, centerY, paint); // X axis
            canvas.drawText("X Axis", centerX + radius, centerY, paint); // X axis label

            canvas.drawLine(centerX, centerY, centerX, centerY - radius, paint); // Y axis
            canvas.drawText("Y Axis", centerX, centerY - radius, paint); // Y axis label

            // Draw axis lines and labels
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            for (int i = 0; i < numPoints; i++) {
                double angle = i * angleStep - Math.PI / 2;
                float startX = (float) (centerX + Math.cos(angle) * radius);
                float startY = (float) (centerY + Math.sin(angle) * radius);
                canvas.drawLine(centerX, centerY, startX, startY, paint);
                canvas.drawText(axisTitles[i], startX, startY, paint);
            }

            // Draw values as points in blue color
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < numPoints; i++) {
                double angle = i * angleStep - Math.PI / 2;
                float x = (float) (centerX + Math.cos(angle) * radius * values[i]);
                float y = (float) (centerY + Math.sin(angle) * radius * values[i]);
                canvas.drawCircle(x, y, 10, paint);
            }
        }
    }

    // Method to handle button click for generating spider chart
    public void generateSpiderChart(View view) {
        String valuesText = xValuesInput.getText().toString();
        String axisTitlesText = yValuesInput.getText().toString();

        // Split values input by commas
        String[] valuesArray = valuesText.split(",");
        // Convert string array to double array for values
        double[] values = new double[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Double.parseDouble(valuesArray[i].trim());
        }

        // Draw spider chart with user input values and blue color
        LinearLayout chartLayout = findViewById(R.id.spider_chart_layout);
        chartLayout.removeAllViews(); // Remove any previous views
        chartLayout.addView(new SpiderChartView(this, values, axisTitlesText));
    }
}
