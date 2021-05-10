package com.example.demofragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demofragment.storage.Repo;
import com.example.demofragment.storage.RepoStorage;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.demofragment.PaginationListener.PAGE_START;

public class RepoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ItemClickListener {

    private Context mContext;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    List<Repo> repoList;


    public RepoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_repo, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        repoList = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new PostRecyclerAdapter(new ArrayList<>(), mContext);
        mRecyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        RepoStorage repoStorage = RepoStorage.getInstance(mContext);
        repoList = repoStorage.getRepoList();
        doApiCall();

        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        return view;
    }

    /**
     * do api call here to fetch data from server
     * In example i'm adding data manually
     */
    public void doApiCall() {
        final ArrayList<PostItem> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    itemCount++;
                    PostItem postItem = new PostItem();
                    postItem.setTitle(repoList.get(i).FullName);
                    postItem.setType(repoList.get(i).Type);
                    postItem.setImageURL(repoList.get(i).AvatarURL);
                    items.add(postItem);
                }
                // do this all stuff on Success of APIs response
                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(items);
                swipeRefresh.setRefreshing(false);

                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doApiCall();
    }

    @Override
    public void onClick(int position, int navigation) {
        MainActivity mainActivity = new MainActivity();
        mainActivity.update(navigation, position);
    }
}
