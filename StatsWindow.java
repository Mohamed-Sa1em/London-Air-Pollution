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
 * Write a description of JavaFX class StatsWindow here.
 *
 * @author (your name)
 * @version 1.0
 */
public class StatsWindow extends BorderPane
{
    // the map used in the stats window
    private MapController map;
    
    // the graphs 
    private Graph no2Graph;
    private Graph pm25Graph;
    private Graph pm10Graph;
    private Graph allGraph;
    
    public StatsWindow()
    {
        //super(root,width,height);
        this.map = new MapController();
        
        this.setLeft(createSelectionBar());
        this.setCenter(createEmpty());
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
        vbox.getStyleClass().add("filters"); // adding stylesheet to the node
        
        Label titleLabel = new Label("Stats");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));

        Button averagePollutantLevel = new Button("Average Pollutant Level");
        averagePollutantLevel.setOnAction(this::avgPollutantScreen);
        
        Button highestPollutantLevel = new Button("Highest Pollutant Levels");
        highestPollutantLevel.setOnAction(this::highPollutantScreen);
        
        Button trendsOverTime = new Button("Trends over Time");
        trendsOverTime.setOnAction(this::trendOverTimeScreen);
        
        vbox.getChildren().addAll(titleLabel, averagePollutantLevel, highestPollutantLevel, trendsOverTime);
        
        vbox.setMaxWidth(110);
        return vbox;
    }
    
    private void avgPollutantScreen(ActionEvent event) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);
                
        HBox column = new HBox(250);
        column.setAlignment(Pos.CENTER);
        
        Label coulumnTitleLabel = new Label("Select the Pollutant,\n starting Year and ending Year");
        coulumnTitleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        VBox pollutantAndYearCoulmn = new VBox(75);
        pollutantAndYearCoulmn.setAlignment(Pos.CENTER);
        
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
        selectButton.setOnAction(selectionEvent -> {makePeriodMap(pollutantSelectorComboBox, startingYearSelectorComboBox, endingYearSelectorComboBox);});
        
        Label orLabel = new Label("OR");
        orLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        VBox coordColumn = new VBox(100);
        coordColumn.setAlignment(Pos.CENTER);
        
        Label mapText = new Label("Enter Easting and Northing:");
        mapText.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        TextField eastingCoord = new TextField("Enter Easting");
        TextField northingCoord = new TextField("Enter Northing");
        
        Button selectCoord = new Button("Select Coord");
        //selectCoord.setOnAction(event -> {mapSelection(eastingCoord.);})
        
        yearBar.getChildren().addAll(startingYearSelectorComboBox, endingYearSelectorComboBox);
        
        pollutantAndYearCoulmn.getChildren().addAll(coulumnTitleLabel, pollutantSelectorComboBox, yearBar, selectButton);
        
        coordColumn.getChildren().addAll(eastingCoord, northingCoord, selectCoord);
        
        column.getChildren().addAll(pollutantAndYearCoulmn, orLabel, coordColumn);
        
        root.getChildren().addAll(column);
        
        this.setCenter(root);
        
    }
    
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
    
    private void displayMap(ComboBox<String> pollutant, ComboBox<String> year) {
        String selectedYear = year.getValue();
        String selectedPollutant = pollutant.getValue();
        
        this.setCenter(map);
        map.showHighestPollutant(selectedYear, selectedPollutant);
    }
    
    private void drawNO2Graph(ActionEvent event) {
        if (no2Graph == null) {this.no2Graph = new Graph("NO2");}
        this.setCenter(no2Graph.returnLineChart());
    }
    
    private void drawPM25Graph(ActionEvent event) {
        if (pm25Graph == null) {this.pm25Graph = new Graph("PM2.5");}
        this.setCenter(pm25Graph.returnLineChart());
    }

    private void drawPM10Graph(ActionEvent event) {
        if (pm10Graph == null) {this.pm10Graph = new Graph("PM10");}
        this.setCenter(pm10Graph.returnLineChart());
    }
    
    private void drawAllThreeGraph(ActionEvent event) {
        if (allGraph == null) {this.allGraph = new Graph("NO2", "PM2.5", "PM10");}
        this.setCenter(allGraph.returnLineChart());
    }
    
    private void mapSelection() {
        //this.setCenter(map);
        //ArrayList<Double> coord = map.selectArea();
    }
    
    private void makePeriodMap(ComboBox<String> pollutant, ComboBox<String> start, ComboBox<String> end) {
        String selectedPollutant = pollutant.getValue();
        String startYear = start.getValue();
        String endYear = end.getValue();
        
        if (Integer.valueOf(startYear) < Integer.valueOf(endYear) ) {
            Graph graph = new Graph (selectedPollutant, startYear, endYear, true);
            this.setCenter(graph.returnLineChart());
        } else {
            this.setCenter(new Label("Invalid Date"));
        }
    }
}