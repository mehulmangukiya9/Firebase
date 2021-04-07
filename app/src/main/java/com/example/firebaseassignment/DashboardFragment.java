package com.example.firebaseassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardFragment extends Fragment {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    NavController navController;

    TextView txt_message;
    Button btn_logOut;


    public DashboardFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = requireActivity().getIntent();
        firebaseUser = intent.getParcelableExtra("user");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_logOut = view.findViewById(R.id.btn_signOut);
        txt_message = view.findViewById(R.id.tv_dashboard_welcome);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        readFireStore();

        btn_logOut.setOnClickListener(view1 -> {

            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), MainActivity.class));

        });
    }

    public void readFireStore()
    {
        DocumentReference docRef = firebaseFirestore.collection("User").document(firebaseUser.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if (doc.exists())
                {
                    Log.d("DashboardFragment",doc.getData().toString());

                    txt_message.setText("Welcome "+doc.get("Name") + " !");


                }

            }
        });
    }
}