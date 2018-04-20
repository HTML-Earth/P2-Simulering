package dk.aau.cs.a310a.GUI;


import dk.aau.cs.a310a.Simulation.Person;
import dk.aau.cs.a310a.Simulation.Simulator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LiveLineChart {
    LineChart lineChart;
    ObservableList<XYChart.Series<Integer, Integer>> chartData;
    XYChart.Series<Integer, Integer> sSeries;
    XYChart.Series<Integer, Integer> iSeries;
    XYChart.Series<Integer, Integer> rSeries;
    public final int ticksPerPoint = 144;

    public LineChart createLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        //yAxis.setForceZeroInRange(false)

        lineChart = new LineChart(xAxis, yAxis);
        chartData = getChartData();

        lineChart.setData(chartData);
        lineChart.setTitle("Live Zombe");
        lineChart.setAnimated(false);
        return lineChart;
    }

    public void resetLineChart() {
        sSeries.getData().clear();
        iSeries.getData().clear();
        rSeries.getData().clear();
    }

    public void updateLineChart() {

        Integer tick = Simulator.clock.getGraphTime();
        int susceptible = Simulator.theSimulator.healthCount(Person.health.Susceptible);
        int infected = Simulator.theSimulator.healthCount(Person.health.Infected);
        int recovered = Simulator.theSimulator.healthCount(Person.health.Recovered);
        sSeries.getData().add(new XYChart.Data(tick, susceptible));
        iSeries.getData().add(new XYChart.Data(tick, infected));
        rSeries.getData().add(new XYChart.Data(tick, recovered));
    }

    private ObservableList<XYChart.Series<Integer, Integer>> getChartData() {
        ObservableList<XYChart.Series<Integer, Integer>> answer = FXCollections
                .observableArrayList();
        sSeries = new XYChart.Series<Integer, Integer>();
        iSeries = new XYChart.Series<Integer, Integer>();
        rSeries = new XYChart.Series<Integer, Integer>();
        sSeries.setName("Susceptible");
        iSeries.setName("Infected");
        rSeries.setName("Recovered");

        answer.addAll(sSeries, iSeries, rSeries);
        return answer;
    }
}
