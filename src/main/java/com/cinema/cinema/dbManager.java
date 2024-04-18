package com.cinema.cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

// Design pattern: Singleton (una volta aperta la connessione, l'oggetto viene riciclato per ogni richiesta successiva)
public class dbManager {
    private static dbManager instance;
    private Connection conn;

    public static dbManager getInstance(String dbName, String serverName, String username, String password){
        if (instance == null) {
            instance = new dbManager(dbName, serverName, username, password);
        }
        return instance;
    }

    private dbManager(String dbName, String serverName, String username, String password)
    {
        try {
            conn = DriverManager.getConnection(serverName + dbName, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username)
    {
        try {
            PreparedStatement stat = bindParams(Settings.searchUtente, List.of(username));

            // if it returns more than 0 rows, the user exists
            return stat.executeQuery().next(); // returns true if there is a next row, otherwise false

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // For credentials check
    public User searchForUser(String username, String password)
    {
        try {
            PreparedStatement stmt = bindParams(Settings.selectUtente, List.of(username, password));

            // return the user id if the user exists, otherwise null
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isAdmin = rs.getBoolean("Admin");
                Integer id = rs.getInt("ID");

                return new User(id, username, password, isAdmin);
            } else {
                return null;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertUser(String username, String password)
    {
        List <String> parameters = List.of(username, password);
        PreparedStatement stmt = bindParams(Settings.insertUtente, parameters);
        try {
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // So you don't have to write a different method each time the number of parameters changes (all of them are strings)
    public PreparedStatement bindParams(String sql, List<String> parameters) {
    try {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < parameters.size(); i++) {
            stmt.setString(i + 1, parameters.get(i));
        }
        return stmt;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }

    public void updateField(String table, String field, String value, String condition)
    {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE " + table + " SET " + field + " = ? WHERE " + condition);
            stmt.setString(1, value);
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*In Java, non esiste un concetto di parametri opzionali come in altri linguaggi come Python o JavaScript. Tuttavia, puoi ottenere un comportamento simile utilizzando il sovraccarico dei metodi. Puoi creare un secondo metodo getFilms() che non accetta parametri e chiama il primo metodo getFilms() con genere impostato su null. */
    public List<Film> getFilms() {
        return getFilms(null);
    }
    
    public List<Film> getFilms(String genere) {
        try {
            String query = Settings.selectFilms;
            if (genere != null && !genere.isEmpty()) {
                query += " WHERE Genere = ?";
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            if (genere != null && !genere.isEmpty()) {
                stmt.setString(1, genere);
            }
            ResultSet rs = stmt.executeQuery();
            List<Film> ls = new ArrayList<>();
            while (rs.next()) {
                ls.add(new Film(rs.getInt("CodFilm"), rs.getString("Titolo"), rs.getInt("AnnoProduzione"), rs.getString("Nazionalità"), rs.getString("Regista"), rs.getString("Genere"), rs.getTime("Durata"), rs.getString("Immagine")));
            }
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Film getFilmByID(Integer codFilm)
    {
        try {
            PreparedStatement stmt = conn.prepareStatement(Settings.selectFilm);
            stmt.setInt(1, codFilm);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Film(rs.getInt("CodFilm"), rs.getString("Titolo"), rs.getInt("AnnoProduzione"), rs.getString("Nazionalità"), rs.getString("Regista"), rs.getString("Genere"), rs.getTime("Durata"), rs.getString("Immagine"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFilm(Integer codFilm)
    {
        try {
            PreparedStatement stmt = conn.prepareStatement(Settings.deleteFilm);
            stmt.setInt(1, codFilm);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFilm(String titolo, Integer annoProduzione, String nazionalità, String regista, String genere, Time durata, String immagine)
    {
        try {
            PreparedStatement stmt = conn.prepareStatement(Settings.insertFilm);
            stmt.setString(1, titolo);
            stmt.setInt(2, annoProduzione);
            stmt.setString(3, nazionalità);
            stmt.setString(4, regista);
            stmt.setString(5, genere);
            stmt.setTime(6, durata);
            stmt.setString(7, immagine);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // getGeneri
    public List<String> getGeneri()
    {
        try {
            PreparedStatement stmt = conn.prepareStatement(Settings.selectGeneri);
            ResultSet rs = stmt.executeQuery();
            List<String> ls = new ArrayList<>();
            while (rs.next()) {
                ls.add(rs.getString("Genere"));
            }
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
