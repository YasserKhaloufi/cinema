/*
Appunti:

-Per le funzioni di login e registrazione il reindirizzamento viene gestito direttamente dal controller JAVA
-In jQuery, $('#[id]') is equivalent to document.getElementById('[id]') in plain JavaScript
-L'utente può modificare il codice per accedere alla lista senza login, perciò ho implementato dei check tramite coockie di sessione nel controller
*/

// Collasso submitLogin e submitRegist in un'unica funzione
function submitUserInfo(endpoint)
{
    // Se  è una richiesta di registrazione prima controllo se i campi sono validi
    if(endpoint == '/chkRegist')
    {
        if (chkUserAndPass())
        {
            // Invio credenziali mediante chiamata AJAX all'apposito endpoint nel controller
            var username = $('#username').val();
            var password = $('#password').val();
            getNextPage(endpoint, 'POST', { username: username, password: password });
        }
    }
    else // Altrimenti è una richiesta di login
    {
        var username = $('#username').val();
        var password = $('#password').val();
        getNextPage(endpoint, 'POST', { username: username, password: password });
    }
}

function getNextPage(endpoint, method, data) {
    $.ajax({
        url: endpoint,
        method: method,
        data: data,
        success: function (response) {
            // Il reindirizzamento verrà gestito dal controller, qui riempio giusto per non mandare in errore AJAX
            renderPage(response);
        },
    });
}

function getFilms() {

    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/filmList',
        method: 'GET',
        dataType: 'json',
        success: function (response) {
            renderPage(response);
        },
    });
}

function loadFilms(genere) {
    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/getFilms',
        method: 'GET',
        data: { genere: genere }, // Aggiunto parametro genere
        dataType: 'json',
        success: function (response) {
            // Se la richiesta ha successo, impagino la lista dei film
            let films = response.data;
            $('#listaFilm').html(getTbodyFromFilmList(films));
        },
        error: function (error) {
            // Altrimenti visualizzo un messaggio di errore
            $('#errore').text(JSON.parse(error.responseText).message);
        }
    });
}

function loadFilmByCod(codFilm) {

    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/getFilm',
        type: 'GET',
        data: { codFilm: codFilm },
        dataType: 'json', // cosi da non dover fare il parse di response
        success: function (response) {
            // Se la richiesta ha successo, impagino la lista dei film
            let film = response.data; // non ho bisogno di fare il parse di data perchè l'oggetto film è già un oggetto JSON (codice java)
            $('#dettagliFilm').html(getDetailTbodyFromFilm(film));
        },
        error: function (error) {
            // Altrimenti visualizzo un messaggio di errore
            $('#errore').text(JSON.parse(error.responseText).message);
        }
    });
}

function deleteFilmByCod(codFilm) {

    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/deleteFilm',
        method: 'GET',
        data: { codFilm: codFilm },
        success: function (response) {
            // Se la richiesta ha successo, ricarico la lista dei film
            loadFilms();
        },
        error: function (error) {
            // Altrimenti visualizzo un messaggio di errore
            $('#errore').text(JSON.parse(error.responseText).message);
        }
    });
}

function inserisciFilm() {
    if (chkFilm()) {

        // Adatto la durata a java.sql.Time
        var durataMinuti = $('#durata').val();
        var ore = Math.floor(durataMinuti / 60), minuti = durataMinuti % 60, durata = new Date();
        durata.setHours(ore); durata.setMinutes(minuti); durata = durata.toISOString().slice(11, 19);

        var immagine = $('#immagine').val();

        var film = {
            titolo: $('#titolo').val(),
            annoProduzione: $('#annoProduzione').val(),
            nazionalita: $('#nazionalita').val(),
            regista: $('#regista').val(),
            genere: $('#genere').val(),
            durata: durata,
            immagine: immagine,
        }

        $.ajax({
            url: '/insertFilm',
            method: 'POST',
            contentType: 'application/json', // Ricorda
            data: JSON.stringify(film),

            success: function (response) {
                loadFilms();
            },
            error: function (error) {
                $('#errore').text(JSON.parse(error.responseText).message);
            }
        });
    }
}

/* Versioni precedenti

V2

Prima da controller JAVA rispondevo all'AJAX con un JSON con le info in base alle quali js reindirizzava da una parte piuttosto che dall'altra, es:
-new ResponseEntity<>(JSONresponse.error("Username già utilizzato"), HttpStatus.UNAUTHORIZED)
-new ResponseEntity<>(JSONresponse.ok("Registrazione effettuata con successo"), HttpStatus.Ok)

Il seguente codice è praticamente identico a quella che era la submitLogin:

function submitRegist() {
    if (chkUserAndPass()) {
        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '/chkRegist',
            method: 'POST',
            data: { username: username, password: password },
            success: function (response) {
                window.location.href = '/' // Reindirizza alla login
            },
            error: function (error) {
                $('#errore').text(JSON.parse(error.responseText).message); // Rimani su registrazione visualizzando l'errore
            }
        });
    }
}

error.responseText sarà sempre una stringa, anche se da java l'hai passata come un oggetto JSON e hai specificato datatype, quindi l'errore va sempre parsato.
-JSON.parse(error.responseText).message
Nel caso invece di risposta positiva, se hai specificato datatype JSON e inviato lato
server i dati usando JSONobject ecc questa risposta verrà automaticamente trattata come oggetto JSON e quindi si potrà accedere ai campi direttamente.
-response.data

V1

L'oggetto window in js rappresenta la finestra del browser,
tutto quello che riguarda js (variabili, funzioni,...) diventa
una property del suddetto oggetto.
Nel seguente script definisco ogni funzione che mi serve per i button come property di window, ma non permetto ciò finchè la pagina HTML da cui viene chiamata la funzione non è stata caricata per intero.
In questo modo non sarà possibile chiamare le funzioni finchè la pagina non è caricata (perchè appunto non ancora definite)

$(document).ready(function() {

    window.funzione = function() {
     // Codice funzione
    }

    ... altre funzioni
}

*/
