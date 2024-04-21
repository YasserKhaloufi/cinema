function getTbodyFromFilmList(listaFilm) {
    // Skippo il tag table e le intestazioni (sono già predisposte nel template HTML)
    var tbody = '';
    for (var i = 0; i < listaFilm.length; i++) 
        tbody += getFilmSummaryTbody(listaFilm[i]);
    
    return tbody;
}

function getFilmSummaryTbody(film){
    var tbody = '';
    tbody += '<tr>';
    tbody += '<td><a href="/dettagliFilm?codFilm=' + film.codFilm + '">' + film.titolo + '</a></td>';
    tbody += '<td>' + film.genere + '</td>';
    tbody += '<td>' + film.durata + '</td>';
    tbody += '<td><img src="Images/' + film.immagine + '" alt="locandina" width="100" height="150"></td>'; // Il path è relativo alla cartella static del progetto
    tbody += '<td td:if="${isAdmin}"><button onclick="deleteFilmByCod(' + film.codFilm + ')">Elimina</button></td>';
    tbody += '</tr>';

    return tbody;
}

function getDetailTbodyFromFilm(film) {
    // Skippo il tag table e le intestazioni (sono già predisposte nel template HTML)
    var tbody = '';
    tbody += '<tr>';
    tbody += '<td>' + film.titolo + '</td>';
    tbody += '<td>' + film.annoProduzione + '</td>';
    tbody += '<td>' + film.nazionalita + '</td>';
    tbody += '<td>' + film.regista + '</td>';
    tbody += '<td>' + film.genere + '</td>';
    tbody += '<td>' + film.durata + '</td>';
    tbody += '<td><img src="Images/' + film.immagine + '" alt="locandina" width="100" height="150"></td>'; // Il path è relativo alla cartella static del progetto
    tbody += '</tr>';

    return tbody;
}

// Eseguo i controlli di base direttamente lato client
function chkUserAndPass() 
{
    var user = document.getElementById("username").value;
    var pass = document.getElementById("password").value;
    var passR = document.getElementById("passR").value;

    regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; // Copiata da prof

    // TO DO: controllare gli spazi, eseguire trim()
    if (user == "" || pass == "" || passR == "") // Se i campi sono vuoti
    {
        // Rimango sulla pagina di registrazione visualizzando opportuno messaggio di errore
        $('#errore').text("I campi non possono essere vuoti");
        return false;
    }
    else // Altrimenti
    {
        if (pass != passR) // Se le password non coincidono
        {
            // Rimango sulla pagina di registrazione visualizzando opportuno messaggio di errore
            $('#errore').text("Le password non coincidono");
            return false;
        }
        else if (!regex.test(pass)) // Se la password non soddisfa i requisiti della regex
        {
            // Rimango sulla pagina di registrazione visualizzando opportuno messaggio di errore
            $('#errore').text("la password inserita non soddisfa i requisiti: <br> -almeno 8 caratteri,<br> -almeno una lettera,<br> -almeno un numero");
            return false;
        }
        else
        {
            // Se tutto è andato bene, ritorno true
            return true;
        }
    }
}

// Controlla se i campi sono nulli
function chkFilm() {
    var titolo = $('#titolo').val();
    var genere = $('#genere').val();
    var annoProduzione = $('#annoProduzione').val();
    var durata = $('#durata').val();
    var regista = $('#regista').val();
    var nazionalita = $('#nazionalita').val();
    var immagine = $('#immagine').val();

    if (titolo === "" || genere === "" || annoProduzione === "" || durata === "" || regista === "" || nazionalita === "" || immagine === "") {
        $('#errore').text("Tutti i campi devono essere compilati");
        return false;
    } else {
        // Altri controlli qui se necessario
        return true;
    }
}

function clearForm() {
    $(':input').val(''); 
};


// Sostituisce il codice HTML della pagina corrente con quello ricevuto dal controller
// function renderPage(response)
// {
//     document.open(); document.write(response); document.close();
// }

// Svuota il contenuto di tutti gli elementi di tipo input