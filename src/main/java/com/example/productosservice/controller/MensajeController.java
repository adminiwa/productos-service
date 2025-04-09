package com.example.productosservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensaje")
public class MensajeController {

    @Value("${mensaje:Mensaje por defecto}")
    private String mensaje;

    @GetMapping
    public String obtenerMensaje() {
        return mensaje;
    }
}
