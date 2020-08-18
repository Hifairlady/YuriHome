package com.edgar.yurihome.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.MainListAdapter;
import com.edgar.yurihome.beans.ClassifyFilterBean;
import com.edgar.yurihome.beans.ComicItem;
import com.edgar.yurihome.interfaces.OnComicListItemClickListener;
import com.edgar.yurihome.scenarios.ComicDetailsActivity;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SharedPreferenceUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainListFragment extends Fragment {
    private static final String ARG_CLASSIFY_FILTERS = "ARG_CLASSIFY_FILTERS";

    private static final int MSG_LOAD_NEXT_PAGE = 1000;

    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private SwipeRefreshLayout srlMain;
    private FloatingActionButton fab;
    private ArrayList<ClassifyFilterBean> classifyFilterBeans = new ArrayList<>();
    private Handler mHandler;
    private Context mContext;

    private boolean isLoading = false;
    private boolean isFinalPage = false;
    private boolean isRefreshing = false;

    private int typeCode = 3243, regionCode = 2304, groupCode = 0, statusCode = 0, sortCode = 1, page = 0;
    private int[] classifyFilters = new int[5];

    public MainListFragment() {
        // Required empty public constructor
    }

    private OnComicListItemClickListener mListItemClickListener = new OnComicListItemClickListener() {
        @Override
        public void onItemClick(int position) {
            ComicItem comicItem = adapter.getComicItemAt(position);
            if (comicItem == null) return;
            Intent intent = new Intent(getActivity(), ComicDetailsActivity.class);
            intent.putExtra("COMIC_DETAILS_URL", Config.getComicDetailsUrl(comicItem.getId()));
            intent.putExtra("COMIC_COVER_URL", comicItem.getCover());
            intent.putExtra("COMIC_TITLE", comicItem.getTitle());
            startActivity(intent);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_top:
                    recyclerView.scrollToPosition(0);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
        MaterialToolbar toolbar = rootView.findViewById(R.id.full_chapter_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        initView(rootView);
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

    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isSlideToBottom(RecyclerView rvArg, boolean isLoadingArg) {
        return !isLoadingArg && rvArg != null &&
                rvArg.computeVerticalScrollExtent() + rvArg.computeVerticalScrollOffset() >=
                        rvArg.computeVerticalScrollRange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getContext();
        int[] typeTagIds = SharedPreferenceUtil.getAllFilterTypeTagId(mContext);
        int[] statTagIds = SharedPreferenceUtil.getAllFilterStatTagId(mContext);
        int[] regionTagIds = SharedPreferenceUtil.getAllFilterRegionTagId(mContext);

        typeCode = typeTagIds[SharedPreferenceUtil.getUserFilterTypeIndex(mContext)];
        regionCode = regionTagIds[SharedPreferenceUtil.getUserFilterRegionIndex(mContext)];
        groupCode = 0;
        statusCode = statTagIds[SharedPreferenceUtil.getUserFilterStatIndex(mContext)];
        sortCode = SharedPreferenceUtil.getUserFilterSortIndex(mContext);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_action_search:
                break;

            case R.id.main_menu_action_filter:
                Dialog dialog = new Dialog(mContext);
                View rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_filter_dialog, null, false);
                dialog.setContentView(rootView);
                initDialogView(rootView, dialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle("Filter");
                dialog.show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPageData(final Handler handler, int type, int region, int group, int status, int sort, int pageNum) {
        if (isFinalPage) return;
        isLoading = true;
        String urlString = Config.getComicsUrlByFilter(type, region, group, status, sort, pageNum);
        JsonUtil.fetchJsonData(handler, urlString);
    }

    private void initView(View rootView) {
        srlMain = rootView.findViewById(R.id.srl_main);
        fab = rootView.findViewById(R.id.fab_top);
        fab.hide();
        recyclerView = rootView.findViewById(R.id.rv_main);
        adapter = new MainListAdapter(mContext);
        adapter.setOnMainListItemClickListener(mListItemClickListener);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
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

        fab.setOnClickListener(mOnClickListener);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter.removeFooterItem();
                String jsonString = (String) msg.obj;
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<ComicItem>>() {
                    }.getType();
                    ArrayList<ComicItem> comicItems = gson.fromJson(jsonString, type);
                    switch (msg.what) {
                        case HttpUtil.REQUEST_JSON_SUCCESS:
                            if (comicItems.isEmpty()) {
                                isFinalPage = true;
                                Snackbar.make(recyclerView, R.string.string_no_more_data, Snackbar.LENGTH_SHORT).show();
                            } else {
                                if (isRefreshing) {
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
                            Snackbar.make(recyclerView, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                            if (page >= 1) {
                                page -= 1;
                            }
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Snackbar.make(recyclerView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                    if (page >= 1) {
                        page -= 1;
                    }
                }
                isLoading = false;
                isRefreshing = false;
                srlMain.setRefreshing(false);
            }
        };

        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //load next page
                    if (isSlideToBottom(recyclerView, isLoading) && adapter.getItemCount() > 0 && !isFinalPage) {
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
    }

    private void refreshPage() {
        page = 0;
        isFinalPage = false;
        isRefreshing = true;
        loadPageData(mHandler, typeCode, regionCode, groupCode, statusCode, sortCode, page);
    }

    private void initDialogView(View rootView, final Dialog dialog) {
        TextView btnCancel, btnApply;
        final Spinner spType, spRegion, spStatus, spSort;
        btnCancel = rootView.findViewById(R.id.tv_cancel);
        btnApply = rootView.findViewById(R.id.tv_apply);
        spType = rootView.findViewById(R.id.sp_classify_type);
        spRegion = rootView.findViewById(R.id.sp_region);
        spStatus = rootView.findViewById(R.id.sp_status);
        spSort = rootView.findViewById(R.id.sp_sort);

        String[] typeTagNames = SharedPreferenceUtil.getAllFilterTypeTagName(mContext);
        String[] statTagNames = SharedPreferenceUtil.getAllFilterStatTagName(mContext);
        String[] regionTagNames = SharedPreferenceUtil.getAllFilterRegionTagName(mContext);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, typeTagNames);
        spType.setAdapter(typeAdapter);
        ArrayAdapter<String> statAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, statTagNames);
        spStatus.setAdapter(statAdapter);
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, regionTagNames);
        spRegion.setAdapter(regionAdapter);

        int userFilterTypeIndex = SharedPreferenceUtil.getUserFilterTypeIndex(mContext);
        int userFilterRegionIndex = SharedPreferenceUtil.getUserFilterRegionIndex(mContext);
        int userFilterStatIndex = SharedPreferenceUtil.getUserFilterStatIndex(mContext);
        int userFilterSortIndex = SharedPreferenceUtil.getUserFilterSortIndex(mContext);

        spType.setSelection(userFilterTypeIndex);
        spStatus.setSelection(userFilterStatIndex);
        spRegion.setSelection(userFilterRegionIndex);
        spSort.setSelection(userFilterSortIndex);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int[] typeTagIds = SharedPreferenceUtil.getAllFilterTypeTagId(mContext);
                int[] statTagIds = SharedPreferenceUtil.getAllFilterStatTagId(mContext);
                int[] regionTagIds = SharedPreferenceUtil.getAllFilterRegionTagId(mContext);

                typeCode = typeTagIds[spType.getSelectedItemPosition()];
                statusCode = statTagIds[spStatus.getSelectedItemPosition()];
                regionCode = regionTagIds[spRegion.getSelectedItemPosition()];
                sortCode = spSort.getSelectedItemPosition();
                SharedPreferenceUtil.storeUserFilterSetting(mContext, spType.getSelectedItemPosition(), spStatus.getSelectedItemPosition(), spRegion.getSelectedItemPosition(), spSort.getSelectedItemPosition());
                refreshPage();
                dialog.dismiss();
            }
        });
    }

}