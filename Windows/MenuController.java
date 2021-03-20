package TicTacToe.Windows;

import TicTacToe.Windows.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private CheckBox checkSignBox;

    // After click the single-player button, this method pass the true to the loadGameWindow method.
    public void singlePlayerClicked(ActionEvent actionEvent){
        loadGameWindow(actionEvent, true);
    }

    // After click the multi-player button, this method pass the false to the loadGameWindow method.
    public void multiPlayerClicked(ActionEvent actionEvent){
        loadGameWindow(actionEvent, false);
    }

    // This method loads a game board to the current window. It gets the ActionEvent (in this case mouse click)
    // and information about the mode of the game board (boolean parameter).
    private void loadGameWindow(ActionEvent actionEvent, boolean isSingleMode){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("gameWindow.fxml"));
        try {
            // Load the scene from gameWindow.fxml
            Parent scene = fxmlLoader.load();
            // Create the scene based on the gameWindow.fxml
            Scene newScene = new Scene(scene);
            // Get the current window thanks to the mouse click
            Stage mainWindow = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            // Get the controller and use drawBoard method, because we have to pass information
            // about preferences to the gameWindow controller.
            GameController gameWindow = fxmlLoader.getController();
            gameWindow.drawBoard(isSingleMode, checkSignBox.isSelected());

            // Set the new scene in the current window
            mainWindow.setScene(newScene);
        }catch (IOException e){
            // Printing the message and stack trace to the console
            System.out.println("Can't load the game window.");
            e.printStackTrace();
        }
    }
}
