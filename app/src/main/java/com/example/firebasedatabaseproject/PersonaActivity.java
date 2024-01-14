package com.example.firebasedatabaseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonaActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String personaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persona);

        databaseReference = FirebaseDatabase.getInstance().getReference("personas");
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personaId == null) {
                    guardarDatosPersona();
                } else {
                    actualizarDatosPersona();
                }
            }
        });


        Button btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatosPersona();
            }
        });
        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                eliminarDatosPersona();
            }
        });
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonaActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void guardarDatosPersona() {
        String cedula = ((EditText) findViewById(R.id.etCedula)).getText().toString();
        String nombre = ((EditText) findViewById(R.id.etNombreRegistro)).getText().toString();
        String genero = obtenerGeneroSeleccionado();
        String provincia = ((EditText) findViewById(R.id.etProvincia)).getText().toString();
        String email = ((EditText) findViewById(R.id.eteEmail)).getText().toString();

        Persona persona = new Persona(cedula, nombre, genero, provincia, email);

        databaseReference.child(cedula).setValue(persona);
    }

    private void obtenerDatosPersona() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Persona");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idPersonaActualizar = input.getText().toString();
                cargarDatosParaActualizar(idPersonaActualizar);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void cargarDatosParaActualizar(String idPersonaActualizar) {
        DatabaseReference personaRef = databaseReference.child(idPersonaActualizar);
        personaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Persona persona = dataSnapshot.getValue(Persona.class);
                    llenarCamposConDatos(persona);
                    personaId = idPersonaActualizar;
                } else {
                    Toast.makeText(PersonaActivity.this, "ID de Persona no válido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PersonaActivity.this, "Error al obtener datos de Persona", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void llenarCamposConDatos(Persona persona) {
        ((EditText) findViewById(R.id.etCedula)).setText(persona.getCedula());
        ((EditText) findViewById(R.id.etNombreRegistro)).setText(persona.getNombre());
        ((EditText) findViewById(R.id.etProvincia)).setText(persona.getProvincia());
        ((EditText) findViewById(R.id.eteEmail)).setText(persona.getEmail());

        RadioGroup radioGroupGenero = findViewById(R.id.radioGroupGenero);
        if ("Hombre".equals(persona.getGenero())) {
            radioGroupGenero.check(R.id.rbHombre);
        } else if ("Mujer".equals(persona.getGenero())) {
            radioGroupGenero.check(R.id.rbMujer);
        }
    }


    private void actualizarDatosPersona() {

        String cedula = ((EditText) findViewById(R.id.etCedula)).getText().toString();
        String nombre = ((EditText) findViewById(R.id.etNombreRegistro)).getText().toString();
        String genero = obtenerGeneroSeleccionado();
        String provincia = ((EditText) findViewById(R.id.etProvincia)).getText().toString();
        String email = ((EditText) findViewById(R.id.eteEmail)).getText().toString();

        Persona personaActualizada = new Persona(cedula, nombre, genero, provincia, email);
        databaseReference.child(personaId).setValue(personaActualizada);
        Toast.makeText(this, "Datos de Persona actualizados en Firebase", Toast.LENGTH_SHORT).show();
        personaId = null;
    }

    private String obtenerGeneroSeleccionado() {
        RadioGroup radioGroupGenero = findViewById(R.id.radioGroupGenero);
        int radioButtonId = radioGroupGenero.getCheckedRadioButtonId();
        if (radioButtonId == R.id.rbHombre) {
            return "Hombre";
        } else if (radioButtonId == R.id.rbMujer) {
            return "Mujer";
        } else {
            return "";
        }
    }

    private void eliminarDatosPersona() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Persona");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cedulaPersonaEliminar = input.getText().toString();
                eliminarPersona(cedulaPersonaEliminar);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void eliminarPersona(String cedulaPersonaEliminar) {
        DatabaseReference personaRef = databaseReference.child(cedulaPersonaEliminar);
        personaRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PersonaActivity.this, "Persona eliminada de Firebase", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonaActivity.this, "Error al eliminar persona", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

