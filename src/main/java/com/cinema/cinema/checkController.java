package com.cinema.cinema;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class checkController {

        dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass);

        // TO DO: inviare da AJAX un oggetto user opportunamente formattato in JSON
    
        // Utilizzo Post dato che devo tornare un JSON all'AJAX, la quale poi farà qualcosa in base al successo o meno
        @PostMapping("/chkLogin")
        public String chkLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
    
            User user = conn.searchForUser(username, password);
            
            if(user != null) { // Se l'utente esiste reindirizzo alla pagina di elenco film
                sessionManager.setLogin(session, user.isAdmin ? true : false); // Salvo in sessione una qualsiasi info per dell'utente (java distingue i dati di sessione di client diversi) per capire se l'utente è loggato o meno nelle pagine seguenti (l'info admin mi servirà più tardi)
                return "forward:/elencoFilm";
    
            } else { // Rimani sulla pagina di login visualizzando errore
                model.addAttribute("errore", "Username o password errati");
                return "html/login"; 
            }
        }
    
        @PostMapping("/chkRegist")
        public String chkRegist(@RequestParam String username, @RequestParam String password,  Model model) {
    
            if(!conn.userExists(username)) { // Se l'utente non esiste
                conn.insertUser(username, password); // Inserisco il nuovo utente nel DB
                model.addAttribute("errore", "Registrazione effettuata con successo");
                return "html/login";
            } else {
                model.addAttribute("errore", "Username già utilizzato");
                return "html/regist";
            }
        }
}
