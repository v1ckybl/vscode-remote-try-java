package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

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

  public List<Email> buscarEmails(String texto) {
        List<Email> resultados = new ArrayList<>();
        String query = texto.toLowerCase();

        for (Email email : emails) {
            boolean coincide =
                (email.getSubject() != null && email.getSubject().toLowerCase().contains(query)) ||
                (email.getContent() != null && email.getContent().toLowerCase().contains(query)) ||
                (email.getSender() != null && email.getSender().toString().toLowerCase().contains(query)) ||
                (email.getRecipients() != null && email.getRecipients().toString().toLowerCase().contains(query));

            if (coincide) {
                resultados.add(email);
            }
        }

        return resultados;
    }
}
