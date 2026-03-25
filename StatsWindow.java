import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.ArrayList;

/**
 * StatsWindow class houses all the statistical functionality of the program, allowing 
 * users to see graphs of the data and see trends over time.
 *
 * @author Salaah Shameem
 * @version 1.1
 */
public class StatsWindow extends BorderPane
{
    // the map used in the stats window
    private MapController map;
    
    // the main graphs 
    private Graph no2Graph;
    private Graph pm25Graph;
    private Graph pm10Graph;
    private Graph allGraph;
    
    private Graph random;
    
    /**
     * Constructor for an object of StatsWindow
     */
    public StatsWindow()
    {
        //super(root,width,height);
        this.map = new MapController();
        
        this.setLeft(createSelectionBar());
        avgPollutantScreen();
    }
    
    /**
     * Place holder text
     */
    private Label createEmpty() {
        Label label = new Label("Hello world");
        return label;
    }
    
    /**
     * Creates the button for the user to select the wanted functionality
     * @return VBox
     */
    private VBox createSelectionBar() {
        VBox vbox = new VBox(50);
        vbox.getStyleClass().add("stats-bar"); // adding stylesheet to the node
        
        Label titleLabel = new Label("Stats");
        titleLabel.getStyleClass().add("bar-title");
        
        ToggleButton averagePollutantLevel = new ToggleButton("Average Pollutant Level");
        averagePollutantLevel.setOnAction(event -> avgPollutantScreen());
        averagePollutantLevel.getStyleClass().add("stats-button");
        
        ToggleButton highestPollutantLevel = new ToggleButton("Highest Pollutant Levels");
        highestPollutantLevel.setOnAction(this::highPollutantScreen);
        highestPollutantLevel.getStyleClass().add("stats-button");
        
        ToggleButton trendsOverTime = new ToggleButton("Trends over Time");
        trendsOverTime.setOnAction(this::trendOverTimeScreen);
        trendsOverTime.getStyleClass().add("stats-button");
        
        ToggleGroup group = new ToggleGroup();
        averagePollutantLevel.setToggleGroup(group);
        highestPollutantLevel.setToggleGroup(group);
        trendsOverTime.setToggleGroup(group);
        
        averagePollutantLevel.setSelected(true);
        
        vbox.getChildren().addAll(titleLabel, averagePollutantLevel, highestPollutantLevel, trendsOverTime);
        return vbox;
    }
    
    /**
     * Creates the average Pollutant Screen, allowing users to either choose to show data from 
     * a range of years or to find the data from a specific grid location.
     */
    private void avgPollutantScreen() {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);
        
        Label coulumnTitleLabel = new Label("Select the Pollutant, starting Year and ending Year \n (The years MUST be different)");
        coulumnTitleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        // for the year-selection side of the screen
        
        HBox yearBar = new HBox(100);
        yearBar.setAlignment(Pos.CENTER);
        
