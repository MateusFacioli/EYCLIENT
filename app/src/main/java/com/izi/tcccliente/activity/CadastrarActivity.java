package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.izi.tcccliente.R;
import com.izi.tcccliente.model.Cliente;

public class CadastrarActivity extends AppCompatActivity {

    private TextInputEditText inputNome;
    private TextInputEditText inputCPF;
    private TextInputEditText inputEmail;
    private TextView txtTelefone;
    private TextInputEditText inputSenha;
    private TextInputEditText confirmar_senha;
    private FloatingActionButton btnFloatNext;
    private Cliente cliente = new Cliente();

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        inicializarComponenetes();

        btnFloatNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senha = inputSenha.getText().toString();
                String consenha = confirmar_senha.getText().toString();
                if(senha.equals(consenha)) {
                    salvarCliente();
                }
                else
                {
                    Toast.makeText(CadastrarActivity.this, "Senhas não coincidem", Toast.LENGTH_LONG).show();
                }





            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    String phoneNumberString = phoneNumber.toString();
                    Toast.makeText(CadastrarActivity.this, phoneNumberString, Toast.LENGTH_SHORT).show();
                    txtTelefone.setText(phoneNumberString);
                }

                // Get email
                String email = account.getEmail();
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
    }

    private void salvarCliente(){


        String nome = inputNome.getText().toString();
        String email = inputEmail.getText().toString();
        String telefone = txtTelefone.getText().toString();
        String senha = inputSenha.getText().toString();

        if(!nome.isEmpty()){
                if(!email.isEmpty()){
                    if(!telefone.isEmpty()){
                        if(!senha.isEmpty()){

                            cliente.setNome(nome);
                            cliente.setEmail(email);
                            cliente.setTelefone(telefone);
                            cliente.setSenha(senha);


                            criaUsuarioFirebase(email, senha);

                        }else{
                            Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this, "Preencha o campo Telefone", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Preencha o campo Email", Toast.LENGTH_SHORT).show();
                }

        }else{
            Toast.makeText(this, "Preencha o campo Nome", Toast.LENGTH_SHORT).show();
        }


    }

    private void criaUsuarioFirebase(String email, String senha){

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           try{
                               // Sign in success, update UI with the signed-in user's information
                               Log.d("TagCerta", "createUserWithEmail:success");
                               FirebaseUser user = mAuth.getCurrentUser();
                               cliente.setUid(user.getUid());
                               cliente.salvar();
                               Intent inicio = new Intent(CadastrarActivity.this, ClienteActivity.class);
                               startActivity(inicio);
                               finish();
                           }catch (Exception e){
                               e.printStackTrace();
                           }


                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch ( FirebaseAuthWeakPasswordException e){
                                excecao = "Digite uma senha mais forte!";
                            }catch ( FirebaseAuthInvalidCredentialsException e){
                                excecao= "Por favor, digite um e-mail válido";
                            }catch ( FirebaseAuthUserCollisionException e){
                                excecao = "Este conta já foi cadastrada";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastrarActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



    private void inicializarComponenetes(){
        inputNome = findViewById(R.id.inputNome);
        inputEmail = findViewById(R.id.inputEmail);
        txtTelefone = findViewById(R.id.txtTelefone);
        inputSenha = findViewById(R.id.inputSenha);
        confirmar_senha=findViewById(R.id.inputConfirmaSenha);
        mAuth = FirebaseAuth.getInstance();
        btnFloatNext = findViewById(R.id.floatingNext);
    }
}
