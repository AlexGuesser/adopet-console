package br.com.alura.command;

import br.com.alura.service.AbrigoService;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class CadastrarAbrigoCommand implements Command {

    private final AbrigoService abrigoService;

    public CadastrarAbrigoCommand(AbrigoService abrigoService) {
        this.abrigoService = requireNonNull(abrigoService);
    }

    @Override
    public void execute() {
        try {
            abrigoService.cadastrarAbrigo();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
