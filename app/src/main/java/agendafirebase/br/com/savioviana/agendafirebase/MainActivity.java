package agendafirebase.br.com.savioviana.agendafirebase;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import agendafirebase.br.com.savioviana.agendafirebase.DAO.AgendaDao;
import agendafirebase.br.com.savioviana.agendafirebase.adapter.TaskListAdapter;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTask;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTaskAuth;


public class MainActivity extends AppCompatActivity {


    private ListView taskList;
    private FloatingActionButton btnNewContact;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private GoogleSignInClient googleSignInClient;

    public ListView getTaskList() {
        return taskList;
    }

    public void setTaskList(ListView taskList) {
        this.taskList = taskList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

        googleApiConnection();

        if(getIntent().hasExtra("message")){
            Toast.makeText(this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(this, ""+FirebaseTaskAuth.getId(), Toast.LENGTH_LONG).show();
        clicksEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item_Logout){
            logoutGoogle();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }//identifica item do menu selecionado

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseTaskAuth.getFirebaseAuth();
        user = FirebaseTaskAuth.getFirebaseUser();

        if (user == null){
            finish();
        }
    }

    private void clicksEvents() {
        btnNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentForm = new Intent(getApplicationContext(), FormContactActivity.class);
                finish();
                intentForm.putExtra("title", "Novo Contato");

                startActivity(intentForm);

            }
        });//direciona para pagina de criar um novo contato

        createAdapter();

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
                finish();
                intent.putExtra("key", view.getTag().toString());

                startActivity(intent);

            }
        });//direciona para pagina de mostrar os detalhes do contato

        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                 FirebaseTask.AGENDA.child(view.getTag().toString()).removeValue();
                 Toast.makeText(MainActivity.this, "Contato Apagado", Toast.LENGTH_SHORT).show();
                return false;
            }
        });//dispara evento para deletar um contato

    }//inicializa os eventos de clicks

    private void logoutGoogle() {
        auth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Usuario desconectado", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        finish();
        startActivity(intent);

    }//realiza o logout da agrnda

    private void initializeComponents() {
        taskList = findViewById(R.id._taskList);
        btnNewContact = findViewById(R.id.btnAdd);
    }

    void createAdapter(){

        FirebaseTask.AGENDA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                ArrayList<AgendaDao> contactList = new ArrayList<AgendaDao>();

                for (DataSnapshot document : iterable){

                    AgendaDao contact = document.getValue(AgendaDao.class);
                    contact.setReference(document.getKey());

                    contactList.add(contact);
                }

                final TaskListAdapter taskListAdapter;
                taskListAdapter = new TaskListAdapter(getApplicationContext(), R.layout.celula_list);

                taskListAdapter.addAll(contactList);

                taskList.setAdapter(taskListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//esta função cria o meu adaptador, ou seja minha lista de contatos




    private void googleApiConnection() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }//conecta a api do google;
}


