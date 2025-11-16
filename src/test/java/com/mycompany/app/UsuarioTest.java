package com.mycompany.app;

import org.junit.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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
  public void testUsuarioMarcaEmailManualmente() {
    //crear usuarios (no Contacto)
    Usuario ana = new Usuario("Ana", "ana@empresa.com");
    Usuario luis = new Usuario("Luis", "luis@empresa.com");
    
    // Verificar que inicialmente no están marcados como leído
    assertFalse(ana.isLeido(), "Ana inicialmente no debería estar marcada como leído");
    assertFalse(luis.isLeido(), "Luis inicialmente no debería estar marcado como leído");
    
    // Ana decide marcarse como "leído" (tal vez indica que ya revisó sus emails)
    ana.marcarComoLeido();
    assertTrue(ana.isLeido(), "Ana debería estar marcada como leído después de marcarComoLeido()");
    
    // Luis permanece sin marcar
    assertFalse(luis.isLeido(), "Luis debería seguir sin estar marcado como leído");
    
    // Ana puede volver a marcarse como no leído
    ana.marcarComoNoLeido();
    assertFalse(ana.isLeido(), "Ana debería estar sin marcar después de marcarComoNoLeido()");
    
    // Luis se marca como leído
    luis.marcarComoLeido();
    assertTrue(luis.isLeido(), "Luis debería estar marcado como leído");
  }

  @Test
  public void testUsuarioMarcaEmailsRecibidos() {
    Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
    Contacto anaContacto = new Contacto("Ana", "ana@empresa.com");
    Contacto luisContacto = new Contacto("Luis", "luis@empresa.com");
    
    Usuario anaUsuario = new Usuario("Ana", "ana@empresa.com");
    Usuario luisUsuario = new Usuario("Luis", "luis@empresa.com");
    
    //enviar el correo
    Email email = new Email("Reunión importante",
                            "Mañana a las 10am", remitente);
    email.getRecipients().add(anaContacto);
    email.getRecipients().add(luisContacto);
    
    SendMail gestor = new SendMail();
    gestor.enviar(email, java.util.Arrays.asList(anaContacto, luisContacto));
    
    //verif bandeja de entrada de cada uno
    assertEquals(1, anaContacto.getBandejaEntrada().getEmails().size());
    assertEquals(1, luisContacto.getBandejaEntrada().getEmails().size());
    
    //pongo en una nueva clase
    Email emailAna = anaContacto.getBandejaEntrada().getEmails().get(0);
    Email emailLuis = luisContacto.getBandejaEntrada().getEmails().get(0);
    
    //verifico no leido
    assertFalse(emailAna.isLeido(), "Email de Ana debería estar sin leer inicialmente");
    assertFalse(emailLuis.isLeido(), "Email de Luis debería estar sin leer inicialmente");
    
    //ana marca manualmente como leido
    emailAna.marcarComoLeido(); 
    assertTrue(emailAna.isLeido());

    assertFalse(emailLuis.isLeido(), "Email de Luis debería seguir sin leer");
    
    //luis abre su correo
    luisContacto.getBandejaEntrada().getEmails().get(0).getContent();
    assertTrue(luisContacto.getBandejaEntrada().getEmails().get(0).isLeido());
    
    // ana marca su correo como no leido
    emailAna.marcarComoNoLeido();
    assertFalse(emailAna.isLeido(), "Email de Ana debería estar sin leer después de marcarComoNoLeido");
    
    // verifico que el correo de ana se marcó como no leido
    assertFalse(anaUsuario.isLeido(), "Ana usuario inicialmente no leído");
    anaUsuario.marcarComoLeido();
    assertTrue(anaUsuario.isLeido(), "Ana usuario debería estar marcado como leído");
  }

  @Test
  public void testRelacionUsuarioContacto() {
    // Crear usuario - automáticamente crea contacto asociado
    Usuario ana = new Usuario("Ana García", "ana@empresa.com");
    
    // Verificar que el contacto se creó automáticamente
    assertNotNull(ana.getContacto(), "Usuario debería tener un contacto asociado");
    assertEquals("Ana García", ana.getContacto().getNombre());
    assertEquals("ana@empresa.com", ana.getContacto().getEmail());
    
    // Verificar que el contacto tiene bandejas
    assertNotNull(ana.getContacto().getBandejaEntrada(), "Contacto debería tener bandeja de entrada");
    assertNotNull(ana.getContacto().getBandejaSalida(), "Contacto debería tener bandeja de salida");
    
    // Crear y enviar un email al contacto de Ana
    Contacto remitente = new Contacto("Luis", "luis@empresa.com");
    Email email = new Email("Test", "Contenido de prueba", remitente);
    
    ana.getContacto().getBandejaEntrada().agregarEmail(email);
    
    // Verificar que Ana puede eliminar el email usando su poder de usuario
    assertFalse(email.isEliminado(), "Email inicialmente no debería estar eliminado");
    ana.eliminarEmail(email);
    assertTrue(email.isEliminado(), "Email debería estar marcado como eliminado después de que Ana lo elimine");
    
    // Ana puede restaurar el email
    ana.restaurarEmail(email);
    assertFalse(email.isEliminado(), "Email debería estar restaurado");
  }
}
