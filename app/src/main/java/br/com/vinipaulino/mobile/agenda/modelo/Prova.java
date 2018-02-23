package br.com.vinipaulino.mobile.agenda.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vinyp on 19/02/2018.
 */

public class Prova implements Serializable {
    private String materia;
    private String data;
    private List<String> topicosProva;

    public Prova(String materia, String data, List<String> topicosProva) {
        this.materia = materia;
        this.data = data;
        this.topicosProva = topicosProva;
    }

    @Override
    public String toString() {
        return this.materia;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getTopicosProva() {
        return topicosProva;
    }

    public void setTopicosProva(List<String> topicosProva) {
        this.topicosProva = topicosProva;
    }
}
