package media.extreme.com.brothercorpappmovil;

import android.content.Intent;
//import androidx.core.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.widget.;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();



        DataModel[] drawerItem = new DataModel[3];

        //drawerItem[0] = new DataModel(R.drawable.connect, "Connect");
        //drawerItem[1] = new DataModel(R.drawable.fixtures, "Fixtures");
        //drawerItem[2] = new DataModel(R.drawable.table, "Table");

        drawerItem[0] = new DataModel(R.drawable.syncro, "SINCRONIZACION");
        drawerItem[1] = new DataModel(R.drawable.gestion, "LECTURAS");
        drawerItem[2] = new DataModel(R.drawable.config, "CONFIGURACION");
        //drawerItem[3] = new DataModel(R.drawable.table, "LECTURAS1");
        //drawerItem[4] = new DataModel(R.drawable.config, "CONFIGURACION");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();



    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                Intent intent = new Intent(MainActivity.this,Drive.class);

                startActivity(intent);
                break;
            case 1:
                try {
                    Intent intent1 = new Intent(MainActivity.this, FindList.class);
                    startActivity(intent1);
                }
                catch(Exception e)
                {
                    Log.e("EDISON", e.toString());
                }

                break;

            case 2:

                fragment = new ConfigList();

                break;
            //case 3:
            //        fragment = new ConfigList();
            //    break;


            default:
                break;
        }

        try {
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(mNavigationDrawerItemTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);

            } else {
                Log.e("EDISON", "Error in creating fragment");
            }
        }
        catch(Exception e)
        {
            Log.e("EDISON", e.toString());
        }
    }


    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new androidx.appcompat.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
