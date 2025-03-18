package justinrojas.battleshipgame;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class StartScreenController {

    @FXML
    private TextField player1Name;

    @FXML
    private TextField player2Name;

    @FXML
    private Button startButton;

    @FXML
    private void startGame() {
        
        String player1 = player1Name.getText().trim();
        String player2 = player2Name.getText().trim();

        
        if (player1.isEmpty() || player2.isEmpty()) {
            showAlert("Error", "Ambos jugadores deben ingresar sus nombres.");
            return;
        }

        
        if (player1.equalsIgnoreCase(player2)) {
            showAlert("Error", "Los nombres no pueden ser iguales.");
            return;
        }

        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game_setup.fxml"));
            Scene scene = new Scene(loader.load());

         
            GameSetupController controller = loader.getController();
            controller.setPlayerNames(player1, player2);

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Coloca tus barcos");
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
