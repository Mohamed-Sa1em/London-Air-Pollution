import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * The WelcomeWindow class houses the code related to the welcome window screen
 *
 * @author Mohammed Aziz
 * @version 1.0
 */
public class WelcomeWindow extends ScrollPane
{
    /**
     * Constructor for WelcomeWindow
     */
    public WelcomeWindow()
    {
        //super(root,width,height);
        VBox content = new VBox();
        this.getStyleClass().add("welcome-contents");
        content.getStyleClass().add("welcome-contents");
        this.setFitToWidth(true);
        
        Label titleLabel = new Label("Welcome to the London Pollution Tracker");
        titleLabel.getStyleClass().add("welcome-title");
        
        Label instructionLabel = new Label(
        "This is a quick guide to show you how to use the app:");
        instructionLabel.getStyleClass().add("welcome-body");
        
        Image navBar = new Image("file:assets/navButtons.png");
        ImageView navBarView = new ImageView(navBar);
        navBarView.setPreserveRatio(true);
        
        Label navInfo = new Label(
        "In the top right right of the screen, you will see these buttons. They allow you to navigate to the different parts of this app.");
        navInfo.getStyleClass().add("welcome-body");
        
        Separator lineBreak = new Separator();
        
        Label dataMapTitle = new Label("Data Map");
        dataMapTitle.getStyleClass().add("welcome-subtitle");
        
        Label dataMapInfo = new Label("This app's map is broken down into 1km by 1km squares, colour coded by how polluted it is. Red for heavily polluted down to green for lightly polluted.");
        dataMapInfo.getStyleClass().add("welcome-body");
        
        Image dataMap = new Image("file:assets/dataMap.png");
        ImageView dataMapView = new ImageView(dataMap);
        dataMapView.setFitWidth(800);
        dataMapView.setPreserveRatio(true);
        
        Label mapDetailsInfo = new Label("When hovering over a grid square, a tooltip containing details about the pollution in that area will appear, alongside its unique area code.");
        mapDetailsInfo.getStyleClass().add("welcome-body");
        
        Image mapDetails = new Image("file:assets/hoverDetails.png");
        ImageView mapDetailsView = new ImageView(mapDetails);
        mapDetailsView.setFitWidth(200);
        mapDetailsView.setPreserveRatio(true);
        
        content.getChildren().addAll(titleLabel,instructionLabel,navBarView,navInfo,lineBreak,dataMapTitle,dataMapInfo,dataMapView,mapDetailsInfo,mapDetailsView);
        this.setContent(content); // sets the content pane as the content
    }
}