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

    public boolean getSpecialCharacter(String word) {

        Pattern regex = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = regex.matcher(word);
        return matcher.find();
    }

    public boolean isContainValid(String email) {

        return email.contains("@");
    }

    private EditText editTextEmail;

    private EditText editTextPassword;

    private EditText editTextPasswordConfirmation;

    private EditText editTextAge;

    private EditText editTextName;

    private EditText editTextUsername;

    private RadioButton radioButtonGenderMale;

    private RadioButton radioButtonGenderFem;

    private TextView textViewGender;

    private Button buttonRegister;

    private Button buttonCancel;

    private View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Sets the inputs values
        setValues();

        // Sets the actions of buttons
        setListener();
    }

    private void setValues() {

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAge = (EditText) findViewById(R.id.ageText);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPasswordConfirmation = (EditText) findViewById(R.id.password2);

        textViewGender = (TextView) findViewById(R.id.textViewGender);

        radioButtonGenderFem = (RadioButton) findViewById(R.id.femButton);
        radioButtonGenderMale = (RadioButton) findViewById(R.id.mascButton);

        buttonRegister = (Button) findViewById(R.id.register_button);
        buttonCancel = (Button) findViewById(R.id.register_cancel);
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

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            passwordValid = false;
        }
        else if (password.length() < minimumSize){
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            passwordValid = false;
        }
        else if (!password.equals(passwordConfirmation)) {
            editTextPasswordConfirmation.setError(getString(R.string.error_different_password));
            focusView = editTextPasswordConfirmation;
            passwordValid = false;
        } else {
            // Nothing to do
        }

        return passwordValid;
    }

    private boolean validateEmail(String email) {
        boolean emailValid = true;

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            emailValid = false;
        } else if (!isContainValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            emailValid = false;
        } else {
            // Nothing to do
        }

        return emailValid;
    }

    private boolean validateName(String name) {
        boolean nameValid = true;

        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            nameValid = false;
        } else if (getSpecialCharacter(name)) {
            editTextName.setError(getString(R.string.error_character));
            focusView = editTextName;
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
        if (TextUtils.isEmpty(editTextAge.getText().toString()) || age < minimumAge || age > maximumAge) {
            focusView = editTextAge;
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

        if (!radioButtonGenderMale.isChecked() && !radioButtonGenderFem.isChecked()) {
            textViewGender.setError(getString(R.string.error_invalid_genre));
            focusView = textViewGender;
            genderValid = false;
        } else {
            // Nothing to do
        }

        return genderValid;
    }

    private boolean validateUsername(String username) {
        boolean usernameValid = true;

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            usernameValid = false;
        } else {
            // Nothing to do
        }

        return usernameValid;
    }

    private String validateChoiceGender(){
        String gender = "";

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