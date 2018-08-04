/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static com.parse.starter.UserListActivity.logout;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    Button signButton;
    TextView loginTextView;
    Boolean signupModeActive = true;

    EditText usernameEditText;
    EditText passwordEditText;

    RelativeLayout backgroundRelativeLayout;


    // SHOWS ALL USERS FROM PARSE SERVER INTO THE LIST VIEW

    public void showUserList() {

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        // WHEN ENTER KEY ON KEYBOARD IS PRESSED && KEY EVENT IS ACTION DOWN (i.e. WHILE KEY IS PRESSED BUT NOT LIFTED UP

        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {

            signButton(view);

        }

        return false;
    }

    // SETTING ONCLICK LISTENER SO THAT PRESSING ANYWHERE IN THE SCREEN EXCEPT EDITTEXTVIEWS WILL RESULT IN DISAPPEARING KEYBOARD

    @Override
    public void onClick(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    public  void loginButtonChanger(View view) {

        // IF THE SIGNUP MODE IS ACTIVE, CLICKING ON TEXTVIEW SHOULD CHANGE BUTTON NAME TO LOGIN AND VICE VERSA

        if(signupModeActive) {

            signupModeActive = false;
            signButton.setText("Login");
            loginTextView.setText(("or SignUp"));

        } else {

            signupModeActive = true;
            signButton.setText("SignUp");
            loginTextView.setText(("or Login"));

        }
    }


    public void signButton(View view) {

        if(signupModeActive) {

            if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {

                Toast.makeText(MainActivity.this, "Username and/or Password are/is required", Toast.LENGTH_SHORT).show();

            } else {

                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Log.i("SignUp", "Successful");

                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }

        } else {

            //IF SIGNUP MODE IS NOT ACTIVE, i.e. USER IS LOGGING IN

            if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {

                Toast.makeText(MainActivity.this, "Username and/or Password are/is required", Toast.LENGTH_SHORT).show();

            } else {

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(e == null && user != null) {

                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            
                        }

                    }
                });

            }

        }

    }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram Clone");

    signButton = (Button) findViewById(R.id.signButton);

    loginTextView = (TextView) findViewById(R.id.loginTextView);

    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);

    passwordEditText.setOnKeyListener(this);

    backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

    backgroundRelativeLayout.setOnClickListener(this);

    // SHOW USER LIST IF USER IS ALREADY LOGGED IN WHEN HE STARTS APPLICATION

      if(ParseUser.getCurrentUser() != null && !logout) {

          showUserList();

      }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}