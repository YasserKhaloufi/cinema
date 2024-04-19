package com.cinema.cinema;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;


@Controller
public class utilController {

    dbManager conn = dbManager.getInstance(Settings.dbName, Settings.server, Settings.dbUser, Settings.dbPass); // Connessione al DB


    @GetMapping("/getFilms") 
    public Object getFilms(@RequestParam(required = false) String genere, Model model, HttpSession session)
    {
        // Dopo gli opportuni controlli di sessione, ritorno l'elenco dei film
        if(sessionManager.isLoggedIn(session))
        {
            List<Film> films = conn.getFilms(genere);
            return new ResponseEntity<>(JSONresponse.ok(films), HttpStatus.OK);
        }
        else // Se non è loggato, ritorna la pagina di login
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
    }

    @GetMapping("/getFilm") // Dopo gli opportuni controlli di sessione, ritorno l'elenco dei film
    public Object getFilm(@RequestParam Integer codFilm, Model model, HttpSession session)
    {
        // Utilizza il cookie di sessione admin per capire se l'utente è loggato o meno
        if(sessionManager.isLoggedIn(session))
        {
            Film film = conn.getFilmByID(codFilm);
            return new ResponseEntity<>(JSONresponse.ok(film), HttpStatus.OK);
        }
        else
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
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
                return new ResponseEntity<>(JSONresponse.ok("Film eliminato"), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(JSONresponse.error("Non sei admin"), HttpStatus.UNAUTHORIZED);
        } 
        else
        {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }
    }

    // TO DO: implementare passaggio file immagine al server
    @PostMapping("/insertFilm")
    public Object insertFilm(@RequestBody Film film, Model model, HttpSession session)
    {
        if(sessionManager.isLoggedIn(session))
        {
            // Qua è meglio non reloadare la pagina, quindi ritorno un JSON con l'esito dell'operazione che js si occuperà di gestire
            if(sessionManager.isAdmin(session))
            {
                // prendi solo il nome dell'immagine
                String path[] = film.getImmagine().split("\\\\"); String nomeImmagine = path[path.length - 1]; 
                film.setImmagine(nomeImmagine);

                conn.insertFilm(film);
                return new ResponseEntity<>(JSONresponse.ok("Film inserito"), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(JSONresponse.error("Non sei admin"), HttpStatus.UNAUTHORIZED);
        } 
        else
        {
            model.addAttribute("errore", "Ti piacerebbe, eh? Devi loggarti prima!");
            return "html/login";
        }   
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cancella tutti i dati di sessione
        return "html/login";
    }
}
