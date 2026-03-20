import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {
    public void writeToCSV(String pollutant) throws Exception {
        
        String filePath = createFilePath(pollutant);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            
            DataSet standardDataSet = createStandardDataSet();
            DataSet APIDataSet = callAPI(standardDataSet, pollutant);
            
            // Write header
            writer.println(pollutant);
            writer.println(APIDataSet.getYear());
            writer.println(APIDataSet.getMetric());
            writer.println(APIDataSet.getUnits());
            writer.println();
            
            // actual values and their field labels
            writer.println("ukgridcode,x,y,value");
            
            for (DataPoint dataPoint: APIDataSet.getData()){
                writer.println(dataPoint.gridCode() + "," + dataPoint.x() + "," + dataPoint.y() + "," + dataPoint.value());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String createFilePath(String pollutant) {
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        String filePath = "UKAirPollutionData/" + folderName + "/current.csv";
        return filePath;
    }
    
    private DataSet createStandardDataSet() {
        DataLoader myLoader = new DataLoader();
        DataSet standardData = myLoader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");
        
        return standardData;
    }
    
    private DataSet callAPI(DataSet dataSet, String pollutant) throws Exception {
        DataRetriever dataRetriever = new DataRetriever();
        DataSet APIDataSet = dataRetriever.getAllDataFromAPI(dataSet, pollutant);
        return APIDataSet;
    }
}