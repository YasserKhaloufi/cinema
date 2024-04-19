package com.cinema.cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 * Nel modello MVC questa è la classe service: si colloca tra il Model e il Controller,
 * per permettere al controller di accedere ai dati del model.
 * 
 * In spring sarebbe da indicare con l'annotazione @Service, che permette a spring di rilevare automaticamente la classe e 
 * e di gestire la sua vita come un bean (oggetto gestito da spring). Tuttavia in questo caso non è necessario, in quanto
 * la classe è già strutturata secondo il modello singleton manualmente.
 * 
 * L'annotazione @Service in Spring crea un Singleton per impostazione predefinita, quindi si potrebbe rimuovere
 * la logica del Singleton manuale con @Service.
 */

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

    // Costruttore
    private dbManager(String dbName, String serverName, String username, String password)
    {
        try {
            conn = DriverManager.getConnection(serverName + dbName, username, password); // Inizializzo la connessione al DB
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean usernameExists(String username)
    {
        try {
            PreparedStatement stmt = bindParams(Settings.searchUtente, List.of(username));

            // se ritorna più di una riga, l'utente esiste
            return stmt.executeQuery().next(); // ritorna true se c'è almeno una riga, false altrimenti

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User checkCredentials(String username, String password)
    {
        try {
            PreparedStatement stmt = bindParams(Settings.selectUtente, List.of(username, password));

            // se l'utente esiste, ritorna un oggetto User, altrimenti null
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                boolean isAdmin = result.getBoolean("Admin"); Integer id = result.getInt("ID");
                return new User(id, username, password, isAdmin); // La password è già md5
            } else {
                return null;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // registrazione utente
    public void insertUser(String username, String password)
    {
        PreparedStatement stmt = bindParams(Settings.insertUtente, List.of(username, password));
        try {
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Film> getFilms(String genere) {
        try {
            String sql = Settings.selectFilms;
            boolean genereExists = false;

            if (genere != null && !genere.isEmpty()) {
                sql += " WHERE Genere = ?";
                genereExists = true;
            }

            PreparedStatement stmt = conn.prepareStatement(sql); // Solo dopo aver verificato di dover aggiungere la clause WHERE o meno costruisco lo statement

            if (genereExists)
                stmt.setString(1, genere);
            
            ResultSet result = stmt.executeQuery();
            List<Film> ls = new ArrayList<>();

            while (result.next())
                ls.add(new Film(result));
            
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*In Java, non esiste un concetto di parametri opzionali come in altri linguaggi come Python o JavaScript. Tuttavia, puoi ottenere un comportamento simile utilizzando il sovraccarico dei metodi. Puoi creare un secondo metodo getFilms() che non accetta parametri e chiama il primo metodo getFilms() con genere impostato su null. */
    public List<Film> getFilms() {
        return getFilms(null);
    }

    public Film getFilmByID(Integer codFilm)
    {
        try {
                PreparedStatement stmt = conn.prepareStatement(Settings.selectFilm);
                stmt.setInt(1, codFilm);
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    return new Film(result);
                } else {
                    return null;
                }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertFilm(Film film)
    {
        try {
            PreparedStatement stmt = conn.prepareStatement(Settings.insertFilm);
            stmt.setString(1, film.getTitolo());
            stmt.setInt(2, film.getAnnoProduzione());
            stmt.setString(3, film.getNazionalita());
            stmt.setString(4, film.getRegista());
            stmt.setString(5, film.getGenere());
            stmt.setTime(6, film.getDurata());
            stmt.setString(7, film.getImmagine());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
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

    // Per bindare un numero variabile di parametri nello statement (finchè sono di tipo stringa)
    private PreparedStatement bindParams(String sql, List<String> parameters) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i)); // in JDBC il settaggio parte da 1
            }
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Per ora inutilizzato
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

}
