package controllers;

import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class ControllerUtils {
    
    public static void mostrarAlerta(Alert.AlertType tipoAlerta, String titulo, String headerText, String contentText) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public static <T> void actualizarTableView(TableView<T> tableView, List<T> lista) {
        tableView.getItems().setAll(lista);
    }
}
