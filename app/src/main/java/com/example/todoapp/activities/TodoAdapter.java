package com.example.todoapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.EnvConfig;
import com.example.todoapp.R;
import com.example.todoapp.models.Todo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TodoAdapter extends BaseAdapter {

    private List<Todo> listTodo;
    private LayoutInflater inflater;
    private Context context;

    public TodoAdapter(Context context, List<Todo> listTodo) {
        this.listTodo = listTodo;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listTodo.size();
    }

    @Override
    public Object getItem(int i) {
        return listTodo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listTodo.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.todo_fragment, null);
            viewHolder = new ViewHolder();
            viewHolder.todoTitle = view.findViewById(R.id.todo_fragment_title);
            viewHolder.todoDescription = view.findViewById(R.id.todo_fragment_description);
            viewHolder.clearButton = view.findViewById(R.id.todo_fragment_clear);
            viewHolder.doneButton = view.findViewById(R.id.todo_fragment_done);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Todo todo = (Todo) getItem(i);
        viewHolder.todoTitle.setText(todo.getTitle());
        viewHolder.todoDescription.setText(todo.getDescription());
        viewHolder.doneButton.setOnClickListener(v -> {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://" + EnvConfig.IP + ":" + EnvConfig.PORT + "/todo/api/mark-as-done?" +
                    "id=" + todo.getId();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {},
                    error -> {}
            );
            queue.add(stringRequest);
            listTodo.remove(todo);
            notifyDataSetChanged();
        });
        viewHolder.clearButton.setOnClickListener(v -> {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://" + EnvConfig.IP + ":" + EnvConfig.PORT + "/todo/api/delete-todo?" +
                    "id=" + todo.getId();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {},
                    error -> {}
            );
            queue.add(stringRequest);
            listTodo.remove(todo);
            notifyDataSetChanged();
        });
        return view;
    }

    static class ViewHolder {
        TextView todoTitle;
        TextView todoDescription;
        Button doneButton;
        Button clearButton;
    }


}
