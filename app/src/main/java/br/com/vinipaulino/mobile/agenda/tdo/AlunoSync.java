package br.com.vinipaulino.mobile.agenda.tdo;

import java.util.List;

import br.com.vinipaulino.mobile.agenda.modelo.Aluno;

/**
 * Created by vinyp on 23/02/2018.
 */

public class AlunoSync {
    private List<Aluno> alunos;
    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }

    public void setMomentoDaUltimaModificacao(String momentoDaUltimaModificacao) {
        this.momentoDaUltimaModificacao = momentoDaUltimaModificacao;
    }
}
