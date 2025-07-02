package com.example.homework_19;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    final int MIN = -10, MAX = 10;
    final int k = 100;
    LineChart lineChart;
    ImageButton send;
    EditText expression;

    ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.line_chart);
        send = findViewById(R.id.send);
        expression = findViewById(R.id.expression);

        send.setOnClickListener(this::onSendClick);
        onSendClick(null);
    }

    private void onSendClick(View v) {
        int n = (MAX-MIN) * k + 1;
        entries = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            entries.add(new Entry((i + MIN*k) / (float)k, 0));
        }

        // Настройка данных
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(Color.parseColor("#FF0000FF"));

        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        lineChart.getLegend().setEnabled(false);
        dataSet.setLineWidth(2f);

        // Добавление данных на график
        LineData lineData = new LineData(dataSet);
        lineData.setHighlightEnabled(false);
        lineChart.setData(lineData);

        // Настройка осей
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        lineChart.getXAxis().setLabelCount(10, true);
//        lineChart.getAxisLeft().setLabelCount(20, true);
        lineChart.getAxisRight().setEnabled(false);

//        lineChart.getXAxis().setAxisMinimum(MIN);
//        lineChart.getXAxis().setAxisMaximum(MAX);

        Description dd = new Description();
        dd.setEnabled(false);
        lineChart.setDescription(dd);


        Argument arg = new Argument("x");
        Expression e = new Expression(expression.getText().toString(), arg);

        float min = Float.MAX_VALUE, max = Float.MIN_VALUE;

        for (int i = 0; i < entries.size(); i++) {
            arg.setArgumentValue(entries.get(i).getX());
            entries.get(i).setY((float)e.calculate());
            if (entries.get(i).getY() > max) max = entries.get(i).getY();
            if (entries.get(i).getY() < min) min = entries.get(i).getY();
        }

        lineChart.getAxisLeft().setAxisMinimum(min-1);
        lineChart.getAxisLeft().setAxisMaximum(max-1);

//        lineChart.setVisibleYRange(min - 1, max + 1, YAxis.AxisDependency.LEFT);
        lineChart.invalidate();
    }

}