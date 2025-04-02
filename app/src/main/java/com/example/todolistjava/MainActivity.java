package com.example.todolistjava;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todolistjava.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTask;
    private ListView listTasks;
    private Button buttonAdd;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private DatabaseHelper dbHelper; // Instância do banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTask = findViewById(R.id.EditTask);
        listTasks = findViewById(R.id.ListTasks);
        buttonAdd = findViewById(R.id.ButtonAdd);

        dbHelper = new DatabaseHelper(this); // Inicializa o banco de dados
        items = dbHelper.obterTodasTarefas(); // Carrega tarefas do banco
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listTasks.setAdapter(itemsAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarTarefa(view);
            }
        });

        listTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return remove(position);
            }
        });
    }

    public void adicionarTarefa(View view) {
        String tarefa = editTask.getText().toString().trim();

        if (!tarefa.isEmpty()) {
            if (dbHelper.adicionarTarefa(tarefa)){
            itemsAdapter.add(tarefa);
            editTask.setText("");
            Toast.makeText(getApplicationContext(), "Tarefa adicionada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao adicionar tarefa!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Informe uma tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean remove(int position) {
        String tarefa = items.get(position);
        dbHelper.removerTarefa(tarefa);
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Tarefa removida!", Toast.LENGTH_LONG).show();
        return true;
    }
}