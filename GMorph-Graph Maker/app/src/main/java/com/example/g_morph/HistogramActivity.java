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

public class HistogramActivity extends AppCompatActivity {
    EditText valuesInput;
    EditText colorInput;
    EditText xAxisInput;
    EditText yAxisInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);

        // EditText fields for entering values, colors, x-axis, and y-axis values
        valuesInput = findViewById(R.id.values_input);
        colorInput = findViewById(R.id.color_input);
        xAxisInput = findViewById(R.id.x_axis_input);
        yAxisInput = findViewById(R.id.y_axis_input);
    }

    // Method to generate histogram
    public void generateHistogram(View view) {
        String valuesText = valuesInput.getText().toString();
        String colorText = colorInput.getText().toString();
        String xAxisText = xAxisInput.getText().toString();
        String yAxisText = yAxisInput.getText().toString();

        // Split values input by commas
        String[] valuesArray = valuesText.split(",");
        String[] colorArray = colorText.split(",");

        // Convert string arrays to double arrays for values
        double[] values = new double[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Double.parseDouble(valuesArray[i].trim());
        }

        // Get the LinearLayout where we will add the histogram
        LinearLayout chartLayout = findViewById(R.id.histogram_layout);

        // Remove any previous views
        chartLayout.removeAllViews();

        // Add the histogram view
        chartLayout.addView(new HistogramView(this, values, colorArray, xAxisText, yAxisText));
    }

    // Custom view for drawing histogram
    class HistogramView extends View {
        private Paint paint = new Paint();
        private double[] values;
        private String[] colors;
        private String xAxisLabel;
        private String yAxisLabel;

        public HistogramView(Context context, double[] values, String[] colors, String xAxisLabel, String yAxisLabel) {
            super(context);
            this.values = values;
            this.colors = colors;
            this.xAxisLabel = xAxisLabel;
            this.yAxisLabel = yAxisLabel;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Draw X and Y axes
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(100, height - 100, width - 100, height - 100, paint); // X axis
            canvas.drawLine(100, 100, 100, height - 100, paint); // Y axis

            // Draw X axis labels
            paint.setTextSize(40);
            float xTextStart = (width - 200) / (values.length * 2) + 100;
            for (int i = 0; i < values.length; i++) {
                float x = (width - 200) / (values.length + 1) * (i + 1) + 100; // Adjust x position based on width
                canvas.drawText(String.valueOf(values[i]), x, height - 50, paint);
            }

            // Draw Y axis labels
            for (int i = 0; i < values.length; i++) {
                float y = height - ((float) (values[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                canvas.drawText(String.valueOf(values[i]), 50, y, paint);
            }

            // Draw histogram bars
            float barWidth = (width - 200) / (values.length * 2);
            for (int i = 0; i < values.length; i++) {
                float left = (width - 200) / (values.length + 1) * (i + 1) + 100 - barWidth / 2;
                float top = height - ((float) (values[i] / 100.0 * (height - 200)) + 100);
                float right = left + barWidth;
                float bottom = height - 100;
                paint.setColor(Color.parseColor(colors[i]));
                canvas.drawRect(left, top, right, bottom, paint);
            }

            // Draw X and Y axis labels
            paint.setTextSize(60);
            canvas.drawText(xAxisLabel, width / 2 - 100, height - 20, paint);
            canvas.drawText(yAxisLabel, 20, height / 2 + 100, paint);
        }
    }
}
