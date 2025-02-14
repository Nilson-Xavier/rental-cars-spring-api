package com.challenge.rental_cars_spring_api.controller;

import com.challenge.rental_cars_spring_api.access.AluguelRestController;
import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.services.AluguelService;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AluguelRestController.class)
class AluguelRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AluguelService mockAluguelService;

    @MockBean
    private AluguelRepository mockAluguelRepository;

    @MockBean
    private CarroRepository mockCarroRepository;

    @Test
    @DisplayName("Processar Arquivo Existente deve processar o arquivo existente")
    public void testProcessarArquivoExistente() throws Exception {
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            doNothing().when(mockAluguelService).processarArquivo(anyString());
            mockMvc.perform(post("/alugueis/processar-arquivo")
                            .param("fileName", "test.rtn")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Arquivo processado com sucesso."));
        }
        verify(mockAluguelService, times(1)).processarArquivo(anyString());
    }

    @Test
    @DisplayName("Processar Arquivo Inexistente deve processar o arquivo inexistente")
    public void testProcessarArquivoInexistente() throws Exception {
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            var fileName = "test.rtn";
            mockMvc.perform(post("/alugueis/processar-arquivo")
                            .param("fileName", fileName)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Arquivo não encontrado: " + fileName));
        }
        verify(mockAluguelService, times(0)).processarArquivo(anyString());
    }

    @Test
    @DisplayName("Processar Arquivo Exception deve lancar uma excecao quando processar o arquivo")
    public void testProcessarArquivoException() throws Exception {
        try(MockedStatic<Files> mockFile = mockStatic(Files.class)) {
            mockFile.when(() -> Files.exists(any(Path.class))).thenThrow(new RuntimeException("Mock Exception !"));
            mockMvc.perform(post("/alugueis/processar-arquivo")
                            .param("fileName", "test.rtn")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Erro ao processar o arquivo."));
        }
        verify(mockAluguelService, times(0)).processarArquivo(anyString());
    }

    @Test
    @DisplayName("Listar Alugueis deve retornar os alugueis esperados")
    public void testListarAlugueis() throws Exception {
        Map<String, Object> responseMap = Map.of(
        "alugueis", Collections.emptyList(),
        "totalNaoPago", ZERO);
        doReturn(Collections.emptyList()).when(mockAluguelService).listarAlugueis();
        doReturn(ZERO).when(mockAluguelService).calcularTotalNaoPago();
        mockMvc.perform(get("/alugueis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alugueis").isArray())
                .andExpect(jsonPath("$.alugueis").isEmpty())
                .andExpect(jsonPath("$.totalNaoPago").value(0));
        verify(mockAluguelService, times(1)).listarAlugueis();
        verify(mockAluguelService, times(1)).calcularTotalNaoPago();
    }

    @Test
    @DisplayName("Listar Alugueis deve lancar uma excecao quando listar os alugueis")
    public void testListarAlugueisException() throws Exception {
        doThrow(new RuntimeException("Mock Exception !")).when(mockAluguelService).listarAlugueis();
        mockMvc.perform(get("/alugueis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.erro").isString())
                .andExpect(jsonPath("$.erro").value("Erro ao listar alugueis"));
        verify(mockAluguelService, times(1)).listarAlugueis();
        verify(mockAluguelService, times(0)).calcularTotalNaoPago();
    }
}