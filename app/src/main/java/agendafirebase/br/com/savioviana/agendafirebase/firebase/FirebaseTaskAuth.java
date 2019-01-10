package agendafirebase.br.com.savioviana.agendafirebase.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseTaskAuth {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    public FirebaseTaskAuth(){

    }

    public static FirebaseAuth getFirebaseAuth(){

        if(firebaseAuth == null){
            inicializarFirebaseAuth();
        }

        return firebaseAuth;
    }

    private static  void inicializarFirebaseAuth(){

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    firebaseUser = user;
                }

            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    public static void logout(){
        firebaseAuth.signOut();
    }


    public static String getId(){
        inicializarFirebaseAuth();

        return firebaseUser.getUid();

    }



}
