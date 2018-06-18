package com.example.pedrolemos.livrosfinal.adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pedrolemos.livrosfinal.LivroActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.fragments.AccountFragment;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.model.UserFavorites;
import com.example.pedrolemos.livrosfinal.model.VolumeInfo;
import com.example.pedrolemos.livrosfinal.utils.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {
    private List<UserFavorites> favoritos;
    private int rowLayout;
    private Context context;

    public class FavoritoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout favoritosLayout;
        TextView nomeFav;
        TextView autorFav;
        TextView categoriaFav;
        ImageView capaFav;
        TextView descricaoFav;
        TextView idFav;
        TextView elimFav;
        CardView card_viewFav;


        public FavoritoViewHolder(View v) {
            super(v);
            favoritosLayout = (LinearLayout) v.findViewById(R.id.favoritos_layout);
            nomeFav = (TextView) v.findViewById(R.id.nomeFav);
            autorFav = (TextView) v.findViewById(R.id.autorFav);
            categoriaFav = (TextView) v.findViewById(R.id.categoriaFav);
            descricaoFav = (TextView) v.findViewById(R.id.descricaoFav);
            capaFav = (ImageView) v.findViewById(R.id.capaFav);
            idFav = (TextView) v.findViewById(R.id.idFav);
            card_viewFav = (CardView) v.findViewById(R.id.card_viewFav);
            elimFav = (TextView) v.findViewById(R.id.elimFav);

            /*elimFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });*/


        }


    }


    public FavoritosAdapter(List<UserFavorites> favoritos, int rowLayout, Context context) {
        this.favoritos = favoritos;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public FavoritosAdapter.FavoritoViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FavoritoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final FavoritoViewHolder holder, final int position) {

        YoYo.with(Techniques.SlideInUp).playOn(holder.card_viewFav);

        try {
            holder.idFav.setText(favoritos.get(position).toString());
        } catch (Exception e) {
            e.printStackTrace();
            holder.idFav.setText("NAOTEMID");
        }


        //TENTAR PÔR NOME
        try {
            int tamanho = favoritos.get(position).nome.length();
            String nomeLivro = favoritos.get(position).nome;
            String nomeLivroReduzido;
            if (tamanho > 42) {
                nomeLivroReduzido = nomeLivro.substring(0, 30);
                nomeLivroReduzido += "...";
                holder.nomeFav.setText(nomeLivroReduzido);
            } else {
                holder.nomeFav.setText(nomeLivro);
            }

            //holder.nome.setText(livros.get(position).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            holder.nomeFav.setText("Unrecognized Book name");
        }


        //TENTAR PÔR AUTOR
        try {
            holder.autorFav.setText(favoritos.get(position).autor);
        } catch (Exception e) {
            e.printStackTrace();
            holder.autorFav.setText("Unrecognized Author");
        }

        //TENTAR PÔR DESCRIÇÃO
        try {
            if (favoritos.get(position).descricao.equals("")) {
                holder.descricaoFav.setText("No description available");
            } else {
                holder.descricaoFav.setText(favoritos.get(position).descricao);
            }

        } catch (Exception e) {
            holder.descricaoFav.setText("No description available");
        }


        //TENTAR PÔR IMAGEM
        try {
            Picasso.with(context).load(favoritos.get(position).capa).fit().centerCrop().into(holder.capaFav);
        } catch (Exception e) {
            e.printStackTrace();
            Picasso.with(context).load(R.drawable.ic_close).fit().centerCrop().into(holder.capaFav);
        }

        //TENTAR PÔR CATEGORIA
        try {
            holder.categoriaFav.setText(favoritos.get(position).categoria);
        } catch (Exception e) {
            e.printStackTrace();
            holder.categoriaFav.setText("Unrecognized Author");
        }

        //TENTAR PÔR ID
        try {
            holder.idFav.setText(favoritos.get(position).hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }


    public void removeItem(final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String utilizador = firebaseAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(utilizador);
        final DatabaseReference favoritosRef = databaseReference.child("Favoritos");

        favoritosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot individual : dataSnapshot.getChildren()) {
                    String key = individual.getKey();

                    List<UserFavorites> fav = new ArrayList<>();
                    final UserFavorites userFavorites = dataSnapshot.child(key).getValue(UserFavorites.class);
                    fav.add(userFavorites);


                    if (favoritos.get(position).nome.equals(fav.get(position).nome)) {
                        Log.w("POSITION:", String.valueOf(position));
                        Log.w("REF:", individual.getRef().toString());

                        DatabaseReference dt = dataSnapshot.child(key).getRef();

                        dt.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Log.w("APAGOU?", "SIM!!!!!");

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        favoritos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoritos.size());

    }

}


