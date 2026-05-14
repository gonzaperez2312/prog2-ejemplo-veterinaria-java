package org.example;

import org.example.model.Mascota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Datos de conexión a la base de datos
    // Asegurarse de que el schema "veterinaria" exista antes de ejecutar (ver schema.sql)
    private static final String URL  = "jdbc:mysql://localhost:3306/veterinaria?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static void main(String[] args) {

        // DriverManager.getConnection() establece la conexión con la BD.
        // El bloque try-with-resources cierra la conexión automáticamente al terminar.
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            // INSERT
            System.out.println("=== INSERT ===");
            insertar(conn, "Firulais", "Perro",  "Juan Pérez",    3);
            insertar(conn, "Michi",    "Gato",   "Ana García",    5);
            insertar(conn, "Piolín",   "Pájaro", "Carlos López",  1);

            // SELECT
            System.out.println("\n=== SELECT todas las mascotas ===");
            listar(conn).forEach(System.out::println);

            // UPDATE
            System.out.println("\n=== UPDATE (actualizar edad de id=1 a 4) ===");
            actualizarEdad(conn, 1, 4);
            listar(conn).forEach(System.out::println);

            // SELECT con filtro por especie
            System.out.println("\n=== SELECT por especie (Perro) ===");
            buscarPorEspecie(conn, "Perro").forEach(System.out::println);

            // DELETE
            System.out.println("\n=== DELETE (eliminar mascota con id=2) ===");
            eliminar(conn, 2);
            listar(conn).forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT: se usa PreparedStatement para parametrizar la consulta.
    // Los "?" son placeholders que se reemplazan con setString/setInt.
    // Esto previene ataques de SQL Injection.
    static void insertar(Connection conn, String nombre, String especie, String propietario, int edad) throws SQLException {
        String sql = "INSERT INTO mascotas (nombre, especie, propietario, edad) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, especie);
        stmt.setString(3, propietario);
        stmt.setInt(4, edad);
        int filas = stmt.executeUpdate(); // executeUpdate() se usa para INSERT, UPDATE y DELETE
        System.out.println(filas + " fila insertada: " + nombre);
    }

    // UPDATE: mismo mecanismo que INSERT.
    // executeUpdate() devuelve la cantidad de filas afectadas.
    static void actualizarEdad(Connection conn, int id, int nuevaEdad) throws SQLException {
        String sql = "UPDATE mascotas SET edad = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, nuevaEdad);
        stmt.setInt(2, id);
        int filas = stmt.executeUpdate();
        System.out.println(filas + " fila actualizada");
    }

    // DELETE: igual que UPDATE, se filtra por id con un placeholder.
    static void eliminar(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM mascotas WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int filas = stmt.executeUpdate();
        System.out.println(filas + " fila eliminada");
    }

    // SELECT con filtro: igual que listar() pero agrega un WHERE con PreparedStatement.
    // Demuestra cómo parametrizar un SELECT para evitar SQL Injection en el filtro.
    static List<Mascota> buscarPorEspecie(Connection conn, String especie) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE especie = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, especie);
        ResultSet rs = stmt.executeQuery(); // executeQuery() también aplica en PreparedStatement
        while (rs.next()) {
            mascotas.add(new Mascota(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getString("propietario"),
                rs.getInt("edad")
            ));
        }
        return mascotas;
    }

    // SELECT: se usa executeQuery() (en lugar de executeUpdate()) porque devuelve resultados.
    // ResultSet es un cursor que recorre las filas devueltas una por una con rs.next().
    static List<Mascota> listar(Connection conn) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mascotas");
        while (rs.next()) {
            // rs.getString(), rs.getInt() leen el valor de cada columna por nombre
            mascotas.add(new Mascota(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getString("propietario"),
                rs.getInt("edad")
            ));
        }
        return mascotas;
    }
}
