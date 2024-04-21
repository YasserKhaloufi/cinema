package com.cinema.cinema.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cinema.cinema.Settings;
import com.cinema.cinema.entities.Film;
import com.cinema.cinema.services.dbManager;
import com.cinema.cinema.utils.sessionManager;

import jakarta.servlet.http.HttpSession;

/*
 * TO DO:
    - implementare l'attivazione dell'account via mail e il recupero password
    - Inserimento immagine
    - Riordinare in sottocartelle senza rompere tutto

 */

// Controller per i redirect alle pagine (template)
@Controller
public class redirectController {

    dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass); // Connessione al DB

    // Reindirizza alla pagina di login
    @GetMapping("/")
    public String login() { 
        return Settings.loginPage;
    }

    // Reindirizza alla pagina di registrazione
    @GetMapping("/regist")
    public String regist() { 
        return Settings.signUpPage;
    }

    // Reindirizza all'elenco dei film
    /* La prima volta che accede ad elencoFilm, sarà il server ad occuparsi di bindare il contenuto, principalmente per non visualizzare il pulsante di cancellazione e di inserimento a chi non è admin. Quando filtra invece se ne occupa il client js */
    @GetMapping(value = "/elencoFilm")
    public String getFilmList(Model model, HttpSession session) {

        if(sessionManager.isLoggedIn(session)) {
            // Riempimento template
            List<Film> films = conn.getFilms(); model.addAttribute("films", films); // Riempimento lista film
            List<String> generi = conn.getGeneri(); model.addAttribute("generi", generi); // Riempimento opzioni filtro genere
            
             
            model.addAttribute("isAdmin", sessionManager.isAdmin(session)); // (per capire se mostrare o meno il pulsante di cancellazione)
            return Settings.filmListPage;
        } else {
            model.addAttribute("errore", Settings.notLoggedInError);
            return Settings.loginPage;
        }
    }

    // Reindirizza alla pagina di dettaglio del singolo film dato il suo codice
    @GetMapping("/dettagliFilm")
    public String dettagliFilm(@RequestParam Integer codFilm, Model model, HttpSession session) { 

        if(sessionManager.isLoggedIn(session))
        {
            model.addAttribute("codFilm", codFilm);
            return Settings.filmDetailsPage;
        }
        else
            model.addAttribute("errore", Settings.notLoggedInError);
            return Settings.loginPage;
    }

    @GetMapping("/inserisciFilm")
    public String inserisciFilm(Model model, HttpSession session) { 

         if(sessionManager.isLoggedIn(session))
         {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(sessionManager.isAdmin(session))
                return Settings.insertFilmPage;
            else
                model.addAttribute("errore", Settings.notAdminError);
                return Settings.filmListPage;
        } 
        else
        {
            model.addAttribute("errore", Settings.notLoggedInError);
            return Settings.loginPage;
        }   
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cancella tutti i dati di sessione
        return "html/login";
    }

    // @GetMapping("/test1")
    // public String test1(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
    //     model.addAttribute("name", name);
    //     return "test1"; 
    // }

    // @GetMapping("/test2")
    // @ResponseBody
    // public String test2(@RequestParam(name="name", required=false, defaultValue="World") String name) {

    //     return "<h1>Hello " + name + "</h1>";
    // }

    // Test per vedere se funziona la notazione Thymeleaf (form che reindirizza alla login)
    // @GetMapping("/test3")
    // public String test(){
    //     return "html/thymeleafTest";
    // }
}
