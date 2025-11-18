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
    
    public void eliminarContacto(Contacto contactoAEliminar) {
      contactoAEliminar.eliminarContacto();
      this.contactos.remove(contactoAEliminar); 
    }
    
    public void editarContacto(Contacto contacto, String nuevoNombre, String nuevoEmail) {
        contacto.setNombre(nuevoNombre);
        contacto.setEmail(nuevoEmail);
    }

    /*public List<Contacto> getContactos() {
        return contactos;
    }*/
    
    public int size() {
        return contactos.size();
    }
}