package br.com.alura.service;

import br.com.alura.client.ClientHttp;
import br.com.alura.domain.Abrigo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    private static final String BASE_ABRIGOS_URL = "http://localhost:8080/abrigos";
    @Mock
    private HttpResponse<String> mockResponse;
    @Mock
    private ClientHttp clientHttp;
    @InjectMocks
    private AbrigoService abrigoService;
    private final Abrigo abrigo0 = new Abrigo(0, "Abrigo0", "123456789", "abrigo0@gmail.com");
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @Test
    void listarAbrigosCadastrados_deveChamarApi() throws IOException, InterruptedException {
        String responseBody = asJsonString(List.of(abrigo0));
        given(
                mockResponse.body()
        ).willReturn(
                responseBody
        );
        given(
                clientHttp.dispararRequisicaoGet(BASE_ABRIGOS_URL)
        ).willReturn(mockResponse);

        abrigoService.listarAbrigosCadastrados();

        verify(clientHttp).dispararRequisicaoGet(BASE_ABRIGOS_URL);
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Abrigos cadastrados:",
                        abrigo0.toString()
                );
    }

    @Test
    void listarAbrigosCadastrados_deveChamarApi_quandoNaoTemAbrigosCadastrados() throws IOException, InterruptedException {
        String responseBody = asJsonString(List.of());
        given(
                mockResponse.body()
        ).willReturn(
                responseBody
        );
        given(
                clientHttp.dispararRequisicaoGet(BASE_ABRIGOS_URL)
        ).willReturn(mockResponse);

        abrigoService.listarAbrigosCadastrados();

        verify(clientHttp).dispararRequisicaoGet(BASE_ABRIGOS_URL);
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Nenhum abrigo cadastrado!"
                );
    }

    @Test
    void cadastrarAbrigo_deveChamarApi() throws IOException, InterruptedException {
        given(mockResponse.statusCode()).willReturn(200);
        provideInput("""
                %s
                %s
                %s
                """.formatted(abrigo0.getNome(), abrigo0.getTelefone(), abrigo0.getEmail()));
        given(mockResponse.body()).willReturn(
                asJsonString(abrigo0)
        );
        given(
                clientHttp.dispararRequisicaoPost(
                        eq(BASE_ABRIGOS_URL),
                        any()
                )
        ).willReturn(mockResponse);


        abrigoService.cadastrarAbrigo();

        ArgumentCaptor<Abrigo> abrigoCaptor = ArgumentCaptor.forClass(Abrigo.class);
        verify(clientHttp).dispararRequisicaoPost(
                eq(BASE_ABRIGOS_URL),
                abrigoCaptor.capture()
        );
        assertThat(abrigoCaptor.getValue()).isEqualTo(abrigo0);
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Digite o nome do abrigo:",
                        "Digite o telefone do abrigo:",
                        "Digite o email do abrigo:",
                        "Abrigo cadastrado com sucesso!",
                        asJsonString(abrigo0)
                );
    }


    @Test
    void cadastrarAbrigo_deveChamarApiEMostrarMensagemDeErroQuandoRetornoNaoFor200() throws IOException, InterruptedException {
        given(mockResponse.statusCode()).willReturn(400);
        given(mockResponse.body()).willReturn("Error message");
        given(
                clientHttp.dispararRequisicaoPost(
                        eq(BASE_ABRIGOS_URL),
                        any()
                )
        ).willReturn(mockResponse);
        provideInput("""
                %s
                %s
                %s
                """.formatted(abrigo0.getNome(), abrigo0.getTelefone(), abrigo0.getEmail()));

        abrigoService.cadastrarAbrigo();

        ArgumentCaptor<Abrigo> abrigoCaptor = ArgumentCaptor.forClass(Abrigo.class);
        verify(clientHttp).dispararRequisicaoPost(
                eq(BASE_ABRIGOS_URL),
                abrigoCaptor.capture()
        );
        assertThat(abrigoCaptor.getValue()).isEqualTo(abrigo0);
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertThat(lines)
                .containsExactly(
                        "Digite o nome do abrigo:",
                        "Digite o telefone do abrigo:",
                        "Digite o email do abrigo:",
                        "Erro ao cadastrar o abrigo:",
                        "Error message"
                );
    }
}