        ComboBox<String> startingYearSelectorComboBox = new ComboBox<>();
        startingYearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023"
        );

        startingYearSelectorComboBox.getSelectionModel().selectFirst();
        
        ComboBox<String> endingYearSelectorComboBox = new ComboBox<>();
        endingYearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023"
        );

        endingYearSelectorComboBox.getSelectionModel().selectFirst();


        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );
        pollutantSelectorComboBox.getSelectionModel().selectFirst();
        

        Button selectButton = new Button("Select");
        selectButton.setOnAction(selectionEvent -> {findAverageValues(pollutantSelectorComboBox, startingYearSelectorComboBox, endingYearSelectorComboBox);});
        
        
        yearBar.getChildren().addAll(startingYearSelectorComboBox, endingYearSelectorComboBox);
        
        root.getChildren().addAll(coulumnTitleLabel, pollutantSelectorComboBox, yearBar, selectButton);
        
        this.setCenter(root);
    }
    
    /**
     * Creates the highest pollutant screen, allowing users to choose a pollutant and a year
     * and a map with the location of the highest recorded value is shown.
     */
    private void highPollutantScreen(ActionEvent event) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);

        
        Label titleLabel = new Label("Select the Pollutant and the Year");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        HBox pollutantAndYearBar = new HBox(75);
        pollutantAndYearBar.setAlignment(Pos.CENTER);
        
        
        ComboBox<String> yearSelectorComboBox = new ComboBox<>();
        yearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023"
        );

        yearSelectorComboBox.getSelectionModel().selectFirst();


        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );
        pollutantSelectorComboBox.getSelectionModel().selectFirst();
        
        Button selectButton = new Button("Select");
        selectButton.setOnAction(selectionEvent -> {displayMap(pollutantSelectorComboBox, yearSelectorComboBox);});
        
        pollutantAndYearBar.getChildren().addAll(pollutantSelectorComboBox, yearSelectorComboBox, selectButton);
        
        root.getChildren().addAll(titleLabel, pollutantAndYearBar);
        this.setCenter(root);
    }
    
    /**
     * Creates the trend over time screen, allowing users to choose a pollutant (or all three together),
     * which produces a graph for the pollutant. 
     */
    private void trendOverTimeScreen(ActionEvent event) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Select the Pollutant");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        HBox pollutantBar = new HBox(75);
        pollutantBar.setAlignment(Pos.CENTER);
        
        Button no2 = new Button("NO2");
        no2.setOnAction(this::drawNO2Graph);
        
        Button pm25 = new Button("PM2.5");
        pm25.setOnAction(this::drawPM25Graph);
        
        Button pm10 = new Button("PM10");
        pm10.setOnAction(this::drawPM10Graph);
        
        Button allThree = new Button("All 3 together");
        allThree.setOnAction(this::drawAllThreeGraph);
        
        pollutantBar.getChildren().addAll(no2, pm25, pm10, allThree);
        
        root.getChildren().addAll(titleLabel, pollutantBar);
        this.setCenter(root);
    }
    
    /**
     * Shows the map with the location of the highest pollutant
     */
    private void displayMap(ComboBox<String> pollutant, ComboBox<String> year) {
        String selectedYear = year.getValue();
        String selectedPollutant = pollutant.getValue();
        
        this.setCenter(map);
        map.showHighestPollutant(selectedYear, selectedPollutant);
    }
    
    /**
     * Draws the NO2 graph
     */
    private void drawNO2Graph(ActionEvent event) {
        if (no2Graph == null) {this.no2Graph = new Graph("NO2");}
        this.setCenter(no2Graph.returnLineChart());
    }
    
    /**
     * Draws the PM2.5 graph
     */
    private void drawPM25Graph(ActionEvent event) {
        if (pm25Graph == null) {this.pm25Graph = new Graph("PM2.5");}
        this.setCenter(pm25Graph.returnLineChart());
    }

    /**
     * Draws the PM10 graph
     */
    private void drawPM10Graph(ActionEvent event) {
        if (pm10Graph == null) {this.pm10Graph = new Graph("PM10");}
        this.setCenter(pm10Graph.returnLineChart());
    }
    
    /**
     * Draws a graph with all three pollutants 
     */
    private void drawAllThreeGraph(ActionEvent event) {
        if (allGraph == null) {this.allGraph = new Graph("NO2", "PM2.5", "PM10");}
        this.setCenter(allGraph.returnLineChart());
    }
    
    /**
     * Draws a graph for a pollutant for a range of years.
     */
    private void findAverageValues(ComboBox<String> pollutant, ComboBox<String> start, ComboBox<String> end) {
        String selectedPollutant = pollutant.getValue();
        String startYear = start.getValue();
        String endYear = end.getValue();
        
        // checks if the date range selected is valid
        if (Integer.valueOf(startYear) >= Integer.valueOf(endYear)) {
            Alert incorrectDateAlert = new Alert(Alert.AlertType.ERROR);
            incorrectDateAlert.setTitle("Error");
            incorrectDateAlert.setHeaderText("Date range selected is not valid");
            
            incorrectDateAlert.showAndWait();
            this.avgPollutantScreen();
            return;
        }
        
        this.setCenter(map);
        map.showAverageForTimePeriod(selectedPollutant, startYear, endYear);
    }
}