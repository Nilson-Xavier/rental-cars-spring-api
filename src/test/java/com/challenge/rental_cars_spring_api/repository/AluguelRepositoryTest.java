package com.challenge.rental_cars_spring_api.repository;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.AluguelRepository;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.ClienteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Date;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AluguelRepositoryTest {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Test
    @DisplayName("Save And Find Aluguel deve salvar e buscar um aluguel")
    public void testSaveAndFindAluguel() {
        Aluguel aluguel = new Aluguel(null, null, null, Date.valueOf("2025-01-01"), Date.valueOf("2025-01-02"), BigDecimal.valueOf(10), false);

        Aluguel savedAluguel = aluguelRepository.save(aluguel);

        Aluguel foundAluguel = aluguelRepository.findById(savedAluguel.getId()).orElse(null);

        Assert.assertNotNull(foundAluguel);
        Assert.assertNull(foundAluguel.getCarro());
        Assert.assertNull(foundAluguel.getCliente());
        Assert.assertEquals("2025-01-01", foundAluguel.getDataAluguel().toString());
        Assert.assertEquals("2025-01-02", foundAluguel.getDataDevolucao().toString());
        Assert.assertEquals("10", foundAluguel.getValor().toString());
        Assert.assertEquals(false, foundAluguel.isPago());
    }
}