package agendafirebase.br.com.savioviana.agendafirebase.DAO;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import agendafirebase.br.com.savioviana.agendafirebase.firebase.FirebaseTask;

public class AgendaDao {

    private String reference;
    private  String name;
    private  String address;
    private String phone;
    private String site;
    private float rating;

    public float getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public AgendaDao(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public AgendaDao() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }


    public void save(){

        if(this.reference == null){

            FirebaseTask.AGENDA.push().setValue(this);

        }else{
            FirebaseTask.AGENDA.child(this.reference).setValue(this);
        }


    }

}
