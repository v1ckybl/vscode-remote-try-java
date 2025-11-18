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
  Email correo1 = new Email("Reunión semanal", "Recordatorio de reunión el lunes a las 10:00.", remitente, Arrays.asList(ana));
  Email correo2 = new Email("Informe mensual", "Por favor enviar el informe antes del viernes.", remitente, Arrays.asList(ana));
  Email correo3 = new Email("Festejo", "Luis invita a un asado el sábado.", luis, Arrays.asList(ana));

  bandejaEntrada.agregarEmail(correo1);
  bandejaEntrada.agregarEmail(correo2);
  bandejaEntrada.agregarEmail(correo3);

  // buscar por asunto
  List<Email> resultadoAsunto = bandejaEntrada.buscarEmails(Filtro.porTextoLibre("reunión"));
  assertEquals(1, resultadoAsunto.size(), "Debería encontrar 1 correo con 'reunión' en el asunto");
  assertEquals("Reunión semanal", resultadoAsunto.get(0).getSubject());

  // buscar por contenido
  List<Email> resultadoContenido = bandejaEntrada.buscarEmails(Filtro.porTextoLibre("informe"));
  assertEquals(1, resultadoContenido.size(), "Debería encontrar 1 correo con 'informe' en el contenido");
  assertEquals("Informe mensual", resultadoContenido.get(0).getSubject());

  // buscar por remitente
  List<Email> resultadoRemitente = bandejaEntrada.buscarEmails(Filtro.porTextoLibre("carlos@empresa.com"));
  assertEquals(2, resultadoRemitente.size(), "Debería encontrar 2 correos enviados por Carlos");

  // buscar por destinatario
  List<Email> resultadoDestinatario = bandejaEntrada.buscarEmails(Filtro.porTextoLibre("ana@empresa.com"));
  assertEquals(3, resultadoDestinatario.size(), "Ana debería aparecer como destinataria en los 3 correos");

  // buscar texto inexistente
  List<Email> resultadoVacio = bandejaEntrada.buscarEmails(Filtro.porTextoLibre("vacaciones"));
  assertTrue(resultadoVacio.isEmpty(), "No debería encontrar correos con el texto 'vacaciones'");
  }

    @Test
    public void testUsuarioEliminaEmail() {

      Contacto r1 = new Contacto("Carlos", "carlos@empresa.com");
      Contacto martu = new Contacto("Martu", "martu@empresa.com");
      Usuario martuUser = new Usuario("Martu", "martu@empresa.com", martu);

      //crear el correo
      Email emailVierne = new Email(
          "Ya es Viernes",
          "Hoy es viernes de cerveza.",
          r1, Arrays.asList(martu));

      //clase que envía
      SendMail gestor = new SendMail();

      //enviar
      gestor.enviar(emailVierne, Arrays.asList(martu));

      //email clonado en la bandeja de martu
      Email emailDeMartu = martu.getBandejaEntrada().getEmails().get(0);
      
      //verificar que el email le llegó a martu
      assertEquals(1, martu.getBandejaEntrada().getEmails().size(),"Martu debería tener un correo en su bandeja de entrada");
      assertTrue(martu.getBandejaEntrada().getEmails().contains(emailDeMartu));
      
      //eliminar usando el usuario
      martuUser.eliminarEmail(emailDeMartu);
      assertTrue(emailDeMartu.isEliminado());
      
      //verificar que ya no está en la bandeja
      assertFalse(martu.getBandejaEntrada().getEmails().contains(emailDeMartu));
    }

    @Test
    public void testFiltroEmailsUCP() {
        // Crear emails de diferentes dominios
        Contacto laura = new Contacto("Laura", "laura@ucp.com");
        Contacto aylen = new Contacto("Aylen", "aylen@gmail.com");
        Contacto vick = new Contacto("Vick", "vick@ucp.com");
        
        Email email1 = new Email("Test UCP 1", "Contenido", laura, Arrays.asList());
        Email email2 = new Email("Test Gmail", "Contenido", aylen, Arrays.asList());
        Email email3 = new Email("Test UCP 2", "Contenido", vick, Arrays.asList());
        
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
    Email e1 = new Email("Tema 1", "Sin leer", remitente, Arrays.asList());
    Email e2 = new Email("Tema 2", "Ya leído", remitente, Arrays.asList());
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

    Email e1 = new Email("Reunión urgente", "Recordatorio de reunión", remitente, Arrays.asList());
    Email e2 = new Email("Comunicado", "Nada importante", remitente, Arrays.asList());

    List<Email> emails = Arrays.asList(e1, e2);

    Filtro filtro = new Filtro();

    // Predicado: el asunto contiene la palabra "reunión"
    List<Email> resultado = filtro.filtrar(emails, e -> e.getSubject().toLowerCase().contains("reunión"));

    assertEquals(1, resultado.size());
    assertEquals("Reunión urgente", resultado.get(0).getSubject());
  }
