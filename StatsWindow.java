import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.ArrayList;

/**
 * StatsWindow class houses all the statistical functionality of the program, allowing 
 * users to see graphs of the data and see trends over time.
 *
 * @author Salaah Shameem
 * @version 1.2
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
        
    /**
     * Constructor for an object of StatsWindow
     */
    public StatsWindow()
    {
        this.map = new MapController();
        
        this.setLeft(createSelectionBar());
        avgPollutantScreen();
    }
    
    /**
     * Creates the button for the user to select the wanted functionality
     * @return VBox
     */
    private VBox createSelectionBar() {
        VBox vbox = new VBox(50);
        vbox.getStyleClass().add("stats-bar");
        
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
        
        // Toggle group
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
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("stats-content");
        
        // Center Content
        VBox centerContent = new VBox(5);
        centerContent.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerContent, Priority.ALWAYS);
        
        Image avgPollutantImg = new Image("file:assets/avgPollute.png");
        ImageView avgPollutantView = new ImageView(avgPollutantImg);
        avgPollutantView.setFitWidth(350);
        avgPollutantView.setPreserveRatio(true);
        
        Label averagePollutantInfo = new Label("This section finds the mean pollution for a specified yearly range and displays it on the static map.");
        averagePollutantInfo.getStyleClass().add("stats-info");
        
        centerContent.getChildren().addAll(avgPollutantView,averagePollutantInfo);
        
        // Footer
        VBox footerVBox = new VBox(20);
        footerVBox.getStyleClass().add("stats-footer");
        
        Label columnTitleLabel = new Label("Select the Pollutant, Starting Year and Ending Year \n (The years MUST be different)");
        columnTitleLabel.getStyleClass().add("stats-subtitle");
        
        // for the year-selection boxes
        HBox yearBar = new HBox(100);
        yearBar.setAlignment(Pos.BOTTOM_CENTER);
        
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
        selectButton.getStyleClass().add("stats-info-button");
        
        yearBar.getChildren().addAll(startingYearSelectorComboBox, endingYearSelectorComboBox);
        
        footerVBox.getChildren().addAll(columnTitleLabel,pollutantSelectorComboBox, yearBar, selectButton);
        VBox.setMargin(footerVBox, new Insets(0, 20, 20, 20));
        
        root.getChildren().addAll(centerContent,footerVBox);
        
        this.setCenter(root);
    }
    
    /**
     * Creates the highest pollutant screen, allowing users to choose a pollutant and a year
     * and a map with the location of the highest recorded value is shown.
     */
    private void highPollutantScreen(ActionEvent event) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("stats-content");
        
        // Center Content
        VBox centerContent = new VBox(5);
        centerContent.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerContent, Priority.ALWAYS);

        Image highPollutantImg = new Image("file:assets/highPollute.png");
        ImageView highPollutantView = new ImageView(highPollutantImg);
        highPollutantView.setFitWidth(500);
        highPollutantView.setPreserveRatio(true);
        
        Label highPollutantInfo = new Label("This section finds the location of the highest record of each pollutant and displays it on the static map.");
        highPollutantInfo.getStyleClass().add("stats-info");
        
        centerContent.getChildren().addAll(highPollutantView,highPollutantInfo);
        
        // Footer
        VBox footerVBox = new VBox(20);
        footerVBox.getStyleClass().add("stats-footer");
        
        Label titleLabel = new Label("Select the Pollutant and the Year");
        titleLabel.getStyleClass().add("stats-subtitle");
        
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
        selectButton.getStyleClass().add("stats-info-button");
        
        pollutantAndYearBar.getChildren().addAll(pollutantSelectorComboBox, yearSelectorComboBox, selectButton);
        footerVBox.getChildren().addAll(titleLabel,pollutantAndYearBar);
        VBox.setMargin(footerVBox, new Insets(0, 20, 20, 20));
        
        root.getChildren().addAll(centerContent, footerVBox);
        this.setCenter(root);
    }
    
    /**
     * Creates the trend over time screen, allowing users to choose a pollutant (or all three together),
     * which produces a graph for the pollutant. 
     */
    private void trendOverTimeScreen(ActionEvent event) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("stats-content");
        
        // Center Content
        VBox centerContent = new VBox(5);
        centerContent.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerContent, Priority.ALWAYS);
        
        Image graphImg = new Image("file:assets/graph.png");
        ImageView graphView = new ImageView(graphImg);
        graphView.setFitWidth(500);
        graphView.setPreserveRatio(true);
        
        Label trendsInfo = new Label("This section finds the mean pollution each year and displays it as a graph.");
        trendsInfo.getStyleClass().add("stats-info");
        
        centerContent.getChildren().addAll(graphView,trendsInfo);
        
        // Footer
        VBox footerVBox = new VBox(20);
        footerVBox.getStyleClass().add("stats-footer");
        
        Label titleLabel = new Label("Select the Pollutant");
        titleLabel.getStyleClass().add("stats-subtitle");
        
        HBox pollutantBar = new HBox(75);
        pollutantBar.getStyleClass().add("stats-footer-content");
        
        Button no2 = new Button("NO2");
        no2.setOnAction(this::drawNO2Graph);
        no2.getStyleClass().add("stats-info-button");
        
        Button pm25 = new Button("PM2.5");
        pm25.setOnAction(this::drawPM25Graph);
        pm25.getStyleClass().add("stats-info-button");
        
        Button pm10 = new Button("PM10");
        pm10.setOnAction(this::drawPM10Graph);
        pm10.getStyleClass().add("stats-info-button");
        
        Button allThree = new Button("All 3 together");
        allThree.setOnAction(this::drawAllThreeGraph);
        allThree.getStyleClass().add("stats-info-button");
        
        pollutantBar.getChildren().addAll(no2, pm25, pm10, allThree);
        footerVBox.getChildren().addAll(titleLabel,pollutantBar);
        VBox.setMargin(footerVBox, new Insets(0, 20, 20, 20));
        
        root.getChildren().addAll(centerContent, footerVBox);
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
     * Works out the average pollutant values for the map area and displays it.
     */
    private void findAverageValues(ComboBox<String> pollutant, ComboBox<String> start, ComboBox<String> end) {
        String selectedPollutant = pollutant.getValue();
        String startYear = start.getValue();
        String endYear = end.getValue();
        
        // checks if the date range selected is valid
        if (Integer.valueOf(startYear) >= Integer.valueOf(endYear)) {
            Alert incorrectDateAlert = new Alert(Alert.AlertType.ERROR);
            incorrectDateAlert.setContentText("Date range selected is not valid");
            incorrectDateAlert.showAndWait();
            
            this.avgPollutantScreen(); // allows the user to choose again
            return;
        }
        
        this.setCenter(map);
        map.showAverageForTimePeriod(selectedPollutant, startYear, endYear);
    }
}