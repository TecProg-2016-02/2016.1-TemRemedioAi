/*
 * File: SignUpActivity.java
 * Purpose: Register a user
 */

package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.Toast;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tra.gppmds.temremdioa.R;

public class SignUpActivity extends AppCompatActivity {

    ParseUser user = new ParseUser();

    private EditText editTextEmail = (EditText) findViewById(R.id.email);
    private EditText editTextPassword = (EditText) findViewById(R.id.password);
    private EditText editTextPasswordConfirmation = (EditText) findViewById(R.id.password2);
    private EditText editTextAge = (EditText) findViewById(R.id.ageText);
    private EditText editTextName = (EditText) findViewById(R.id.name);
    private EditText editTextUsername = (EditText) findViewById(R.id.username);
    private RadioButton radioButtonGenderMale = (RadioButton) findViewById(R.id.mascButton);
    private RadioButton radioButtonGenderFem = (RadioButton) findViewById(R.id.femButton);
    private TextView textViewGender = (TextView) findViewById(R.id.textViewGender);
    private Button buttonRegister = (Button) findViewById(R.id.register_button);
    private Button buttonCancel = (Button) findViewById(R.id.register_cancel);
    private View focusView = null;

    private boolean getSpecialCharacter(String word) {

        // checks for characters other than those found as parameters in compile
        // then compare with variable word 
        Pattern regex = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = regex.matcher(word);
        return matcher.find();
    }

    private boolean isContainValid(String email) {
        // checks if variable email contains "@"
        return email.contains("@");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Sets the actions of buttons
        setListener();
    }

    private void setListener() {

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean validatePassoword(String password, String passwordConfirmation) {
        boolean passwordValid = true;
        int minimumSize = 6;

        // if = true, set error message and return to focusView in all cases

        // verified if variablea password is empty
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword; // Saves password error in variable focusView
            passwordValid = false;
        }
        // verified if variablea password is too short
        else if (password.length() < minimumSize){
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword; // Saves password error in variable focusView
            passwordValid = false;
        }
        // verified if variablea password it's the same as passwordConfirmation
        else if (!password.equals(passwordConfirmation)) {
            editTextPasswordConfirmation.setError(getString(R.string.error_different_password));
            focusView = editTextPasswordConfirmation; // Saves password error in variable focusView
            passwordValid = false;
        } else {
            // Nothing to do
        }

        return passwordValid;
    }

    private boolean validateEmail(String email) {
        boolean emailValid = true;

        // if = true, set error message and return to focusView in all cases

        // verified if variablea email is empty
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail; // Saves email error in variable focusView
            emailValid = false;
        // verified if variablea email contains a invalid character
        } else if (!isContainValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail; // Saves email error in variable focusView
            emailValid = false;
        } else {
            // Nothing to do
        }

        return emailValid;
    }

    private boolean validateName(String name) {
        boolean nameValid = true;

        // if = true, set error message and return to focusView in all cases

        // verified if variablea name is empty
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName; // Saves name error in variable focusView
            nameValid = false;
        // verified if variablea name contains a special character
        } else if (getSpecialCharacter(name)) {
            editTextName.setError(getString(R.string.error_character));
            focusView = editTextName; // Saves name error in variable focusView
            nameValid = false;
        } else {
            // Nothing to do
        }

        return nameValid;
    }

    private boolean validateAge(int age) {
        boolean ageValid = true;
        int minimumAge = 0;
        int maximumAge = 100;

        age = Integer.parseInt(editTextAge.getText().toString());
        // verified if variablea age is empty or if off limits
        if (TextUtils.isEmpty(editTextAge.getText().toString()) || age < minimumAge || age > maximumAge) {
            focusView = editTextAge; // Saves age error in variable focusView
            ageValid = false;
            if (age < minimumAge || age > maximumAge) {
                editTextAge.setError(getString(R.string.error_invalid_age));
            } else {
                editTextAge.setError(getString(R.string.error_field_required));
            }
        } else {
            // Nothing to do
        }

        return ageValid;
    }

    private boolean validateGender() {
        boolean genderValid = true;

        // verified if the button was selected
        if (!radioButtonGenderMale.isChecked() && !radioButtonGenderFem.isChecked()) {
            textViewGender.setError(getString(R.string.error_invalid_genre));
            focusView = textViewGender; // Saves gender error in variable focusView
            genderValid = false;
        } else {
            // Nothing to do
        }

        return genderValid;
    }

    private boolean validateUsername(String username) {
        boolean usernameValid = true;

        // verified if variablea username is empty
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername; // Saves focusView error in variable focusView
            usernameValid = false;
        } else {
            // Nothing to do
        }

        return usernameValid;
    }

    private String validateChoiceGender(){
        String gender = "";

        // check which sex choice was made
        if (radioButtonGenderFem.isChecked()) {
            gender = "Feminino";
        } else if (radioButtonGenderMale.isChecked()) {
            gender = "Masculino";
        } else {
            // Nothing to do
        }

        return gender;
    }

    private void resetErrors(){
        editTextEmail.setError(null);
        editTextName.setError(null);
        editTextPassword.setError(null);
        editTextPasswordConfirmation.setError(null);
        editTextAge.setError(null);
        radioButtonGenderMale.setError(null);
        radioButtonGenderFem.setError(null);
        textViewGender.setError(null);
        editTextUsername.setError(null);
    }

    private void attemptRegister() {

        resetErrors();

        // Store values at the time of the login attempt.
        // Checks inputs password
        String password = editTextPassword.getText().toString();
        String passwordConfirmation = editTextPasswordConfirmation.getText().toString();
        boolean validPassoword = validatePassoword(password, passwordConfirmation);

        // Checks inputs email
        String email = editTextEmail.getText().toString();
        boolean validEmail = validateEmail(email);

        // Checks inputs name
        String name = editTextName.getText().toString();
        boolean validName = validateName(name);

        // Checks inputs age
        int age = 0;
        boolean validAge = validateAge(age);

        // Check inputs gender
        boolean validGender = validateGender();
        String gender = validateChoiceGender();

        // Checks inputs username
        String username = editTextUsername.getText().toString();
        boolean validUsername = validateUsername(username);

        if (!validPassoword || !validEmail || !validName || !validAge || !validGender || validUsername) {
            // Show errors for user
            focusView.requestFocus();

        } else {

            user.setEmail(email);
            user.setPassword(password);
            user.setUsername(username);
            user.put("Name", name);
            user.put("Age" , age);
            user.put("Gender" , gender);
            user.signUpInBackground ( new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null ) {
                        Toast.makeText(getApplicationContext(), "Cadastrado efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro no cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            finish();
        }
    }


}