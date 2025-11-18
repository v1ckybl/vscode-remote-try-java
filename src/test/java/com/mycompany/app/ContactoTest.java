package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class ContactoTest {
    
    private Contacto contacto;
    
    @BeforeEach
    void setUp() {
        contacto = new Contacto();
    }
    

//crear un contacto usando el constructor por defecto
    @Test
    void testCrearContactoVacio() {
        assertNotNull(contacto);
        assertNull(contacto.getNombre());
        assertNull(contacto.getEmail());
    }
    
    @Test
    void testCrearContacto() {
        String nombre = "Tini Stoessel";
        String email = "tini.stoessel@email.com";
        
        Contacto contactoCompleto = new Contacto(nombre, email);
        
        assertNotNull(contactoCompleto);
        assertEquals(nombre, contactoCompleto.getNombre());
        assertEquals(email, contactoCompleto.getEmail());
    }
    
    @Test
    void testEditarNombreContacto() {
        GestorContactos gestor = new GestorContactos();
        Contacto steve = new Contacto("Steve Jobs", "steve@email.com");

        assertEquals("Steve Jobs", steve.getNombre());
        assertEquals("steve@email.com", steve.getEmail());

        String nuevoNombre = "Steve Wozniak";
        String nuevoEmail = "swozniak@nuevo.com";
        
        gestor.editarContacto(steve, nuevoNombre, nuevoEmail);

        // VERIFICACIÓN: Los campos fueron actualizados
        assertEquals(nuevoNombre, steve.getNombre(), "El nombre debe ser actualizado directamente.");
        assertEquals(nuevoEmail, steve.getEmail(), "El email debe ser actualizado directamente.");
        }
    

//editar el email de un contacto
    @Test
    void testEditarEmailContacto() {
        String emailInicial = "maria@empresa.com";
        String emailNuevo = "maria.garcia@empresa.com";
        
        contacto.setEmail(emailInicial);
        assertEquals(emailInicial, contacto.getEmail());
        
        // Editar email
        contacto.setEmail(emailNuevo);
        assertEquals(emailNuevo, contacto.getEmail());
    }
    

    @Test
    void testEliminarContacto() {
    GestorContactos gestor = new GestorContactos();
    Contacto Silvia = new Contacto("Silvia Hoferek", "silvia.hoferek@ucp.com");
    Contacto Martu = new Contacto("Martina Perduca", "martu@email.com");

    gestor.agregarContacto(Silvia);
    gestor.agregarContacto(Martu);

    assertEquals(2, gestor.size());

    // Verificar que se creó correctamente
    assertNotNull(Silvia.getNombre());
    assertNotNull(Silvia.getEmail());

    // Eliminar contacto con el nuevo método
    gestor.eliminarContacto(Silvia);

    // Verificar que los campos se limpiaron
    assertNull(Silvia.getNombre(), "El nombre debería estar en null tras eliminar el contacto");
    assertNull(Silvia.getEmail(), "El email debería estar en null tras eliminar el contacto");

    assertEquals(1, gestor.size(), "Queda 1 contacto en la lista."); 
    }


}