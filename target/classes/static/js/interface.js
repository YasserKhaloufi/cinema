/*
Appunti:

-In jQuery, $('#[id]') is equivalent to document.getElementById('[id]') in plain JavaScript
-L'utente può modificare il codice per accedere alla lista senza login, perciò ho implementato dei check tramite coockie di sessione nel controller
*/

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
            sendRequest(endpoint, 'POST', { username: username, password: password }, "/", "");
        }
    }
    else // Altrimenti è una richiesta di login
    {
        var username = $('#username').val();
        var password = $('#password').val();
        
        // Invio credenziali mediante chiamata AJAX all'apposito endpoint nel controller
        sendRequest(endpoint, 'POST', { username: username, password: password }, "/elencoFilm", "");
        //getNextPage(endpoint, 'POST', { username: username, password: password });
    }
}

// Per invio richieste ai controller, con tanto di redirect in caso di successo
function sendRequest(endpoint, method, data, successPage, errorPage) {

    $.ajax({
        url: endpoint,
        method: method,
        data: data,
        success: function (response) {
            window.location.href = successPage; // reindirizzamento alla pagina successiva
            alert(response); 
        },
        error: function (error) {
            if(errorPage != "") // Se è stata specificata una pagina di errore
                window.location.href = errorPage; // reindirizzamento alla pagina di errore
            else // Altrimenti rimango sulla stessa e visualizzo un messaggio di errore
                $('#errore').text(error.responseText); 
        }
    });

}

function loadFilms(genere) {
    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/getFilms',
        method: 'GET',
        data: { genere: genere }, // Aggiunto parametro genere
        success: function (response) {
            // Se la richiesta ha successo, impagino la lista dei film
            let films = response;
            $('#listaFilm').html(getTbodyFromFilmList(films)); // inserisco nel tbody della tabella
        },
        error: function (error) {
            // Altrimenti visualizzo un messaggio di errore
            $('#errore').text(error.responseText);
        }
    });
}

function loadFilmByCod(codFilm) {

    // Richiesta all'apposito endpoint nel controller per ottenere la lista dei film
    $.ajax({
        url: '/getFilm',
        type: 'GET',
        data: { codFilm: codFilm },
        success: function (response) {
            let film = response
            $('#dettagliFilm').html(getDetailTbodyFromFilm(film));
        },
        error: function (error) {
            // Altrimenti visualizzo un messaggio di errore
            $('#errore').text(error.responseText);
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
            $('#errore').text(error.responseText);
        }
    });
}

function inserisciFilm() {
    if (chkFilm()) {

        // Adatto la durata in minuti a java.sql.Time
        var durata = minutesToTime($('#durata').val());

        // Leggi il file come data URL
        var file = document.querySelector('#immagine').files[0];
        var reader = new FileReader();

        reader.onloadend = function() { // Quando la lettura è completata
            var base64data = reader.result;

            var film = {
                titolo: $('#titolo').val(),
                annoProduzione: $('#annoProduzione').val(),
                nazionalita: $('#nazionalita').val(),
                regista: $('#regista').val(),
                genere: $('#genere').val(),
                durata: durata,
                immagine: base64data, // invia l'immagine come stringa base64
            }

            $.ajax({
                url: '/insertFilm',
                method: 'POST',
                contentType: 'application/json', // Ricorda
                data: JSON.stringify(film),

                success: function (response) {
                    window.location.href = "/elencoFilm"; // Reindirizza alla lista dei film
                    // Sleep per dare il tempo al server di salvarsi l'immagine del film
                    setTimeout(function() {
                         loadFilms();
                    }, 1000);
                },
                error: function (error) {
                    $('#errore').text(error.responseText);
                }
            });
        }

        /* La ragione per cui reader.readAsDataURL(file) è posto alla fine della funzione è perché prima di iniziare la lettura del file, è necessario definire cosa succede quando la lettura è completata. Questo è fatto impostando la funzione di callback reader.onloadend.*/
        reader.readAsDataURL(file); // Inizia la lettura del file

        /* Se reader.readAsDataURL(file) fosse chiamato prima di impostare reader.onloadend, ci sarebbe il rischio che la lettura del file terminasse prima che la funzione di callback fosse impostata. In tal caso, la funzione di callback non sarebbe chiamata, e il codice all'interno di reader.onloadend non sarebbe eseguito. */
    }
}

// function getNextPage(endpoint, method, data) {
//     $.ajax({
//         url: endpoint,
//         method: method,
//         data: data,
//         success: function (response) {
//             // Il reindirizzamento verrà gestito dal controller, qui riempio giusto per non mandare in errore AJAX
//             renderPage(response);
//         },
//     });
// }

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
