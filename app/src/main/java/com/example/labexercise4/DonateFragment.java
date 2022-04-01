package com.example.labexercise4;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;


public class DonateFragment extends android.app.Fragment {

    View mLayoutView;

    Button donateButton;
    RadioGroup paymentMethod;
    NumberPicker amountPicker;

    private DBHelper mydb;

    public static DonateFragment newInstance(int index) {

        DonateFragment f = new DonateFragment();

        // Supply index input as an argument.
        // Google recommends using bundles to pass in arguments
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        mLayoutView = inflater.inflate(R.layout.fragment_donate, null);


        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mLayoutView == null)
            return;

        donateButton = (Button) mLayoutView.findViewById(R.id.donateButton);
        paymentMethod = (RadioGroup) mLayoutView.findViewById(R.id.paymentMethod);
        amountPicker = (NumberPicker) mLayoutView.findViewById(R.id.amountPicker);

        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        mydb = new DBHelper(getActivity());

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run();
            }
        });

    }
    public void run()     {

        int value = getShownIndex();
            int amount  = amountPicker.getValue();
        int radioId = paymentMethod.getCheckedRadioButtonId();

        String method = "";
        if (radioId == R.id.PayPal){
            method = "PayPal";
        } else {
            method = "Direct";
        }

        if(amount == 0 || method == ""){
            Toast.makeText(getActivity().getApplicationContext(), "Please fill up the fields!", Toast.LENGTH_SHORT).show();
        } else {

            // inserting a new donation
            if (mydb.insertDonation(Integer.toString(value), Integer.toString(amount))) {
                Toast.makeText(getActivity().getApplicationContext(), "Amount Donated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Donation Unsuccessful. ", Toast.LENGTH_SHORT).show();
            }

            ContactListFragment fragment = new ContactListFragment();
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.contactdetails_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();


            // Refresh the fragment in which the list of data is re-displayed
            ContactListFragment contactListFragment = (ContactListFragment) getFragmentManager().findFragmentById(R.id.contactlist_fragment_container);
            if (contactListFragment != null) {
                contactListFragment.refresh();
            }
        }

    }


}

