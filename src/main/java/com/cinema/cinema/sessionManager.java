package com.cinema.cinema;

import jakarta.servlet.http.HttpSession;

public class sessionManager {

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("Admin") != null;
    }

    public static void setLogin(HttpSession session, boolean isAdmin) {
        session.setAttribute("Admin", isAdmin);
    }

    public static boolean isAdmin(HttpSession session) {
        return isLoggedIn(session) && (boolean) session.getAttribute("Admin");
    }
}