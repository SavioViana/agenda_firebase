package agendafirebase.br.com.savioviana.agendafirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTaskAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button btnReset;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeComponents();

        clicksEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseTaskAuth.getFirebaseAuth();
    }

    private void clicksEvents() {

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Um email foi enviado a você para recuperação da sua senha", Toast.LENGTH_LONG).show();
                    finish();;
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Email não registradd", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initializeComponents(){
        editEmail = findViewById(R.id.editEmail);
        btnReset = findViewById(R.id.btnReset);

    }
}
