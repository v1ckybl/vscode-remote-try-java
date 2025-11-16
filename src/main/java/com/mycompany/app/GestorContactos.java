package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

public class GestorContactos {
    private List<Contacto> contactos;
    
    public GestorContactos() {
        this.contactos = new ArrayList<>();
    }
    
    public void agregarContacto(Contacto contacto) {
        contactos.add(contacto);
    }
    
    public boolean eliminarContacto(String email) {
      return contactos.removeIf(contacto -> contacto.getEmail().equals(email));
    }
    
    public boolean editarContacto(String email, String nuevoNombre, String nuevoEmail) {
        for (Contacto c : contactos) {
            if (c.getEmail().equals(email)) {
                if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                    c.setNombre(nuevoNombre);
                }
                if (nuevoEmail != null && !nuevoEmail.isEmpty()) {
                    c.setEmail(nuevoEmail);
                }
                return true; // edición exitosa
            }
        }
        return false; // no encontrado
    }

  
    public List<Contacto> getContactos() {
        return contactos;
    }
    
    public int size() {
        return contactos.size();
    }
}
