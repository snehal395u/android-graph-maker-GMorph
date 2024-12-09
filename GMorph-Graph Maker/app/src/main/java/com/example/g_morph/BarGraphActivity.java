package com.example.g_morph;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BarGraphActivity extends AppCompatActivity {
    EditText valuesInput;
    EditText labelsInput;
    EditText xAxisTitleInput;
    EditText yAxisTitleInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);

        valuesInput = findViewById(R.id.values_input);
        labelsInput = findViewById(R.id.labels_input);
        xAxisTitleInput = findViewById(R.id.x_axis_title_input);
        yAxisTitleInput = findViewById(R.id.y_axis_title_input);
    }

    public void generateGraph(View view) {
        String valuesText = valuesInput.getText().toString();
        String labelsText = labelsInput.getText().toString();
        String xAxisTitle = xAxisTitleInput.getText().toString();
        String yAxisTitle = yAxisTitleInput.getText().toString();

        String[] valuesArray = valuesText.split(",");
        String[] labelsArray = labelsText.split(",");

        int[] values = new int[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Integer.parseInt(valuesArray[i].trim());
        }

        Intent intent = new Intent(this, GraphDisplayActivity.class);
        intent.putExtra("values", values);
        intent.putExtra("labels", labelsArray);
        intent.putExtra("xAxisTitle", xAxisTitle);
        intent.putExtra("yAxisTitle", yAxisTitle);
        startActivity(intent);
    }
}
