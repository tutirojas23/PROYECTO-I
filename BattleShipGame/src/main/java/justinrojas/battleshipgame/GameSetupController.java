package justinrojas.battleshipgame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;
import java.util.List;

public class GameSetupController {

    @FXML
    private GridPane gameGrid;

    @FXML
    private VBox shipContainer;

    @FXML
    private Button readyButton;

    @FXML
    private Label playerName;

    private String player1Name;
    private String player2Name;

    private Ship selectedShip = null;
    private List<Ship> ships = new ArrayList<>();
    private List<Button> placedShips = new ArrayList<>();
    private boolean isHorizontal = true;

    private boolean isPlayer1Turn = true;
     @FXML
    private void toggleShipDirection() {
        isHorizontal = !isHorizontal;
    }

   
    public void setPlayerNames(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;

        
        playerName.setText("Tablero de " + player1Name);
    }

    @FXML
    public void initialize() {
        createGrid();
    }

    private void createGrid() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Button cell = new Button();
                cell.setMinSize(30, 30);
                cell.setOnDragOver(this::allowDrop);
                cell.setOnDragDropped(this::placeShip);
                gameGrid.add(cell, col, row);
            }
        }
    }

    private void createShips() {
        shipContainer.getChildren().clear();
        ships.clear();

        ships.add(new Acorazado());
        ships.add(new Crucero());
        ships.add(new Destroyer());
        ships.add(new Submarino());

        for (Ship ship : ships) {
            Button shipButton = new Button(ship.getName());
            shipButton.setUserData(ship);
            shipButton.setOnDragDetected(this::startDrag);
            shipContainer.getChildren().add(shipButton);
        }
    }

   @FXML
    private void startDrag(MouseEvent event) {
        Button shipButton = (Button) event.getSource();
        Dragboard db = shipButton.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(shipButton.getText());
        db.setContent(content);
        selectedShip = (Ship) shipButton.getUserData();
        selectedShip.setPlaced(false);  
        event.consume();
    }

    @FXML
    private void allowDrop(DragEvent event) {
        if (event.getGestureSource() != event.getSource()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }
    private boolean canPlaceShip(int row, int col, Ship ship) {
        
        if (isHorizontal) {
            
            if (col + ship.getSize() > 10) {
                return false; 
            }
            
            for (int i = 0; i < ship.getSize(); i++) {
                Button cell = (Button) gameGrid.getChildren().get(row * 10 + col + i);
                if (cell.getStyle().equals("-fx-background-color: gray;")) {
                    return false; 
                }
            }
        } else {
            
            if (row + ship.getSize() > 10) {
                return false; 
            }
            
            for (int i = 0; i < ship.getSize(); i++) {
                Button cell = (Button) gameGrid.getChildren().get((row + i) * 10 + col);
                if (cell.getStyle().equals("-fx-background-color: gray;")) {
                    return false; 
                }
            }
        }
        return true;
    }
     private void placeShipOnBoard(int row, int col, Ship ship) {
      
        for (int i = 0; i < ship.getSize(); i++) {
            Button cell;
            if (isHorizontal) {
                cell = (Button) gameGrid.getChildren().get(row * 10 + col + i);
            } else {
                cell = (Button) gameGrid.getChildren().get((row + i) * 10 + col);
            }
            cell.setStyle("-fx-background-color: gray;");
        }
        ship.placeShip(); 
    }

    @FXML
    private void placeShip(DragEvent event) {
        if (selectedShip != null) {
            Button cell = (Button) event.getSource();
            int row = GridPane.getRowIndex(cell);
            int col = GridPane.getColumnIndex(cell);

            if (canPlaceShip(row, col, selectedShip)) {
                placeShipOnBoard(row, col, selectedShip);  
                placedShips.add(cell); 
                selectedShip.placeShip(true);
                selectedShip = null;
            } else {
                showAlert("Error", "No puedes colocar el barco aquí.");
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    private void confirmPlacement() {
        if (placedShips.size() < ships.size()) {
            showAlert("Error", "Debes colocar todos los barcos antes de continuar.");
            return;
        }

        if (isPlayer1Turn) {
            isPlayer1Turn = false;
            placedShips.clear();
            gameGrid.getChildren().forEach(node -> node.setStyle(""));
            setPlayerNames(player1Name,player2Name);
            createShips();
        } else {
            startGame();
        }
    }

    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game_setup.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) readyButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Juego en progreso");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}
