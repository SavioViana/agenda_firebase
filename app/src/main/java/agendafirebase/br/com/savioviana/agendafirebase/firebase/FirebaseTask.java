package agendafirebase.br.com.savioviana.agendafirebase.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseTask {



    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

    public static DatabaseReference AGENDA = DATABASE.getReference("agenda");


}
