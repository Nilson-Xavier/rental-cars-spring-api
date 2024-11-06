package com.challenge.rental_cars_spring_api.controller;

import com.challenge.rental_cars_spring_api.access.CarrosRestController;
import com.challenge.rental_cars_spring_api.core.queries.ListarCarrosQuery;
import com.challenge.rental_cars_spring_api.core.queries.dtos.ListarCarrosQueryResultItem;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarrosRestController.class)
public class CarrosRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarroRepository carroRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private AluguelRepository aluguelRepository;

    @MockBean
    private ListarCarrosQuery listarCarrosQuery;

    private List<ListarCarrosQueryResultItem> expectedCars;

    @BeforeEach
    void setup() {
        expectedCars = List.of(
                new ListarCarrosQueryResultItem(1L, "Modelo A"),
                new ListarCarrosQueryResultItem(2L, "Modelo B")
        );
        when(listarCarrosQuery.execute()).thenReturn(expectedCars);
    }

    @Test
    @DisplayName("Listar Carros")
    void testListarCarrosReturnsExpectedCars() throws Exception {
        mockMvc.perform(get("/carros/listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(expectedCars.get(0).id()))
                .andExpect(jsonPath("$[0].modelo").value(expectedCars.get(0).modelo()))
                .andExpect(jsonPath("$[1].id").value(expectedCars.get(1).id()))
                .andExpect(jsonPath("$[1].modelo").value(expectedCars.get(1).modelo()));
    }
}