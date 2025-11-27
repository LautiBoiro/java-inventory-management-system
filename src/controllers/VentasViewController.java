package controllers;

import exceptions.CantidadInvalidaException;
import exceptions.ProductoNoSeleccionadoException;
import exceptions.StockInsuficienteException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Producto;
import models.GestorArchivoProductos;
import models.ItemCarrito;

public class VentasViewController implements Initializable {

    private List<Producto> listaProductos = new ArrayList<>();
    private List<ItemCarrito> itemsCarrito = new ArrayList<>();
    private GestorArchivoProductos gestorProductos;
    
    @FXML
    private TableView<Producto> tbvProductos;
    
    @FXML
    private TableColumn<Producto, String> colNombre;
    
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    
    @FXML
    private TableColumn<Producto, Integer> colStock;
    
    @FXML
    private TextField txtCantidad;
    
    @FXML
    private ListView<ItemCarrito> lsvCarrito;
    
    @FXML
    private Button btnAgregar;
    
    @FXML
    private Button btnFinalizarCompra;
    
    @FXML
    private void agregar(ActionEvent e) {
        try {
            Producto productoSeleccionado = tbvProductos.getSelectionModel().getSelectedItem();
            
            if (productoSeleccionado == null) {
                throw new ProductoNoSeleccionadoException("Debe seleccionar un producto");
            }
            
            String cantidadTexto = txtCantidad.getText();
            if (cantidadTexto == null || cantidadTexto.trim().isEmpty()) {
                throw new CantidadInvalidaException("Debe ingresar una cantidad");
            }
            
            int cantidad = Integer.parseInt(cantidadTexto);
            
            if (cantidad <= 0) {
                throw new CantidadInvalidaException("La cantidad debe ser mayor a 0");
            }
            
            if (productoSeleccionado.getStock() < cantidad) {
                throw new StockInsuficienteException("Stock insuficiente. Stock disponible: " + productoSeleccionado.getStock());
            }
            
            boolean encontrado = false;
            for (ItemCarrito item : itemsCarrito) {
                if (item.getProducto().getNombre().equals(productoSeleccionado.getNombre())) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                ItemCarrito nuevoItem = new ItemCarrito(productoSeleccionado, cantidad);
                itemsCarrito.add(nuevoItem);
            }
            
            productoSeleccionado.setStock(productoSeleccionado.getStock() - cantidad);
            
            lsvCarrito.getItems().setAll(itemsCarrito);
            tbvProductos.refresh();
            txtCantidad.clear();
            
        } catch (ProductoNoSeleccionadoException ex) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Producto no seleccionado", ex.getMessage());
        } catch (CantidadInvalidaException ex) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Cantidad inválida", ex.getMessage());
        } catch (StockInsuficienteException ex) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Stock insuficiente", ex.getMessage());
        } catch (NumberFormatException ex) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Cantidad inválida", "Debe ingresar un número válido");
        }
    }
    
    @FXML
    private void finalizarCompra(ActionEvent e) {
        try {
            if (itemsCarrito.isEmpty()) {
                ControllerUtils.mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Carrito vacío", "Debe agregar productos al carrito");
                return;
            }
            
            abrirCarrito();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void cargarProductos() {
        gestorProductos = new GestorArchivoProductos("productos.dat");
        listaProductos = gestorProductos.deserializarProductos();
        
        if (listaProductos == null) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar productos", "No se pudo leer el archivo de productos");
            listaProductos = new ArrayList<>();
        } else {
            ControllerUtils.actualizarTableView(tbvProductos, listaProductos);
        }
    }
    
    private void abrirCarrito() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CarritoView.fxml"));
            Scene scene = new Scene(loader.load());
            
            CarritoViewController controller = loader.getController();
            controller.setCarritoData(itemsCarrito, listaProductos, gestorProductos);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Carrito de Compras");
            stage.showAndWait();
            
            itemsCarrito.clear();
            lsvCarrito.getItems().clear();
            cargarProductos();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        cargarProductos();
    }
}