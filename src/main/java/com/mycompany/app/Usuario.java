package com.mycompany.app;

import java.util.List;

public class Usuario {
  private String nombre;
  private String email;
  private Contacto contacto; // Relación Usuario -> Contacto
  private boolean leido;

  public Usuario(String nombre, String email, Contacto contacto) {
      this.nombre = nombre;
      this.email = email;
      this.contacto = contacto;
  }

  public Usuario(String nombre2, String email2) {
    this.nombre = nombre2;
    this.email = email2;
  }

  public String getNombre() {
      return nombre;
  }

  public String getEmail() {
      return email;
  }

  /*public void setNombre(String nombre) {
      this.nombre = nombre;
  }

  public void setEmail(String email) {
    this.email = email;
  }*/

  // Acceder al contacto asociado
  public Contacto getContacto() {
      return contacto;
  }

  public void marcarComoLeido(Email email) {
      email.marcarComoLeido();
  }

  public void marcarComoNoLeido(Email email) {
    email.marcarComoNoLeido();
  }
  

  // Usuario puede eliminar emails de su contacto asociado
  public void eliminarEmail(Email email) {
    email.eliminado = true; // Marcar como eliminado 
    contacto.getBandejaEntrada().removerEmail(email); 
  }

  public void restaurarEmail(Email email) {
    email.restaurar(); 
    contacto.getBandejaEntrada().agregarEmail(email);
  }
    
/*    public void eliminarEmail(Email email) {
    email.marcarComoEliminado();
    this.contacto.getBandejaEntrada().removerEmail(email);
} */
   
  public Email crearBorrador(String subject, String content) {
    Email borrador = new Email(subject, content, this.contacto, true);

    this.contacto.getBandejaBorradores().agregarEmail(borrador);
    return borrador;
  }

  public void editarBorrador(Email borrador, String newSubject, String newContent) {
      borrador.setSubject(newSubject);
      borrador.setContent(newContent);
  }
  
  public void enviarBorrador(Email borrador, List<Contacto> recipients) {
    borrador.marcarComoEnviado(); 
    //volar el borrador 
    this.contacto.getBandejaBorradores().removerEmail(borrador);
    //destinatarios
    borrador.getRecipients().addAll(recipients);
    
    //enviar
    SendMail gestor = new SendMail();
    gestor.enviar(borrador, recipients);
}

}