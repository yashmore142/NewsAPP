package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.Adapeter.CategoryRVAdapter;
import com.example.newsapp.Adapeter.NewsRVAdapter;
import com.example.newsapp.Model.Articles;
import com.example.newsapp.Model.CategoryRVModel;
import com.example.newsapp.Model.NewsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{

    private RecyclerView newsRecycler,categoryRecycler;
    private ProgressBar loadingProgress;

    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private ArrayList<NewsModel> newsModelArrayList;

    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecycler=findViewById(R.id.recyclerNews);
        categoryRecycler=findViewById(R.id.recyclerCategories);
        loadingProgress=findViewById(R.id.progressLoading);

        articlesArrayList=new ArrayList<>();
        categoryRVModelArrayList=new ArrayList<>();

        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);

        newsRecycler.setLayoutManager(new LinearLayoutManager(this));

        newsRecycler.setAdapter(newsRVAdapter);
        categoryRecycler.setAdapter(categoryRVAdapter);

        getCategories();
        getNews("All");

        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://unsplash.com/photos/3n0_-gz6_cc"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://unsplash.com/photos/gz9njd0zYbQ"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://unsplash.com/photos/OgvqXGL7XO4"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://unsplash.com/photos/9HI8UJMSdZA"));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://unsplash.com/photos/fIq0tET6llw"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://unsplash.com/photos/505eectW54k"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://unsplash.com/photos/nLUb9GThIcg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://unsplash.com/photos/l-NIPb-9Njg"));


        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingProgress.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL ="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=e01c8ab733d94db6bf8de5e707f68991";
        String url ="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=e01c8ab733d94db6bf8de5e707f68991";
        String BASE_URL ="https://newsapi.org/";

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI =retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")){
            call=retrofitAPI.getAllNews(url);
        }else
        {
            call =retrofitAPI.getAllNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel=response.body();
                loadingProgress.setVisibility(View.GONE);
                ArrayList<Articles> articles=newsModel.getArticles();
                for (int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),
                            articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContact()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Fail to get News",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onCategoryClick(int position) {
        String category =categoryRVModelArrayList.get(position).getCategory();
        getNews(category);
    }
}