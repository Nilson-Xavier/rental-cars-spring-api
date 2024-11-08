package com.challenge.rental_cars_spring_api.controller;

import com.challenge.rental_cars_spring_api.access.AluguelRestController;
import com.challenge.rental_cars_spring_api.core.services.AluguelService;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
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

//    @Mock
//    private ClassPathResource mockClassPathResource;

//    @Mock
//    private Resource mockResource;

    @Mock
    private File mockFile;

    @BeforeEach
    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(new AluguelRestController(aluguelService, aluguelRepository, listarAlugueisQuery)).build();
    }

    @Test
    @DisplayName("Processar Arquivo deve processar o arquivo")
    public void testProcessarArquivo() throws Exception {
        String fileName = "test.rtn";

//        Resource mockResource = mock(Resource.class);
//        when(mockResource.getFile()).thenReturn(fileName);
//        when(resource.getInputStream()).thenReturn(new FileInputStream("src/test/resources/example.txt"));
//        when(resourceLoader.getResource("classpath:example.txt")).thenReturn(resource);
//        when(aluguelService.processarArquivo(anyString())).thenReturn("Arquivo processado com sucesso.");
//        when(mockResource.getFile()).thenReturn(mockFile);
//        when(mockResource.getFilename()).thenReturn(fileName);

//        File mockFile = mock(File.class);
//        MockedStatic<File> mockFile = Mockito.mockStatic(File.class);
        File file = new File("src/test/resources/test2.rtn");

//        when(mockResource.getFile()).thenReturn(file);
//        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream("mock content".getBytes()));

        MockedStatic<ClassPathResource> mockClassPathResource = Mockito.mockStatic(ClassPathResource.class);
        Resource mockResource = mockClassPathResource.getClass();
        when(mockResource.getFile()).thenReturn(file);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream("mock content".getBytes()));


//        when(mockFile.exists()).thenReturn(true);
//        when(file.exists()).thenReturn(true);
        Mockito.doReturn(true).when(mockFile).exists(); //mockFile.when(() -> file).exists();
//        mockFile.when(() -> file.exists()).thenReturn(true);

        doNothing().when(aluguelService).processarArquivo(anyString());

        mockMvc.perform(post("/alugueis/processar-arquivo")
                        .param("fileName", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo processado com sucesso."));

        verify(aluguelService, times(1)).processarArquivo(anyString());
    }

    @Test
    @DisplayName("Listar Alugueis deve retornar os alugueis esperados")
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