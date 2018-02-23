package br.com.vinipaulino.mobile.agenda.activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import br.com.vinipaulino.mobile.agenda.BuildConfig;
import br.com.vinipaulino.mobile.agenda.DAO.AlunoDAO;
import br.com.vinipaulino.mobile.agenda.helper.FormularioHelper;
import br.com.vinipaulino.mobile.agenda.R;
import br.com.vinipaulino.mobile.agenda.modelo.Aluno;
import br.com.vinipaulino.mobile.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    private static final int CODIGO_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if (aluno != null) {
            helper.preencheFormulario(aluno);
        }

        Button botaoFoto = findViewById(R.id.formulario_botao);

        botaoFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(FormularioActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider", arquivoFoto));

                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_CAMERA) {
            if (resultCode == RESULT_OK) {
                    helper.carregaImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegaAluno();

                // Aqui instanciamos o DAO e inserimos o novo aluno no banco
                AlunoDAO dao = new AlunoDAO(this);

                if (aluno.getId() == null) {
                    dao.insere(aluno);
                } else {
                    dao.altera(aluno);
                }

                dao.close();

             //  new InsereAlunoTask(aluno).execute();
                Call<Void> call = new RetrofitInicializador().getAlunoService().insere(aluno);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i("onResponse", "Requisição com sucesso");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("onFailure", "reauisição falhou");
                    }
                });

                Toast.makeText(FormularioActivity.this, "Aluno "
                + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
