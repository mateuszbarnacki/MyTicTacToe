package TicTacToe;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class GameWindow {
    @FXML
    private GridPane gridPane;
    private TicTacToeBoard ticTacToeBoard;
    private boolean isCircle = true;
    private boolean isSingleMode;

    // This method draw the board in a game window. It uses the information about
    // user preferences.
    public void drawBoard(boolean isSingleMode, boolean startWithX){
        this.isSingleMode = isSingleMode;
        // In multi-player mode we can start using x sign.
        if(!isSingleMode && startWithX){
            isCircle = false;
        }
        // Create a blank game board
        ticTacToeBoard = new TicTacToeBoard();
        // For every field we create a stack pane to which we can add rectangles/labels.
        for(int y = 0; y < 3; ++y){
            for(int x = 0; x < 3; ++x){
                StackPane stackPane = new StackPane();
                // Create a specific rectangle
                Rectangle rectangle = new Rectangle(100, 100);
                rectangle.setFill(Color.WHITESMOKE);
                // Add event handler for every rectangle
                rectangle.setOnMouseClicked(e -> processMouseClick(e, stackPane));
                stackPane.getChildren().add(rectangle);
                // Add every stack pane to the specific location on grid pane.
                gridPane.add(stackPane, x, y);
            }
        }
        // In single mode when we play as the X computer has a first turn.
        if(isSingleMode && startWithX){
            ObservableList<Node> children = gridPane.getChildren();
            // Lock all the fields during the animation
            for(Node node : children){
                node.setDisable(true);
            }
            // Create an animation using a timeline
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
                Random random = new Random();
                int randomField, newX, newY;
                int temp = ticTacToeBoard.getCounter();
                // In this loop we look for a field which we can fill in
                while (ticTacToeBoard.getCounter() != (temp + 1)) {
                    randomField = random.nextInt(9);
                    newX = randomField % 3;
                    newY = randomField / 3;
                    // Only draw the sign, we are sure that is not the end of the game
                    drawSignsOnGameBoard(newX, newY);
                }
                // Unlock all the fields
                for(Node node : children){
                    node.setDisable(false);
                }
            }));
            timeline.play();
        }
    }

    // This method performs the actions after click on the field.
    private void processMouseClick(MouseEvent e, StackPane stackPane){
        ObservableList<Node> children = gridPane.getChildren();
        // This loop looks for the coordinates of the clicked field
        for(int y = 0; y < 3; ++y){
            for(int x = 0; x < 3; ++x){
                if(children.get(y*3+x) == stackPane){
                    refresh(e, x, y);
                    // In single-mode we enable computer to take the next turn
                    if(isSingleMode) {
                        singleMode(e);
                    }
                }
            }
        }
    }

    // This method makes the next move on the board
    private void singleMode(MouseEvent e){
        ObservableList<Node> children = gridPane.getChildren();
        // The move is creating if it's not the end of the game
        if (!ticTacToeBoard.isEndOfTheGame()) {
            if(ticTacToeBoard.getCounter() != 9) {
                // Lock all the fields during the animation
                for(Node node : children){
                    node.setDisable(true);
                }
                // Prepare the animation, almost the same as in draw board method
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
                    Random random = new Random();
                    int randomField, newX, newY;
                    int temp = ticTacToeBoard.getCounter();
                    while (ticTacToeBoard.getCounter() != (temp + 1)) {
                        randomField = random.nextInt(9);
                        newX = randomField % 3;
                        newY = randomField / 3;
                        // The only change, every move could be the end of the game
                        refresh(e, newX, newY);
                    }
                    // Unlock the fields
                    for(Node node : children){
                        node.setDisable(false);
                    }
                }));
                timeline.play();
            }
        }
    }

    // This method refresh the game board and check end of the game conditions
    private void refresh(MouseEvent e, int firstCoordinate, int secondCoordinate){
        drawSignsOnGameBoard(firstCoordinate, secondCoordinate);
        if(ticTacToeBoard.isEndOfTheGame() || (ticTacToeBoard.getCounter() == 9)){
            endOfTheGame(e);
        }
    }

    // This method draw the appropriate sign on the board
    private void drawSignsOnGameBoard(int firstCoordinate, int secondCoordinate){
        // We check the current sign using a flag "isCircle"
        if(isCircle){
            // Try to change field value
            if(ticTacToeBoard.changeFieldValue(firstCoordinate, secondCoordinate, (short)1)) {
                // Draw appropriate sign on the board
                drawCircle(firstCoordinate, secondCoordinate);
                isCircle = false;
            }
        } else {
            // Try to change field value
            if(ticTacToeBoard.changeFieldValue(firstCoordinate, secondCoordinate, (short)2)) {
                // Draw appropriate sign on the board
                drawCross(firstCoordinate, secondCoordinate);
                isCircle = true;
            }
        }
    }

    // This method draws the circle at appropriate field
    private void drawCircle(int firstCoordinate, int secondCoordinate){
        StackPane pane = getRectangle(firstCoordinate, secondCoordinate);
        pane.getChildren().add(prepareLabel("O"));
    }

    // This method draws the cross at appropriate field
    private void drawCross(int firstCoordinate, int secondCoordinate){
        StackPane pane = getRectangle(firstCoordinate, secondCoordinate);
        pane.getChildren().add(prepareLabel("X"));
    }

    // This method returns the stack pane using passed coordinates
    private StackPane getRectangle(int firstCoordinate, int secondCoordinate){
        ObservableList<Node> children = gridPane.getChildren();
        return  (StackPane) children.get(secondCoordinate*3+firstCoordinate);
    }

    // This method prepares X and O labels
    private Label prepareLabel(String text){
        Label label = new Label();
        Font font = new Font("Georgia", 50);
        label.setFont(font);
        label.setText(text);
        return label;
    }

    // This method show the dialog box with information about the result
    // of the game. Close of this box leads to the load of the menu window.
    private void endOfTheGame(MouseEvent e){
        // Lock the current window.
        gridPane.setDisable(true);
        // Prepare the dialog window
        Dialog<ButtonType> dialog = new Dialog<>();
        DialogPane dialogPane = new DialogPane();
        VBox vBox = prepareEndOfTheGameContent();
        dialogPane.setContent(vBox);
        dialog.setDialogPane(dialogPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        // Add event handler which load menu window after closing dialog box.
        dialog.setOnHidden(event -> loadMenuWindow(e));
        // Show the dialog
        dialog.show();
    }

    // This method prepare the content of the dialog box.
    private VBox prepareEndOfTheGameContent(){
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Label signLabel, textLabel;
        // Check the draw conditions
        if((ticTacToeBoard.getCounter() == 9) && (!ticTacToeBoard.isEndOfTheGame())){
            signLabel = prepareLabel("O/X");
            textLabel = new Label("Draw!");
        }else {
            // Decide who win
            if (!isCircle) {
                signLabel = prepareLabel("O");
            } else {
                signLabel = prepareLabel("X");
            }
            textLabel = new Label("Wins!");
        }
        // Add appropriate labels
        vBox.getChildren().add(signLabel);
        vBox.getChildren().add(textLabel);
        return vBox;
    }

    // This method loads the menu window
    private void loadMenuWindow(MouseEvent e){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("menuWindow.fxml"));
        try {
            // Load the scene from menuWindow.fxml
            Parent scene = fxmlLoader.load();
            // Prepare the scene based on the loaded one
            Scene newScene = new Scene(scene);
            // Get the stage of current window thanks to the last mouse click
            Stage mainWindow = (Stage) ((Node)e.getSource()).getScene().getWindow();
            // Display new scene
            mainWindow.setScene(newScene);
        }catch (IOException ioe){
            // Reaction to the problems with loading a menu window
            System.out.println("Can't load the main window");
            ioe.printStackTrace();
        }
    }
}