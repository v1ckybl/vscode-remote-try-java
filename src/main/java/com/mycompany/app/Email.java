package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

public class Email implements IMarcador {

  private String subject;
  private String content;
  private Contacto sender;
  private List<Contacto> recipients;
  private List<Contacto> ccRecipients;

  private boolean leido; //por defecto falso
  private boolean important;
  private boolean borrador;
  public boolean eliminado;
  private boolean favorito;


    //constructor sin destinatarios
  /*public Email(String subject, String content, Contacto sender) {
      this.subject = subject;
      this.content = content;
      this.sender = sender;
      this.recipients = new ArrayList<>();
      this.leido = false; // por defecto no leido obvio
      this.important = false; // por defecto no es importante
      this.borrador = false; // por defecto no es borrador
      this.eliminado = false; // por defecto no eliminado
      this.favorito = false; //por defecto no esta en fav
  }*/

  //constructor con destinatarios
  public Email(String subject, String content, Contacto sender, List<Contacto> recipients) {
    this.subject = subject;
    this.content = content;
    this.sender = sender;
    this.recipients = new ArrayList<>(recipients); // Crear copia para evitar modificaciones externas
    this.ccRecipients = new ArrayList<>(); // Inicializar la lista CC
    this.leido = false;
    this.important = false;
    this.borrador = false;
    this.eliminado = false;
    this.favorito = false;
    
  }
  
  //constructor para borrador 
    public Email(String subject, String content, Contacto sender, boolean isBorrador) {
    this.subject = subject;
    this.content = content;
    this.sender = sender;
    this.recipients = new ArrayList<>();
    this.ccRecipients = new ArrayList<>();
    this.leido = false;
    this.important = false;
    this.borrador = isBorrador;
    this.eliminado = false;
    this.favorito = false;
}
  //uso básico
  public void restaurar() {
      this.eliminado = false;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    this.marcarComoLeido(); //se marca automáticamente como leído al abrir el contenido
    return content;
  }

  public Contacto getSender() {
    return sender;
  }

  public List<Contacto> getRecipients() {
    return recipients;
  }
  
  public List<Contacto> getCcRecipients() {
    return ccRecipients;
  }

  @Override
  public void marcarComoLeido() {
      this.leido = true;
  }

  @Override
  public void marcarComoNoLeido() {
      this.leido = false;
  }

  @Override
  public boolean isLeido() {
    return leido;
  }

  public boolean isBorrador() {
    return borrador;
  }

  public boolean isImportant() {
    return important;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public Email copiar() {
      Email copia = new Email(this.getSubject(), this.getContent(), this.getSender(), this.getRecipients());   
      //copiar CC
      copia.getCcRecipients().addAll(this.getCcRecipients()); 
   
      //transferir estados
      /*copia.leido = this.leido;
      copia.important = this.important;
      copia.borrador = this.borrador;
      copia.eliminado = this.eliminado;*/
      return copia;
  }

  //setters para editar borradores
  public void setSubject(String subject) {
      this.subject = subject;
  }

  public void setContent(String content) {
      this.content = content;
  }

  // cambiar el estado de borrador a enviado
  public void marcarComoEnviado() {
      this.borrador = false;
  }

  public void marcarComoFavorito() {
    this.favorito = true;
  }

  public boolean esFavorito() {
    return favorito;
  }

}