package com.mycompany.app;

public class Contacto {
    // el contacto contiene la información del nombre y correo electrónico de una persona. 
    // Este podrá ser utilizado tanto como remitente o como destinatario en un correo. 
    
    private String nombre;
    private String email;
    private Bandeja bandejaEntrada; 
    private Bandeja bandejaSalida;
    private Bandeja bandejaBorradores; 
    //esas bandejas para que cada Contacto tenga su propio historial.

    public Contacto() {
        this.bandejaEntrada = new Bandeja();
        this.bandejaSalida = new Bandeja();
        this.bandejaBorradores = new Bandeja();
    }

    public Contacto(String nombre, String email) { //agregar this bandeja de entrada para que cada contacto tenga su propia bandeja
        this.nombre = nombre;
        this.email = email;
        this.bandejaEntrada = new Bandeja();
        this.bandejaSalida = new Bandeja();
        this.bandejaBorradores = new Bandeja();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

     public Bandeja getBandejaEntrada() {
        return bandejaEntrada;
    }

    public Bandeja getBandejaSalida() {
      return bandejaSalida;
    }

    public Bandeja getBandejaBorradores() {
      return bandejaBorradores;
    }

    @Override
    public String toString() {
        return nombre + " <" + email + ">";
    }

    //metodo eliminar contacto, vacia la info de todo
    public void eliminarContacto() {
      this.nombre = null;
      this.email = null;
      this.bandejaEntrada = null;
      this.bandejaSalida = null;
      this.bandejaBorradores = null;
    }

}