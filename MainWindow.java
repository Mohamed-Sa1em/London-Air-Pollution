
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import java.awt.event.ActionListener;

/**
 * Write a description of JavaFX class MainWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MainWindow extends Application
{
    private MapController map = new MapController();
    private WelcomeWindow welcome = new WelcomeWindow();
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();
        root.setBorder(new Border(
                new BorderStroke(
                    Color.BLACK,               // border color
                    BorderStrokeStyle.SOLID,   // line style
                    null,
                    new BorderWidths(3)        // thickness
                )
            ));
        root.setTop(createMenuBar(root));

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(root, 1200, 728);
        stage.setResizable(false); // makes the window non resizeable by the user
        stage.setTitle("Air Pollution Monitor");
        stage.setScene(scene);

        root.setCenter(new WelcomeWindow());
        //MapController myPollutionMap = new MapController();
        //root.setCenter(myPollutionMap);

        // Show the Stage (window)
        stage.show();
    }

    private HBox createMenuBar(BorderPane root) {
        HBox topBar = new HBox(10);
        topBar.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0;");

        Button welcomeWindowButton = new Button("Welcome");
        welcomeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e){
                    root.setCenter(welcome);
                    root.setLeft(null);

                }
            });
        welcomeWindowButton.setFont(new Font(20));

        Button dataMapButton = new Button("Data Map");
        dataMapButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e){
                    root.setCenter(map);
                    root.setLeft(createFiltersAndControlsBar());
                    DataLoader myLoader = new DataLoader();
                    DataSet no2Data = myLoader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");
                    map.plotData(no2Data);

                }
            });
        dataMapButton.setFont(new Font(20));

        Button pollutionStatsButton = new Button("Pollution Stats");
        pollutionStatsButton.setFont(new Font(20));

        topBar.getChildren().addAll(welcomeWindowButton, dataMapButton, pollutionStatsButton);

        return topBar;
    }

    private VBox createFiltersAndControlsBar() {
        VBox filtersBar = new VBox(20);
        filtersBar.setStyle("-fx-border-color: black; -fx-border-width: 0 2 0 0;");

        Label filtersAndControlsLabel = new Label ("Filters and Controls");
        Label selectYearLabel = new Label("Select Year");

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


        Label pollutantSelectionLabel = new Label("Select Pollutant");

        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "pm2.5",
            "pm10"

        );


        pollutantSelectorComboBox.getSelectionModel().selectFirst();

        //Handles the change of the year and the pollutant
        //Loading the new dataset

        yearSelectorComboBox.setOnAction(event -> {
                    String selectedYear = yearSelectorComboBox.getValue();
                    String currentPollutant = pollutantSelectorComboBox.getValue(); 
                    updateMapData(currentPollutant, selectedYear);});

        pollutantSelectorComboBox.setOnAction(event -> {
                    String selectedpollutant = pollutantSelectorComboBox.getValue();
                    String currentYear = yearSelectorComboBox.getValue();
                    updateMapData(selectedpollutant,currentYear);});


        filtersBar.getChildren().addAll(filtersAndControlsLabel, selectYearLabel, yearSelectorComboBox, pollutantSelectionLabel, pollutantSelectorComboBox);

        return filtersBar;
    }

    /**
     * Helper methood that get both the pollutant and the year
     * Then updates the map 
     */
    private void updateMapData(String pollutant, String year) {
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";

        DataLoader myLoader = new DataLoader();
        DataSet newData = myLoader.loadDataFile(filePath);

        if (newData != null) {
            map.plotData(newData);
        } else {
            System.out.println("Error: Could not find file " + filePath);
        }
    }
}