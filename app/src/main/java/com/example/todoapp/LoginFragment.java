package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private EditText loginUsername;
    private EditText loginPassword;
    private Button buttonLogin;

    private UserViewModel userViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize UI components
        loginUsername = rootView.findViewById(R.id.loginUsername);
        loginPassword = rootView.findViewById(R.id.loginPassword);
        buttonLogin = rootView.findViewById(R.id.buttonLogin);

        // Get an instance of UserViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Set click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve entered username and password
                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user exists in the database
                    userViewModel.getUserByUsername(username).observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if (user != null && user.getPassword().equals(password)) {
                                // Login successful, navigate to the home screen or any other desired destination
                                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                navigateToMainActivity();
                            } else {
                                // Invalid credentials, display error message
                                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return rootView;
    }

    private void navigateToMainActivity() {
        // Create an intent to launch the MainActivity
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Optional: Finish the current activity to prevent going back to the login screen
    }
}