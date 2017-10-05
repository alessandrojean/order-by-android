package etelg.etim3e.tcc.tcc.model;

import java.io.Serializable;

/**
 * Created by Alessandro on 09/09/2015.
 */
public class Aluno implements Serializable {
    private String rm;
    private String cpf;
    private String nome;
    private String telefone;
    private String celular;
    private String urlFoto;
    private int idTurma;

    public static final String KEY_RM = "rm";
    public static final String KEY_NOME = "nome";
    public static final String KEY_TURMA = "turma";

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }
}
