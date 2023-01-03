/**
 * Aplicativo para listar filmes assistidos, assim como letterbox
 * versão 1.0
 * Alunos: Rafael Benck e João Vitor Lopes
 *
 * **/

package com.example.myapplication;

import static com.example.myapplication.R.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rv;

    private Button btDeletar;
    private ArrayAdapter adapter;
    private ArrayList<Produto> listaDeProdutos;
    private ArrayList<Film> listaDeFilmes;
    //**************FIREBASE ***********************//
    private FirebaseDatabase firebaseDatabase;
    //classe que faz referencia ao banco de dados
    private DatabaseReference reference;
    // classe que aponta para um nó do banco
    private ChildEventListener childEventListener;
    //classe que escuta eventos nos nós filhos
    private Query query;
    //classe que permite fazer uma consulta no banco
    private FirebaseAuth auth;
    //classe responsável pela autenticação
    private FirebaseAuth.AuthStateListener authStateListener;
    private FilmAdapter filmAdapter;
    private View view;

    //Classe responsável por ficar "ouvindo" as mudanças na autenticação
//**************FIREBASE ***********************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_main);


        listaDeProdutos = new ArrayList<>();

        filmAdapter = new FilmAdapter(listaDeProdutos);




        FloatingActionButton fab = findViewById(id.floatingActionButtonInluir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
                intent.putExtra("acao", "inserir");
                startActivity(intent);


            }
        });


        filmAdapter = new FilmAdapter(listaDeProdutos);
        rv = findViewById(id.recycle_main);
        rv.setAdapter(filmAdapter);





class ItemTouchHandler extends ItemTouchHelper.SimpleCallback{


    public ItemTouchHandler(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == 4){
            int position = viewHolder.getAdapterPosition();
            excluir(position);
            listaDeProdutos.remove(position);
            filmAdapter.notifyItemRemoved(position);
            }
    if( direction == 8){
        int position = viewHolder.getAdapterPosition();
           adicionar(position);
    }

    }
}
      ItemTouchHelper helper =  new ItemTouchHelper(
              new ItemTouchHandler(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
        helper.attachToRecyclerView(rv);



//**************FIREBASE ***********************//
        auth = FirebaseAuth.getInstance();
        //Fica "escutando" as mudanças
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if( auth.getCurrentUser() == null ){
                    finish();
                }
            }
        };
        auth.addAuthStateListener( authStateListener ); //adiciona um ouvinte
//**************FIREBASE ***********************//

    }

    @Override
    protected void onStart() {
        super.onStart();
        listaDeProdutos.clear(); //limpa a lista de produtos
        firebaseDatabase = FirebaseDatabase.getInstance(); //pega uma instancia do banco
        reference = firebaseDatabase.getReference();//volta para o nó raiz do Firebase
        query = reference.child("films");//cria consulta baseada nos nomes
        childEventListener = new ChildEventListener() {//fica ouvindo alterações nos nós filhos

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
                //metodo quando um novo filho é adicionado
                try {
                    String idUserProd = snapshot.child("idUsuario").getValue(String.class);
                    if(idUserProd.equals(auth.getCurrentUser().getUid())) {
                        //retorna o idUsuraio como string
                        Produto prod = new Produto();
                        prod.setId(snapshot.getKey());
                        prod.setNome(snapshot.child("nome").getValue(String.class));
                        prod.setSinopse(snapshot.child("sinopse").getValue(String.class));
                        prod.setGenero(snapshot.child("genero").getValue(String.class));
                        prod.setIdUsuario(idUserProd);

                        listaDeProdutos.add(prod);
                        System.out.println();//adiciona o produto na lista
                            System.out.println(listaDeProdutos);
                        filmAdapter = new FilmAdapter(listaDeProdutos);
                        rv = findViewById(id.recycle_main);
                        rv.setAdapter(filmAdapter);
                        filmAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName) {
                //metodo quando um filho é alterado
                for (Produto p: listaDeProdutos) {
                    if ( p.getId().equals(  snapshot.getKey() ) ){
                        p.setNome( snapshot.child("nome").getValue(String.class) );
                        p.setGenero( snapshot.child("categoria").getValue(String.class) );
                        filmAdapter.notifyDataSetChanged();
                        //notifica os ouvintes que os dados foram alterados
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (Produto p: listaDeProdutos) {
                    if ( p.getId().equals(  snapshot.getKey() ) ){
                        listaDeProdutos.remove( p );
                        filmAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        query.addChildEventListener(  childEventListener );
        //Adicione um ouvinte para eventos nos nós filhos
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener( childEventListener );//remove o ouvinte na consulta
    }




    private void excluir(int posicao) {
        Produto prodSelecionado = listaDeProdutos.get(posicao);
                reference.child("films").child( prodSelecionado.getId() ).removeValue();
                //efetivamente remove o produto selecionado

            }

private void adicionar(int position){
    String idProduto = listaDeProdutos.get(position).getId();

    Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
    intent.putExtra("acao", "editar");
    Produto prodSelecionado = listaDeProdutos.get(position);



    intent.putExtra("idFilm", idProduto);
    intent.putExtra("nome", prodSelecionado.getNome());
    intent.putExtra("genero", prodSelecionado.getGenero());
    intent.putExtra("sinopse", prodSelecionado.getSinopse());

    startActivity(intent);


}



    @Override
    protected void onRestart() {
        super.onRestart();
        carregarLista();
    }

    private void carregarLista(){
        if( listaDeProdutos.size() == 0 ){
            Produto fake = new Produto("Lista Vazia ", "");
            listaDeProdutos.add( fake );

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add( "Novo item" );
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSair) {
            auth.signOut(); // Logoof do user atual
        }
        if( id == R.id.menuAddProduto){
            Intent intent = new Intent( MainActivity.this, FormularioActivity.class);
            intent.putExtra("acao", "inserir");
            startActivity( intent );
        }
        return super.onOptionsItemSelected(item);
    }



}