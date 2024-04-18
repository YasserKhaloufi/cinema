package com.cinema.cinema;

public class Settings {
    // DB related
    public static final String dbName = "db_cinema_2024";
    public static final String server = "jdbc:mysql://localhost/";
    public static final String dbUser = "root";
    public static final String dbPass = "";

    // Prepared statementes
    public static final String insertUtente = "INSERT INTO utenti (Username, Password, Admin) VALUES(?, md5(?), 0)";
    public static final String selectUtente = "SELECT * FROM utenti WHERE Username = ? AND Password = md5(?)";
    public static final String searchUtente = "SELECT * FROM utenti WHERE Username = ?";
    public static final String insertFilm = "INSERT INTO film (Titolo, AnnoProduzione, Nazionalità, Regista, Genere, Durata, Immagine) VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String selectFilms = "SELECT * FROM film";
    public static final String selectFilm = "SELECT * FROM film WHERE CodFilm = ?";
    public static final String deleteFilm = "DELETE FROM film WHERE CodFilm = ?";
    public static final String selectGeneri = "SELECT DISTINCT Genere FROM film";

    public static final String imgDir = "Images/";
}
