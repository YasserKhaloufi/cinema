package com.cinema.cinema;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

// Per costruire risposte JSON
public class JSONresponse {
    
    /* Consiglio copilot: you can modify the ok method to accept an Object instead of a String. 
    This will allow you to pass in a list of objects directly, 
    which will then be converted to JSON by the JSONObject class */
    // Qualsiasi oggetto passato come parametro verrà convertito in JSON
    
    public static String error(Object message)
    {
        JSONObject json = new JSONObject();
        // Lo status è gestito da un parametro di ResponseEntity
        // json.put("status", "error");
        json.put("message", message);
        return json.toString();
    }

    public static String ok(Object data)
    {
        JSONObject json = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();

        // Lo status è gestito da un parametro di ResponseEntity
        // json.put("status", "ok");

        /* Perchè controllo se l'oggetto è di tipo Film?:
        The JSONObject class in Java can convert an object to a JSON string, but it depends on the type of the object. If the object is a Map, List, Number, Boolean, String, or null, it can be converted directly to a JSON string. If the object is a custom class, it needs to be serializable to JSON, otherwise it'll be treated as a string on the client side and you'll need to do JSON.parse first (nel metodo loadFilmByCod in redirect.js)
        */
        try {
            if (data instanceof Film) {
                String jsonString = mapper.writeValueAsString(data);
                json.put("data", new JSONObject(jsonString));
            } else {
                json.put("data", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }
}
