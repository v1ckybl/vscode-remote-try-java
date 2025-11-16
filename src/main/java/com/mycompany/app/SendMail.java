package com.mycompany.app;

import java.util.List;

public class SendMail {
    private String status = "Pending";

    public String getStatus() {
      return status;
    }

    @Override
    public void enviar(Email email, List<Contacto> recipients) {
    Contacto remitente = email.getSender();

    //guarda el correo en la bandeja de salida del remitente
    remitente.getBandejaSalida().agregarEmail(email);

    //guarda el correo en la bandeja de entrada de cada destinatario
    // CADA DESTINATARIO RECIBE SU PROPIA COPIA DEL EMAIL
    
    for (Contacto destinatario : recipients) {
      
      Email copia = email.copiar();
      
        destinatario.getBandejaEntrada().agregarEmail(copia);
    }

    this.status = "Sent";
    }
}
