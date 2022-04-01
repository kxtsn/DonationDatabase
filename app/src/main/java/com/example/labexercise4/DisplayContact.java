package com.example.labexercise4;

import android.app.Activity;
import android.os.Bundle;

public class DisplayContact  extends Activity {

    ContactDetailsFragment contactDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_layout);

        Bundle extras = getIntent().getExtras();
        int ix = -1;
        if(extras !=null)
            ix = extras.getInt("id", -1);

        if (savedInstanceState == null) {
            contactDetailsFragment = ContactDetailsFragment.newInstance(ix);
            getFragmentManager().beginTransaction().add(R.id.contactdetails_fragment_container, contactDetailsFragment).commit();
        }else{
            contactDetailsFragment = (ContactDetailsFragment)getFragmentManager().findFragmentById(R.id.contactdetails_fragment_container);
        }
    }
}
