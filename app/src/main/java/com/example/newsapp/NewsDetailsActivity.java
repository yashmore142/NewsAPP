package com.example.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {
    String title,desc,content,imageURL,url;
    private TextView tittleTV,subDesTV,contentTV;
    private ImageView imgNews;
    private Button btnReadAllNews;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        tittleTV = findViewById(R.id.idTxtTitle);
        subDesTV = findViewById(R.id.idTxtSubDescription);
        contentTV = findViewById(R.id.idTxtContent);

        imgNews = findViewById(R.id.imageNews);
        btnReadAllNews = findViewById(R.id.btnReadAllNews);

        tittleTV.setText(title);
        subDesTV.setText(desc);
        contentTV.setText(content);
      //  Picasso.get().load(imageURL).into(imgNews);


        btnReadAllNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
}
