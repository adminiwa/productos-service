package com.example.productosservice.controller;

import com.example.productosservice.model.Producto;
import com.example.productosservice.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private String obtenerToken() throws Exception {
        String requestBody = """
            {
                "username": "admin",
                "password": "1234"
            }
            """;

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return new ObjectMapper().readTree(response).get("token").asText();
    }

    @Test
    void testObtenerTodosLosProductos() throws Exception {
        String token = obtenerToken();

        Producto producto1 = new Producto(1L, "Producto A", 10.0, 5);
        Producto producto2 = new Producto(2L, "Producto B", 20.0, 10);

        when(productoService.listarTodos()).thenReturn(Arrays.asList(producto1, producto2));

        mockMvc.perform(get("/api/productos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Producto A")))
                .andExpect(jsonPath("$[1].nombre", is("Producto B")));
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        String token = obtenerToken();

        Producto producto = new Producto(1L, "Producto A", 10.0, 5);
        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Producto A")));
    }

    @Test
    void testCrearProducto() throws Exception {
        String token = obtenerToken();

        Producto producto = new Producto(null, "Producto Nuevo", 25.0, 15);
        Producto productoGuardado = new Producto(1L, "Producto Nuevo", 25.0, 15);

        when(productoService.guardar(any(Producto.class))).thenReturn(productoGuardado);

        mockMvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Producto Nuevo")));
    }

    @Test
    void testActualizarProducto() throws Exception {
        String token = obtenerToken();

        Producto productoExistente = new Producto(1L, "Producto Viejo", 20.0, 10);
        Producto productoActualizado = new Producto(1L, "Producto Actualizado", 30.0, 20);

        when(productoService.obtenerPorId(1L)).thenReturn(productoExistente);
        when(productoService.guardar(any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/api/productos/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Producto Actualizado")));
    }

    @Test
    void testEliminarProducto() throws Exception {
        String token = obtenerToken();

        Producto producto = new Producto(1L, "Producto A", 10.0, 5);
        when(productoService.obtenerPorId(1L)).thenReturn(producto);
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
