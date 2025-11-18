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

    Email email = new Email("Prueba", "Hola, este es un correo de prueba", remitente, Arrays.asList(c1));

    //bandeja de entrada del destinatario
    Bandeja bandejaEntrada = new Bandeja();

    //simular que le llega un correo al destinatario
    bandejaEntrada.agregarEmail(email);

    assertEquals(1, bandejaEntrada.getEmails().size(), "La bandeja debería tener un correo recibido");
    assertEquals("Prueba", bandejaEntrada.getEmails().get(0).getSubject());
    assertFalse(bandejaEntrada.getEmails().get(0).isLeido(), "El correo recibido debería estar sin leer");
    assertEquals(remitente, bandejaEntrada.getEmails().get(0).getSender());
  }

  //NO SE TOCAAAA ESTE TEST
  @Test
  public void testLeerContenido() {
    Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
    Contacto meli = new Contacto("Meli", "meli@empresa.com");
    Contacto marto = new Contacto("Luis", "luis@empresa.com");

    //Usuario meliUser = new Usuario("Meli", "meli@empresa.com", meli);
    Usuario martoUser = new Usuario("Luis", "luis@empresa.com", marto);

    //enviar el correo
    Email reunion = new Email("Reunión importante",
        "Mañana a las 10am", remitente, Arrays.asList(meli, marto));

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
 
    @Test
  public void testCrearBorrador() {
    Contacto dualipa = new Contacto("Carlos", "dualipa@empresa.com");
    Usuario dualipaUser = new Usuario("Carlos User", "dualipa@empresa.com", dualipa);

    Email borrador = dualipaUser.crearBorrador(
        "Prueba borrador",
        "Este es el contenido inicial del borrador.");

    assertTrue(borrador.isBorrador());

    //borrador está en la bandeja de borradores del remitente
    assertEquals(1, dualipa.getBandejaBorradores().getEmails().size(),"El contacto debe tener 1 email en su bandeja de borradores.");
    assertTrue(dualipa.getBandejaBorradores().getEmails().contains(borrador));
  }

  @Test
  public void testEditarYEnviarBorrador() {
    Contacto dualipa = new Contacto("Dua Lipa", "dualipa@empresa.com");
    Usuario dualipaUser = new Usuario("Dua Lipa", "dualipa@empresa.com", dualipa);
    Contacto rodridepaul = new Contacto("Rodri De Paul", "rodridepaul@empresa.com");

    Email borrador = dualipaUser.crearBorrador(
        "Prueba borrador",
        "Cerveza el viernes.");

    //editar el borrador
    String nuevoTitulo = "Reunión de cerveza, Martes 10 AM";
    String nuevoContenido = "El viernes no, mejor el martes.";

    dualipaUser.editarBorrador(borrador, nuevoTitulo, nuevoContenido);

    assertEquals(nuevoTitulo, borrador.getSubject(), "El asunto del borrador debe haberse actualizado.");
    assertEquals(nuevoContenido, borrador.getContent(), "El contenido del borrador debe haberse actualizado.");
    assertTrue(borrador.isBorrador());

    //ahora enviamos el borrador
    dualipaUser.enviarBorrador(borrador, Arrays.asList(rodridepaul));
    assertFalse(borrador.isBorrador());
    
    //borradores vaciooo
    assertEquals(0, dualipa.getBandejaBorradores().getEmails().size());
    
    //email llegó al destinatario (rodridepaul)
    assertEquals(1, rodridepaul.getBandejaEntrada().getEmails().size());
    
    //copia recibida por rodridepaul contiene el contenido editado
    Email emailRecibido = rodridepaul.getBandejaEntrada().getEmails().get(0);
    assertNotSame(borrador, emailRecibido);
    assertEquals("El viernes no, mejor el martes.", emailRecibido.getContent());
  }

  @Test
public void testMarcarComoLeidoYNoLeidoConCCO() {
    Contacto aylen = new Contacto("Aylen", "aylen@empresa.com");
    Contacto piccolini = new Contacto("Pato Pico", "pato@empresa.com");
    Contacto fercho = new Contacto("Fercho", "fercho@empresa.com");
    Contacto jaqui = new Contacto("Jaqui CCO", "jaqui@empresa.com"); //CC
    
    Usuario piccoliniUser = new Usuario("Pato Pico", "pato@empresa.com", piccolini);
    
    //enviar el correo
    Email email = new Email("Reunión importante",
                            "Mañana a las 10am", 
                            aylen, 
                            Arrays.asList(fercho, piccolini));
                            //destinatario CCO 
                            email.getCcRecipients().add(jaqui); 
    
    SendMail gestor = new SendMail();
    // la lista de recipients SÓLO debe incluir TO/PARA. El CCO lo maneja el email.
    gestor.enviar(email, java.util.Arrays.asList(fercho, piccolini));
    
    assertEquals(1, piccolini.getBandejaEntrada().getEmails().size(), "Pato recibió el correo TO.");
    assertEquals(1, fercho.getBandejaEntrada().getEmails().size(), "Fercho recibió el correo TO.");
    assertEquals(1, jaqui.getBandejaEntrada().getEmails().size(), "Jaqui recibió el correo CCO."); 
    
    Email emailPato = piccolini.getBandejaEntrada().getEmails().get(0);
    Email emailJaqui = jaqui.getBandejaEntrada().getEmails().get(0); 
    
    //pato NO debe ver a jaqui en destinatarios
    assertFalse(emailPato.getRecipients().contains(jaqui),"ERROR CCO: pato (TO) NO debe ver a jaqui (CCO) en la lista de destinatarios.");
        
    // Ambos no deben tener la lista CCO adjunta
    assertTrue(emailPato.getCcRecipients().isEmpty(), "La lista CCO de Pato debe estar vacía.");
    assertTrue(emailJaqui.getCcRecipients().isEmpty(), "La lista CCO de Jaqui debe estar vacía.");
}

}



