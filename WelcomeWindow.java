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


/**
 * Write a description of JavaFX class WelcomeWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class WelcomeWindow extends VBox
{
    public WelcomeWindow()
    {
        //super(root,width,height);
        this.getStyleClass().add("welcome-contents");
        ScrollPane welcomeScroll = new ScrollPane(this);
        welcomeScroll.setFitToWidth(true);
        
        Label titleLabel = new Label("Welcome to the London Pollution Tracker");
        titleLabel.getStyleClass().add("welcome-title");
        
        Label instructionLabel = new Label(
        "This is a quick guide to show you how to use the app");
        
        instructionLabel.setFont(Font.font("Roboto Medium",16));
        

        this.getChildren().addAll(titleLabel,instructionLabel);
    }
}