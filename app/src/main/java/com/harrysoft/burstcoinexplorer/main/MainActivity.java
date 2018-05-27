package com.harrysoft.burstcoinexplorer.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.harrysoft.burstcoinexplorer.BuildConfig;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.AccountsFragment;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;
import com.harrysoft.burstcoinexplorer.burst.explorer.AndroidBurstExplorer;
import com.harrysoft.burstcoinexplorer.burst.explorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.burst.utils.BurstUtils;
import com.harrysoft.burstcoinexplorer.events.EventsFragment;
import com.harrysoft.burstcoinexplorer.explore.ExploreFragment;
import com.harrysoft.burstcoinexplorer.observe.ObserveFragment;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, HasSupportFragmentInjector, SearchView.OnQueryTextListener {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject
    BurstBlockchainService burstBlockchainService;
    private BurstExplorer burstExplorer;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    @Nullable
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit, new Answers());

        viewPager = findViewById(R.id.main_viewpager);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        burstExplorer = new AndroidBurstExplorer(this);

        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEARCH)) {
            search(intent.getStringExtra(SearchManager.QUERY));
        }

        setupViewPager();
    }

    private void search(String query) {
        Toast toast = Toast.makeText(this, R.string.searching, Toast.LENGTH_LONG);
        toast.show();
        burstBlockchainService.determineSearchRequestType(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(type -> {
                    toast.cancel();
                    navigateSearchResult(query, type);
                }, t -> {});
    }

    private void navigateSearchResult(String request, SearchRequestType searchRequestType) {
        switch (searchRequestType) {
            case ACCOUNT_RS:
                try {
                    burstExplorer.viewAccountDetails(BurstUtils.toNumericID(request));
                } catch (BurstUtils.ReedSolomon.DecodeException e) {
                    displayInvalidSearchError();
                }
                break;

            case ACCOUNT_ID:
                burstExplorer.viewAccountDetails(new BigInteger(request));
                break;

            case BLOCK_ID:
                burstExplorer.viewBlockDetailsByID(new BigInteger(request));
                break;

            case BLOCK_NUMBER:
                burstExplorer.viewBlockDetailsByNumber(new BigInteger(request));
                break;

            case TRANSACTION_ID:
                burstExplorer.viewTransactionDetailsByID(new BigInteger(request));
                break;

            case INVALID:
                displayInvalidSearchError();
                break;

            case NO_CONNECTION:
                Toast.makeText(this, R.string.search_no_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void displayInvalidSearchError() {
        Toast.makeText(this, R.string.search_invalid, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_explore:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_accounts:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_events:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.navigation_observe:
                viewPager.setCurrentItem(3);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (searchManager != null && searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ExploreFragment(), getString(R.string.title_explore));
        adapter.addFragment(new AccountsFragment(), getString(R.string.title_accounts));
        adapter.addFragment(new EventsFragment(), getString(R.string.title_events));
        adapter.addFragment(new ObserveFragment(), getString(R.string.title_observe));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        } else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }

        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigationView.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
