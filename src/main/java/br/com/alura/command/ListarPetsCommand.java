package br.com.alura.command;

import br.com.alura.service.PetService;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class ListarPetsCommand implements Command {

    private final PetService petService;

    public ListarPetsCommand(PetService petService) {
        this.petService = requireNonNull(petService);
    }

    @Override
    public void execute() {
        try {
            petService.listarPetsDoAbrigo();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
