package com.uninabiogardenoo65.controller; // 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/uninabiogarden";
    private static final String USER = "salvo"; 
    private static final String PASSWORD = "123";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    
     
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("DEBUG: Connessione a PostgreSQL riuscita!");
            }
        } catch (SQLException e) {
            System.err.println("ERRORE: Impossibile connettersi al database. Controlla che PostgreSQL sia in esecuzione e che le credenziali siano corrette.");
            e.printStackTrace();
        }
    }
}