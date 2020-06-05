package com.example.christianbooklibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variable for navigation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //Variables
    ListView localListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
        localListView = findViewById(R.id.localBooksList);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.my_toolbar);

        /*-----------------------Toolbar--------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Most Read Books");

        /*-----------------Navigation drawer menu-----------------------*/

        //Hide or show items
        Menu menu = navigationView.getMenu();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        //List of books titles on local device and their description, in this case the description is the author
        String localBookTitle[] = {"Deeper Shopping", "Rediscovering The Kingdom", "Simply Christian", "Wealth Without Theft"};

        String localBookDesc[] = {"Dr. Myles Munroe", "Dr. Myles Munroe", "N.T Wright", "Kolawole Oyeyemi"};

        MyAdapter adapter = new MyAdapter(this, localBookTitle, localBookDesc);
        localListView.setAdapter(adapter);

        localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items = localListView.getItemAtPosition(position).toString();
                Intent start = new Intent(getApplicationContext(), PDFViewerActivity.class);
                start.putExtra("pdfFileName", items);
                startActivity(start);
            }
        });

        //connectionStatus();
    }

    // Adapter class for list view
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String bookT[];
        String bookD[];

        MyAdapter(Context c, String title[], String description[]) {
            super(c, R.layout.row, R.id.bookTitle, title);
            this.context = c;
            this.bookT = title;
            this.bookD = description;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.bookTitle);
            TextView myDescription = row.findViewById(R.id.bookDesc);

            // Setting resources on views
            myTitle.setText(bookT[position]);
            myDescription.setText(bookD[position]);

            return row;
        }
    }


    //Check internet connection even when connected to network.
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //Method checks the connection status
    private void connectionStatus() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED &&
                        (isOnline() == true)) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (connected == true) {
            Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();
        } else if (connected == false) {
            Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_inspirational:
                Toast.makeText(getApplicationContext(), "Inspirational books", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_prayer:
                Toast.makeText(getApplicationContext(), "Books on prayer", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
