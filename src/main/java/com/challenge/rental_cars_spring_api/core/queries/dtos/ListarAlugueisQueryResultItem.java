package com.challenge.rental_cars_spring_api.core.queries.dtos;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;

import java.text.SimpleDateFormat;

public record ListarAlugueisQueryResultItem(String dataAluguel, String modeloCarro, Integer kmCarro,
                                            String nomeCliente, String telefoneCliente, String dataDevolucao,
                                            String valor, String pago) {

    public static ListarAlugueisQueryResultItem from(Aluguel aluguel) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return new ListarAlugueisQueryResultItem(
                sdf.format(aluguel.getDataAluguel()),
                aluguel.getCarro().getModelo(),
                aluguel.getCarro().getKm(),
                aluguel.getCliente().getNome(),
                formatarTelefone(aluguel.getCliente().getTelefone()),
                sdf.format(aluguel.getDataDevolucao()),
                String.format("%.2f", aluguel.getValor()),
                aluguel.isPago() ? "SIM" : "NÃO");
    }
    private static String formatarTelefone(String telefone) {
        return "+55(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 7) + "-" + telefone.substring(7);
    }
}

