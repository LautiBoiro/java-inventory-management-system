package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Producto;
import models.GestorArchivoProductos;
import models.GestorArchivoTicket;
import models.ItemCarrito;

public class CarritoViewController implements Initializable {

    private List<ItemCarrito> carritoData;
    private List<Producto> listaProductos;
    private GestorArchivoProductos gestorProductos;
    private GestorArchivoTicket gestorTicket;
    
    @FXML
    private TableView<ItemCarrito> tbvCarrito;
    
    @FXML
    private TableColumn<ItemCarrito, String> colNombre;
    
    @FXML
    private TableColumn<ItemCarrito, Integer> colCantidad;
    
    @FXML
    private TableColumn<ItemCarrito, Double> colSubtotal;
    
    @FXML
    private Label lblTotal;
    
    @FXML
    private Button btnConfirmar;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private void confirmar(ActionEvent e) {
        if (carritoData == null || carritoData.isEmpty()) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Carrito vacío", "No hay productos para confirmar");
            return;
        }
        
        double total = calcularTotal();
        
        gestorTicket = new GestorArchivoTicket();
        gestorTicket.escribirTicket(carritoData, total);
        
        gestorProductos.serializarProductos(listaProductos);
        
        ControllerUtils.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Compra confirmada", "El ticket ha sido generado correctamente");
        
        Stage stage = (Stage) btnConfirmar.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void cancelar(ActionEvent e) {
        for (ItemCarrito item : carritoData) {
            item.getProducto().setStock(item.getProducto().getStock() + item.getCantidad());
        }
        
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
    public void setCarritoData(List<ItemCarrito> items, List<Producto> productos, GestorArchivoProductos gestor) {
        this.carritoData = items;
        this.listaProductos = productos;
        this.gestorProductos = gestor;
        tbvCarrito.getItems().setAll(carritoData);
        actualizarTotal();
    }
    
    private double calcularTotal() {
        double total = 0.0;
        for (ItemCarrito item : carritoData) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    private void actualizarTotal() {
        double total = calcularTotal();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }
}