//como q no entiendo mucho los filtros pero bueno aca hay dos test más

  @Test
  public void testFiltroDominioYTextoConCantantes() {
    // crear contactos 
    SendMail gestor = new SendMail();
    Contacto shakira = new Contacto("Shakira", "shakira@pop.com");
    Contacto badbunny = new Contacto("Bad Bunny", "badbunny@trap.com");
    Contacto taylor = new Contacto("Taylor Swift", "taylor@pop.com");

    // crear correos 
    Email correo1 = new Email("Nuevo hit", "Shakira lanza 'Chantaje'", shakira, List.of(taylor));
    Email correo2 = new Email("Colaboración", "Bad Bunny propone cancion con Shakira", badbunny, List.of(shakira));
    Email correo3 = new Email("Gira mundial", "Taylor anuncia tour con 87 fechas y 3 cambios de vestuario por show", taylor, List.of(shakira));
    Email correo4 = new Email("Nuevo hit", "Bad Bunny lanza 'ojitos lindos'", badbunny, List.of(taylor));
    Email correo5 = new Email("Receta de empanadas", "Shakira comparte su secreto para la masa perfecta", shakira, List.of(badbunny));
    
    gestor.enviar(correo1, Arrays.asList(taylor));
    gestor.enviar(correo2, Arrays.asList(shakira));
    gestor.enviar(correo3, Arrays.asList(shakira));
    gestor.enviar(correo4, Arrays.asList(taylor));
    gestor.enviar(correo5, Arrays.asList(badbunny));

    //lista de la bandeja de cada destinatario
    List<Email> bandejaDeShakira = shakira.getBandejaEntrada().getEmails();
    List<Email> bandejaDeBadbunny = badbunny.getBandejaEntrada().getEmails();

    // filtrar por dominio y texto
    List<Email> resultado = new Filtro().filtroDominioYTexto(bandejaDeShakira, "pop.com", "gira");

    // verficarrrr
    assertEquals(1, resultado.size(), "Debe encontrar solo 1 correo de pop.com con 'gira'");
    assertEquals("Gira mundial", resultado.get(0).getSubject());

    // otro caso nmsss con "trap.com" y texto "conejo"
    List<Email> resultado2 = new Filtro().filtroDominioYTexto(bandejaDeBadbunny, "pop.com", "empanadas");
    assertEquals(1, resultado2.size(), "Debe encontrar 1 correo de trap.com con 'empanadas'");
    assertEquals("Receta de empanadas", resultado2.get(0).getSubject());
  }

  @Test
  public void testFiltroNoLeidosYParaConCantantes() {
    SendMail gestor = new SendMail();
    Contacto daddy = new Contacto("Daddy Yankee", "daddy@reggaeton.com");
    Contacto karol = new Contacto("Karol G", "karol@reggaeton.com");
    Contacto jbalvin = new Contacto("J Balvin", "balvin@colombia.com");
    Contacto rosalia = new Contacto("Rosalía", "rosalia@motomami.com");
    Usuario karolUser = new Usuario("Karol User", "karol@reggaeton.com", karol);

    Email correo1 = new Email("Gasolina remix", "Daddy quiere reversionar el clásico con Karol", daddy, List.of(karol));
    Email correo2 = new Email("Motomami tour", "Rosalía invita a Karol a abrir su show", rosalia, List.of(karol)); 
    Email correo3 = new Email("Colaboración", "J Balvin propone tema con Karol y Rosalía", jbalvin, List.of(karol, rosalia)); 
    Email correo4 = new Email("Fiesta privada", "Daddy organiza fiesta con reggaetón clásico", daddy, List.of(rosalia));
   
    gestor.enviar(correo1, Arrays.asList(karol));
    gestor.enviar(correo2, Arrays.asList(karol));
    gestor.enviar(correo3, Arrays.asList(karol, rosalia));
    gestor.enviar(correo4, Arrays.asList(rosalia));

    
    List<Email> bandejaDeKarol = karol.getBandejaEntrada().getEmails();
    //List<Email> todosLosCorreos = List.of(correo1, correo2, correo3, correo4);
    Email copiaCorreo3Karol = bandejaDeKarol.get(2);
    karolUser.marcarComoLeido(copiaCorreo3Karol);


    List<Email> resultadoKarol = new Filtro().filtroNoLeidosYPara(bandejaDeKarol, "karol@reggaeton.com");
    assertEquals(2, resultadoKarol.size(), "Karol debería tener 2 correos no leídos.");

    /*List<Email> resultadoRosalia = new Filtro().filtroNoLeidosYPara(todosLosCorreos, "rosalia@motomami.com");
    assertEquals(1, resultadoRosalia.size(), "Rosalía solo tiene 1 correo no leído, el de Daddy Yankee");*/
  }

  @Test
  public void testMarcarComoFavorito() {
    Contacto lali = new Contacto("Lali", "lali@pop.com");
    Contacto tini = new Contacto("Tini", "tini@pop.com");
    Usuario laliUser = new Usuario("Lali User", "lali@pop.com", lali);
    SendMail gestor = new SendMail();

    Bandeja bandejalali = lali.getBandejaEntrada();

    Email correo1 = new Email("Nuevo single", "Tini lanza 'buenos aires'", tini, List.of(lali));
    Email correo2 = new Email("Fiesta", "Invitación a fiesta", tini, List.of(lali));
    Email correo3 = new Email("Gira", "Fechas confirmadas para el tour Futttura", tini, List.of(lali));

    gestor.enviar(correo1, Arrays.asList(lali));
    gestor.enviar(correo2, Arrays.asList(lali));
    gestor.enviar(correo3, Arrays.asList(lali));
    //correos clonados en bandejas de cada contacto
    Email emailLali = lali.getBandejaEntrada().getEmails().get(0);
    Email email3Lali = lali.getBandejaEntrada().getEmails().get(2);
    
    laliUser.marcarComoFavorito(emailLali);
    laliUser.marcarComoFavorito(email3Lali);

    List<Email> favoritos = bandejalali.getFavoritos();
    assertEquals(2, favoritos.size(), "Lali debería tener 2 correos favoritos");

    List<Email> resultado = bandejalali.buscarFavoritos(Filtro.porTextoLibre("gira"));
    assertEquals(1, resultado.size(), "Debería encontrar 1 favorito con 'gira'");
    assertEquals("Gira", resultado.get(0).getSubject());
  }

  @Test
  public void testFiltrarPorDominioTrap() {
    Contacto lali = new Contacto("Lali", "lali@pop.com");
    Contacto tini = new Contacto("Tini", "tini@trap.com");
    Contacto maria = new Contacto("María Becerra", "maria@trap.com");
    Contacto nicki = new Contacto("Nicki", "nicki@reggaeton.com");

    Email correo1 = new Email("Nuevo tema", "Lali propone colaboración", lali, List.of(tini));
    Email correo2 = new Email("Gira", "Fechas confirmadas", tini, List.of(lali));
    Email correo3 = new Email("Remix", "María quiere sumar a Nicki", maria, List.of(nicki));
    Email correo4 = new Email("Fiesta", "Nicki invita a todas", nicki, List.of(lali, tini));

    List<Email> todos = List.of(correo1, correo2, correo3, correo4);

    List<Email> resultado = new Filtro().filtrarPorDominio(todos, "trap.com");

    assertEquals(2, resultado.size(), "Debería encontrar 2 correos enviados desde el dominio trap.com");
    assertTrue(resultado.contains(correo2), "El correo de Tini debería estar incluido");
    assertTrue(resultado.contains(correo3), "El correo de María debería estar incluido");
  }

  @Test
  public void testFiltrarPorPalabraEnAsunto() {
  Contacto lali = new Contacto("Lali", "lali@pop.com");
  Contacto tini = new Contacto("Tini", "tini@trap.com");
  Contacto maria = new Contacto("María Becerra", "maria@trap.com");
  Contacto nicki = new Contacto("Nicki", "nicki@reggaeton.com");

  Email correo1 = new Email("Nuevo tema", "Lali propone colaboración", lali, List.of(tini));
  Email correo2 = new Email("Gira importante", "Fechas confirmadas", tini, List.of(lali));
  Email correo3 = new Email("Remix explosivo", "María quiere sumar a Nicki", maria, List.of(nicki));
  Email correo4 = new Email("Fiesta", "Nicki invita a todas", nicki, List.of(lali, tini));

  List<Email> todos = List.of(correo1, correo2, correo3, correo4);

  List<Email> resultado = new Filtro().filtrar(todos, Filtro.asuntoContiene("importante"));

  assertEquals(1, resultado.size(), "Debería encontrar 1 correo con 'importante' en el asunto");
  assertTrue(resultado.contains(correo2), "El correo con asunto 'Gira importante' debería estar incluido");
}

}


