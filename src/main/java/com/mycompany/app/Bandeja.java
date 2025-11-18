package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Bandeja {
  private List<Email> emails;
  
  
  public Bandeja() {
    this.emails = new ArrayList<>();
  }
  
  public void agregarEmail(Email email) { //o sea recibir
    emails.add(email);
  }

  public List<Email> getEmails() {
    return new ArrayList<>(emails);
  }
  
  /*public void limpiarBandeja() {
    emails.clear();
  } */

  /*public List<Email> buscarEmails(String texto) {
    List<Email> resultados = new ArrayList<>();
    String query = texto.toLowerCase();

    for (Email email : emails) {
      boolean coincide = (email.getSubject() != null && email.getSubject().toLowerCase().contains(query)) ||
          (email.getContent() != null && email.getContent().toLowerCase().contains(query)) ||
          (email.getSender() != null && email.getSender().toString().toLowerCase().contains(query)) ||
          (email.getRecipients() != null && email.getRecipients().toString().toLowerCase().contains(query));

      if (coincide) {
        resultados.add(email);
      }
    }

    return resultados;
  }*/

  //la bandeja es la que tiene la lista de emails, por lo tanto ella es la que sabe cómo recorrerlos y devolver los que cumplen la condición.
  public List<Email> buscarEmails(Predicate<Email> filtro) {
    return emails.stream()
                 .filter(filtro)
                 .collect(Collectors.toList());
}

  public void removerEmail(Email email) {
    emails.removeIf(e -> e == email);
  }

   // bandeja especial de favoritos
    public List<Email> getFavoritos() {
        return emails.stream()
                     .filter(Email::esFavorito)
                     .collect(Collectors.toList());
    }

    public List<Email> buscarFavoritos(Predicate<Email> filtro) {
        return getFavoritos().stream()
                             .filter(filtro)
                             .collect(Collectors.toList());
    }
}
