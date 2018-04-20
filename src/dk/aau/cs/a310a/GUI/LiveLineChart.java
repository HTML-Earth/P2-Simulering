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
    ObservableList<XYChart.Series<String, Double>> chartData;
    XYChart.Series<String, Double> sSeries;
    XYChart.Series<String, Double> iSeries;
    XYChart.Series<String, Double> rSeries;

    public LineChart createLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        lineChart = new LineChart(xAxis, yAxis);
        chartData = getChartData();

        lineChart.setData(chartData);
        lineChart.setTitle("Live Zombe");
        return lineChart;
    }

    public void updateLineChart() {
        int tick = Simulator.clock.getCurrentTick();
        String susceptible = Simulator.theSimulator.healthCount(Person.health.Susceptible);
        String infected = Simulator.theSimulator.healthCount(Person.health.Infected);
        String recovered = Simulator.theSimulator.healthCount(Person.health.Recovered);
        sSeries.getData().add(new XYChart.Data(tick, susceptible));
        iSeries.getData().add(new XYChart.Data(tick, infected));
        rSeries.getData().add(new XYChart.Data(tick, recovered));
        lineChart.setData(chartData);
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() {
        double sValue = 10;
        double iValue = 20;
        ObservableList<XYChart.Series<String, Double>> answer = FXCollections
                .observableArrayList();
        sSeries = new XYChart.Series<String, Double>();
        iSeries = new XYChart.Series<String, Double>();
        rSeries = new XYChart.Series<String, Double>();
        sSeries.setName("Susceptible");
        iSeries.setName("Infected");
        rSeries.setName("Recovered");

        answer.addAll(sSeries, iSeries, rSeries);
        return answer;
    }
}
