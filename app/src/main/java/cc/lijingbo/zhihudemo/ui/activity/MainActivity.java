package cc.lijingbo.zhihudemo.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lijingbo.zhihudemo.R;
import cc.lijingbo.zhihudemo.bean.LatestNewsBean;
import cc.lijingbo.zhihudemo.bean.ThemesBean;
import cc.lijingbo.zhihudemo.presenter.IZhiHPresenter;
import cc.lijingbo.zhihudemo.presenter.ZhiHPresenter;
import cc.lijingbo.zhihudemo.ui.adapter.ZhiHLatestAdapter;
import cc.lijingbo.zhihudemo.ui.adapter.ZhiHThemesAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements IMainActivity, SwipeRefreshLayout.OnRefreshListener,
    NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
  @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.drawerlayout) DrawerLayout mDrawerlayout;
  //@BindView(R.id.nav_view) NavigationView mNavView;
  @BindView(R.id.nav_list_view) ListView mNavListView;
  private Unbinder mBind;
  private IZhiHPresenter iZhiHPresenter;
  private List<LatestNewsBean.StoryBean> storiesList = new ArrayList<>();
  private List<ThemesBean.Theme> mThemesList = new ArrayList<>();
  private ZhiHLatestAdapter mAdapter;
  private ZhiHThemesAdapter mNavListAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar(mToolbar);
    mBind = ButterKnife.bind(this);
    initData();
    initView();
  }

  private void initData() {
    iZhiHPresenter = new ZhiHPresenter(this, mRecyclerView);
  }

  private void initView() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    mAdapter = new ZhiHLatestAdapter(this, storiesList);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);

    //ListView mNavMenuListView= (ListView) mNavView.findViewById(R.id.nav_menu_list_view);
    mNavListAdapter = new ZhiHThemesAdapter(this, mThemesList);
    View view = LayoutInflater.from(this).inflate(R.layout.nav_header, null, false);
    mNavListView.addHeaderView(view);
    mNavListView.setAdapter(mNavListAdapter);
    onRefresh();

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, R.string.drawer_open,
            R.string.drawer_close);
    toggle.syncState();
    mDrawerlayout.addDrawerListener(toggle);
  }

  @Override protected void onDestroy() {
    mBind.unbind();
    super.onDestroy();
  }

  @Override public void updateAppBarTitle(String name) {
    mToolbar.setTitle(name);
  }

  @Override public void showProgressDialog() {
    if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override public void updateListData(List<LatestNewsBean.StoryBean> stories) {
    storiesList.clear();
    storiesList.addAll(stories);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void updateThemesData(List<ThemesBean.Theme> list) {
    mThemesList.clear();
    mThemesList.addAll(list);
    mNavListAdapter.notifyDataSetChanged();
  }

  @Override public void hideProgressDialog() {
    if (mSwipeRefreshLayout.isRefreshing()) {
      mSwipeRefreshLayout.setRefreshing(false);
    }
  }

  @Override public void onRefresh() {
    iZhiHPresenter.getZhiHLatest();
    getSlideThemes();
  }

  public void getSlideThemes() {
    iZhiHPresenter.getThemes();
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    return false;
  }
}
