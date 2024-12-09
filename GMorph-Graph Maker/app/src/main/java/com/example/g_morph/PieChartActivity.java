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

public class PieChartActivity extends AppCompatActivity {
    EditText valuesInput;
    EditText colorInput;
    Button generateButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        // EditText fields for entering values and colors
        valuesInput = findViewById(R.id.values_input);
        colorInput = findViewById(R.id.color_input);

        // Button for generating pie chart
        generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePieChart(view);
            }
        });
    }

    // Method to generate pie chart
    public void generatePieChart(View view) {
        String valuesText = valuesInput.getText().toString();
        String colorText = colorInput.getText().toString();

        // Split values input by commas
        String[] valuesArray = valuesText.split(",");
        String[] colorArray = colorText.split(",");

        // Convert string arrays to double arrays for values
        double[] values = new double[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Double.parseDouble(valuesArray[i].trim());
        }

        // Get the LinearLayout where we will add the pie chart
        LinearLayout chartLayout = findViewById(R.id.pie_chart_layout);

        // Remove any previous views
        chartLayout.removeAllViews();

        // Add the pie chart view
        chartLayout.addView(new PieChartView(this, values, colorArray, valuesArray));
    }

    // Custom view for drawing pie chart
    class PieChartView extends View {
        private Paint paint = new Paint();
        private double[] values;
        private String[] colors;
        private String[] valueTexts;

        public PieChartView(Context context, double[] values, String[] colors, String[] valueTexts) {
            super(context);
            this.values = values;
            this.colors = colors;
            this.valueTexts = valueTexts;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();
            int radius = Math.min(width, height) / 2;
            int cx = width / 2;
            int cy = height / 2;
            float startAngle = 0;

            for (int i = 0; i < values.length; i++) {
                float sweepAngle = (float) (360 * values[i] / sumArray(values));
                paint.setColor(Color.parseColor(colors[i]));
                canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, startAngle, sweepAngle, true, paint);
                drawValueText(canvas, startAngle, sweepAngle, values[i], cx, cy, radius);
                startAngle += sweepAngle;
            }
        }

        // Method to calculate the sum of an array
        private double sumArray(double[] array) {
            double sum = 0;
            for (double value : array) {
                sum += value;
            }
            return sum;
        }

        // Method to draw the value text inside the pie chart
        private void drawValueText(Canvas canvas, float startAngle, float sweepAngle, double value, int cx, int cy, int radius) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            float angle = startAngle + sweepAngle / 2;
            float textX = (float) (cx + radius / 2 * Math.cos(Math.toRadians(angle)));
            float textY = (float) (cy + radius / 2 * Math.sin(Math.toRadians(angle)));
            canvas.drawText(String.valueOf(value), textX, textY, paint);
        }
    }
}
