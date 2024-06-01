package br.com.alura.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Abrigo {

    private long id;
    private String nome;
    private String telefone;
    private String email;

    // Default constructor is required by Jackson
    private Abrigo() {
    }

    public Abrigo(long id, String nome, String telefone, String email) {
        this(nome, telefone, email);
        this.id = id;
    }


    public Abrigo(String nome, String telefone, String email) {
        this.nome = requireNonNull(nome);
        this.telefone = requireNonNull(telefone);
        this.email = requireNonNull(email);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Abrigo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Abrigo abrigo)) return false;

        if (id != abrigo.id) return false;
        if (!Objects.equals(nome, abrigo.nome)) return false;
        if (!Objects.equals(telefone, abrigo.telefone)) return false;
        return Objects.equals(email, abrigo.email);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
