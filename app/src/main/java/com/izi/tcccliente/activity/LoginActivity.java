package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.config.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    //layout
    private TextInputEditText inputLoginUsuario;
    private TextInputEditText inputLoginSenha;
    private Button btnLogar;
    private Button btnesqueci_Senha;


    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        inicializarComponentes();


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    if(inputLoginUsuario.getText().toString()!=""|| inputLoginSenha.getText().toString()!="")
                    {
                        logarComFirebase(inputLoginUsuario.getText().toString(), inputLoginSenha.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Favor preencher  todos os dados de login", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this, "Favor preencher os dados de login", Toast.LENGTH_LONG).show();
                }


            }
        });


        btnesqueci_Senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(reset);
            }
        });

    }

    private void logarComFirebase(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TagLoginCerto", "signInWithEmail:success");

                            final FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference reference = mDatabase
                                    .child("cliente")
                                    .child(user.getUid());

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){
                                        Intent mapa = new Intent(LoginActivity.this, ClienteActivity.class);
                                        startActivity(mapa);
                                        finish();

                                    }else {
                                        Toast.makeText(LoginActivity.this, "Favor Logar com uma conta valida", Toast.LENGTH_LONG).show();
                                        mAuth.getInstance().signOut();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TagLoginErrado", "Email ou Senha invalidos!", task.getException());
                            Toast.makeText(LoginActivity.this, "Erro! E-mail ou senha inválidos",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void inicializarComponentes(){
        inputLoginUsuario = findViewById(R.id.inputLoginUsuario);
        inputLoginSenha = findViewById(R.id.inputLoginSenha);
        btnLogar = findViewById(R.id.btnLogar);
        btnesqueci_Senha=findViewById(R.id.btnEsquecer_Senha);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = ConfiguracaoFirebase.getFirebase();

    }
}
