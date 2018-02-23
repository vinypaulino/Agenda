package br.com.vinipaulino.mobile.agenda.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.vinipaulino.mobile.agenda.R;
import br.com.vinipaulino.mobile.agenda.activity.FormularioActivity;
import br.com.vinipaulino.mobile.agenda.modelo.Aluno;

/**
 * Created by vinyp on 14/02/2018.
 */

public class FormularioHelper {
    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;
    private final ImageView campoFoto;

    private Aluno aluno;


    public FormularioHelper(FormularioActivity activity) {
        this.aluno = new Aluno();
        this.campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        this.campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        this.campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        this.campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        this.campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        this.campoFoto = (ImageView) activity.findViewById(R.id.foto_aluno_form);
    }

    public Aluno pegaAluno() {

        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());
        return aluno;
    }

    public void preencheFormulario(Aluno aluno){
        this.aluno = aluno;
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setRating(aluno.getNota().intValue());
        if (aluno.getCaminhoFoto() != null) {
            carregaImagem(aluno.getCaminhoFoto());
        }
    }

    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
    }
}