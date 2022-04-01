package com.example.labexercise4;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{//AppCompatActivity {

    ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            contactListFragment = ContactListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.contactlist_fragment_container, contactListFragment).commit();
        }else{
            contactListFragment = (ContactListFragment)getFragmentManager().findFragmentById(R.id.contactlist_fragment_container);
        }
    }
}