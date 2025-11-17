package com.mycompany.app;


public class Usuario implements IMarcador {
    private String nombre;
    private String email;
    private Contacto contacto; // Relación Usuario -> Contacto
    private boolean leido;

    public Usuario(String nombre, String email, Contacto contacto) {
        this.nombre = nombre;
        this.email = email;
        this.contacto = contacto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    // Acceder al contacto asociado
    public Contacto getContacto() {
        return contacto;
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

    // Usuario puede eliminar emails de su contacto asociado
    public void eliminarEmail(Email email) {
        email.eliminado = true; // Marcar como eliminado
        // Opcional: remover físicamente de las bandejas
        if (contacto != null) {
            contacto.getBandejaEntrada().getEmails().removeIf(e -> e.eliminado);
        }
    }

    // Usuario puede restaurar emails
    public void restaurarEmail(Email email) {
        email.restaurar(); // Usar método existente en Email
    }

    
}
