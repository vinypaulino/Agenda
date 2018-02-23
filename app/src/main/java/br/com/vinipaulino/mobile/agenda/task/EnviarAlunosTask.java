package br.com.vinipaulino.mobile.agenda.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.vinipaulino.mobile.agenda.DAO.AlunoDAO;
import br.com.vinipaulino.mobile.agenda.web.WebClient;
import br.com.vinipaulino.mobile.agenda.converter.AlunoConverter;
import br.com.vinipaulino.mobile.agenda.modelo.Aluno;

/**
 * Created by vinyp on 19/02/2018.
 */

public class EnviarAlunosTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private ProgressDialog alertDialog;

    public EnviarAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = ProgressDialog.show(context, "Aguarde", "Enviando para o servidor ...", true, true);
        alertDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        WebClient webClient = new WebClient();
        AlunoConverter converter = new AlunoConverter();
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        String json = converter.toJson(alunos);
        String resposta = webClient.post(json);

        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        alertDialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();

    }
}

