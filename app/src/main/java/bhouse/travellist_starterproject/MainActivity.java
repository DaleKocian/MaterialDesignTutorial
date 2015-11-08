package bhouse.travellist_starterproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

public class MainActivity extends Activity {
    public static final String SHOW_AS_LIST = "Show as list";
    public static final String SHOW_AS_GRID = "Show as grid";
    public static final int ACTION_BAR_ELEVATION = 7;
    public static final int GRID_VIEW_SPAN_COUNT = 2;
    public static final int LIST_VIEW_SPAN_COUNT = 1;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TravelListAdapter mAdapter;
    private Menu menu;
    private boolean isListView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isListView = true;
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(LIST_VIEW_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mAdapter = new TravelListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        TravelListAdapter.OnItemClickListener onItemClickListener = new TravelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent transitionIntent = new Intent(MainActivity.this, DetailActivity.class);
                transitionIntent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);
                ActivityOptionsCompat options = createActivityOptions(v);
                ActivityCompat.startActivity(MainActivity.this, transitionIntent, options.toBundle());
            }
        };
        mAdapter.setOnItemClickListener(onItemClickListener);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpActionBar();
    }

    private ActivityOptionsCompat createActivityOptions(View v) {
        ImageView placeImage = (ImageView) v.findViewById(R.id.placeImage);
        LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.placeNameHolder);
        View navigationBar = findViewById(android.R.id.navigationBarBackground);
        View statusBar = findViewById(android.R.id.statusBarBackground);
        Pair<View, String> imagePair = Pair.create((View) placeImage, placeImage.getTransitionName());
        Pair<View, String> holderPair = Pair.create((View) placeNameHolder, placeNameHolder.getTransitionName());
        Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> toolbarPair = Pair.create((View) toolbar, toolbar.getTransitionName());
        return ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, holderPair, navPair, statusPair,
                toolbarPair);
    }

    private void setUpActionBar() {
        if (toolbar != null) {
            setActionBar(toolbar);
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(false);
                getActionBar().setDisplayShowTitleEnabled(true);
                getActionBar().setElevation(ACTION_BAR_ELEVATION);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(GRID_VIEW_SPAN_COUNT);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle(SHOW_AS_LIST);
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(LIST_VIEW_SPAN_COUNT);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle(SHOW_AS_GRID);
            isListView = true;
        }
    }
}
