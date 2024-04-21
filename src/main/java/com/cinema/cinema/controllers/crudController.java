package com.cinema.cinema.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.cinema.Settings;
import com.cinema.cinema.entities.Film;
import com.cinema.cinema.services.dbManager;
import com.cinema.cinema.utils.sessionManager;

import jakarta.servlet.http.HttpSession;


@RestController
public class crudController {

    dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass); // Connessione al DB

    // Ritorna JSON lista oggetti film
    @GetMapping("/getFilms") 
    public Object getFilms(@RequestParam(required = false) String genere, Model model, HttpSession session) // Object perchè ritorno stringa/lista
    {
        if(sessionManager.isLoggedIn(session))
        {
            /*in Spring Boot, quando ritorni un oggetto o una lista di oggetti da un controller, il framework automaticamente serializza l'oggetto o la lista in JSON e imposta il tipo MIME della risposta a application/json, così la resp ricevuta lato js è già intepretata di conseguenza senza dover fare parse */
            List<Film> films = conn.getFilms(genere);
            return new ResponseEntity<>(films, HttpStatus.OK); // Ritorno la lista di film in formato JSON (la conversione è gestita da Spring Boot)
        }
        else 
            return new ResponseEntity<>(Settings.notLoggedInError, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getFilm") // Dopo gli opportuni controlli di sessione, ritorno l'elenco dei film
    public Object getFilm(@RequestParam Integer codFilm, Model model, HttpSession session)
    {
        // Utilizza il cookie di sessione admin per capire se l'utente è loggato o meno
        if(sessionManager.isLoggedIn(session))
        {
            Film film = conn.getFilmByID(codFilm);
            return new ResponseEntity<>(film, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(Settings.notLoggedInError, HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/deleteFilm")
    public Object deleteFilm(@RequestParam Integer codFilm, Model model, HttpSession session)
    {
        if(sessionManager.isLoggedIn(session))
        {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(sessionManager.isAdmin(session))
            {
                conn.deleteFilm(codFilm);
                return new ResponseEntity<>("Film eliminato", HttpStatus.OK);
            }
            else
                return new ResponseEntity<>("Non sei admin", HttpStatus.UNAUTHORIZED);
        } 
        else
        {
            return new ResponseEntity<>(Settings.notLoggedInError, HttpStatus.UNAUTHORIZED);
        }
    }

    /* Quando ricevi una richiesta HTTP, il corpo della richiesta viene analizzato e mappato all'oggetto del parametro annotato con @RequestBody. 
     * In questo caso nel corpo della richiesta è contenuto un oggetto film in formato JSON, che quindi verrà mappato automaticamente a un oggetto film.
    */
    // TO DO: implementare passaggio file immagine al server
    @PostMapping("/insertFilm")
    public Object insertFilm(@RequestBody Film film, Model model, HttpSession session) throws IOException
    {
        if(sessionManager.isLoggedIn(session))
        {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(sessionManager.isAdmin(session))
            {
                // Decodifica la stringa base64
                // byte[] decodedImg = Base64.getDecoder().decode(film.getImmagine().split(",")[1].getBytes(StandardCharsets.UTF_8));
                // Path destinationFile = Paths.get(Settings.imgDir, film.getTitolo() + ".jpg");
                // Files.write(destinationFile, decodedImg, StandardOpenOption.CREATE);

                // Imposta il percorso dell'immagine al nome del file
                film.setImmagine(film.getTitolo() + ".jpg");

                conn.insertFilm(film);
                return new ResponseEntity<>("Film inserito", HttpStatus.OK);
            }
            else
                return new ResponseEntity<>("Non sei admin", HttpStatus.UNAUTHORIZED);
        } 
        else
        {
            return new ResponseEntity<>(Settings.notLoggedInError, HttpStatus.UNAUTHORIZED);
        }   
    }
}
