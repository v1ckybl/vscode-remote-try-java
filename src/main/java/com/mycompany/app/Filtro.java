package com.mycompany.app;
import java.util.List;
import java.util.function.Predicate;
 
public class Filtro {
    //busqueda de email RF4
    public static Predicate<Email> porTextoLibre(String texto) {
        String query = texto.toLowerCase();

        return email ->
            (email.getSubject() != null && email.getSubject().toLowerCase().contains(query)) ||
            (email.getContent() != null && email.getContent().toLowerCase().contains(query)) ||
            (email.getSender() != null && email.getSender().toString().toLowerCase().contains(query)) ||
            (email.getRecipients() != null &&
                 email.getRecipients().toString().toLowerCase().contains(query));
    }


    //RF5
    //1. filtra emails no leídos
    public List<Email> noLeidosEnInbox(List<Email> emails) {
        return emails.stream()
            .filter(email -> !email.isLeido())
            .toList();
    }

    //2. ejemplo de filtro para emails de dominio ucp.com
    public List<Email> filtroDominioUCP(List<Email> emails) {
    return emails.stream()
        .filter(email -> email.getSender().getEmail().endsWith("@ucp.com"))
        .toList();
  }

  //3. filtro simple: asunto contiene una palabra
    public static Predicate<Email> asuntoContiene(String palabra) {
        String q = palabra.toLowerCase();
        return email ->
            email.getSubject() != null &&
            email.getSubject().toLowerCase().contains(q);
    }

    //4. filtro complejo: dominio y texto
    public List<Email> filtroDominioYTexto(List<Email> emails, String dominio, String texto) {
    String query = texto.toLowerCase();

    return emails.stream()
        .filter(email ->
            email.getSender().getEmail().endsWith("@" + dominio) &&
            (
                (email.getSubject() != null && email.getSubject().toLowerCase().contains(query)) ||
                (email.getContent() != null && email.getContent().toLowerCase().contains(query))
            )
        )
        .toList();
    }

    //5. combina no leido + cantidad de destinatarios
    public List<Email> filtroNoLeidosYPara(List<Email> emails, String emailDestino) {
    String destinoLower = emailDestino.toLowerCase();

    return emails.stream()
        .filter(email ->
            !email.isLeido() &&
            email.getRecipients().stream()
                .anyMatch(rec -> rec.getEmail().toLowerCase().equals(destinoLower))
        )
        .toList();
    }

    //método genérico para usar cualquier predicado
    public List<Email> filtrar(List<Email> emails, Predicate<Email> condicion) {
        return emails.stream()
            .filter(condicion)
            .toList();
    }

    //esto q esta abajo lo dejo por las dudas xq ya estaba
/*
  //predicado para filtrar emails por cantidad mínima de destinatarios
  private Predicate<Email> tieneDestinatariosMayorOIgualA(int cantidad) {
        return email -> email.getRecipients().size() >= cantidad;
  }*/

//filtro genérico por dominio
  public List<Email> filtrarPorDominio(List<Email> emails, String dominio) {
    return emails.stream()
        .filter(email -> email.getSender().getEmail().endsWith("@" + dominio))
        .toList();
  }

  
}