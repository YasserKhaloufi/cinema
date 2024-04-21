package com.cinema.cinema.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class Film {
    // 	CodFilm	Titolo	AnnoProduzione	Nazionalità	Regista	Genere	Durata	Immagine
    public int CodFilm;
    public String Titolo;
    public int AnnoProduzione;
    public String Nazionalita;
    public String Regista;
    public String Genere;
    public Time Durata;
    public String Immagine;

    public Film(int CodFilm, String Titolo, int AnnoProduzione, String Nazionalità, String Regista, String Genere, Time time, String Immagine) {
        this.CodFilm = CodFilm;
        this.Titolo = Titolo;
        this.AnnoProduzione = AnnoProduzione;
        this.Nazionalita = Nazionalità;
        this.Regista = Regista;
        this.Genere = Genere;
        this.Durata = time;
        this.Immagine = Immagine;
    }

    // Costruttore da resultset
    public Film(ResultSet result) throws SQLException {
        this.CodFilm = result.getInt("CodFilm");
        this.Titolo = result.getString("Titolo");
        this.AnnoProduzione = result.getInt("AnnoProduzione");
        this.Nazionalita = result.getString("Nazionalità");
        this.Regista= result.getString("Regista");
        this.Genere = result.getString("Genere");
        this.Durata =  result.getTime("Durata");
        this.Immagine = result.getString("Immagine");
    }
    

    public int getCodFilm() {
        return CodFilm;
    }

    public void setCodFilm(int codFilm) {
        CodFilm = codFilm;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public int getAnnoProduzione() {
        return AnnoProduzione;
    }

    public void setAnnoProduzione(int annoProduzione) {
        AnnoProduzione = annoProduzione;
    }

    public String getNazionalita() {
        return Nazionalita;
    }

    public void setNazionalita(String nazionalità) {
        Nazionalita = nazionalità;
    }

    public String getRegista() {
        return Regista;
    }

    public void setRegista(String regista) {
        Regista = regista;
    }

    public String getGenere() {
        return Genere;
    }

    public void setGenere(String genere) {
        Genere = genere;
    }

    public Time getDurata() {
        return Durata;
    }

    public void setDurata(Time durata) {
        Durata = durata;
    }

    public String getImmagine() {
        return Immagine;
    }

    public void setImmagine(String immagine) {
        Immagine = immagine;
    }
}
