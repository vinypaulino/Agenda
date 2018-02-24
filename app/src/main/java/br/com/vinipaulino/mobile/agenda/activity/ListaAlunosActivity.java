package br.com.vinipaulino.mobile.agenda.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.vinipaulino.mobile.agenda.DAO.AlunoDAO;
import br.com.vinipaulino.mobile.agenda.R;
import br.com.vinipaulino.mobile.agenda.adapter.AlunosAdapter;
import br.com.vinipaulino.mobile.agenda.modelo.Aluno;
import br.com.vinipaulino.mobile.agenda.retrofit.RetrofitInicializador;
import br.com.vinipaulino.mobile.agenda.task.EnviarAlunosTask;
import br.com.vinipaulino.mobile.agenda.tdo.AlunoSync;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {

    private static final int CODIGO_SMS = 1;
    private ListView listaAlunos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaAlunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View view, int position, long id) {
                Aluno aluno = (Aluno) lista.getItemAtPosition(position);

                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, CODIGO_SMS);
        }
        callBuscaAluno();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.enviar_notas:
                new EnviarAlunosTask(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent vaiParaProvas = new Intent(ListaAlunosActivity.this, ProvasActivity.class);
                startActivity(vaiParaProvas);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();


        carregaLista();
    }

    private void callBuscaAluno() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                AlunoDAO alunoDAO = new AlunoDAO(ListaAlunosActivity.this);
                alunoDAO.sincroniza(alunoSync.getAlunos());
                alunoDAO.close();
                carregaLista();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
            }
        });
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);
        // Fazer Ligação
        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });
        // Ir para o Site
        MenuItem itemSite = menu.add("Ir para Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();

        if (!site.startsWith("http://")) {
            site = "http://" + site;
        }

        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);
        // Enviar Mensagem
        MenuItem sms = menu.add("Sms");
        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse("sms:" + aluno.getTelefone()));
        sms.setIntent(intentSms);

        //Intent Implicita vai para o mapa 
        MenuItem vaiProMapa = menu.add("Vai pro Mapa");
        Intent intentVaiProMapa = new Intent(Intent.ACTION_VIEW);
        intentVaiProMapa.setData(Uri.parse("geo:0,0?z=14&q=" + aluno.getEndereco()));
        vaiProMapa.setIntent(intentVaiProMapa);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Call<Void> call = new RetrofitInicializador().getAlunoService().deleta(aluno.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                        dao.deleta(aluno);
                        dao.close();
                        carregaLista();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ListaAlunosActivity.this, "Não foi possivel remover o aluno", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
    }
}

