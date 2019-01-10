package agendafirebase.br.com.savioviana.agendafirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTask;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTaskAuth;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView textForgot;
    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private SignInButton btnSignIn;

    private FirebaseAuth auth;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();

        googleApiConnection();

        clicksEvents();
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseTaskAuth.getFirebaseAuth();
        //FirebaseTask.AGENDA = FirebaseTask.DATABASE.getReference("agenda"+FirebaseTaskAuth.getId());
        /*Log.i("meulog", "Auth: "+auth.getUid());
        if (auth.getUid() != null){

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
            Log.i("meulog", "NAo NULO");
        }else{
            Log.i("meulog", "NULO");
        }*/
    }

    private void googleApiConnection() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }//conecta a api do google;

    private void clicksEvents() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "E-mail ou senha invalida", Toast.LENGTH_SHORT).show();
                }else{
                    login(email, password);
                }


            }
        });//dispara o evento de login com email e senha

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });//direcionar para activity de cadastro

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });//direciona para activity de reedefinir senha


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });//dispara evendo de login com a conta google

    }//eventos de click dos bottons

    private void signIn() {

        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, 1);

    }//função para login com conta google

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseLoginGoogle(account);

            }else{
                Toast.makeText(this, "Falha na requisição", Toast.LENGTH_SHORT).show();
            }

        }

    }//verifica resultado da activity

    private void firebaseLoginGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseTask.AGENDA = FirebaseTask.DATABASE.getReference("agenda"+FirebaseTaskAuth.getId());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(LoginActivity.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//metodo para logar com a conta google

    private void login(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseTask.AGENDA = FirebaseTask.DATABASE.getReference("agenda"+FirebaseTaskAuth.getId());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    
                }else{
                    Toast.makeText(LoginActivity.this, "Email ou senha incorreta", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//função para login com email e senha


    public void initializeComponents(){
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        textForgot = findViewById(R.id.textForgot);

        btnSignIn = findViewById(R.id.btnSignIn);
    }//inicializa os componentes da activity

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Falha na conexão", Toast.LENGTH_SHORT).show();
    }//implementação da onConnectionFailedListener
}
