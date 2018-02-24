package br.com.vinipaulino.mobile.agenda.service;

import br.com.vinipaulino.mobile.agenda.modelo.Aluno;
import br.com.vinipaulino.mobile.agenda.tdo.AlunoSync;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vinyp on 22/02/2018.
 */

public interface AlunoService {
    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();

    @DELETE("aluno/{id}")
    Call<Void> deleta(@Path("id") String id);
}
