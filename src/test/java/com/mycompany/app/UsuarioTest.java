package com.mycompany.app;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class UsuarioTest {
  
  @Test
  public void crearUsuario() {
    //debe crear un usuario con nombre y email
    String nombre = "Juan Perez";
    String email = "juan.perez@example.com";
    Usuario usuario = new Usuario(nombre, email);
    assertEquals("Juan Perez", usuario.getNombre());
    assertEquals("juan.perez@example.com", usuario.getEmail());
  }


  @Test
  public void testMarcarComoLeidoYNoLeido() {
    Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
    Contacto anaContacto = new Contacto("Ana", "ana@empresa.com");
    Contacto luisContacto = new Contacto("Luis", "luis@empresa.com");
    
    Usuario anaUsuario = new Usuario("Ana", "ana@empresa.com", anaContacto);
    Usuario luisUsuario = new Usuario("Luis", "luis@empresa.com", luisContacto);
    
    //enviar el correo
    Email email = new Email("Reunión importante",
                            "Mañana a las 10am", remitente, Arrays.asList(anaContacto, luisContacto));
    
    SendMail gestor = new SendMail();
    gestor.enviar(email, java.util.Arrays.asList(anaContacto, luisContacto));

    assertEquals("Sent", gestor.getStatus());
    
    //verif bandeja de entrada de cada uno
    assertEquals(1, anaContacto.getBandejaEntrada().getEmails().size());
    assertEquals(1, luisContacto.getBandejaEntrada().getEmails().size());
    
    //correos clonados en bandejas de cada contacto
    Email emailAna = anaContacto.getBandejaEntrada().getEmails().get(0);
    Email emailLuis = luisContacto.getBandejaEntrada().getEmails().get(0);
    
    //verifico no leido
    assertFalse(emailAna.isLeido(), "Email de Ana debería estar sin leer inicialmente");
    assertFalse(emailLuis.isLeido(), "Email de Luis debería estar sin leer inicialmente");
    
    //ana marca manualmente como leido
    anaUsuario.marcarComoLeido(emailAna); 
    assertTrue(emailAna.isLeido());

    assertFalse(emailLuis.isLeido(), "Email de Luis debería seguir sin leer");
    
    //luis abre su correo
    luisUsuario.getContacto().getBandejaEntrada().getEmails().get(0).getContent();
    assertTrue(luisUsuario.getContacto().getBandejaEntrada().getEmails().get(0).isLeido());
    
    // ana marca su correo como no leido
    anaUsuario.marcarComoNoLeido(emailAna);
    assertFalse(emailAna.isLeido(), "Email de Ana debería estar sin leer después de marcarComoNoLeido");
    
    // verifico que el correo de ana se marcó como no leido
    assertFalse(emailAna.isLeido(), "Correo de Ana inicialmente no leído");
    anaUsuario.marcarComoLeido(emailAna);
    assertTrue(emailAna.isLeido(), "Ana marco como leído su correo");
  }

  @Test
  public void testRestaurarEmail() {
    // Crear usuarios y contactos
    Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
    Contacto martuContacto = new Contacto("Martu", "martu@empresa.com");
    Usuario martuUsuario = new Usuario("Martu", "martu@empresa.com", martuContacto);

    // Crear y enviar un email
    Email emailImportante = new Email(
        "Proyecto Urgente",
        "Necesitamos revisar el proyecto antes del viernes.",
        remitente,
        Arrays.asList(martuContacto));

    SendMail gestor = new SendMail();
    gestor.enviar(emailImportante, Arrays.asList(martuContacto));

    //verificar que el email llegó a la bandeja
    assertEquals(1, martuContacto.getBandejaEntrada().getEmails().size(),
        "Martu debería tener 1 email en su bandeja");

    //copia en bandeja de martu
    Email emailEnBandeja = martuContacto.getBandejaEntrada().getEmails().get(0);

    //verificar estado inicial
    assertFalse(emailEnBandeja.isEliminado(), "Email inicialmente no debe estar eliminado");
    assertTrue(martuContacto.getBandejaEntrada().getEmails().contains(emailEnBandeja),
        "Email debe estar en la bandeja inicialmente");

    //el usuario elimina el email
    martuUsuario.eliminarEmail(emailEnBandeja);

    // Verificar que se marcó como eliminado y se removió de la bandeja
    assertTrue(emailEnBandeja.isEliminado(), "Email debería estar marcado como eliminado");
    assertEquals(0, martuContacto.getBandejaEntrada().getEmails().size(),"Bandeja debería estar vacía después de eliminar");
    assertFalse(martuContacto.getBandejaEntrada().getEmails().contains(emailEnBandeja),"Email no debería estar en la bandeja después de eliminarlo");

    // el usuario restaura el email
    martuUsuario.restaurarEmail(emailEnBandeja);

    //verificar que se desmarcó como eliminado
    assertFalse(emailEnBandeja.isEliminado(), "Email debería estar restaurado (no eliminado)");

    //verificar que el email restaurado está de vuelta en la bandeja
    assertEquals(1, martuContacto.getBandejaEntrada().getEmails().size(),"Bandeja debería tener el email restaurado");
    assertTrue(martuContacto.getBandejaEntrada().getEmails().contains(emailEnBandeja));
  }
 
}