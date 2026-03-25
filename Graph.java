import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.HashMap;
import java.util.Set;

/**
 * The Graph class is used to create graphs for the relevant data and display within the Statistic Page.
 *
 * @author Salaah Shameem
 * @version 1.1
 */
public class Graph 
{
    private Calculation calc;
    
    // private attributes 
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    
    private String pollutant;

    // attributes that may be needed for three line graphs together  
    private XYChart.Series<Number, Number> series2;
    private XYChart.Series<Number, Number> series3;

    private String pollutant2;
    private String pollutant3;


    /**
     * Constructor for objects of class Graph with only one pollutant
     */
    public Graph(String pollutant)
    {
        this.pollutant = pollutant;
        this.calc = new Calculation();
        
        // Create axes
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2017);
        xAxis.setUpperBound(2024);
        xAxis.setLabel("Time");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average Reading \n across London");

        // Create line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average " + this.pollutant + " levels over time");

        // Create data series, finding average and adding to series
        series = new XYChart.Series<>();
        series.setName(this.pollutant);
        HashMap<String, Double> averages = calc.findAverageFromAllYears(this.pollutant);
        addToSeries(averages, series);
        
        // Add series to chart
        lineChart.getData().add(series);
    }
    
    /**
     * Constructor for objects of class Graph with three pollutants
     */
    public Graph(String pollutantOne, String pollutantTwo, String pollutantThree) {
        this.pollutant = pollutantOne;
        this.pollutant2 = pollutantTwo;
        this.pollutant3 = pollutantThree;
        
        this.calc = new Calculation();

        
        // Create axes
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2017);
        xAxis.setUpperBound(2024);
        xAxis.setLabel("Time");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average Reading \n across London");

        // Create line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average polluant levels over time");
        
        // Create data series and finding averages
        series = new XYChart.Series<>();
        series.setName(this.pollutant);
        HashMap<String, Double> averageOne = calc.findAverageFromAllYears(this.pollutant);
        addToSeries(averageOne, series);
        
        series2 = new XYChart.Series<>();
        series2.setName(this.pollutant2);
        HashMap<String, Double> averageTwo = calc.findAverageFromAllYears(this.pollutant2);
        addToSeries(averageTwo, series2);
        
        series3 = new XYChart.Series<>();
        series3.setName(this.pollutant3);
        HashMap<String, Double> averageThree = calc.findAverageFromAllYears(this.pollutant3);
        addToSeries(averageThree, series3);
        
        // adding the averages to the respective series
        addToSeries(averageOne, this.series);
        addToSeries(averageTwo, this.series2);
        addToSeries(averageThree, this.series3);
        
         // Add series to chart
        lineChart.getData().addAll(series, series2, series3);
    }
    
    /**
     * @return the linechart
     */
    public LineChart<Number, Number> returnLineChart() {
        return lineChart;
    }
    
    /**
     * Adds the data to the series
     */
    private void addToSeries(HashMap<String, Double> map, XYChart.Series<Number, Number> series) {
        Set<String> years = map.keySet();
        
        for (String year: years) {
            int intYear = Integer.valueOf(year);
            series.getData().add(new XYChart.Data<>(intYear, map.get(year)));
        }
    }
    
}