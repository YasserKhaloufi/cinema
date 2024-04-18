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

    Issue:
        - Inserimento film
        - Riordinare in sottocartelle senza rompere tutto
        
 * TO DO:
    Aggiungere un filtro in base al genere (pagina con textbox richiede via AJAX la lista di film filtrata)
    implementare l'attivazione dell'account via mail e il recupero password
 */

@Controller
public class redirectController {
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
        // Utilizza il cookie di sessione admin per capire se l'utente è loggato o meno
        Boolean isAdmin = (Boolean) session.getAttribute("Admin");
        if(isAdmin != null) {
            List<Film> films = getFilmList(); model.addAttribute("films", films); // (Servirà per la costruzione della tabella film, per capire se mostrare o meno il pulsante di cancellazione) 
            List<String> generi = getGeneri(); model.addAttribute("generi", generi);
            
            model.addAttribute("isAdmin", isAdmin);
            return "html/elencoFilm";
        } else {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }
    }

    // Metodo per ottenere la lista di film
    private List<Film> getFilmList() {
        return dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass).getFilms();
    }

    // Distinct dei generi
    private List<String> getGeneri() {
        return dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass).getGeneri();
    }

    @GetMapping("/dettagliFilm")
    public String dettagliFilm(@RequestParam Integer codFilm, Model model, HttpSession session) { 

        Boolean isAdmin = (Boolean) session.getAttribute("Admin");
        if(isAdmin != null)
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

         // Utilizza il cookie di sessione admin per capire se l'utente è loggato o meno
         Boolean isAdmin = (Boolean) session.getAttribute("Admin");
         if(isAdmin != null)
         {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(isAdmin)
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
