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
import android.util.Log;

import static junit.framework.Assert.*;

public class SignUpActivity extends AppCompatActivity {

    ParseUser user = new ParseUser();
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

        Button buttonRegister = (Button) findViewById(R.id.register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.register_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean validatePassoword(String password, String passwordConfirmation,
                                      EditText editTextPassword, EditText editTextPasswordConfirmation) {

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
            
            Log.e("SignUpActivity -> validatePassoword", 
                  + password" != " + passwordConfirmation);

            editTextPasswordConfirmation.setError(getString(R.string.error_different_password));
            focusView = editTextPasswordConfirmation; // Saves password error in variable focusView
            passwordValid = false;
        } else {
            // Nothing to do
        }


        return passwordValid;
    }

    private boolean validateEmail(String email, EditText editTextEmail) {
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

        Log.i("SignUpActivity -> validateEmail", 
              "Email = " + email);

        return emailValid;
    }

    private boolean validateName(String name, EditText editTextName) {
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

        Log.i("SignUpActivity -> validateName", 
              "Name = " + name);

        return nameValid;
    }

    private boolean validateAge(int age, EditText editTextAge) {
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

    private boolean validateGender(TextView textViewGender,  
                                   RadioButton radioButtonGenderMale,
                                   RadioButton radioButtonGenderFem) {
        
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

    private boolean validateUsername(String username, EditText editTextUsername) {
        boolean usernameValid = true;

        // verified if variablea username is empty
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername; // Saves focusView error in variable focusView
            usernameValid = false;
        } else {
            // Nothing to do
        }

        Log.i("SignUpActivity -> validateUsername", 
              "Username = " + username);
        
        return usernameValid;
    }

    private String validateChoiceGender(RadioButton radioButtonGenderMale,
                                        RadioButton radioButtonGenderFem){
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

    private void attemptRegister() {

        // Store values at the time of the login attempt.
        // Checks inputs password
        EditText editTextPassword = (EditText) findViewById(R.id.password);
        EditText editTextPasswordConfirmation = (EditText) findViewById(R.id.password2);
        editTextPassword.setError(null);
        editTextPasswordConfirmation.setError(null);
        String password = editTextPassword.getText().toString();
        String passwordConfirmation = editTextPasswordConfirmation.getText().toString();
        boolean validPassoword = validatePassoword(password, passwordConfirmation, 
                                                   editTextPassword, editTextPasswordConfirmation);

        // Checks inputs email
        EditText editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.setError(null);
        String email = editTextEmail.getText().toString();
        assertTrue(validateEmail(email, editTextEmail) != null);
        boolean validEmail = validateEmail(email, editTextEmail);

        // Checks inputs name
        EditText editTextName = (EditText) findViewById(R.id.name);
        editTextName.setError(null);
        String name = editTextName.getText().toString();
        assertTrue(validateName(name, editTextName) != null);
        boolean validName = validateName(name, editTextName);

        // Checks inputs age
        int age = 0;
        EditText editTextAge = (EditText) findViewById(R.id.ageText);
        editTextAge.setError(null);
        assertTrue(validateAge(age, editTextAge) != null);
        boolean validAge = validateAge(age, editTextAge);

        // Check inputs gender
        RadioButton radioButtonGenderMale = (RadioButton) findViewById(R.id.mascButton);
        RadioButton radioButtonGenderFem = (RadioButton) findViewById(R.id.femButton);
        TextView textViewGender = (TextView) findViewById(R.id.textViewGender);
        radioButtonGenderMale.setError(null);
        radioButtonGenderFem.setError(null);
        textViewGender.setError(null);
        assertTrue(validateGender(textViewGender, radioButtonGenderMale, 
                                  radioButtonGenderFem) != null);
        boolean validGender = validateGender(textViewGender, radioButtonGenderMale, 
                                                             radioButtonGenderFem);
        String gender = validateChoiceGender(radioButtonGenderMale, radioButtonGenderFem);

        // Checks inputs username
        EditText editTextUsername = (EditText) findViewById(R.id.username);
        editTextUsername.setError(null);
        String username = editTextUsername.getText().toString();
        assertTrue(validateUsername(username, editTextUsername) != null);
        boolean validUsername = validateUsername(username, editTextUsername);

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