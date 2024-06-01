package br.com.alura;

import java.util.Scanner;

import static br.com.alura.DefaultConfiguration.*;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
        try {
            int opcaoEscolhida;
            while (true) {
                printMenuOpcpes();

                String textoDigitado = new Scanner(System.in).nextLine();
                opcaoEscolhida = Integer.parseInt(textoDigitado);

                switch (opcaoEscolhida) {
                    case 1 -> COMMAND_EXECUTOR.executeCommand(LISTAR_ABRIGO_COMMAND);
                    case 2 -> COMMAND_EXECUTOR.executeCommand(CADASTRAR_ABRIGO_COMMAND);
                    case 3 -> COMMAND_EXECUTOR.executeCommand(LISTAR_PETS_COMMAND);
                    case 4 -> COMMAND_EXECUTOR.executeCommand(IMPORTAR_PETS_COMMAND);
                    case 5 -> COMMAND_EXECUTOR.executeCommand(FINALIZANDO_PROGRAMA_COMMAND);
                    default -> System.out.println("NÚMERO INVÁLIDO!");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenuOpcpes() {
        System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
        System.out.println("1 -> Listar abrigos cadastrados");
        System.out.println("2 -> Cadastrar novo abrigo");
        System.out.println("3 -> Listar pets do abrigo");
        System.out.println("4 -> Importar pets do abrigo");
        System.out.println("5 -> Sair");
    }

}
