package com.example.firebaseassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    TextView tv_name, tv_email, tv_city, tv_birthdate, tv_gender;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String current_user_id;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_name = view.findViewById(R.id.profile_tv_name);
        tv_email = view.findViewById(R.id.profile_tv_email);
        tv_city = view.findViewById(R.id.profile_tv_city);
        tv_birthdate = view.findViewById(R.id.profile_tv_birthdate);
        tv_gender = view.findViewById(R.id.profile_tv_gender);
        

        readFireStore();
    }

    private void readFireStore()
    {
        DocumentReference docRef = firebaseFirestore.collection("User").document(current_user_id);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if (doc.exists())
                {
                    tv_name.setText("Name : "+doc.get("Name"));
                    tv_email.setText("Email : "+doc.get("Email"));
                    tv_city.setText("City : "+doc.get("City"));
                    tv_birthdate.setText("D.O.B : "+doc.get("Birthdate"));
                    tv_gender.setText("Gender : "+doc.get("Gender"));
                }
            }
        });
    }
}