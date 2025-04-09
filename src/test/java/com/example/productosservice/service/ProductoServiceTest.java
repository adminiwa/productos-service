package com.example.productosservice.service;

import com.example.productosservice.model.Producto;
import com.example.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    private ProductoRepository productoRepository;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = Mockito.mock(ProductoRepository.class);
        productoService = new ProductoService(productoRepository);
    }

    @Test
    void testObtenerTodosLosProductos() {
        // Arrange
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Producto 1");
        p1.setPrecio(10.0);
        p1.setCantidad(5);

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Producto 2");
        p2.setPrecio(20.0);
        p2.setCantidad(3);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        List<Producto> productos = productoService.listarTodos();

        // Assert
        assertEquals(2, productos.size());
        assertEquals("Producto 1", productos.get(0).getNombre());
    }

    @Test
    void testObtenerProductoPorId() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Prueba");
        producto.setPrecio(15.0);
        producto.setCantidad(2);

        when(productoRepository.findById(1L)).thenReturn(java.util.Optional.of(producto));

        // Act
        Producto resultado = productoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Producto Prueba", resultado.getNombre());
    }
}
