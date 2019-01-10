package agendafirebase.br.com.savioviana.agendafirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import agendafirebase.br.com.savioviana.agendafirebase.DAO.AgendaDao;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTask;
import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTaskAuth;

public class ContactViewActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtSite;
    private RatingBar rating;
    private FloatingActionButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        txtName = findViewById(R.id.name);
        txtPhone = findViewById(R.id.phone);
        txtAddress = findViewById(R.id.address);
        txtSite = findViewById(R.id.site);
        rating = findViewById(R.id.ratingBar);
        btnEdit = findViewById(R.id.btnEdit);
        rating.setIsIndicator(true);
        setTitle("Contato");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseTask.AGENDA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AgendaDao agendaDao = dataSnapshot.child(getIntent().getStringExtra("key").toString()).getValue(AgendaDao.class);

                txtName.setText(agendaDao.getName());
                txtAddress.setText(agendaDao.getAddress());
                txtSite.setText(agendaDao.getSite());
                txtPhone.setText(agendaDao.getPhone());
                rating.setRating(agendaDao.getRating());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormContactActivity.class);
                finish();
                intent.putExtra("title", "Editar Contato");
                intent.putExtra("key", getIntent().getStringExtra("key"));

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
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
