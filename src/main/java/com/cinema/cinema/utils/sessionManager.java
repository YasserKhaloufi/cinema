package com.cinema.cinema.utils;

import jakarta.servlet.http.HttpSession;

public class sessionManager {
    // Salvo in sessione una qualsiasi info per dell'utente (java distingue i dati di sessione di client diversi) per capire se l'utente è loggato o meno nelle pagine seguenti (l'info admin mi servirà più tardi)

    // Controllo se l'utente è loggato
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("Admin") != null;
    }

    // Setto la sessione (login effettuata o meno)
    public static void setLogin(HttpSession session, boolean isAdmin) {
        session.setAttribute("Admin", isAdmin);
    }

    // Controllo se l'utente è admin
    public static boolean isAdmin(HttpSession session) {
        return isLoggedIn(session) && (boolean) session.getAttribute("Admin");
    }
}