package com.example.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.EnvConfig;
import com.example.todoapp.R;
import com.example.todoapp.models.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private Button addToDoButton;
    private ListView todoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        addToDoButton = findViewById(R.id.main_btn_add);
        todoListView = findViewById(R.id.main_lv_list_todo);

        addToDoButton.setOnClickListener(view -> navigateToAddActivity());

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<Todo> listTodo = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + EnvConfig.IP + ":" + EnvConfig.PORT + "/todo/api/get-todo";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Gson gson = new Gson();
                        TypeToken<Collection<Todo>> collectionType = new TypeToken<Collection<Todo>>(){};
                        ArrayList<Todo> responseTodo = gson.fromJson(response, collectionType.getType());
                        listTodo.addAll(responseTodo);
                        todoListView.setAdapter(new TodoAdapter(this, listTodo));
                    },
                    error -> {}
            );
            queue.add(stringRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddActivity() {
        Intent intent = new Intent(MainActivity.this, AddTodo.class);
        startActivity(intent);
    }
}