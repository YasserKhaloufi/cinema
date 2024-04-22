package com.cinema.cinema;

public class Settings {
    public static final String imgDir = "/Images";
    public static final String inBetweenPath = "/src/main/resources/static";
    public static final String currentDir = System.getProperty("user.dir");
    public static final String imgPath = currentDir + inBetweenPath + imgDir;
    
    // DB related
    public static final String dbName = "db_cinema_2024";
    public static final String server = "jdbc:mysql://localhost/";
    public static final String dbUser = "root";
    public static final String dbPass = "";

    // Prepared statements

    // Utenti
    public static final String insertUtente = "INSERT INTO utenti (Username, Password, Admin) VALUES(?, md5(?), 0)";
    public static final String selectUtente = "SELECT * FROM utenti WHERE Username = ? AND Password = md5(?)";
    public static final String searchUtente = "SELECT * FROM utenti WHERE Username = ?";

    // Film
    public static final String insertFilm = "INSERT INTO film (Titolo, AnnoProduzione, Nazionalità, Regista, Genere, Durata, Immagine) VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String selectFilms = "SELECT * FROM film";
    public static final String selectFilm = "SELECT * FROM film WHERE CodFilm = ?";
    public static final String deleteFilm = "DELETE FROM film WHERE CodFilm = ?";
    public static final String selectGeneri = "SELECT DISTINCT Genere FROM film";    

    // Errors
    public static final String notLoggedInError = "Ti piacerebbe, eh? Devi loggarti prima!";
    public static final String notAdminError = "Non sei autorizzato a fare questa operazione!";

    // Pagine
    public static final String loginPage = "html/login";
    public static final String signUpPage = "html/regist";
    public static final String filmListPage = "html/elencoFilm";
    public static final String filmDetailsPage = "html/dettagliFilm";
    public static final String insertFilmPage = "html/inserisciFilm";
}
