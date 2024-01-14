package com.example.firebasedatabaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Inventario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario);

        Spinner spMenu = findViewById(R.id.spMenu);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_menu, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMenu.setAdapter(adapter);

        spMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        abrirVentanaVentas();
                        break;
                    case 1:
                        abrirVentanaCompras();
                        break;
                    case 2:
                        abrirVentanaListaProductos();
                        break;
                    case 3:
                        cerrarSesion();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void abrirVentanaVentas() {
        Intent intentVentas = new Intent(this, Ventas.class);
        startActivity(intentVentas);
    }

    private void abrirVentanaCompras() {
        Intent intentCompras = new Intent(this, Compras.class);
        startActivity(intentCompras);
    }

    private void abrirVentanaListaProductos() {
        Intent intentListaProductos = new Intent(this, ListaProductos.class);
        startActivity(intentListaProductos);
    }

    private void cerrarSesion() {

        Intent intentCerrarSesion = new Intent(this, Login.class);
        startActivity(intentCerrarSesion);
        finish();
    }
}
