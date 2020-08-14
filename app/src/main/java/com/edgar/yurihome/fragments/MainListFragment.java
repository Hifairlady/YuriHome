package com.edgar.yurihome.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.MainListAdapter;
import com.edgar.yurihome.beans.ComicItem;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainListFragment extends Fragment {
    private static final String ARG_CLASSIFY_FILTERS = "ARG_CLASSIFY_FILTERS";

    private static final int MSG_LOAD_NEXT_PAGE = 1000;

    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private SwipeRefreshLayout srlMain;
    private FloatingActionButton fab;
    private ArrayList<ComicItem> comicItems = new ArrayList<>();
    private Handler mHandler;

    private boolean isLoading = false;
    private boolean isFinalPage = false;

    private int typeCode = 3243, regionCode = 2304, groupCode = 0, statusCode = 0, sortCode = 1, page = 0;
    private int[] classifyFilters = new int[5];

    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance(int[] filters) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putIntArray(ARG_CLASSIFY_FILTERS, filters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            classifyFilters = getArguments().getIntArray(ARG_CLASSIFY_FILTERS);
            if (classifyFilters == null || classifyFilters.length != 5) return;
            typeCode = classifyFilters[0];
            regionCode = classifyFilters[1];
            groupCode = classifyFilters[2];
            statusCode = classifyFilters[3];
            sortCode = classifyFilters[4];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
        MaterialToolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        srlMain = rootView.findViewById(R.id.srl_main);
        fab = rootView.findViewById(R.id.fab_top);
        fab.hide();
        recyclerView = rootView.findViewById(R.id.rv_main);
        adapter = new MainListAdapter(getContext(), comicItems);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == ComicItem.ITEM_TYPE_FOOTER) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter.removeFooterItem();
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        if (comicItems.isEmpty()) {
                            isFinalPage = true;
                            Snackbar.make(recyclerView, R.string.string_no_more_data, Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (srlMain.isRefreshing()) {
                                adapter.setComicItems(comicItems);
                            } else {
                                adapter.appendComicItems(comicItems);
                            }
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(recyclerView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        if (page >= 1) {
                            page -= 1;
                        }
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(recyclerView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        if (page >= 1) {
                            page -= 1;
                        }
                        break;

                    default:
                        break;
                }
                isLoading = false;
                srlMain.setRefreshing(false);
            }
        };

        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isFinalPage = false;
                loadPageData(mHandler, typeCode, regionCode, groupCode, statusCode, sortCode, page);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //load next page
                    if (isSlideToBottom(recyclerView, isLoading) && adapter.getItemCount() > 0) {
                        page += 1;
                        adapter.addFooterItem();
                        loadPageData(mHandler, typeCode, regionCode, groupCode, statusCode, sortCode, page);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager1 = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager1 != null && layoutManager1.findFirstCompletelyVisibleItemPosition() <= 9 && fab.isShown()) {
                    fab.hide();
                }
                if (layoutManager1 != null && layoutManager1.findLastCompletelyVisibleItemPosition() > 14 && !fab.isShown()) {
                    fab.show();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPageData(mHandler, typeCode, regionCode, groupCode, statusCode, sortCode, page);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_list_frag_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_action_search:
                break;

            case R.id.main_menu_action_filter:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isSlideToBottom(RecyclerView rvArg, boolean isLoadingArg) {
        return !isLoadingArg && rvArg != null &&
                rvArg.computeVerticalScrollExtent() + rvArg.computeVerticalScrollOffset() >=
                        rvArg.computeVerticalScrollRange();
    }

    private void loadPageData(final Handler handler, int type, int region, int group, int status, int sort, int pageNum) {
        if (isFinalPage) return;
        isLoading = true;
        String urlString = Config.getComicsUrlByFilter(type, region, group, status, sort, pageNum);
        HttpUtil.sendRequestWithOkhttp(urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = HttpUtil.REQUEST_JSON_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = Message.obtain();
                message.what = parseJsonString(response);
                handler.sendMessage(message);
            }
        });
    }

    private int parseJsonString(Response response) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) return result;
            String jsonString = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ComicItem>>() {
            }.getType();
            ArrayList<ComicItem> items = gson.fromJson(jsonString, type);
            comicItems = new ArrayList<>(items);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}