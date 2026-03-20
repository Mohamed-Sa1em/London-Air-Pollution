import javafx.application.Application;
import java.io.FileInputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import java.awt.event.ActionListener;
import javafx.concurrent.Task;


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
    private StatsWindow stats = new StatsWindow();
    private InteractiveMap liveMap = new InteractiveMap();
    private boolean staticMap = false;
    private BorderPane root = new BorderPane();
    
    private final CSVFiller CSVfiller = new CSVFiller();
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override

    public void start(Stage stage) throws Exception,java.io.FileNotFoundException

    {
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
        scene.getStylesheets().add("styles.css");
        stage.setResizable(false); // makes the window non resizeable by the user
        stage.setTitle("Air Pollution Monitor");
        stage.setScene(scene);

        root.setCenter(new WelcomeWindow());
        //MapController myPollutionMap = new MapController();
        //root.setCenter(myPollutionMap);

        // Show the Stage (window)
        stage.show();
    }


    private HBox createMenuBar(BorderPane root) throws Exception, java.io.FileNotFoundException {
        HBox topBar = new HBox(10);
        topBar.getStyleClass().add("nav-bar");
        
        Image image = new Image("file:assets/Logo2.png");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100); 
        imageView.getStyleClass().add("logo");
        
        Region spacing = new Region();
        topBar.setHgrow(spacing, Priority.ALWAYS);
        
        Button welcomeWindowButton = new Button("Welcome");
        welcomeWindowButton.setOnAction(event -> {welcomeButtonPressed(event, root);});
        welcomeWindowButton.getStyleClass().add("nav-bar-button");

        Button dataMapButton = new Button("Data Map");
        
        dataMapButton.setOnAction(event -> {
            try
            {
                dataMapButtonPressed(event, root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        dataMapButton.setFont(new Font(20));
        dataMapButton.setMinWidth(dataMapButton.getPrefWidth());
        dataMapButton.getStyleClass().add("nav-bar-button");

        Button pollutionStatsButton = new Button("Pollution Stats");
        pollutionStatsButton.setOnAction(event -> {statsButtonPressed(event, root);});
        pollutionStatsButton.getStyleClass().add("nav-bar-button");

        topBar.getChildren().addAll(imageView, spacing, welcomeWindowButton, dataMapButton, pollutionStatsButton);

        return topBar;
    }

    private VBox createFiltersAndControlsBar() throws Exception {
        VBox filtersBar = new VBox(20);
        filtersBar.getStyleClass().add("filters");

        Label filtersAndControlsLabel = new Label ("Filters and Controls");
        Label selectYearLabel = new Label("Select Year");

        ComboBox<String> yearSelectorComboBox = new ComboBox<>();

        yearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023",
            "live"
        );

        yearSelectorComboBox.getSelectionModel().selectFirst();


        Label pollutantSelectionLabel = new Label("Select Pollutant");

        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );
        
        
        Label pollutionLevelSelectionLabel = new Label("Select Level");

        ComboBox<String>  pollutionLevelSelectorComboBox = new ComboBox<>();

        pollutionLevelSelectorComboBox.getItems().addAll(
            "All",
            "High",
            "Medium",
            "Low"
        );


        pollutantSelectorComboBox.getSelectionModel().selectFirst();
        pollutionLevelSelectorComboBox.getSelectionModel().selectFirst();

        Button interactiveSwap = new Button("Change Map");
        interactiveSwap.setOnAction(event -> swapMapButton(event,root));

        //Handles the change of the year and the pollutant
        //Loading the new dataset


        yearSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        pollutantSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        
        pollutionLevelSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        filtersBar.getChildren().addAll(filtersAndControlsLabel, selectYearLabel, yearSelectorComboBox, pollutantSelectionLabel, pollutantSelectorComboBox,pollutionLevelSelectionLabel,pollutionLevelSelectorComboBox,interactiveSwap);
        
        
        return filtersBar;
    }
    
    private void createCSVFiles() {
        String[] pollutants = new String[]{"NO2", "PM2.5", "PM10"};
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Your background logic here (runs off the UI thread)
                CSVfiller.fillAllCSV(pollutants);
                return null;
            }
        };

        // Update UI safely once done
        task.setOnSucceeded(e -> {
            // Safe to touch UI elements here
            System.out.println("Done!");
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true); // dies when app closes
        thread.start();
    }
    
    private void welcomeButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(welcome);
        root.setLeft(null);
    }
    
    private void dataMapButtonPressed(ActionEvent event, BorderPane root) throws Exception {
        root.setCenter(map);
        root.setLeft(createFiltersAndControlsBar());
        DataLoader myLoader = new DataLoader();
        DataSet no2Data = myLoader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");
        map.plotData(no2Data,"All");
    }
    
    private void swapMapButton(ActionEvent event, BorderPane root){
        if (staticMap){
            root.setCenter(liveMap);
        }
        else{
            root.setCenter(map);
        }
        staticMap = !(staticMap);
    }
    
    private void statsButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(stats);
        root.setLeft(null);
    }
    
    private void comboBoxSelection(ComboBox<String> yearSelectorComboBox, ComboBox<String> pollutantSelectorComboBox, ComboBox<String> pollutionLevelComboBox) throws Exception {
        String selectedPollutant = pollutantSelectorComboBox.getValue();
        String selectedYear = yearSelectorComboBox.getValue();
        String selectedPollutionLevel = pollutionLevelComboBox.getValue();
        map.updateMapData(selectedPollutant, selectedYear, selectedPollutionLevel);

    }
}