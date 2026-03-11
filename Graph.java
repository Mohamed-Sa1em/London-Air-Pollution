import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.*;

/**
 * Write a description of class Graph here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Graph 
{
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
        
        // Create axes
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2017);
        xAxis.setUpperBound(2024);
        xAxis.setLabel("Time");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average Reading \n across the UK");

        // Create line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average " + this.pollutant + " levels over time");

        // Create data series
        series = new XYChart.Series<>();
        series.setName(this.pollutant);
        
        findAverages(this.pollutant, this.series);
        
         // Add series to chart
        lineChart.getData().add(series);
    }
    
    /**
     * Constructor for objects of class Graph with only one pollutant
     */
    public Graph(String pollutantOne, String pollutantTwo, String pollutantThree) {
        pollutant = pollutantOne;
        pollutant2 = pollutantTwo;
        pollutant3 = pollutantThree;
        
        // Create axes
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2017);
        xAxis.setUpperBound(2024);
        xAxis.setLabel("Time");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average Reading \n across the UK");

        // Create line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average polluant levels over time");
        
        // Create data series
        series = new XYChart.Series<>();
        series.setName(this.pollutant);
        
        series2 = new XYChart.Series<>();
        series2.setName(this.pollutant2);
        
        series3 = new XYChart.Series<>();
        series3.setName(this.pollutant3);
        
        findAverages(this.pollutant, this.series);
        findAverages(this.pollutant2, this.series2);
        findAverages(this.pollutant3, this.series3);
        
         // Add series to chart
        lineChart.getData().addAll(series, series2, series3);
    }

    public LineChart<Number, Number> returnLineChart() {
        return lineChart;
    }
    
    private void findAverages(String pollutant, XYChart.Series<Number, Number> series) {
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        
        ArrayList<String> years = new ArrayList<>(List.of("2018", "2019", "2020", "2021", "2022", "2023"));
        DataLoader myLoader = new DataLoader();
        for (String year: years) {
            String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";
            DataSet data = myLoader.loadDataFile(filePath);
            List<DataPoint> dataPoints = data.getData();
            
            List<DataPoint> relevantData = findRelevantData(dataPoints);
            
            double average = findIndividualAverage(relevantData);
            addToGraph(year, average, series);
        }
    }
    
    private double findIndividualAverage(List<DataPoint> dp) {
        double sum = 0;
        for (DataPoint dataPoint: dp) {
            sum += dataPoint.value();
        }
        
        return sum / dp.size();
    }
    
    private void addToGraph(String year, double average, XYChart.Series<Number, Number> series) {
        int intYear = Integer.valueOf(year);
        series.getData().add(new XYChart.Data<>(intYear, average));
    }
    
    private List<DataPoint> findRelevantData(List<DataPoint> data) {
        double Max_Easting = 553297;
        double Min_Easting = 510394;
        double Max_Northing = 193305;
        double Min_Northing = 168504;
        
        ArrayList<DataPoint> relevant = new ArrayList<DataPoint>();
        for (DataPoint dataPoint: data) {
            double This_Easting = dataPoint.x();
            double This_Northing = dataPoint.y();
            if (This_Easting >= Min_Easting && This_Easting <= Max_Easting &&
                This_Northing >= Min_Northing && This_Northing <= Max_Northing
            ) {
                relevant.add(dataPoint);
            }
        }
        
        return relevant;
    }
}