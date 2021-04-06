package ca.cmpt213.asn4.tictactoe.ui;

import ca.cmpt213.asn4.tictactoe.game.Board;
import ca.cmpt213.asn4.tictactoe.game.Board.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * TicTacToe
 *
 * represents the game system
 * handles input and show output to user
 *
 * has a gameBoard
 */
public class TicTacToe extends Application {

    // Constants
    private final int BOARD_SIZE = 4;
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 600;

    // GUI elements
    final private BorderPane borderPane = new BorderPane();
    final private GridPane gridPane = new GridPane();
    final private VBox userPanel = new VBox();

    // Variables
    private Board gameBoard;
    private Player currentPlayer;
    private boolean gameRunning = true;
    final private HashMap<String,ImageView> imageViewsMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage){
        gameBoard = new Board(BOARD_SIZE);
        currentPlayer = Player.ONE;

        setUpGrid();
        setUpUserPanel();

        // setup
        borderPane.setCenter(gridPane);
        borderPane.setBottom(userPanel);

        primaryStage.setScene(new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setTitle("TicTacToe");
        primaryStage.show();
    }

    // set up functions
    private void setUpGrid(){
        final int BORDER_SIZE = 20;
        final int GAP_SIZE = 8;

        Image blank = new Image("file:img/blank.png");
        int scaledWidth = (WINDOW_WIDTH - BORDER_SIZE * 2 - GAP_SIZE * (BOARD_SIZE - 1))/BOARD_SIZE;

        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                ImageView imageView = new ImageView(blank);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(scaledWidth);

                int finalRow = row;
                int finalCol = col;
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> handleClick(finalRow,finalCol));

                imageViewsMap.put(row+","+col,imageView);
                gridPane.add(imageView,col,row);
            }
        }
        
        // set styles for gridPane
        gridPane.setPadding(new Insets(BORDER_SIZE));
        gridPane.setVgap(GAP_SIZE);
        gridPane.setHgap(GAP_SIZE);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxWidth(WINDOW_WIDTH);
        gridPane.setMaxHeight(WINDOW_WIDTH);
        gridPane.setStyle("-fx-border-color: black;-fx-background-color: #eaeaea");

    }
    private void setUpUserPanel() {
        Text gameState = new Text("Game running");
        gameState.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        userPanel.getChildren().add(gameState);

        Button resetButton = new Button("Reset Game");
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> reset());
        resetButton.setPrefSize(100,20);
        userPanel.getChildren().add(resetButton);

        Text gameInfo = new Text("player 1 -> X\nplayer 2 -> O");
        userPanel.getChildren().add(gameInfo);

        // styles for vbox
        userPanel.setSpacing(30);
        userPanel.setAlignment(Pos.CENTER);
        userPanel.setPrefWidth(WINDOW_WIDTH);
        userPanel.setPrefHeight(WINDOW_HEIGHT - WINDOW_WIDTH);
        userPanel.setStyle("-fx-border-color: black;-fx-background-color: #828282");
    }

    // other functions
    private void handleClick(int row,int col){
        if(!gameRunning){
            System.out.println("ignoring click, game ended");
            return;
        }
        boolean success = gameBoard.makeAMove(row, col, currentPlayer);
        if(success){
            updateGrid(row,col);
            gameRunning = false;
            switch (gameBoard.checkCondition()){
                case WIN_P1 -> updatePanelText("Player 1 wins");
                case WIN_P2 -> updatePanelText("Player 2 wins");
                case DRAW -> updatePanelText("Game draw");
                case PLAYING -> gameRunning = true;
                default -> throw new IllegalArgumentException("Unexpected game state");
            }
            nextPlayer();
        } else {
            System.out.println("Move failed");
        }
    }
    private void nextPlayer(){
        switch (currentPlayer){
            case ONE -> currentPlayer = Player.TWO;
            case TWO -> currentPlayer = Player.ONE;
            default -> throw new IllegalArgumentException("Unexpected player: " + currentPlayer);
        }
    }
    private void reset(){
        gameBoard.reset();
        gameRunning = true;
        currentPlayer = Player.ONE;
        Image blank = new Image("file:img/blank.png");
        for(Node node:gridPane.getChildren()){
            ((ImageView) node).setImage(blank);
        }
        updatePanelText("Game running");
    }

    // update ui
    private void updateGrid(int row, int col) {
        Image image;
        switch (currentPlayer){
            case ONE -> image = new Image("file:img/cross.png");
            case TWO -> image = new Image("file:img/circle.png");
            default -> throw new IllegalStateException("Unexpected player: " + currentPlayer);
        }
        imageViewsMap.get(row+","+col).setImage(image);
    }
    private void updatePanelText(String str){
        Text gameState = (Text) userPanel.getChildren().get(0);
        gameState.setText(str);
    }

    // main
    public static void main(String[] args) {
        launch(args);
    }
}
