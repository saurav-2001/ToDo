package com.example.todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private EditText signupUsername;
    private EditText signupPassword;
    private Button buttonSignUp;

    private UserViewModel userViewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialize UI components
        signupUsername = rootView.findViewById(R.id.signupUsername);
        signupPassword = rootView.findViewById(R.id.signupPassword);
        buttonSignUp = rootView.findViewById(R.id.buttonSignup);

        // Get an instance of UserViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Set click listener for the sign-up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve entered username and password
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new User object
                    User newUser = new User(username, password);

                    // Insert the new user using the UserViewModel
                    userViewModel.insertUser(newUser);

                    // Display success message and navigate to the login page
                    Toast.makeText(requireContext(), "Sign-up successful. Please log in.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}