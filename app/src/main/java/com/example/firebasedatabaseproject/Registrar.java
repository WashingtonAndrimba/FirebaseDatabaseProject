package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registrar extends AppCompatActivity {

    private EditText etCorreo, etContraseña;
    private Button btnRegistrarse;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        etCorreo = findViewById(R.id.lvlRegistroIngreseCorreo);
        etContraseña = findViewById(R.id.lvlRegistroIngreseContraseña);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        mAuth = FirebaseAuth.getInstance();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String correo = etCorreo.getText().toString().trim();
        String contraseña = etContraseña.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Registrar.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(Registrar.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
