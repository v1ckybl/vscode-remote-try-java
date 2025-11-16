package com.mycompany.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GestorContactoTest {
    private GestorContactos gestor;
    
    @BeforeEach
    void setUp() {
        gestor = new GestorContactos();
    }
    
    @Test
    void testAgregarContacto() {
        Contacto contacto = new Contacto("Juan", "juan@email.com");
        gestor.agregarContacto(contacto);
        
        assertEquals(1, gestor.size());
    }
    
    @Test
    void testEliminarContacto() {
        Contacto contacto = new Contacto("Juan", "juan@email.com");
        gestor.agregarContacto(contacto);
        
        boolean eliminado = gestor.eliminarContacto("juan@email.com");
        
        assertTrue(eliminado);
        assertEquals(0, gestor.size());
    }
    
    @Test
    public void testEditarContactoDesdeGestor() {
        GestorContactos gestor = new GestorContactos();
        Contacto c1 = new Contacto("Aylén", "aylen@mail.com");
        gestor.agregarContacto(c1);

        boolean editado = gestor.editarContacto("aylen@mail.com", "Aylén Chiesa", "aylen.chiesa@mail.com");

        assertTrue(editado);
        assertEquals("Aylén Chiesa", c1.getNombre());
        assertEquals("aylen.chiesa@mail.com", c1.getEmail());
    }
}
