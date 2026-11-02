package me.dio.designpatterns.service.strategy;

import me.dio.designpatterns.model.Cliente;

public interface ValidacaoClienteStrategy {
    void validar(Cliente cliente);
}
