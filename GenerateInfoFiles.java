import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * 
 * @author Andres Julian Martin 
 */
public class GenerateInfoFiles {

    // Listas de nombres y apellidos para crear vendedores aleatorios dados
    static String[] nombres = { "Andres", "Camila", "Julian", "Juli", "Luis",
                                "Jineth", "Jose", "Andrea", "Camila", "Maria" };
    static String[] apellidos = { "Gomez", "Rodriguez", "Martinez", "Lopez",
                                  "Garcia", "Vargas", "Sanchez", "Ramirez" };

    // Lista de productos disponibles
    static String[] productos = { "Laptop", "Mouse", "Teclado", "Monitor",
                                  "Impresora", "Tablet", "Celular", "Audifonos",
                                  "Camara", "Parlante" };

    // Tipos de documento
    static String[] tiposDoc = { "CC", "CE", "TI", "PP" };

    // generar números aleatorios dados
    static Random random = new Random();

    public static void main(String[] args) {
        try {
            System.out.println("Generando archivos txt...");

            createProductsFile(10);
            System.out.println("productos.txt creado");

            createSalesManInfoFile(5);
            System.out.println("vendedores.txt creado");

            for (int i = 1; i <= 5; i++) {
                createSalesMenFile(15, "vendedor" + i, 1000000000L + i);
                System.out.println("ventas_vendedor" + i + " creado");
            }

            System.out.println("TODO LISTO");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Crea el archivo productos.txt con productos de prueba disponibles.
     */
    public static void createProductsFile(int cantidad) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter("productos.txt"));

        for (int i = 1; i <= cantidad; i++) {
            String id = "" + i;
            String nombre = productos[random.nextInt(productos.length)];
            int precio = 10000 + random.nextInt(500000);

            writer.println(id + ";" + nombre + ";" + precio);
        }

        writer.close();
    }

    /**
     * Crea el archivo vendedores.txt con vendedores de prueba aleatorios.
     */
    public static void createSalesManInfoFile(int cantidad) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter("vendedores.txt"));

        for (int i = 0; i < cantidad; i++) {
            String tipo = tiposDoc[random.nextInt(tiposDoc.length)];
            long numero = 1000000000L + random.nextInt(1000000);
            String nombre = nombres[random.nextInt(nombres.length)];
            String apellido = apellidos[random.nextInt(apellidos.length)];

            writer.println(tipo + ";" + numero + ";" + nombre + ";" + apellido);
        }

        writer.close();
    }

    /**
     * Crea un archivo de ventas para un vendedor aleatorio.
     */
    public static void createSalesMenFile(int cantidad, String nombre, long id) throws Exception {
        String archivo = "ventas_" + nombre + "_" + id + ".txt";
        PrintWriter writer = new PrintWriter(new FileWriter(archivo));

        // Primera línea: tipo y número de documento
        writer.println("CC;" + id);

        // Líneas de productos vendidos
        for (int i = 0; i < cantidad; i++) {
            int idProducto = 1 + random.nextInt(10);
            int cantidadVendida = 1 + random.nextInt(20);

            writer.println(idProducto + ";" + cantidadVendida + ";");
        }

        writer.close();
    }
}