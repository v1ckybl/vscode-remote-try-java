package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        String nombre = "Juan Pérez";
        String email = "juan.perez@email.com";
        
        Contacto contactoCompleto = new Contacto(nombre, email);
        
        assertNotNull(contactoCompleto);
        assertEquals(nombre, contactoCompleto.getNombre());
        assertEquals(email, contactoCompleto.getEmail());
    }
    
  
    @Test
    void testEditarNombreContacto() {
        String nombreInicial = "María García";
        String nombreNuevo = "María García López";
        
        contacto.setNombre(nombreInicial);
        assertEquals(nombreInicial, contacto.getNombre());
        
        //editar nombre
        contacto.setNombre(nombreNuevo);
        assertEquals(nombreNuevo, contacto.getNombre());
    }
    

//editar el email de un contacto
    @Test
    void testEditarEmailContacto() {
        String emailInicial = "maria@empresa.com";
        String emailNuevo = "maria.garcia@nuevaempresa.com";
        
        contacto.setEmail(emailInicial);
        assertEquals(emailInicial, contacto.getEmail());
        
        // Editar email
        contacto.setEmail(emailNuevo);
        assertEquals(emailNuevo, contacto.getEmail());
    }
    

    @Test
    void testEliminarContacto() {
    // Crear contacto con datos
    Contacto contacto = new Contacto("Ana López", "ana.lopez@email.com");

    // Verificar que se creó correctamente
    assertNotNull(contacto.getNombre());
    assertNotNull(contacto.getEmail());
    assertNotNull(contacto.getBandejaEntrada());
    assertNotNull(contacto.getBandejaSalida());

    // Eliminar contacto con el nuevo método
    contacto.eliminarContacto();

    // Verificar que los campos se limpiaron
    assertNull(contacto.getNombre(), "El nombre debería estar en null tras eliminar el contacto");
    assertNull(contacto.getEmail(), "El email debería estar en null tras eliminar el contacto");
    assertNull(contacto.getBandejaEntrada(), "La bandeja de entrada debería estar en null");
    assertNull(contacto.getBandejaSalida(), "La bandeja de salida debería estar en null");
}

    @Test
    void testContactoComoRemitente() {
        String nombre = "Carlos Sender";
        String email = "carlos.sender@email.com";
        
        Contacto remitente = new Contacto(nombre, email);
        
        // Verificar que los datos son correctos para usar como remitente
        assertNotNull(remitente.getNombre());
        assertNotNull(remitente.getEmail());
        assertEquals(nombre, remitente.getNombre());
        assertEquals(email, remitente.getEmail());
        assertTrue(remitente.getEmail().contains("@"));
    }
    
    @Test
    void testContactoComoDestinatario() {
        String nombre = "Laura Receiver";
        String email = "laura.receiver@email.com";
        
        Contacto destinatario = new Contacto(nombre, email);
        
        // Verificar que los datos son correctos para usar como destinatario
        assertNotNull(destinatario.getNombre());
        assertNotNull(destinatario.getEmail());
        assertEquals(nombre, destinatario.getNombre());
        assertEquals(email, destinatario.getEmail());
        assertTrue(destinatario.getEmail().contains("@"));
    }
}