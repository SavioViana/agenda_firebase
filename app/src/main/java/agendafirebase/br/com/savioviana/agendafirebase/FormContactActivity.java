package agendafirebase.br.com.savioviana.agendafirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import agendafirebase.br.com.savioviana.agendafirebase.DAO.AgendaDao;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTask;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTaskAuth;

public class FormContactActivity extends AppCompatActivity {

    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText site;
    private RatingBar rating;
    private FloatingActionButton btnSave;


    private AgendaDao agenda;

    public AgendaDao getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaDao agenda) {
        this.agenda = agenda;
    }

    public EditText getName() {
        return name;
    }

    public void setName(EditText name) {
        this.name = name;
    }

    public EditText getAddress() {
        return address;
    }

    public void setAddress(EditText address) {
        this.address = address;
    }

    public EditText getPhone() {
        return phone;
    }

    public void setPhone(EditText phone) {
        this.phone = phone;
    }

    public EditText getSite() {
        return site;
    }

    public void setSite(EditText site) {
        this.site = site;
    }

    public RatingBar getRating() {
        return rating;
    }

    public void setRating(RatingBar rating) {
        this.rating = rating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contact);

        this.name = findViewById(R.id.editName);
        this.address = findViewById(R.id.editAddress);
        this.phone = findViewById(R.id.editPhone);
        this.site = findViewById(R.id.editSite);
        this.rating = findViewById(R.id.ratingBar);
        this.btnSave = findViewById(R.id.btn_save);

        setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(!getIntent().hasExtra("key")) {


            this.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getName().getText().toString().isEmpty() || getPhone().getText().toString().isEmpty()) {
                        Toast.makeText(FormContactActivity.this, "nome e telefone obrigatorio!", Toast.LENGTH_SHORT).show();
                    } else {

                        agenda = new AgendaDao();
                        agenda.setName(getName().getText().toString());
                        agenda.setPhone(getPhone().getText().toString());
                        agenda.setRating(getRating().getRating());
                        if (!getAddress().getText().toString().isEmpty())
                            agenda.setAddress(getAddress().getText().toString());

                        if (!getSite().getText().toString().isEmpty())
                            agenda.setSite(getSite().getText().toString());

                        agenda.save();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("message", "Contato Salvo");

                        startActivity(intent);

                    }

                }
            });

        }else{

            FirebaseTask.AGENDA.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AgendaDao agendaDao = dataSnapshot.child(getIntent().getStringExtra("key").toString()).getValue(AgendaDao.class);

                    name.setText(agendaDao.getName());
                    address.setText(agendaDao.getAddress());
                    site.setText(agendaDao.getSite());
                    phone.setText(agendaDao.getPhone());
                    rating.setRating(agendaDao.getRating());

                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            this.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getName().getText().toString().isEmpty() || getPhone().getText().toString().isEmpty()) {
                        Toast.makeText(FormContactActivity.this, "nome e telefone obrigatorio!", Toast.LENGTH_SHORT).show();
                    } else {

                        agenda = new AgendaDao();
                        agenda.setName(getName().getText().toString());
                        agenda.setPhone(getPhone().getText().toString());
                        agenda.setRating(getRating().getRating());
                        if (!getAddress().getText().toString().isEmpty())
                            agenda.setAddress(getAddress().getText().toString());

                        if (!getSite().getText().toString().isEmpty())
                            agenda.setSite(getSite().getText().toString());

                        agenda.setReference(getIntent().getStringExtra("key"));

                        agenda.save();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("message", "Contato Salvo");
                        startActivity(intent);

                    }

                }
            });


        }

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseTaskAuth.getFirebaseUser() == null){
            finish();
        }
    }
}
