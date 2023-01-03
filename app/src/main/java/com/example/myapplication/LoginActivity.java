package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCadastro;
    private FirebaseAuth auth; // classe responsável pela autenticação
    private FirebaseAuth.AuthStateListener authStateListener;
    //Classe responsável por ficar "ouvindo" as mudanças
    private FirebaseUser usuario; //classe que permite manipular
    // o perfil de um usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });
        auth = FirebaseAuth.getInstance(); //instancia a Autenticação do usuário
        authStateListener = new FirebaseAuth.AuthStateListener() {
            //Fica "escutando" as mudanças
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Quando há uma alteração na autenticação do usuário
                usuario = auth.getCurrentUser(); //usuário recebe o usuário atual
                if( usuario != null ){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        auth.addAuthStateListener( authStateListener ); //Faz o registro dos "ouvintes"

    }
    private void cadastrar(){
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        if( email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Todos os campos são obrigatórios!",
                    Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword( email, senha) //Cria o usuário
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //adiciona um ouvinte no processo de criação de usuário
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Task é o objeto que diz se deu certo o processo
                            if( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            }else{
                                task.getException().toString();
                                Toast.makeText(LoginActivity.this,
                                        "Erro ao criar o user!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void entrar(){
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        if( email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Todos os campos são obrigatórios!",
                    Toast.LENGTH_LONG).show();
        }else{
            auth.signInWithEmailAndPassword( email, senha )//metodo para autenticar usuário
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            }else{
                                task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Erro ao logar!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


}