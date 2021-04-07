package com.example.firebaseassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    FirebaseUser current_user;

    NavController nav_control;

    EditText edt_email, edt_password;
    TextView tv_register;
    Button btn_login;

    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.login_email);
        edt_password = view.findViewById(R.id.login_password);

        btn_login = view.findViewById(R.id.login_btn);
        tv_register = view.findViewById(R.id.login_tv_registerHere);

        nav_control = Navigation.findNavController(getActivity(), R.id.host_fragment);

        tv_register.setOnClickListener(view1->{
            nav_control.navigate(R.id.registerFragment);
        });

        btn_login.setOnClickListener(view2->{

            if (!checkEmptyFields())
            {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                loginUser(email,password);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        current_user = firebaseAuth.getCurrentUser();

        if (current_user != null)
        {
            updateUI(current_user);
            Toast.makeText(getActivity().getApplicationContext(),"User Already Logged In",Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser(String email, String password)
    {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(getActivity(), task -> {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Login Success!", Toast.LENGTH_SHORT).show();
                            current_user = firebaseAuth.getCurrentUser();
                            updateUI(current_user);
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    private void updateUI(FirebaseUser current_user)
    {
        requireActivity().finish();
        Intent myIntent = new Intent(requireActivity(), DashboardActivity.class);
        myIntent.putExtra("user", current_user);
        requireActivity().startActivity(myIntent);
    }

    private boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email required!");
            edt_email.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_password.getText().toString()))
        {
            edt_password.setError("Password required!");
            edt_password.requestFocus();
            return true;
        }
        return false;
    }
}