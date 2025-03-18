module justinrojas.battleshipgame {
    requires javafx.controls;
    requires javafx.fxml;

    opens justinrojas.battleshipgame to javafx.fxml;
    exports justinrojas.battleshipgame;
}
