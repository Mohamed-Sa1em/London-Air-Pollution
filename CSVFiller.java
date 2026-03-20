
/**
 * Write a description of class CSVFiller here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CSVFiller
{
    private final CSVWriter CSVWriter = new CSVWriter();;
    public CSVFiller() {}
    
    public void fillAllCSV(String[] pollutants) throws Exception {
        for (String pollutant: pollutants) {
            CSVWriter.writeToCSV(pollutant);
        }
    }
}