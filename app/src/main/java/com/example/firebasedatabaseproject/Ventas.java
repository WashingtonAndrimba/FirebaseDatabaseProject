package com.example.firebasedatabaseproject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Ventas extends AppCompatActivity {

    private EditText etVentasCodigo, etVentasCantidad;
    private TextView tvVentasNombre, tvStock, tvPrecioVentas, tvVentasTotalPagar;
    private Button btnVentasBuscar, btnVentasTotalPagar, btnVender;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventas);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("productos"); // Ajusta la referencia según tu estructura

        etVentasCodigo = findViewById(R.id.lvlVentasCodigo);
        etVentasCantidad = findViewById(R.id.lvlCantidad);
        tvVentasNombre = findViewById(R.id.tvVentasNombre);
        tvStock = findViewById(R.id.tvStock);
        tvPrecioVentas = findViewById(R.id.tvPrecioVentas);
        tvVentasTotalPagar = findViewById(R.id.tvVentasTotalPagar);
        btnVentasBuscar = findViewById(R.id.btnVentasBuscar);
        btnVentasTotalPagar = findViewById(R.id.btnVentasTotalPagar);
        btnVender = findViewById(R.id.btnVender);

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                venderProducto();
            }
        });

        btnVentasBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto();
            }
        });

        btnVentasTotalPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularTotalPagar();
            }
        });
    }

    private void buscarProducto() {
        String codigo = etVentasCodigo.getText().toString();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código del producto", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(codigo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {

                        Producto producto = dataSnapshot.getValue(Producto.class);

                        tvVentasNombre.setText("Nombre: " + producto.getNombre());
                        tvStock.setText("Stock: " + producto.getStock());
                        tvPrecioVentas.setText("Precio de Ventas: " + producto.getPrecioVenta());
                    } else {

                        Toast.makeText(Ventas.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();

                        tvVentasNombre.setText("");
                        tvStock.setText("");
                        tvPrecioVentas.setText("");
                        tvVentasTotalPagar.setText("");
                    }
                } else {

                    Toast.makeText(Ventas.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calcularTotalPagar() {
        String cantidadString = etVentasCantidad.getText().toString();

        if (cantidadString.isEmpty()) {
            Toast.makeText(this, "Ingrese la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadString);
        int stock = Integer.parseInt(tvStock.getText().toString().replace("Stock: ", ""));
        if (cantidad > stock) {
            Toast.makeText(this, "No hay stock suficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        double precioVentas = Double.parseDouble(tvPrecioVentas.getText().toString().replace("Precio de Ventas: ", ""));
        double totalPagar = cantidad * precioVentas;
        tvVentasTotalPagar.setText("Total a pagar: " + totalPagar);
    }

    private void venderProducto() {
        String codigo = etVentasCodigo.getText().toString();
        String cantidadString = etVentasCantidad.getText().toString();
        if (codigo.isEmpty() || cantidadString.isEmpty()) {
            Toast.makeText(this, "Ingrese el código y la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadString);
        int stock = Integer.parseInt(tvStock.getText().toString().replace("Stock: ", ""));
        if (cantidad > stock) {
            Toast.makeText(this, "No hay stock suficiente", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(codigo).child("stock").setValue(stock - cantidad).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Ventas.this, "Venta realizada con éxito", Toast.LENGTH_SHORT).show();
                    etVentasCodigo.setText("");
                    etVentasCantidad.setText("");
                    tvVentasNombre.setText("");
                    tvStock.setText("");
                    tvPrecioVentas.setText("");
                    tvVentasTotalPagar.setText("");
                } else {
                    Toast.makeText(Ventas.this, "Error al realizar la venta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
