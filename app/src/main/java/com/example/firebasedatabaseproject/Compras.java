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
import com.google.firebase.database.ValueEventListener;

public class Compras extends AppCompatActivity {

    private EditText etCompraCodigo, etCompraCantidad;
    private TextView tvCompraNombre, tvCompraStock, tvCompraPrecioCompra, tvCompraTotalPagar;
    private Button btnCompraBuscar, btnCompraTotalPagar, btnComprar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compras);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("productos"); // Ajusta la referencia según tu estructura


        etCompraCodigo = findViewById(R.id.compraCodigo);
        etCompraCantidad = findViewById(R.id.compraCantidad);
        tvCompraNombre = findViewById(R.id.tvCompraNombreProducto);
        tvCompraStock = findViewById(R.id.tvcompraStock);
        tvCompraPrecioCompra = findViewById(R.id.tvcompraPrecioCompra);
        tvCompraTotalPagar = findViewById(R.id.tvcompraTotalPagar);
        btnCompraBuscar = findViewById(R.id.btnCompraBuscar);
        btnCompraTotalPagar = findViewById(R.id.btncompraTotalPagar);
        btnComprar = findViewById(R.id.btnComprar);


        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarProducto();
            }
        });


        btnCompraBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto();
            }
        });


        btnCompraTotalPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularTotalPagar();
            }
        });
    }

    private void buscarProducto() {
        String codigo = etCompraCodigo.getText().toString();


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

                        tvCompraNombre.setText("Nombre: " + producto.getNombre());
                        tvCompraStock.setText("Stock: " + producto.getStock());
                        tvCompraPrecioCompra.setText("Precio de Compra: " + producto.getPrecioVenta());
                    } else {

                        Toast.makeText(Compras.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();

                        tvCompraNombre.setText("");
                        tvCompraStock.setText("");
                        tvCompraPrecioCompra.setText("");
                        tvCompraTotalPagar.setText("");
                    }
                } else {

                    Toast.makeText(Compras.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calcularTotalPagar() {
        String cantidadString = etCompraCantidad.getText().toString();
        if (cantidadString.isEmpty()) {
            Toast.makeText(this, "Ingrese la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }
        int cantidad = Integer.parseInt(cantidadString);
        double precioCompra = Double.parseDouble(tvCompraPrecioCompra.getText().toString().replace("Precio de Compra: ", ""));
        double totalPagar = cantidad * precioCompra;
        tvCompraTotalPagar.setText("Total a pagar: " + totalPagar);
    }

    private void comprarProducto() {
        String codigo = etCompraCodigo.getText().toString();
        String cantidadString = etCompraCantidad.getText().toString();
        if (codigo.isEmpty() || cantidadString.isEmpty()) {
            Toast.makeText(this, "Ingrese el código y la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }
        int cantidad = Integer.parseInt(cantidadString);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productos");
        databaseReference.child(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Producto producto = dataSnapshot.getValue(Producto.class);
                    int nuevoStock = producto.getStock() + cantidad;
                    databaseReference.child(codigo).child("stock").setValue(nuevoStock);
                    Toast.makeText(Compras.this, "Compra realizada. Nuevo stock: " + nuevoStock, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Compras.this, "Producto no encontrado en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Compras.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

