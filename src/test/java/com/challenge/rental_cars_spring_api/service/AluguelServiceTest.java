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
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Test
    @DisplayName("Listar Alugueis deve listar os alugueis")
    public void testListarAlugueis() {
        Aluguel aluguel = new Aluguel(1L, new Carro(), new Cliente(),
                Date.valueOf("2023-01-01"), Date.valueOf("2023-01-02"),
                new BigDecimal("100"), false);
        when(aluguelRepository.findAll()).thenReturn(Collections.singletonList(aluguel));
        List<ListarAlugueisQueryResultItem> alugueis = aluguelService.listarAlugueis();
        assertEquals(1, alugueis.size());
        assertEquals("100,00", alugueis.get(0).valor());
    }

    @Test
    @DisplayName("Calcular Total NaoPago deve calcular o total de alugueis não pagos")
    public void testCalcularTotalNaoPago() {
        Aluguel aluguel = new Aluguel(1L, new Carro(), new Cliente(), Date.valueOf("2023-01-01"),
                Date.valueOf("2023-01-02"), new BigDecimal("100"), false);
        when(aluguelRepository.findAll()).thenReturn(Collections.singletonList(aluguel));
        BigDecimal totalNaoPago = aluguelService.calcularTotalNaoPago();
        assertEquals(new BigDecimal("100.00"), totalNaoPago);
    }

    @Test
    @DisplayName("Processar Arquivo Existente deve processar o arquivo existente")
    public void testProcessarArquivoExistente() {
        String filePath = "/test.rtn";
        InputStream is = new ByteArrayInputStream("1".repeat(20).getBytes());
        Carro carro = new Carro(1L, "Modelo X", "2020", 4, 100,
                "Fabricante X", BigDecimal.valueOf(100));
        Cliente cliente = new Cliente(1L, "Cliente Y", "12345678901", "987654321",
                "11234567890");
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.newInputStream(Paths.get(filePath))).thenReturn(is);
            when(carroRepository.findById(anyLong())).thenReturn(Optional.of(carro));
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
            when(aluguelRepository.save(Mockito.any(Aluguel.class))).thenReturn(new Aluguel());
            aluguelService.processarArquivo(filePath);
        }
        verify(aluguelRepository, atLeastOnce()).save(Mockito.any(Aluguel.class));
    }

    @Test
    @DisplayName("Processar Arquivo Existente Carro Nao Encontrado " +
            "deve processar o arquivo existente com carro nao encontrado")
    public void testProcessarArquivoExistenteCarroNaoEncontrado() {
        String filePath = "/test.rtn";
        InputStream is = new ByteArrayInputStream("1".repeat(20).getBytes());
        Cliente cliente = new Cliente(1L, "Cliente Y", "12345678901", "987654321",
                "11234567890");
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.newInputStream(Paths.get(filePath))).thenReturn(is);
            when(carroRepository.findById(anyLong())).thenReturn(Optional.empty());
            aluguelService.processarArquivo(filePath);
        }
        verify(aluguelRepository, never()).save(Mockito.any(Aluguel.class));
    }

    @Test
    @DisplayName("Processar Arquivo Existente Cliente Nao Encontrado " +
            "deve processar o arquivo existente com cliente nao encontrado")
    public void testProcessarArquivoExistenteClienteNaoEncontrado() {
        String filePath = "/test.rtn";
        InputStream is = new ByteArrayInputStream("1".repeat(20).getBytes());
        Carro carro = new Carro(1L, "Modelo X", "2020", 4, 100,
                "Fabricante X", BigDecimal.valueOf(100));
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.newInputStream(Paths.get(filePath))).thenReturn(is);
            when(carroRepository.findById(anyLong())).thenReturn(Optional.of(carro));
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
            aluguelService.processarArquivo(filePath);
        }
        verify(aluguelRepository, never()).save(Mockito.any(Aluguel.class));
    }

    @Test
    @DisplayName("Processar Arquivo Exception deve processar o arquivo e lancar uma excecao")
    public void testProcessarArquivoException() {
        String filePath = "/test.rtn";
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.newInputStream(Paths.get(filePath))).
                    thenThrow(new RuntimeException("Mock Exception !"));
            aluguelService.processarArquivo(filePath);
        }
        verify(aluguelRepository, never()).save(Mockito.any(Aluguel.class));
    }
}
