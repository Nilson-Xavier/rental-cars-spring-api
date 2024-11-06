package com.challenge.rental_cars_spring_api.service;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.queries.dtos.ListarAlugueisQueryResultItem;
import com.challenge.rental_cars_spring_api.core.services.AluguelService;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.ClienteRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AluguelServiceTest {
    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private AluguelService aluguelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarAlugueis() {
        Aluguel aluguel = new Aluguel(1L, new Carro(), new Cliente(), Date.valueOf("2023-01-01"), Date.valueOf("2023-01-02"), new BigDecimal("100"), false);
        when(aluguelRepository.findAll()).thenReturn(Collections.singletonList(aluguel));

        List<ListarAlugueisQueryResultItem> alugueis = aluguelService.listarAlugueis();
        assertEquals(1, alugueis.size());
        assertEquals(new BigDecimal("100"), alugueis.get(0).valor());
    }

    @Test
    public void testCalcularTotalNaoPago() {
        Aluguel aluguel = new Aluguel(1L, new Carro(), new Cliente(), Date.valueOf("2023-01-01"), Date.valueOf("2023-01-02"), new BigDecimal("100"), false);
        when(aluguelRepository.findAll()).thenReturn(Collections.singletonList(aluguel));

        BigDecimal totalNaoPago = aluguelService.calcularTotalNaoPago();
        assertEquals(new BigDecimal("100"), totalNaoPago);
    }

    @Test
    public void testProcessarArquivo() throws Exception {
        String filePath = "path/to/test/file.rtn";
        Carro carro = new Carro(1L, "Modelo X", "2020", 4, 100, "Fabricante X", BigDecimal.valueOf(100));
        Cliente cliente = new Cliente(1L, "Cliente Y", "12345678901", "987654321", "11234567890");

        when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        doNothing().when(aluguelRepository).save(Mockito.any(Aluguel.class));

        aluguelService.processarArquivo(filePath);
        verify(aluguelRepository, atLeastOnce()).save(Mockito.any(Aluguel.class));
    }
}
