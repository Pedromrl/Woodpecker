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

public class LivrosAdapter extends RecyclerView.Adapter<LivrosAdapter.LivroViewHolder> {
    private List<VolumeInfo> livros;
    private List<Items> items;
    private int rowLayout;
    private Context context;

    public static class LivroViewHolder extends RecyclerView.ViewHolder {
        LinearLayout livrosLayout;
        TextView nome;
        TextView autor;
       // TextView descricao;
        ImageView capa;
        TextView id;
        CardView card_view;


        public LivroViewHolder(View v) {
            super(v);
            livrosLayout = (LinearLayout) v.findViewById(R.id.livros_layout);
            nome = (TextView) v.findViewById(R.id.nome);
            autor = (TextView) v.findViewById(R.id.autor);
          //  descricao = (TextView) v.findViewById(R.id.descricao);
            capa = (ImageView) v.findViewById(R.id.capa);
            id = (TextView) v.findViewById(R.id.id);
            card_view = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public LivrosAdapter(List<VolumeInfo> livros, List<Items> items, int rowLayout, Context context) {
        this.livros = livros;
        this.items = items;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public LivrosAdapter.LivroViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LivroViewHolder holder, final int position) {

        YoYo.with(Techniques.SlideInUp).playOn(holder.card_view);

        try{
            holder.id.setText(items.get(position).getId());
        }catch (Exception e){
            e.printStackTrace();
            holder.id.setText("NAOTEMID");
        }


        //TENTAR PÔR NOME
        try{
            int tamanho = livros.get(position).getTitle().length();
            String nomeLivro = livros.get(position).getTitle();
            String nomeLivroReduzido;
            if (tamanho > 42){
                nomeLivroReduzido = nomeLivro.substring(0, 30);
                nomeLivroReduzido += "...";
                holder.nome.setText(nomeLivroReduzido);
            }else{
                holder.nome.setText(nomeLivro);
            }

          //holder.nome.setText(livros.get(position).getTitle());
        }catch (Exception e){
            e.printStackTrace();
            holder.nome.setText("Unrecognized Book name");
        }


        //TENTAR PÔR AUTOR
        try{
            holder.autor.setText(livros.get(position).getAuthors().get(0));
        }catch(Exception e) {
            e.printStackTrace();
            holder.autor.setText("Unrecognized Author");
        }

        //TENTAR PÔR DESCRIÇÃO
       /* try{
            if(livros.get(position).getDescription().equals("")){
                holder.descricao.setText("No description available");
            }else{
                holder.descricao.setText(livros.get(position).getDescription());
            }

        }catch(Exception e){
            holder.descricao.setText("No description available");
        }*/


        //TENTAR PÔR IMAGEM
        try{
            Picasso.with(context).load(livros.get(position).getImageLinks().getThumbnail()).fit().centerCrop().into(holder.capa);
        }catch (Exception e){
            e.printStackTrace();
            Picasso.with(context).load(R.drawable.ic_close).fit().centerCrop().into(holder.capa);
        }



    }

    @Override
    public int getItemCount() {
        return livros.size();
    }



}
