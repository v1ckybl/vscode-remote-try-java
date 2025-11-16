package com.mycompany.app;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BandejaTest {
     @Test
    public void testBuscarEmailsEnBandejaDeEntrada() {
      // Crear contactos
      Contacto remitente = new Contacto("Carlos", "carlos@empresa.com");
      Contacto ana = new Contacto("Ana", "ana@empresa.com");
      Contacto luis = new Contacto("Luis", "luis@empresa.com");

      // bandeja de entrada de Ana
      Bandeja bandejaEntrada = ana.getBandejaEntrada();

      // crear y agregar correos simulados
      Email correo1 = new Email("Reunión semanal", "Recordatorio de reunión el lunes a las 10:00.", remitente);
      correo1.getRecipients().add(ana);

      Email correo2 = new Email("Informe mensual", "Por favor enviar el informe antes del viernes.", remitente);
      correo2.getRecipients().add(ana);

      Email correo3 = new Email("Festejo", "Luis invita a un asado el sábado.", luis);
      correo3.getRecipients().add(ana);

      bandejaEntrada.agregarEmail(correo1);
      bandejaEntrada.agregarEmail(correo2);
      bandejaEntrada.agregarEmail(correo3);

      // aca probamos las busquedassss

      // buscar por asunto
      List<Email> resultadoAsunto = bandejaEntrada.buscarEmails("reunión");
      assertEquals(1, resultadoAsunto.size(), "Debería encontrar 1 correo con 'reunión' en el asunto");
      assertEquals("Reunión semanal", resultadoAsunto.get(0).getSubject());

      // buscar por contenido
      List<Email> resultadoContenido = bandejaEntrada.buscarEmails("informe");
      assertEquals(1, resultadoAsunto.size(), "Debería encontrar 1 correo con 'reunión' en el asunto");
      assertEquals("Informe mensual", resultadoContenido.get(0).getSubject());

      // buscar por remitente
      List<Email> resultadoRemitente = bandejaEntrada.buscarEmails("carlos@empresa.com");
      assertEquals(2, resultadoRemitente.size(), "Debería encontrar 2 correos enviados por Carlos");

      // buscar por destinatario
      List<Email> resultadoDestinatario = bandejaEntrada.buscarEmails("ana@empresa.com");
      assertEquals(3, resultadoDestinatario.size(), "Ana debería aparecer como destinataria en los 3 correos");

      // buscar texto inexistente
      List<Email> resultadoVacio = bandejaEntrada.buscarEmails("vacaciones");
      assertTrue(resultadoVacio.isEmpty(), "No debería encontrar correos con el texto 'vacaciones'");
    }
    
    @Test
    public void testIsEliminado() {

      Contacto r1 = new Contacto("Carlos", "carlos@empresa.com");
      Contacto martu = new Contacto("Ana", "ana@empresa.com");
      Usuario martuUser = new Usuario("Martu", "martu@empresa.com");

      // Crear el correo
      Email email = new Email(
          "Ya es Viernes",
          "Hoy es viernes de cerveza.",
          r1);

      // Agregar destinatarios
      email.getRecipients().add(martu);

      //clase que envía
      SendMail gestor = new SendMail();

      //enviar
      gestor.enviar(email, Arrays.asList(martu));

      // verificar status sent y que se guardo en bandeja de salida
      assertEquals("Sent", gestor.getStatus(), "El estado del correo debería ser 'Sent'");
      assertEquals(1, r1.getBandejaSalida().getEmails().size(),
          "El remitente debería tener un correo en su bandeja de salida");

      martuUser.eliminarEmail(email);
      assertTrue(martuUser.getBandejaEntrada().getEmails().get(0).isEliminado());

    }

    @Test
    public void testFiltroEmailsUCP() {
        // Crear emails de diferentes dominios
        Contacto laura = new Contacto("Laura", "laura@ucp.com");
        Contacto aylen = new Contacto("Aylen", "aylen@gmail.com");
        Contacto vick = new Contacto("Vick", "vick@ucp.com");
        
        Email email1 = new Email("Test UCP 1", "Contenido", laura);
        Email email2 = new Email("Test Gmail", "Contenido", aylen);
        Email email3 = new Email("Test UCP 2", "Contenido", vick);
        
        List<Email> todosLosEmails = Arrays.asList(email1, email2, email3);
        
        // Usar el filtro
        Filtro filtro = new Filtro();
        List<Email> emailsUCP = filtro.filtroDominioUCP(todosLosEmails);
        
        // Verificar resultados
        assertEquals(2, emailsUCP.size(), "Debería haber 2 emails de UCP");
        assertTrue(emailsUCP.contains(email1), "Debería incluir email1");
        assertTrue(emailsUCP.contains(email3), "Debería incluir email3");
        assertFalse(emailsUCP.contains(email2), "No debería incluir email de Gmail");
    }

    @Test
    public void testNoLeidosEnInbox() {
    Contacto remitente = new Contacto("Carlos", "carlos@ucp.com");
    Email e1 = new Email("Tema 1", "Sin leer", remitente);
    Email e2 = new Email("Tema 2", "Ya leído", remitente);
    e2.marcarComoLeido();

    List<Email> emails = Arrays.asList(e1, e2);

    Filtro filtro = new Filtro();
    List<Email> resultado = filtro.noLeidosEnInbox(emails);

    assertEquals(1, resultado.size());
    assertFalse(resultado.get(0).isLeido(), "El correo debería estar sin leer");
  }

  @Test
  public void testFiltrarConPredicado() {
    Contacto remitente = new Contacto("Laura", "laura@ucp.com");

    Email e1 = new Email("Reunión urgente", "Recordatorio de reunión", remitente);
    Email e2 = new Email("Comunicado", "Nada importante", remitente);

    List<Email> emails = Arrays.asList(e1, e2);

    Filtro filtro = new Filtro();

    // Predicado: el asunto contiene la palabra "reunión"
    List<Email> resultado = filtro.filtrar(emails, e -> e.getSubject().toLowerCase().contains("reunión"));

    assertEquals(1, resultado.size());
    assertEquals("Reunión urgente", resultado.get(0).getSubject());
  }
//como q no entiendo mucho los filtros pero bueno aca hay dos test más

}
