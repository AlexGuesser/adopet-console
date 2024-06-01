package br.com.alura.command;

public class FinalizandoProgramaCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Finalizando o programa...");
        System.exit(0);
    }

}
