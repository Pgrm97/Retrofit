package com.pucmm.retrofit;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Post> elements;
    private List<Comment> elements_comment;

    @Override
    public void onBackPressed() {
        CallPost();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CallPost();
    }

    private class PostAdapterComment extends RecyclerView.Adapter<PostAdapterComment.MyViewHolderComment> implements Serializable{

        public class MyViewHolderComment extends RecyclerView.ViewHolder{
            TextView _id;
            TextView name;
            TextView email;
            TextView body;


            public MyViewHolderComment(@NonNull View itemView) {
                super(itemView);
                _id = itemView.findViewById(R.id._id_comment);
                name = itemView.findViewById(R.id.name_comment);
                email = itemView.findViewById(R.id.email_comment);
                body = itemView.findViewById(R.id.body_comment);
            }
        }

        @NonNull
        @Override
        public MyViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_comment, parent, false);

            return new MyViewHolderComment(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderComment holder, int position) {
            Comment element = elements_comment.get(position);
            holder._id.setText(String.valueOf(element.getId()));
            holder.body.setText(element.getBody());
            holder.name.setText(element.getName());
            holder.email.setText(element.getEmail());
        }

        @Override
        public int getItemCount() {
            return elements_comment.size();
        }


    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> implements Serializable{

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView _id;
            TextView title;
            TextView body;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                _id = itemView.findViewById(R.id._id_comment);
                title = itemView.findViewById(R.id.title);
                body = itemView.findViewById(R.id.body);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post element = elements.get(position);
            holder._id.setText(String.valueOf(element.getId()));
            holder.body.setText(element.getBody());
            holder.title.setText(element.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerView = findViewById(R.id.recycle_view);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Call<List<Comment>> listCallComment = retrofit.create(APIService.class).getCommentsByPost(Integer.parseInt(String.valueOf(holder._id.getText())));

                    listCallComment.enqueue(new Callback<List<Comment>>() {
                        @Override
                        public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                            elements_comment = response.body();

                            PostAdapterComment adapter = new PostAdapterComment();

                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<Comment>> call, Throwable t) {
                            Log.e("onFailure: ", t.getMessage());
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return elements.size();
        }


    }

    private void CallPost(){
        recyclerView = findViewById(R.id.recycle_view);



        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<List<Post>> listCall = retrofit.create(APIService.class).getPosts();

        listCall.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                elements = response.body();

                PostAdapter adapter = new PostAdapter();

                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }
}