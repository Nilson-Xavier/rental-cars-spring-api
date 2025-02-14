package com.challenge.rental_cars_spring_api.repository;

import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.infrastructure.repositories.ClienteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Save And Find Cliente deve salvar e buscar um cliente")
    public void testSaveAndFindCliente() {
        Cliente cliente = new Cliente(null, "Cliente X", "12345678910", "123456789", "11999999999");

        Cliente savedCliente = clienteRepository.save(cliente);

        Cliente foundCliente = clienteRepository.findById(savedCliente.getId()).orElse(null);

        Assert.assertNotNull(foundCliente);
        Assert.assertEquals("Cliente X", foundCliente.getNome());
        Assert.assertEquals("12345678910", foundCliente.getCpf());
        Assert.assertEquals("123456789", foundCliente.getCnh());
        Assert.assertEquals("11999999999", foundCliente.getTelefone());
    }
}