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

public class LineGraphActivity extends AppCompatActivity {

    EditText xValuesInput;
    EditText yValuesInput;
    EditText colorInput;
    EditText xAxisInput;
    EditText yAxisInput;
    Button generateButton;
    LinearLayout graphLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        // EditText fields
        xValuesInput = findViewById(R.id.x_values_input);
        yValuesInput = findViewById(R.id.y_values_input);
        colorInput = findViewById(R.id.color_input);
        xAxisInput = findViewById(R.id.x_axis_input);
        yAxisInput = findViewById(R.id.y_axis_input);

        // Button
        generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateGraph();
            }
        });

        // LinearLayout for graph
        graphLayout = findViewById(R.id.graph_layout);
    }

    private void generateGraph() {
        String xValuesText = xValuesInput.getText().toString();
        String yValuesText = yValuesInput.getText().toString();
        String colorText = colorInput.getText().toString();
        String xAxisText = xAxisInput.getText().toString();
        String yAxisText = yAxisInput.getText().toString();

        String[] xValuesArray = xValuesText.split(",");
        String[] yValuesArray = yValuesText.split(",");
        String[] colorArray = colorText.split(",");

        int[] xValues = new int[xValuesArray.length];
        int[] yValues = new int[yValuesArray.length];

        for (int i = 0; i < xValuesArray.length; i++) {
            xValues[i] = Integer.parseInt(xValuesArray[i].trim());
        }

        for (int i = 0; i < yValuesArray.length; i++) {
            yValues[i] = Integer.parseInt(yValuesArray[i].trim());
        }

        // Add the line graph view to graphLayout
        graphLayout.removeAllViews();
        graphLayout.addView(new LineGraphView(this, xValues, yValues, colorArray, xAxisText, yAxisText));
    }

    // Custom view for drawing line graph
    class LineGraphView extends View {
        private Paint paint = new Paint();
        private int[] xValues;
        private int[] yValues;
        private String[] colors;
        private String xAxisLabel;
        private String yAxisLabel;

        public LineGraphView(Context context, int[] xValues, int[] yValues, String[] colors, String xAxisLabel, String yAxisLabel) {
            super(context);
            this.xValues = xValues;
            this.yValues = yValues;
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
            for (int i = 0; i < xValues.length; i++) {
                float x = (width - 200) / (xValues.length + 1) * (i + 1) + 100; // Adjust x position based on width
                canvas.drawText(String.valueOf(xValues[i]), x, height - 50, paint);
            }

            // Draw Y axis labels
            for (int i = 0; i < yValues.length; i++) {
                float y = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100); // Adjust y position based on height
                canvas.drawText(String.valueOf(yValues[i]), 50, y, paint);
            }

            // Draw line graph
            paint.setStrokeWidth(8);
            for (int i = 0; i < xValues.length - 1; i++) {
                float startX = (width - 200) / (xValues.length + 1) * (i + 1) + 100;
                float startY = height - ((float) (yValues[i] / 100.0 * (height - 200)) + 100);
                float endX = (width - 200) / (xValues.length + 1) * (i + 2) + 100;
                float endY = height - ((float) (yValues[i + 1] / 100.0 * (height - 200)) + 100);
                paint.setColor(Color.parseColor(colors[i]));
                canvas.drawLine(startX, startY, endX, endY, paint);
            }

            // Draw X and Y axis labels
            paint.setTextSize(60);
            canvas.drawText(xAxisLabel, width / 2 - 100, height - 20, paint); // X-axis label
            canvas.drawText(yAxisLabel, 20, height / 2 + 100, paint); // Y-axis label
        }
    }
}
