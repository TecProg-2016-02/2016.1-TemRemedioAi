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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setValues();
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

    private void attemptRegister() {

        // Reset errors.
        editTextName.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextPasswordConfirmation.setError(null);
        editTextAge.setError(null);
        radioButtonGenderMale.setError(null);
        radioButtonGenderFem.setError(null);
        textViewGender.setError(null);
        editTextUsername.setError(null);

        // Store values at the time of the login attempt.
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirmation = editTextPasswordConfirmation.getText().toString();
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        int age = 0;
        String gender = null;

        boolean invalidInput = false;
        View focusView = null;

        // Validate password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            invalidInput = true;
        }
        else if (password.length() < 6){
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            invalidInput = true;
        }
        else if (!password.equals(passwordConfirmation)) {
            editTextPasswordConfirmation.setError(getString(R.string.error_different_password));
            focusView = editTextPasswordConfirmation;
            invalidInput = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            invalidInput = true;
        } else if (!isContainValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            invalidInput = true;
        }

        // Validate name
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            invalidInput = true;
        } else if (getSpecialCharacter(name)) {
            editTextName.setError(getString(R.string.error_character));
            focusView = editTextName;
            invalidInput = true;
        }

        // Validate age
        if (TextUtils.isEmpty(editTextAge.getText().toString())) {
            editTextAge.setError(getString(R.string.error_field_required));
            focusView = editTextAge;
            invalidInput = true;
        } else {
            age = Integer.parseInt(editTextAge.getText().toString());
            if (age < 0 || age > 100) {
                editTextAge.setError(getString(R.string.error_invalid_age));
            }
        }

        // Validate gender
        if (!radioButtonGenderMale.isChecked() && !radioButtonGenderFem.isChecked()) {
            textViewGender.setError(getString(R.string.error_invalid_genre));
            focusView = textViewGender;
            invalidInput = true;
        }
        else if (radioButtonGenderFem.isChecked()) gender = "Feminino";
        else if (radioButtonGenderMale.isChecked()) gender = "Masculino";

        // Validate username
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.error_field_required));
            focusView = editTextUsername;
            invalidInput = true;
        }

        if (invalidInput) {
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

    public boolean getSpecialCharacter(String word) {
        Pattern regex = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = regex.matcher(word);
        return matcher.find();
    }

    public boolean isContainValid(String email) {
        return email.contains("@");
    }
}