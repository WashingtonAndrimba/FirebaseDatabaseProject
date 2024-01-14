package com.example.firebasedatabaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText etUser, etPassword;
    private Button btnLogin, btnRegistrar;
    private FirebaseAuth firebaseAuth;
    private Spinner spMenuLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = FirebaseAuth.getInstance();

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        spMenuLogin = findViewById(R.id.spMenuLogin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMenuLogin.setAdapter(adapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedOption = spMenuLogin.getSelectedItem().toString();
                if (firebaseAuth.getCurrentUser() != null) {
                    loginUser(selectedOption);

                } else {
                    abrirActividadSeleccionada(selectedOption);
                }
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
            }
        });

    }
    private void loginUser(String selectedOption) {
        String email = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            showToast("Login exitoso");
                            abrirActividadSeleccionada(selectedOption);
                        } else {
                            showToast("Usuario y/o contraseña incorrectos");
                            Log.e("LoginActivity", "Error en el login", task.getException());
                        }
                    }
                });
    }

    private void abrirActividadSeleccionada(String selectedOption) {
        Intent intent;
        switch (selectedOption) {
            case "Persona":
                intent = new Intent(Login.this, PersonaActivity.class);
                break;
            case "Producto":
                intent = new Intent(Login.this, ProductoActivity.class);
                break;
            case "Inventario":
                intent = new Intent(Login.this, Inventario.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
