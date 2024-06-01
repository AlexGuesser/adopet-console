package br.com.alura.service;

import br.com.alura.client.ClientHttp;
import br.com.alura.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.Objects.requireNonNull;

public class PetService {

    private final ClientHttp clientHttp;

    public PetService(ClientHttp clientHttp) {
        this.clientHttp = requireNonNull(clientHttp);
    }

    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id do abrigo:");
        String id = new Scanner(System.in).nextLine();

        HttpResponse<String> response = clientHttp.dispararRequisicaoGet(
                "http://localhost:8080/abrigos/" + id + "/pets"
        );
        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID não cadastrado!");
            return;
        }
        String responseBody = response.body();
        List<Pet> pets = Arrays.stream(
                new ObjectMapper().readValue(responseBody, Pet[].class)
        ).toList();
        System.out.println("Pets cadastrados:");
        pets.forEach(System.out::println);
    }

    public void importarPetsUsandoArquivo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        String nomeArquivo = new Scanner(System.in).nextLine();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + nomeArquivo);
            return;
        }
        String line;
        while ((line = reader.readLine()) != null) {
            String[] campos = line.split(",");

            Pet pet = new Pet(
                    campos[0],
                    campos[1],
                    campos[2],
                    Integer.parseInt(campos[3]),
                    campos[4],
                    Float.parseFloat(campos[5])
            );

            HttpResponse<String> response = clientHttp.dispararRequisicaoPost(
                    "http://localhost:8080/abrigos/" + idOuNome + "/pets",
                    pet
            );

            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Pet cadastrado com sucesso: " + pet.getNome());
            } else if (statusCode == 404) {
                System.out.println("Id ou nome do abrigo não encontado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Erro ao cadastrar o pet: " + pet.getNome());
                System.out.println(responseBody);
                break;
            }
        }
        reader.close();
    }

}
