package com.example.firebasedatabaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListaProductos extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView lvlListaProductos;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaproductos);


        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        listaProductos = new ArrayList<>();

        lvlListaProductos = findViewById(R.id.lvListaProductos);

        ArrayAdapter<Producto> adapter = new ArrayAdapter<Producto>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listaProductos) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Producto producto = getItem(position);

                if (producto != null) {
                    text1.setText(String.format("%s: %s", producto.getCodigo(), producto.getNombre()));
                    text2.setText(String.format("Stock: %d, Precio: %.2f", producto.getStock(), producto.getPrecioCosto()));
                }

                return view;
            }
        };
        Button btnListaRegresar = findViewById(R.id.btnListaRegresar);
        btnListaRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaProductos.this, Login.class);
                startActivity(intent);
            }
        });

        lvlListaProductos.setAdapter(adapter);

        cargarListaProductos();
    }

    private void cargarListaProductos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaProductos.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        listaProductos.add(producto);
                    }
                }

                ((ArrayAdapter<Producto>) lvlListaProductos.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
