package com.example.jxr.gameeandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

/**
 * MainActivity contains the main framework or each fragment
 * Maintain the navigation to each fragment
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // replace actionbar with toolbar
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Display username on navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView mTextView = (TextView) headerView.findViewById(R.id.textView);
        mTextView.setText(mAuth.getCurrentUser().getDisplayName());

        // display user profile photo on nav drawer header
        Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photoUrl != null) {
            ImageView headerPhoto = (ImageView) headerView.findViewById(R.id.headerPhoto);
            Glide.with(this).load(photoUrl).into(headerPhoto);
        }


        FragmentManager fragmentManager = getFragmentManager();

        Fragment firstFragment = new MainFragment();
        Intent intent = getIntent();

        // if it is navigate from PostDetailActivity to ChatActivity
        if (intent.getStringExtra("goToChatFragment") != null){
            firstFragment = new ChannelListFragment();
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, firstFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment nextFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                nextFragment = new MainFragment();
                break;

            case R.id.log_out:
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_chat:
                nextFragment = new ChannelListFragment();
                break;

            case R.id.nav_profile:
                nextFragment = new MyProfileFragment();
                break;

            default:
                break;
        }

        if (nextFragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // use to show or hide floating button in fragments
    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }

}
