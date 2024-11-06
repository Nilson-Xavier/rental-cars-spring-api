package com.challenge.rental_cars_spring_api.repository;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.CarroRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarroRepositoryTest {

    @Autowired
    private CarroRepository carroRepository;

    @Test
    public void testSaveAndFindCarro() {
        Carro carro = new Carro(null, "Model S", "2022", 5, 0, "Tesla", BigDecimal.valueOf(100));

        Carro savedCarro = carroRepository.save(carro);

        Carro foundCarro = carroRepository.findById(savedCarro.getId()).orElse(null);

        Assert.assertNotNull(foundCarro);
        Assert.assertEquals("Model S", foundCarro.getModelo());
        Assert.assertEquals("2022", foundCarro.getAno());
        Assert.assertEquals(Integer.valueOf(5), foundCarro.getQtdPassageiros());
        Assert.assertEquals("Tesla", foundCarro.getFabricante());
        Assert.assertEquals(BigDecimal.valueOf(100), foundCarro.getVlrDiaria());
    }
}