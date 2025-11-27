package exceptions;

public class ProductoNoSeleccionadoException extends Exception {
    
    public ProductoNoSeleccionadoException(String mensaje) {
        super(mensaje);
    }
}