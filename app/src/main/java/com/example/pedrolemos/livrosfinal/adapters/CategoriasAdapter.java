package com.example.pedrolemos.livrosfinal.adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pedrolemos.livrosfinal.LivroActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.model.VolumeInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder> {
    private List<VolumeInfo> livros;
    private List<Items> items;
    private int rowLayout;
    private Context context;

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        LinearLayout livrosLayout;
        TextView nome;
        TextView autor;
        // TextView descricao;
        ImageView capa;
        TextView id;
        CardView card_view;


        public CategoriaViewHolder(View v) {
            super(v);
            livrosLayout = (LinearLayout) v.findViewById(R.id.livros_layout_cat);
            nome = (TextView) v.findViewById(R.id.nome_cat);
            capa = (ImageView) v.findViewById(R.id.capa_cat);
            id = (TextView) v.findViewById(R.id.id_cat);
            card_view = (CardView) v.findViewById(R.id.card_view_cat);
        }
    }

    public CategoriasAdapter(List<VolumeInfo> livros, List<Items> items, int rowLayout, Context context) {
        this.livros = livros;
        this.items = items;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public CategoriasAdapter.CategoriaViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, final int position) {

        YoYo.with(Techniques.FadeIn).playOn(holder.card_view);

        try {
            holder.id.setText(items.get(position).getId());
        } catch (Exception e) {
            e.printStackTrace();
            holder.id.setText("NAOTEMID");
        }


        //TENTAR PÔR NOME
        try {
            int tamanho = livros.get(position).getTitle().length();
            String nomeLivro = livros.get(position).getTitle();
            String nomeLivroReduzido;
            if (tamanho > 42) {
                nomeLivroReduzido = nomeLivro.substring(0, 30);
                nomeLivroReduzido += "...";
                holder.nome.setText(nomeLivroReduzido);
            } else {
                holder.nome.setText(nomeLivro);
            }

            //holder.nome.setText(livros.get(position).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            holder.nome.setText("Unrecognized Book name");
        }


        //TENTAR PÔR IMAGEM
        try {
            Picasso.with(context).load(livros.get(position).getImageLinks().getThumbnail()).fit().centerCrop().into(holder.capa);
        } catch (Exception e) {
            e.printStackTrace();
            Picasso.with(context).load(R.drawable.default_img).fit().centerCrop().into(holder.capa);
        }


    }

    @Override
    public int getItemCount() {
        return livros.size();
    }


}
