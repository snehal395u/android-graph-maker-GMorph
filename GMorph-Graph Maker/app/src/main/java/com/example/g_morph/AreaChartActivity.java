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

public class AreaChartActivity extends AppCompatActivity {

    private LinearLayout chartContainer;
    private EditText editTextData;
    private EditText editTextColors;
    private EditText editTextXLabels;
    private Button buttonGenerate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_chart);

        chartContainer = findViewById(R.id.chart_container);
        editTextData = findViewById(R.id.editTextData);
        editTextColors = findViewById(R.id.editTextColors);
        editTextXLabels = findViewById(R.id.editTextXLabels);
        buttonGenerate = findViewById(R.id.buttonGenerate);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChart();
            }
        });
    }

    private void generateChart() {
        // Get data from EditText fields
        String dataText = editTextData.getText().toString();
        String colorsText = editTextColors.getText().toString();

        // Convert data and colors strings to arrays
        String[] dataStringArray = dataText.split(",");
        String[] colorsStringArray = colorsText.split(",");

        int[] data = new int[dataStringArray.length];
        int[] colors = new int[colorsStringArray.length];

        for (int i = 0; i < dataStringArray.length; i++) {
            data[i] = Integer.parseInt(dataStringArray[i]);
            colors[i] = Color.parseColor(colorsStringArray[i]);
        }

        // Clear existing chart views
        chartContainer.removeAllViews();

        // Create new area chart view
        AreaChartView areaChartView = new AreaChartView(this, data, colors);
        chartContainer.addView(areaChartView);
    }

    public class AreaChartView extends View {
        private int[] data;
        private int[] colors;
        private Paint paint;

        public AreaChartView(Context context, int[] data, int[] colors) {
            super(context);
            this.data = data;
            this.colors = colors;
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int width = getWidth();
            int height = getHeight();

            // Calculate the width of each bar
            int barWidth = width / data.length;

            // Find the maximum value in the data array
            int maxValue = getMaxValue(data);

            // Calculate the scale factor for mapping data values to the height of the view
            float scaleFactor = (float) height / maxValue;

            // Increase the size of lines in the area chart
            paint.setStrokeWidth(5);

            // Draw the area chart
            for (int i = 0; i < data.length - 1; i++) {
                int x1 = i * barWidth;
                int x2 = (i + 1) * barWidth;
                int y1 = height - (int) (data[i] * scaleFactor);
                int y2 = height - (int) (data[i + 1] * scaleFactor);

                // Set color
                paint.setColor(colors[i]);

                // Draw the polygon connecting the data points
                canvas.drawLine(x1, y1, x2, y2, paint);
                canvas.drawLine(x2, y2, x2, height, paint); // Connect to the x-axis
            }

            // Draw x-axis line
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3); // Set the size of the x-axis line
            canvas.drawLine(0, height, width, height, paint);

            // Draw y-axis line
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3); // Set the size of the y-axis line
            canvas.drawLine(0, 0, 0, height, paint);

            // Draw x-axis labels from EditText
            String xLabels = editTextXLabels.getText().toString();
            String[] xLabelsArray = xLabels.split(",");
            paint.setColor(Color.BLACK); // Set label color to black
            paint.setTextSize(30);
            for (int i = 0; i < xLabelsArray.length; i++) {
                float xPos = (i + 0.5f) * barWidth;
                float yPos = height + 40; // Adjust the position of x-axis labels
                canvas.drawText(xLabelsArray[i].trim(), xPos, yPos, paint);
            }

            // Draw y-axis values
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            for (int i = 0; i <= maxValue; i += 10) {
                float yPos = height - (int) (i * scaleFactor);
                canvas.drawText(Integer.toString(i), 20, yPos, paint);
            }
        }

        // Helper method to find the maximum value in the data array
        private int getMaxValue(int[] data) {
            int maxValue = data[0];
            for (int value : data) {
                if (value > maxValue) {
                    maxValue = value;
                }
            }
            return maxValue;
        }
    }
}
