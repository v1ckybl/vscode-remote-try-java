package com.mycompany.app;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class EmailTest {
    //pruebaCI

  @Test
  public void testEnvioDeCorreo() {//raro este test

    Contacto remitente = new Contacto("Remitente 1", "remitente@demo.com");
    Contacto c1 = new Contacto("Contacto 1", "contacto1@demo.com");

    Email email = new Email("Prueba", "Hola, este es un correo de prueba", remitente);
    email.getRecipients().add(c1);

    //bandeja de entrada del destinatario
    Bandeja bandejaEntrada = new Bandeja();

    //simular que le llega un correo al destinatario
    bandejaEntrada.agregarEmail(email);

    assertEquals(1, bandejaEntrada.getEmails().size(), "La bandeja debería tener un correo recibido");
    assertEquals("Prueba", bandejaEntrada.getEmails().get(0).getSubject());
    assertFalse(bandejaEntrada.getEmails().get(0).isLeido(), "El correo recibido debería estar sin leer");
    assertEquals(remitente, bandejaEntrada.getEmails().get(0).getSender());
  }

  @Test
  public void testEnvioDeCorreoEntreBandejas() {
    // Crear remitente y destinatarios
    Contacto r1 = new Contacto("Carlos", "carlos@empresa.com");
    Contacto ana = new Contacto("Ana", "ana@empresa.com");
    Contacto luis = new Contacto("Luis", "luis@empresa.com");

    // Crear el correo
    Email email = new Email(
        "Reunión semanal",
        "Recordatorio de reunión el lunes a las 10:00.",
        r1);

    // Agregar destinatarios
    email.getRecipients().add(ana);
    email.getRecipients().add(luis);

    //clase que envía
    SendMail gestor = new SendMail();

    //enviar
    gestor.enviar(email, Arrays.asList(ana, luis));

    // Verificar que el estado del envío cambió
    assertEquals("Sent", gestor.getStatus(), "El estado del correo debería ser 'Sent'");

    // Verificar que el correo se guardó en la bandeja de salida del remitente
    assertEquals(1, r1.getBandejaSalida().getEmails().size(),
        "El remitente debería tener un correo en su bandeja de salida");
    assertEquals("Reunión semanal", r1.getBandejaSalida().getEmails().get(0).getSubject());

    // Verificar que cada destinatario recibió el correo en su bandeja de entrada
    assertEquals(1, ana.getBandejaEntrada().getEmails().size(), "Ana debería tener un correo en su bandeja de entrada");
    assertEquals(1, luis.getBandejaEntrada().getEmails().size(),
        "Luis debería tener un correo en su bandeja de entrada");

    // Verificar que el remitente del correo recibido sea correcto
    assertEquals(r1, ana.getBandejaEntrada().getEmails().get(0).getSender());
    assertEquals(r1, luis.getBandejaEntrada().getEmails().get(0).getSender());

    //verificar leido
    assertFalse(ana.getBandejaEntrada().getEmails().get(0).isLeido(),
        "El correo en la bandeja de Ana debería estar sin leer");
    assertFalse(luis.getBandejaEntrada().getEmails().get(0).isLeido(),
        "El correo en la bandeja de Luis debería estar sin leer");

    //ana abre el correo (o sea mira el contenido)
    ana.getBandejaEntrada().getEmails().get(0).getContent();

    //ENOTNCES SE MARCA COMO LEIDOOOOOOOO ALTOKE
    assertTrue(ana.getBandejaEntrada().getEmails().get(0).isLeido());

    //luis no leyo su correo
    assertFalse(luis.getBandejaEntrada().getEmails().get(0).isLeido());

  }
  // creo que me da algo raro este test jeje (pero por ahora lo dejo asi) (xq me sale el continue)

  @Test
  public void testMarcarComoLeido() {
    // Crear remitente y destinatarios
    Contacto r1 = new Contacto("Carlos", "carlos@empresa.com");
    Contacto ana = new Contacto("Ana", "ana@empresa.com");
    Contacto luis = new Contacto("Luis", "luis@empresa.com");

    // Crear el correo
    Email email = new Email(
        "Ya es Viernes",
        "Hoy es viernes de cerveza.",
        r1);

    // Agregar destinatarios
    email.getRecipients().add(ana);
    email.getRecipients().add(luis);

    //clase que envía
    SendMail gestor = new SendMail();

    //enviar
    gestor.enviar(email, Arrays.asList(ana, luis));

    // verificar el estatus (sent)
    assertEquals("Sent", gestor.getStatus(), "El estado del correo debería ser 'Sent'");

    //guardado en bandeja de salida
    assertEquals(1, r1.getBandejaSalida().getEmails().size(),"El remitente debería tener un correo en su bandeja de salida");

    //verificar el guardado en bandeja de entrada de cada uno
    assertEquals(1, ana.getBandejaEntrada().getEmails().size(), "Ana debería tener un correo en su bandeja de entrada");
    assertEquals(1, luis.getBandejaEntrada().getEmails().size(),"Luis debería tener un correo en su bandeja de entrada");

    //verificar leido
    assertFalse(ana.getBandejaEntrada().getEmails().get(0).isLeido(),"El correo en la bandeja de Ana debería estar sin leer");
    assertFalse(luis.getBandejaEntrada().getEmails().get(0).isLeido(),"El correo en la bandeja de Luis debería estar sin leer");

    //ana abre el correo (o sea mira el contenido)
    ana.getBandejaEntrada().getEmails().get(0).getContent();

    //ENOTNCES SE MARCA COMO LEIDOOOOOOOO
    assertTrue(ana.getBandejaEntrada().getEmails().get(0).isLeido());

    //luis no leyo su correo
    assertFalse(luis.getBandejaEntrada().getEmails().get(0).isLeido());
  }

  @Test
  public void testCopiasIndependientesEnDestinatarios() {
    // remitente y destinatarios bla bla
    Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
    Contacto ana = new Contacto("Ana", "ana@empresa.com");
    Contacto luis = new Contacto("Luis", "luis@empresa.com");

    // crear email ORIGINALLLL
    Email email = new Email("Reunión", "La reunión es a las 10hs.", remitente);
    email.getRecipients().add(ana);
    email.getRecipients().add(luis);

    // enviar email
    SendMail gestor = new SendMail();
    gestor.enviar(email, Arrays.asList(ana, luis));

    // verificar que ambos recibieron el correo
    assertEquals(1, ana.getBandejaEntrada().getEmails().size());
    assertEquals(1, luis.getBandejaEntrada().getEmails().size());

    Email emailAna = ana.getBandejaEntrada().getEmails().get(0);
    Email emailLuis = luis.getBandejaEntrada().getEmails().get(0);

    //sirve para verificar que dos objetos no son la misma instancia en memoria,(copias, no la misma referencia)
    assertNotSame(emailAna, emailLuis, "Cada destinatario debe recibir una copia independiente del correo");

    // al principio, ninguno está leído
    assertFalse(emailAna.isLeido());
    assertFalse(emailLuis.isLeido());

    // ana abre el correo (lee su copia)
    emailAna.getContent(); // esto marca como leído

    // verificar que solo el de Ana se marcó como leído
    assertTrue(emailAna.isLeido(), "El correo de Ana debería estar marcado como leído");
    assertFalse(emailLuis.isLeido(), "El correo de Luis no debería afectarse, sigue sin leer");
    }

    //NO SE TOCAAAA ESTE TEST PERFE
  @Test
   public void testLeerContenido() {
   Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
   Contacto meli = new Contacto("Meli", "meli@empresa.com");
   Contacto marto = new Contacto("Luis", "luis@empresa.com");

   //Usuario meliUser = new Usuario("Meli", "meli@empresa.com", meli);
   Usuario martoUser = new Usuario("Luis", "luis@empresa.com", marto);

   //enviar el correo
   Email reunion = new Email("Reunión importante",
                            "Mañana a las 10am", remitente);
   
   reunion.getRecipients().add(meli);
   reunion.getRecipients().add(marto);

   SendMail gestor = new SendMail();
   gestor.enviar(reunion, Arrays.asList(meli, marto));

   assertTrue(remitente.getBandejaSalida().getEmails().contains(reunion));

   //correos clonados en bandejas de cada contacto
   Email emailMeli = meli.getBandejaEntrada().getEmails().get(0);
   Email emailMarto = marto.getBandejaEntrada().getEmails().get(0);

   assertNotSame(emailMeli, emailMarto);
   
   //verif bandeja de entrada de cada uno
   assertEquals(1, meli.getBandejaEntrada().getEmails().size());
   assertEquals(1, marto.getBandejaEntrada().getEmails().size());
   
   //verifico no leido
   assertFalse(emailMeli.isLeido());
   assertFalse(emailMarto.isLeido());
  
   //marto abre su correo
   martoUser.getContacto().getBandejaEntrada().getEmails().get(0).getContent();
   assertTrue(marto.getBandejaEntrada().getEmails().get(0).isLeido());
  
   //meli abre el correo
   meli.getBandejaEntrada().getEmails().get(0).getContent();
   assertTrue(emailMeli.isLeido(), "Meli marco como leído su correo");
 }

}
