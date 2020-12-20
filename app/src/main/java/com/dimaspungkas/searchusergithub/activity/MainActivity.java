package com.dimaspungkas.searchusergithub.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dimaspungkas.searchusergithub.R;
import com.dimaspungkas.searchusergithub.adapter.RecyclerViewAdapter;
import com.dimaspungkas.searchusergithub.api.APIClient;
import com.dimaspungkas.searchusergithub.api.APIInterface;
import com.dimaspungkas.searchusergithub.model.MainData;
import com.dimaspungkas.searchusergithub.model.MainDataResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView = null;
    private CoordinatorLayout coordinatorLayout;
    public RecyclerViewAdapter adapter;
    MainDataResponse mainDataList;
    List<MainData> dataList = new ArrayList<>();

    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private RelativeLayout emptyView;
    private TextView errorTitle, errorMessage;
    private ProgressBar progressBarBottom, progressBar;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyView = findViewById(R.id.empty_view);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        coordinatorLayout = findViewById(R.id.container);
        recyclerView = findViewById(R.id.recycler_view);
        progressBarBottom = findViewById(R.id.progressBarBottom);
        progressBar = findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                currentPage+=1;
                                progressBarBottom.setVisibility(View.VISIBLE);
                                fetchNextPage(keyword);
                            }
                        }, 1000);
                    }
                }
            }
        });

    }

    private void prepareData(MainDataResponse mainDataList) {
        adapter = new RecyclerViewAdapter(mainDataList.getItems());
        recyclerView.setAdapter(adapter);
    }

    private void fetchFirstPage(final String keyword) {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        data.put("q", keyword);
        APIInterface apiInterface = new APIClient().getService();
        Call<MainDataResponse> mainDataListCall= apiInterface.getMainDataList(data);
        mainDataListCall.enqueue(new Callback<MainDataResponse>() {
            @Override
            public void onResponse(Call<MainDataResponse> call, Response<MainDataResponse> response) {
                if (response.isSuccessful()) {
                    mainDataList = response.body();
                    if(response.body().getTotalCount() == 0){
                        if(emptyView.getVisibility() == View.INVISIBLE) {
                            mainDataList.getItems().clear();
                            prepareData(mainDataList);
                        }
                        viewError(View.VISIBLE, getString(R.string.title_error), getString(R.string.message_error));
                    }else {
                        viewError(View.INVISIBLE, "", "");
                        Toast.makeText(MainActivity.this,
                                " Sucessful",
                                Toast.LENGTH_SHORT).show();
                        prepareData(mainDataList);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You clicked too much, wait a minute", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MainDataResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,
                        "Request failed. Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNextPage(final String keyword) {
        Log.d(TAG, "loadNextPage: " + currentPage);
        Map<String, String> data = new HashMap<>();
        data.put("q", keyword);
        data.put("page", String.valueOf(currentPage));
        APIInterface apiInterface = new APIClient().getService();
        Call<MainDataResponse> mainDataListCall= apiInterface.getMainDataList(data);
        mainDataListCall.enqueue(new Callback<MainDataResponse>() {
            @Override
            public void onResponse(Call<MainDataResponse> call, Response<MainDataResponse> response) {
                if (response.isSuccessful()) {
                    MainDataResponse mainDataList2 = response.body();
                    if(mainDataList.getItems().size() != mainDataList.getTotalCount()) {
                        mainDataList.getItems().addAll(mainDataList2.getItems());
                        Log.d("new size ", mainDataList.getItems().size() + "");
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MainActivity.this, "Last Item!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please Change The Keyword", Toast.LENGTH_SHORT).show();
                }
                progressBarBottom.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MainDataResponse> call, Throwable t) {
                progressBarBottom.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,
                        "Request failed. Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu );

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.app_name));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if(emptyView.getVisibility() == View.INVISIBLE) {
                    mainDataList.getItems().clear();
                    adapter.notifyDataSetChanged();
                }
                viewError(View.VISIBLE, getString(R.string.name), getString(R.string.app_name));
                return true;
            }
        });

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                fetchFirstPage(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });


        return true;
    }

    private void viewError(int visibility, String title, String message){
        emptyView.setVisibility(visibility);
        errorTitle.setText(title);
        errorMessage.setText(message);
    }

}