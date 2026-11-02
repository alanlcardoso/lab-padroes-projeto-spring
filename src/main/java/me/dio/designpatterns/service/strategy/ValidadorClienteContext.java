package me.dio.designpatterns.service.strategy;

import me.dio.designpatterns.model.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidadorClienteContext {

    private final List<ValidacaoClienteStrategy> validadores;

    public ValidadorClienteContext(List<ValidacaoClienteStrategy> validadores) {
        this.validadores = validadores;
    }

    public void executarValidacoes(Cliente cliente) {
        if (validadores != null) {
            validadores.forEach(validador -> validador.validar(cliente));
        }
    }
}
