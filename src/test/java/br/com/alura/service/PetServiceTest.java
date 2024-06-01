package br.com.alura.service;

import br.com.alura.client.ClientHttp;
import br.com.alura.domain.Abrigo;
import br.com.alura.domain.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;
import java.util.List;

import static br.com.alura.TestUtils.asJsonString;
import static br.com.alura.TestUtils.provideInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private HttpResponse<String> mockResponse;
    @Mock
    private ClientHttp clientHttp;

    @InjectMocks
    private PetService petService;

    private final Abrigo abrigo0 = new Abrigo(
            0,
            "Abrigo0",
            "123456789",
            "abrigo0@gmail.com"
    );

    private final Pet pet0 = new Pet(
            0,
            "Cachorro",
            "Max",
            "Caramelo",
            3,
            "Caramelo",
            27.0f
    );

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void listarPetsDoAbrigo_deveChamarApi() throws IOException, InterruptedException {
        String responseBody = asJsonString(List.of(pet0));
        given(
                mockResponse.body()
        ).willReturn(
                responseBody
        );
        // given
        given(
                clientHttp.dispararRequisicaoGet(eq(pegaUrlBaseParaAbrigo(abrigo0)))
        ).willReturn(mockResponse);
        provideInput("""
                %s
                """.formatted(abrigo0.getId()));

        // when
        petService.listarPetsDoAbrigo();

        // then
        verify(clientHttp).dispararRequisicaoGet(eq(pegaUrlBaseParaAbrigo(abrigo0)));
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Digite o id do abrigo:",
                        "Pets cadastrados:",
                        pet0.toString()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {404, 500})
    void listarPetsDoAbrigo_deveChamarApi_eTratarErros404ou500(int responseCode) throws IOException, InterruptedException {
        // given
        given(mockResponse.statusCode()).willReturn(responseCode);
        given(
                clientHttp.dispararRequisicaoGet(eq(pegaUrlBaseParaAbrigo(abrigo0)))
        ).willReturn(mockResponse);
        provideInput("""
                %s
                """.formatted(abrigo0.getId()));

        // when
        petService.listarPetsDoAbrigo();

        // then
        verify(clientHttp).dispararRequisicaoGet(eq(pegaUrlBaseParaAbrigo(abrigo0)));
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Digite o id do abrigo:",
                        "ID não cadastrado!"
                );

    }

    private String pegaUrlBaseParaAbrigo(Abrigo abrigo0) {
        return "http://localhost:8080/abrigos/" + abrigo0.getId() + "/pets";
    }
}