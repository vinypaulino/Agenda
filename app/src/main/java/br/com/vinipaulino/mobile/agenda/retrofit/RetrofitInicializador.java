package br.com.vinipaulino.mobile.agenda.retrofit;

import br.com.vinipaulino.mobile.agenda.service.AlunoService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by vinyp on 22/02/2018.
 */

public class RetrofitInicializador {
    private final Retrofit retrofit;

    public  RetrofitInicializador(){
        retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.25.42:8080/api/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
    }

    public AlunoService getAlunoService() {
        return  retrofit.create(AlunoService.class);
    }
}
