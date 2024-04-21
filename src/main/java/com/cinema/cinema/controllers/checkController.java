package com.cinema.cinema.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.cinema.Settings;
import com.cinema.cinema.entities.User;
import com.cinema.cinema.services.dbManager;
import com.cinema.cinema.utils.sessionManager;

import jakarta.servlet.http.HttpSession;

@RestController
public class checkController {

        dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass); // Connessione al DB

        // TO DO: inviare da AJAX un oggetto user opportunamente formattato in JSON
    
        @PostMapping("/chkLogin")
        public ResponseEntity<String> chkLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
    
            User user = conn.checkCredentials(username, password);
            
            if(user != null) { // Se l'utente esiste ritorno una risposta success
                sessionManager.setLogin(session, user.isAdmin ? true : false); // Abilito la sessione
                return new ResponseEntity<>("Login effettuata",HttpStatus.OK);
    
            } else { // Rimani sulla pagina di login visualizzando errore
                return new ResponseEntity<>("Username o password errati", HttpStatus.UNAUTHORIZED);
            }
        }
    
        @PostMapping("/chkRegist")
        public ResponseEntity<String> chkRegist(@RequestParam String username, @RequestParam String password,  Model model) {
    
            if(!conn.usernameExists(username)) { // Se l'utente non esiste
                conn.insertUser(username, password); // Inserisco il nuovo utente nel DB
                return new ResponseEntity<>("Registrazione effettuata con successo", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Username già utilizzato", HttpStatus.CONFLICT);
            }
        }
}
