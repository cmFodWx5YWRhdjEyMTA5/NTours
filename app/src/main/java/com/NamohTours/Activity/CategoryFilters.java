package com.NamohTours.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NamohTours.Adapter.ProductFilterAdapter;
import com.NamohTours.Model.TourCategoryFilterGroups;
import com.NamohTours.Model.TourCategoryFilterGroupsDetails;
import com.NamohTours.Model.TourCategoryFilterResponse;
import com.NamohTours.R;
import com.NamohTours.Service.ConnectionDetector;
import com.NamohTours.rest.ApiClient;
import com.NamohTours.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.NamohTours.Service.Prefs.MY_PREFS_NAME;
import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.TOKEN_KEY;

public class CategoryFilters extends AppCompatActivity {


    private static final String TAG = CategoryFilters.class.getSimpleName();

    Button btnReset, btnApply;
    TextView txtNofilter;
    LinearLayout llFilterBtnLayout;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private SharedPreferences RegisterPrefences;
    String restoretoken;
    ConnectionDetector cd;

    Toolbar toolbar;

    Integer category_id;
    String CategoryId, subCategory, Sub_Parent_Id, ParentId;

    TourCategoryFilterResponse tourCategoryResponse;
    RecyclerView recyclerView;
    List<TourCategoryFilterGroups> filterList;
    TextView productTitle;
    private ProgressBar tourFilterProgress;

