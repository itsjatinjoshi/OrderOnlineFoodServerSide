package com.example.orderonlinefoodserverside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.orderonlinefoodserverside.Common.Common;
import com.example.orderonlinefoodserverside.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    Button btnSignIn;
    MaterialEditText etPhoneNo, etPassword;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPhoneNo = (MaterialEditText) findViewById(R.id.etPhoneNo);
        etPassword = (MaterialEditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(etPhoneNo.getText().toString().trim(), etPassword.getText().toString().trim());
            }
        });

    }

    private void signInUser(String etPhone, String etPass) {

        final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("Signin");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        final String userPhone = etPhone;
        final String userPassword = etPass;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userPhone).exists()) {
                    progressDialog.dismiss();
                    User user = dataSnapshot.child(userPhone).getValue(User.class);
                    user.setPhone(userPhone);
                    if (Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(userPassword)) {
                            Intent homeIntent = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                           } else {
                            Toast.makeText(SignIn.this, "Sorry, Wrong password !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignIn.this, "Please Login with Staff Account !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignIn.this, "User Not Exist !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
