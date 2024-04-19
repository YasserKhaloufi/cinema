package com.cinema.cinema;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

/*
 * TO DO:
    - implementare l'attivazione dell'account via mail e il recupero password
    - Inserimento immagine
    - Riordinare in sottocartelle senza rompere tutto

 */

@Controller
public class redirectController {

    dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass); // Connessione al DB

    @GetMapping("/")
    public String login() { // Reindirizza alla pagina di login
        return "html/login";
    }

    @GetMapping("/regist")
    public String regist() { // Reindirizza alla pagina di registrazione
        return "html/regist";
    }

    // In origine era @GetMapping, ma dato che gli veniva inoltrata la richiesta inviata a /chkLogin (che è POST), ho dovuto cambiare il metodo per supportare entrambi i metodi di richiesta con la notazione @RequestMapping
    @RequestMapping(value = "/elencoFilm", method = {RequestMethod.GET, RequestMethod.POST})
    public String getFilmList(Model model, HttpSession session) {

        if(sessionManager.isLoggedIn(session)) {
            // Riempimento template
            List<Film> films = conn.getFilms(); model.addAttribute("films", films); // Riempimento lista film
            List<String> generi = conn.getGeneri(); model.addAttribute("generi", generi); // Riempimento opzioni filtro genere
            
            // (Servirà per la costruzione della tabella film, per capire se mostrare o meno il pulsante di cancellazione) 
            model.addAttribute("isAdmin", sessionManager.isAdmin(session));
            return "html/elencoFilm";
        } else {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }
    }

    @GetMapping("/dettagliFilm")
    public String dettagliFilm(@RequestParam Integer codFilm, Model model, HttpSession session) { 

        if(sessionManager.isLoggedIn(session))
        {
            model.addAttribute("codFilm", codFilm);
            return "html/dettagliFilm";
        }
        else
        {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }
    }

    @GetMapping("/inserisciFilm")
    public String inserisciFilm(Model model, HttpSession session) { 

         if(sessionManager.isLoggedIn(session))
         {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(sessionManager.isAdmin(session))
                return "html/inserisciFilm";
            else
                model.addAttribute("errore", "Non sei admin");
                return "html/login";    
        } 
        else
        {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }   
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
