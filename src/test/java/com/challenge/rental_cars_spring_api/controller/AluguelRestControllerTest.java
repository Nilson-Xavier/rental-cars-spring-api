package com.challenge.rental_cars_spring_api.controller;

import com.challenge.rental_cars_spring_api.access.AluguelRestController;
import com.challenge.rental_cars_spring_api.core.services.AluguelService;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AluguelRestController.class)
public class AluguelRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AluguelService aluguelService;

    @MockBean
    private AluguelRepository aluguelRepository;

    @MockBean
    private CarroRepository carroRepository;

    @MockBean
    private ListarAlugueisQuery listarAlugueisQuery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AluguelRestController(aluguelService, aluguelRepository, listarAlugueisQuery)).build();
    }

    @Test
    public void testProcessarArquivo() throws Exception {
        String fileName = "test.rtn";

        doNothing().when(aluguelService).processarArquivo(anyString());

        mockMvc.perform(post("/alugueis/processar-arquivo")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo processado com sucesso."));

        verify(aluguelService, times(1)).processarArquivo(anyString());
    }

    @Test
    public void testListarAlugueis() throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("alugueis", Collections.emptyList());
        responseMap.put("totalNaoPago", BigDecimal.ZERO);

        when(aluguelService.listarAlugueis()).thenReturn(Collections.emptyList());
        when(aluguelService.calcularTotalNaoPago()).thenReturn(BigDecimal.ZERO);

        mockMvc.perform(get("/alugueis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alugueis").isArray())
                .andExpect(jsonPath("$.alugueis").isEmpty())
                .andExpect(jsonPath("$.totalNaoPago").value(0));

        verify(aluguelService, times(1)).listarAlugueis();
        verify(aluguelService, times(1)).calcularTotalNaoPago();
    }
}