    String secemp = "";
    private List<TourCategoryFilterGroupsDetails> currentSelectedItems = new ArrayList<>();
    private List<String> mtourProductfilterIds = new ArrayList<>();
    private List<String> FiltersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filters);

        toolbar = (Toolbar) findViewById(R.id.FilterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtNofilter = (TextView) findViewById(R.id.txtNoFilter);
        btnApply = (Button) findViewById(R.id.btnFilterApply);
        btnReset = (Button) findViewById(R.id.btnFilterReset);

        llFilterBtnLayout = (LinearLayout) findViewById(R.id.tour_filter_btn_layout);
        tourFilterProgress = (ProgressBar) findViewById(R.id.tour_Filter_Progress);

        tourFilterProgress.setVisibility(View.VISIBLE);

        // getting token from shared preference
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        restoretoken = prefs.getString(TOKEN_KEY, null);


        // get User Register or Not and change value to true or false

        RegisterPrefences = getSharedPreferences(Register_Preference, Context.MODE_PRIVATE);
        productTitle = (TextView) findViewById(R.id.txtFilterHeading);

        recyclerView = (RecyclerView) findViewById(R.id.tour_Filter_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryFilters.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(CategoryFilters.this, DividerItemDecoration.HORIZONTAL));
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        CategoryId = getIntent().getStringExtra("cat_id");
        subCategory = getIntent().getStringExtra("Sub");
        ParentId = getIntent().getStringExtra("parent_id");
        Sub_Parent_Id = getIntent().getStringExtra("sub_parent_id");
        FiltersList = new ArrayList<String>();


        FiltersList = getIntent().getStringArrayListExtra("Filters");

        //Creating Api Interface
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (cd.isConnectingToInternet(getApplicationContext())) {


            if (CategoryId != null) {

                category_id = Integer.parseInt(CategoryId);
                txtNofilter.setVisibility(View.GONE);
                Call<TourCategoryFilterResponse> call = apiService.getCategorybyId(restoretoken, category_id);


                call.enqueue(new Callback<TourCategoryFilterResponse>() {
                    @Override
                    public void onResponse(Call<TourCategoryFilterResponse> call, Response<TourCategoryFilterResponse> response) {

                        tourCategoryResponse = response.body();


                        String sucess;

                        if ((sucess = tourCategoryResponse.getSuccess()) != null) {

                            if (sucess.equals("true")) {

                                tourFilterProgress.setVisibility(View.GONE);


                                if ((tourCategoryResponse.getTourCategoryDetailResponses().getFilters().getFilter_groups().size()) > 0) {
                                    filterList = response.body().getTourCategoryDetailResponses().getFilters().getFilter_groups();


                                    // getting selected check box and set selected
                                    String filterIDs;

                                    for (int i = 0; i < filterList.size(); i++) {

                                        for (int k = 0; k < filterList.get(i).getTourCategoryFilterGroupsDetailses().size(); k++) {

                                            filterIDs = filterList.get(i).getTourCategoryFilterGroupsDetailses().get(k).getFilter_id();

                                            if (FiltersList != null) {
                                                for (int j = 0; j < FiltersList.size(); j++) {

                                                    if (FiltersList.get(j).equals(filterIDs)) {

                                                        filterList.get(i).getTourCategoryFilterGroupsDetailses().get(k).setSelected(true);
                                                        mtourProductfilterIds.add(FiltersList.get(j));

                                                    }

                                                }
                                            }


                                        }


                                    }


                                    // recyclerView.setAdapter(new FilterAdapter(filterList, R.layout.list_item_tour_filter, getApplicationContext()));
                                    recyclerView.setAdapter(new ProductFilterAdapter(filterList, getApplicationContext(), new ProductFilterAdapter.OnItemCheckListener() {
                                        @Override
                                        public void onItemCheck(TourCategoryFilterGroupsDetails item) {
                                            currentSelectedItems.add(item);

                                            secemp = secemp + "\n" + item.getFilter_name().toString();

                                            mtourProductfilterIds.add(item.getFilter_id());


                                        }

                                        @Override
                                        public void onItemUncheck(TourCategoryFilterGroupsDetails item) {

                                            currentSelectedItems.remove(item);
                                            mtourProductfilterIds.remove(item.getFilter_id());

                                            secemp = secemp.replace(item.getFilter_name().toString(), "");


                                        }
                                    }));

                                }

                                if ((tourCategoryResponse.getTourCategoryDetailResponses().getFilters().getFilter_groups().size()) <= 0)

                                {
                                    tourFilterProgress.setVisibility(View.INVISIBLE);
                                    txtNofilter.setVisibility(View.VISIBLE);
                                    llFilterBtnLayout.setVisibility(View.INVISIBLE);
                                }
                            }

                            if (sucess.equals("false")) {

                                tourFilterProgress.setVisibility(View.INVISIBLE);
                                txtNofilter.setVisibility(View.VISIBLE);
                                llFilterBtnLayout.setVisibility(View.INVISIBLE);


                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<TourCategoryFilterResponse> call, Throwable t) {

                        Snackbar.make(recyclerView, "Sorry , Please Try Later !", Snackbar.LENGTH_LONG).show();

                    }
                });


                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)

                    {

                        //  Toast.makeText(getApplicationContext(),"Selected Filter :"+secemp.trim(),Toast.LENGTH_SHORT).show();

                        String simple = mtourProductfilterIds.toString();
                        String filters = TextUtils.join(",", mtourProductfilterIds);


                        if (filterList != null) {


                            if (!TextUtils.isEmpty(filters)) {

                                Intent filter = new Intent(CategoryFilters.this, TourProduct.class);
                                filter.putExtra("filters", filters);
                                filter.putExtra("category", CategoryId);
                                filter.putExtra("parent_id", ParentId);
                                filter.putExtra("sub_parent_id", Sub_Parent_Id);
                                filter.putExtra("Sub", subCategory);
                                filter.putExtra("FilterCount", String.valueOf(mtourProductfilterIds.size()));
                                filter.putExtra("Filters", (ArrayList<String>) mtourProductfilterIds);
                                startActivity(filter);
                                finish();
                            } else {
                                Snackbar.make(btnApply, "Please Select Filters ", Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            //  Snackbar.make(btnApply, "No Filters ", Snackbar.LENGTH_LONG).show();
                        }


                    }
                });


                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (filterList != null) {
                            String count = String.valueOf(recyclerView.getAdapter().getItemCount());


                            for (int i = 0; i < filterList.size(); i++) {

                                for (int j = 0; j < filterList.get(i).getTourCategoryFilterGroupsDetailses().size(); j++) {

                                    filterList.get(i).getTourCategoryFilterGroupsDetailses().get(j).setSelected(false);
                                    FiltersList.clear();
                                    mtourProductfilterIds.clear();


                                }

                            }


                            recyclerView.getAdapter().notifyDataSetChanged();

                        } else {

                        }


                    }
                });


            }

        } else {

            recyclerView.setVisibility(View.GONE);
            txtNofilter.setVisibility(View.VISIBLE);
            txtNofilter.setText("No internet connection");

            Snackbar.make(recyclerView, "Please turn on your mobile data or wifi ", Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    public void onBackPressed() {

        Intent productFilter = new Intent(CategoryFilters.this, TourProduct.class);
        productFilter.putExtra("cat_id", CategoryId);
        productFilter.putExtra("parent_id", ParentId);
        productFilter.putExtra("sub_parent_id", Sub_Parent_Id);
        productFilter.putExtra("Sub", subCategory);
        startActivity(productFilter);
        finish();

        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
