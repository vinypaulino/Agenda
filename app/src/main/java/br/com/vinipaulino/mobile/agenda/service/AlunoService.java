package br.com.vinipaulino.mobile.agenda.service;

import br.com.vinipaulino.mobile.agenda.modelo.Aluno;
import br.com.vinipaulino.mobile.agenda.tdo.AlunoSync;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by vinyp on 22/02/2018.
 */

public interface AlunoService {
    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();
}
