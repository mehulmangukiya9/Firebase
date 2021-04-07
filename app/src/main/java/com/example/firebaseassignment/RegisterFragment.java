package com.example.firebaseassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private NavController nav_controller;

    EditText edt_name, edt_email, edt_password, edt_confirmPassword, edt_city, edt_birthdate, edt_gender;
    Button btn_register;

    public RegisterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_name = view.findViewById(R.id.register_name);
        edt_email = view.findViewById(R.id.register_email);
        edt_password = view.findViewById(R.id.register_password);
        edt_confirmPassword = view.findViewById(R.id.register_confirmPassword);
        edt_city = view.findViewById(R.id.register_city);
        edt_birthdate = view.findViewById(R.id.register_birthdate);
        edt_gender = view.findViewById(R.id.register_gender);
        btn_register = view.findViewById(R.id.register_button);

        nav_controller = Navigation.findNavController(getActivity(), R.id.host_fragment);

        btn_register.setOnClickListener(view1 -> {

            if (!checkEmptyFields())
            {
                if (edt_password.getText().length()<6)
                {
                    edt_password.setError("Invalid Password, Password Should be at least 6 characters");
                    edt_password.requestFocus();
                }else {
                    if (!edt_password.getText().toString().equals(edt_confirmPassword.getText().toString()))
                    {
                        edt_confirmPassword.setError("Password not match!");
                        edt_confirmPassword.requestFocus();
                    }else
                    {
                        String name = edt_name.getText().toString();
                        String email = edt_email.getText().toString();
                        String password = edt_password.getText().toString();
                        String city = edt_city.getText().toString();
                        String birthdate = edt_birthdate.getText().toString();
                        String gender = edt_gender.getText().toString();

                        User user = new User(name, email, password, city, birthdate, gender);

                        createUser(user);

                    }
                }

            }

        });
    }

    private void createUser(User user)
    {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        writeIntoFireStore(user, firebaseUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void writeIntoFireStore(User user, FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",user.getName());
        userMap.put("Email",user.getEmail());
        userMap.put("City",user.getCity());
        userMap.put("Birthdate",user.getBirthdate());
        userMap.put("Gender",user.getGender());


        firebaseFirestore.collection("User").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(getActivity().getApplicationContext(),"Registration Success!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                nav_controller.navigate(R.id.loginFragment);
            }else
            {
                Toast.makeText(getActivity().getApplicationContext(),"FireStore Error!",Toast.LENGTH_LONG).show();
            }
        });
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
        else if (TextUtils.isEmpty(edt_confirmPassword.getText().toString()))
        {
            edt_confirmPassword.setError("Confirm Password required!");
            edt_confirmPassword.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_name.getText().toString()))
        {
            edt_name.setError("Name required!");
            edt_name.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_city.getText().toString()))
        {
            edt_city.setError("City required!");
            edt_city.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_birthdate.getText().toString()))
        {
            edt_birthdate.setError("Birthdate required!");
            edt_birthdate.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(edt_gender.getText().toString()))
        {
            edt_gender.setError("Gender required!");
            edt_gender.requestFocus();
            return true;
        }

        return false;
    }
}