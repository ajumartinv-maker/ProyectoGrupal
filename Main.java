import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Clase principal que lee los archivos generados por GenerateInfoFiles
 * y crea los reportes de vendedores y productos.
 * 
 * @author Andres Julian Martin Vargas
  */
public class Main {
	/**
	 * Metodo principal. Lee los archivos de productos, vendedores y ventas,
	 * calcula los totales y genera los reportes ordenados.
	 * 
	 */

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando reportes...");

            // Arreglos para guardar los datos
            int[] idProducto = new int[100];
            String[] nombreProducto = new String[100];
            int[] precioProducto = new int[100];
            int[] cantidadVendida = new int[100];
            int totalProductos = 0;

            String[] nombreVendedor = new String[100];
            long[] documentoVendedor = new long[100];
            long[] dineroVendedor = new long[100];
            int totalVendedores = 0;

            // 1. Leer productos
            Scanner lector = new Scanner(new File("productos.txt"));
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String[] partes = linea.split(";");

                // Validar que la linea tenga 3 partes
                if (partes.length != 3) {
                    System.out.println("ADVERTENCIA: Linea mal formada en productos.txt: " + linea);
                    continue;
                }

                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                int precio = Integer.parseInt(partes[2]);

                // Validar que el precio sea positivo
                if (precio < 0) {
                    System.out.println("ADVERTENCIA: Precio negativo en producto " + nombre);
                    continue;
                }

                idProducto[totalProductos] = id;
                nombreProducto[totalProductos] = nombre;
                precioProducto[totalProductos] = precio;
                cantidadVendida[totalProductos] = 0;

                totalProductos++;
            }
            lector.close();
            System.out.println("Productos leidos: " + totalProductos);

            // 2. Leer vendedores
            lector = new Scanner(new File("vendedores.txt"));
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String[] partes = linea.split(";");

                documentoVendedor[totalVendedores] = Long.parseLong(partes[1]);
                nombreVendedor[totalVendedores] = partes[2] + " " + partes[3];
                dineroVendedor[totalVendedores] = 0;

                totalVendedores++;
            }
            lector.close();
            System.out.println("Vendedores leidos: " + totalVendedores);

            // 3. Leer archivos de ventas
            File carpeta = new File(".");
            File[] archivos = carpeta.listFiles();

            for (int a = 0; a < archivos.length; a++) {
                String nombre = archivos[a].getName();

                if (nombre.startsWith("ventas_") && nombre.endsWith(".txt")) {

                    lector = new Scanner(archivos[a]);

                    // Primera linea: tipo;documento
                    long documento = -1;
                    if (lector.hasNextLine()) {
                        String primera = lector.nextLine();
                        String[] partes = primera.split(";");
                        documento = Long.parseLong(partes[1]);
                    }

                    // Buscar el vendedor
                    int posVendedor = -1;
                    for (int v = 0; v < totalVendedores; v++) {
                        if (documentoVendedor[v] == documento) {
                            posVendedor = v;
                            break;
                        }
                    }

                    // Leer las ventas
                    while (lector.hasNextLine()) {
                        String linea = lector.nextLine();
                        if (linea.equals("")) continue;

                        String[] partes = linea.split(";");

                        // Validar que la linea tenga al menos 2 partes
                        if (partes.length < 2) {
                            System.out.println("ADVERTENCIA: Linea mal formada en " + nombre);
                            continue;
                        }

                        int id = Integer.parseInt(partes[0]);
                        int cant = Integer.parseInt(partes[1]);

                        // Validar que la cantidad sea positiva
                        if (cant < 0) {
                            System.out.println("ADVERTENCIA: Cantidad negativa en " + nombre);
                            continue;
                        }

                        // Buscar el producto
                        boolean encontrado = false;
                        for (int p = 0; p < totalProductos; p++) {
                            if (idProducto[p] == id) {
                                cantidadVendida[p] = cantidadVendida[p] + cant;
                                if (posVendedor != -1) {
                                    dineroVendedor[posVendedor] =
                                        dineroVendedor[posVendedor] + (precioProducto[p] * cant);
                                }
                                encontrado = true;
                                break;
                            }
                        }

                        // Validar que el producto exista
                        if (!encontrado) {
                            System.out.println("ADVERTENCIA: Producto con ID " + id + " no existe.");
                        }
                    }
                    lector.close();
                }
            }
            System.out.println("Ventas procesadas.");

            // 4. Ordenar vendedores de mayor a menor dinero (burbuja)
            for (int i = 0; i < totalVendedores - 1; i++) {
                for (int j = 0; j < totalVendedores - 1 - i; j++) {
                    if (dineroVendedor[j] < dineroVendedor[j + 1]) {
                        // Intercambiar dinero
                        long auxDinero = dineroVendedor[j];
                        dineroVendedor[j] = dineroVendedor[j + 1];
                        dineroVendedor[j + 1] = auxDinero;

                        // Intercambiar nombre
                        String auxNombre = nombreVendedor[j];
                        nombreVendedor[j] = nombreVendedor[j + 1];
                        nombreVendedor[j + 1] = auxNombre;
                    }
                }
            }

            // 5. Crear reporte de vendedores
            PrintWriter escritor = new PrintWriter(new FileWriter("reporte_vendedores.txt"));
            escritor.println("Nombre;Recaudado");
            for (int i = 0; i < totalVendedores; i++) {
                escritor.println(nombreVendedor[i] + ";" + dineroVendedor[i]);
            }
            escritor.close();
            System.out.println("reporte_vendedores.txt creado.");

            // 6. Ordenar productos de mayor a menor cantidad (burbuja)
            for (int i = 0; i < totalProductos - 1; i++) {
                for (int j = 0; j < totalProductos - 1 - i; j++) {
                    if (cantidadVendida[j] < cantidadVendida[j + 1]) {
                        // Intercambiar cantidad
                        int auxCant = cantidadVendida[j];
                        cantidadVendida[j] = cantidadVendida[j + 1];
                        cantidadVendida[j + 1] = auxCant;

                        // Intercambiar nombre
                        String auxNombre = nombreProducto[j];
                        nombreProducto[j] = nombreProducto[j + 1];
                        nombreProducto[j + 1] = auxNombre;

                        // Intercambiar precio
                        int auxPrecio = precioProducto[j];
                        precioProducto[j] = precioProducto[j + 1];
                        precioProducto[j + 1] = auxPrecio;
                    }
                }
            }

            // 7. Crear reporte de productos
            escritor = new PrintWriter(new FileWriter("reporte_productos.txt"));
            escritor.println("Nombre;Precio;Cantidad");
            for (int i = 0; i < totalProductos; i++) {
                escritor.println(nombreProducto[i] + ";" + precioProducto[i] + ";" + cantidadVendida[i]);
            }
            escritor.close();
            System.out.println("reporte_productos.txt creado.");

            System.out.println("TODO LISTO");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}