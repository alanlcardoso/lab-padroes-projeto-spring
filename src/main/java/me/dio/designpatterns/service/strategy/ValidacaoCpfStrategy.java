package me.dio.designpatterns.service.strategy;

import me.dio.designpatterns.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCpfStrategy implements ValidacaoClienteStrategy {

    @Override
    public void validar(Cliente cliente) {
        if (cliente.getCpf() == null || cliente.getCpf().replaceAll("\\D", "").length() != 11) {
            throw new IllegalArgumentException("CPF inválido: deve conter 11 dígitos numéricos.");
        }
    }
}
