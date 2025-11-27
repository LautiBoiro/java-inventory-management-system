package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GestorArchivoTicket {
    
    public void escribirTicket(List<ItemCarrito> itemsCarrito, double totalFinal) {
        File archivoTicket = new File("ticket.txt");
        BufferedWriter bw = null;
        
        try {
            FileWriter fw = new FileWriter(archivoTicket);
            bw = new BufferedWriter(fw);
            
            if (itemsCarrito == null || itemsCarrito.isEmpty()) {
                return;
            }
            
            bw.write("=======================================");
            bw.newLine();
            bw.write("          TICKET DE COMPRA");
            bw.newLine();
            bw.write("=======================================");
            bw.newLine();
            
            bw.write("---------------------------------------");
            bw.newLine();
            
            for (ItemCarrito item : itemsCarrito) {
                bw.write("Producto: " + item.getNombreProducto());
                bw.newLine();
                bw.write("Cantidad: " + item.getCantidad());
                bw.newLine();
                bw.write("Precio unitario: $" + item.getProducto().getPrecio());
                bw.newLine();
                bw.write("Subtotal: $" + item.getSubtotal());
                bw.newLine();
                bw.write("---------------------------------------");
                bw.newLine();
            }
            
            bw.write("TOTAL: $" + totalFinal);
            bw.newLine();
            bw.write("=======================================");
            
        } catch (IOException e) {
            System.out.println("Error al escribir el ticket: " + e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}