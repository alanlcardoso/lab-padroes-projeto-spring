package me.dio.designpatterns.service.impl;

import me.dio.designpatterns.model.Cliente;
import me.dio.designpatterns.model.Endereco;
import me.dio.designpatterns.repository.ClienteRepository;
import me.dio.designpatterns.repository.EnderecoRepository;
import me.dio.designpatterns.service.ClienteService;
import me.dio.designpatterns.service.ViaCepService;
import me.dio.designpatterns.service.strategy.ValidadorClienteContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;
    private final ValidadorClienteContext validadorContext;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              EnderecoRepository enderecoRepository,
                              ViaCepService viaCepService,
                              ValidadorClienteContext validadorContext) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepService = viaCepService;
        this.validadorContext = validadorContext;
    }

    @Override
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente com ID " + id + " não encontrado."));
    }

    @Override
    public Cliente inserir(Cliente cliente) {
        validadorContext.executarValidacoes(cliente);
        return salvarClienteComCep(cliente);
    }

    @Override
    public Cliente atualizar(Long id, Cliente cliente) {
        buscarPorId(id);
        cliente.setId(id);
        validadorContext.executarValidacoes(cliente);
        return salvarClienteComCep(cliente);
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private Cliente salvarClienteComCep(Cliente cliente) {
        String cepNormalizado = cliente.getEndereco().getCep().replaceAll("\\D", "");
        Endereco endereco = enderecoRepository.findById(cepNormalizado).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cepNormalizado);
            if (novoEndereco == null || novoEndereco.getCep() == null) {
                throw new IllegalArgumentException("CEP " + cepNormalizado + " não encontrado no ViaCEP.");
            }
            novoEndereco.setCep(cepNormalizado);
            return enderecoRepository.save(novoEndereco);
        });

        cliente.setEndereco(endereco);
        return clienteRepository.save(cliente);
    }
}
