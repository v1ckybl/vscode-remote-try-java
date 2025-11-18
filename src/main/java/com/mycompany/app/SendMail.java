package com.mycompany.app;

import java.util.List;

public class SendMail implements ISend {
    private String status = "Pending";

    public String getStatus() {
      return status;
    }

    @Override
    public void enviar(Email email, List<Contacto> recipients) {
    Contacto remitente = email.getSender();

    //guarda el correo en la bandeja de salida del remitente
    remitente.getBandejaSalida().agregarEmail(email);

    //cada destinatario PARA recibe su propia copia del email   
    for (Contacto destinatario : recipients) {
      Email copia = email.copiar();
      destinatario.getBandejaEntrada().agregarEmail(copia);
      copia.getCcRecipients().clear();
    } 
    //destinatarios cc
    for (Contacto destinatarioCC : email.getCcRecipients()) {
      Email copiaCC = email.copiar();
      destinatarioCC.getBandejaEntrada().agregarEmail(copiaCC);
      //eliminar la lista CC de la copia enviada
      copiaCC.getCcRecipients().clear();
    }
    
    this.status = "Sent";
}

}

