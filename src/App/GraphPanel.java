package App;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class EfficiencyChartPanel extends JPanel {

    public EfficiencyChartPanel() {
        setLayout(new GridLayout(1, 3)); // 1 row, 3 columns

        add(createEfficiencyChart("Graph 1")); // Example for graph 1
        add(createEfficiencyChart("Graph 2")); // Example for graph 2
        add(createEfficiencyChart("Graph 3")); // Example for graph 3
    }

    private JPanel createEfficiencyChart(String title) {
        XYSeries series = new XYSeries("Efficiency");
        Random rand = new Random();
        for (int month = 1; month <= 12; month++) {
            series.add(month, rand.nextDouble() * 100); // Random efficiency values for demonstration
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Chart title
                "Month", // X-axis label
                "Efficiency (%)", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL,
                true, // Show legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }
}

