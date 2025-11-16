package com.mycompany.app;

import java.util.List;

public interface ISend {
    void enviar(Email email, List<Contacto> recipients);
}
