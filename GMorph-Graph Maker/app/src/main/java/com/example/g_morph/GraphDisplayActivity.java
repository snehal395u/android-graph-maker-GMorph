package com.example.g_morph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GraphDisplayActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_display);

        int[] values = getIntent().getIntArrayExtra("values");
        String[] labels = getIntent().getStringArrayExtra("labels");
        String xAxisTitle = getIntent().getStringExtra("xAxisTitle");
        String yAxisTitle = getIntent().getStringExtra("yAxisTitle");

        LinearLayout chartLayout = findViewById(R.id.graph_layout);

        // Create custom view for displaying the graph
        chartLayout.addView(new GraphView(this, values, labels, xAxisTitle, yAxisTitle));
    }

    class GraphView extends View {
        private Paint paint = new Paint();
        private Paint textPaint = new Paint();
        private int[] values;
        private String[] labels;
        private String xAxisTitle;
        private String yAxisTitle;
        private int maxValue;
        private int barWidth;

        public GraphView(Context context, int[] values, String[] labels, String xAxisTitle, String yAxisTitle) {
            super(context);
            this.values = values;
            this.labels = labels;
            this.xAxisTitle = xAxisTitle;
            this.yAxisTitle = yAxisTitle;
            this.maxValue = calculateMaxValue(values);
            paint.setColor(Color.BLUE);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(30);
            paint.setStrokeWidth(5);
            barWidth = 100;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Draw Y axis
            canvas.drawLine(100, 100, 100, height - 100, paint);

            // Draw X axis
            canvas.drawLine(100, height - 100, width - 100, height - 100, paint);

            // Draw Y axis title
            canvas.drawText(yAxisTitle, 50, height / 2, textPaint);

            // Draw X axis title
            canvas.drawText(xAxisTitle, width / 2, height - 50, textPaint);

            // Draw Y axis labels
            for (int i = 0; i <= maxValue; i += 10) {
                float yPos = height - 100 - ((height - 200) * i / maxValue);
                canvas.drawText(Integer.toString(i), 50, yPos, textPaint);
            }

            // Draw X axis labels and bars
            int barSpacing = (width - 200 - barWidth * values.length) / (values.length + 1);
            float xPos = 100 + barSpacing;
            for (int i = 0; i < values.length; i++) {
                float yPos = height - 100 - ((height - 200) * values[i] / maxValue);
                canvas.drawText(labels[i], xPos + barWidth / 2, height - 50, textPaint);
                canvas.drawRect(xPos, yPos, xPos + barWidth, height - 100, paint);
                xPos += barWidth + barSpacing;
            }
        }

        private int calculateMaxValue(int[] values) {
            int maxValue = 0;
            for (int value : values) {
                if (value > maxValue) {
                    maxValue = value;
                }
            }
            return maxValue;
        }
    }
}
