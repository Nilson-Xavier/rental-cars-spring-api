package com.challenge.rental_cars_spring_api.core.queries.dtos;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;

import java.math.BigDecimal;
import java.sql.Date;

public record ListarAlugueisQueryResultItem(Date dataAluguel, String modeloCarro, Integer kmCarro,
                                            String nomeCliente, String telefoneCliente, Date dataDevolucao,
                                            BigDecimal valor, String pago) {

    public static ListarAlugueisQueryResultItem from(Aluguel aluguel) {
        return new ListarAlugueisQueryResultItem(
                aluguel.getDataAluguel(),
                aluguel.getCarro().getModelo(),
                aluguel.getCarro().getKm(),
                aluguel.getCliente().getNome(),
                formatarTelefone(aluguel.getCliente().getTelefone()),
                aluguel.getDataDevolucao(),
                aluguel.getValor(),
                aluguel.isPago() ? "SIM" : "NÃO");
    }
    private static String formatarTelefone(String telefone) {
        return "+XX(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 7) + "-" + telefone.substring(7);
    }
}

