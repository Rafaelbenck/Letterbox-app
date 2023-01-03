package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmsViewHolder>{
    private final ArrayList<Produto> produtos;

    public FilmAdapter(ArrayList<Produto> produto){
        this.produtos = produto;

    }


    @NonNull
    @Override
    public FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Criar Layout do XMl de cada 'card' individual
        // ** R.layout.filmItem é a localização do XML
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
         return new FilmsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FilmsViewHolder holder, int position) {
     Produto filme = produtos.get(position);
    holder.bind(filme);


    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

     class FilmsViewHolder extends RecyclerView.ViewHolder{
    TextView txtUser;
    TextView txtSinopse;
    TextView txtGenero;
    Button btAlterar;


        public FilmsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txt_nome);
            txtSinopse = itemView.findViewById(R.id.txt_sinopse);
            txtGenero = itemView.findViewById(R.id.txt_nota);
        }

        public void bind(Produto produto) {
            txtUser.setText(produto.getNome());
            txtSinopse.setText(produto.getSinopse());
            txtGenero.setText(produto.getGenero());

        }

    }
}
