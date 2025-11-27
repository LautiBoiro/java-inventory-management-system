package models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

public class GestorArchivoProductos {
    
    private String nombreArchivo;
    
    public GestorArchivoProductos(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public List<Producto> deserializarProductos() {
        List<Producto> productos = new ArrayList<>();
        ObjectInputStream ois = null;
        
        try {
            FileInputStream fis = new FileInputStream(nombreArchivo);
            ois = new ObjectInputStream(fis);
            productos = (List<Producto>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        return productos;
    }
    
    public void serializarProductos(List<Producto> productos) {
        ObjectOutputStream oos = null;
        
        try {
            FileOutputStream fos = new FileOutputStream(nombreArchivo);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(productos);
        } catch (Exception e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
