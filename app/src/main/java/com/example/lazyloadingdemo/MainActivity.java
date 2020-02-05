package com.example.lazyloadingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    RecyclerView  recyclerView;
    List<Item> items = new ArrayList<>();
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random10Data();
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(recyclerView, this,items);
        recyclerView.setAdapter(adapter);

        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if(items.size() <= 50) // Change max size
                {
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());

                            //Random more data
                            int index = items.size();
                            int end = index+10;
                            for(int i=index;i<end;i++)
                            {
                                String name = UUID.randomUUID().toString();
                                Item item = new Item(name,name.length());
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    },3000); // Time to load
                }else{
                    Toast.makeText(MainActivity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void random10Data() {
        for(int i =0;i<10;i++)
        {
            String name = UUID.randomUUID().toString();
            Item item = new Item(name,name.length());
            items.add(item);
        }
    }
}
