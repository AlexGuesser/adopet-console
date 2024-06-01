package br.com.alura;

import br.com.alura.client.ClientHttp;
import br.com.alura.command.*;
import br.com.alura.service.AbrigoService;
import br.com.alura.service.PetService;

public class DefaultConfiguration {

    public static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();

    public static final ClientHttp CLIENT_HTTP = new ClientHttp();

    public static final AbrigoService ABRIGO_SERVICE = new AbrigoService(
            CLIENT_HTTP
    );

    public static final PetService PET_SERVICE = new PetService(
            CLIENT_HTTP
    );

    public static final Command LISTAR_ABRIGO_COMMAND = new ListarAbrigoCommand(
            ABRIGO_SERVICE
    );

    public static final Command CADASTRAR_ABRIGO_COMMAND = new CadastrarAbrigoCommand(
            ABRIGO_SERVICE
    );

    public static final Command LISTAR_PETS_COMMAND = new ListarPetsCommand(
            PET_SERVICE
    );

    public static final Command IMPORTAR_PETS_COMMAND = new ImportarPetsCommand(
            PET_SERVICE
    );

    public static final Command FINALIZANDO_PROGRAMA_COMMAND = new FinalizandoProgramaCommand();

}
