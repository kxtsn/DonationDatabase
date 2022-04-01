package com.example.labexercise4;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactListFragment extends android.app.Fragment {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    public final static int REQUEST_CODE_NEW_CONTACT = 1;


    // Views
    boolean mDualPane;
    View    mLayoutView;
    Button  mNewContactButton;
    TextView mTextView;
    private ListView obj;

    // Database
    DBHelper mydb = null;
    ArrayList mArrayList;  // the list of all contacts

    public static ContactListFragment newInstance(){

        ContactListFragment f = new ContactListFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.contact_list_layout, null);
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // initialise the DB
        refresh();

        // At the click on an item, start a new activity that will display the content of the database
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                // Here either start a new activity or display it on the same window

                // The index of the Contact that will be shown in the new activity DislayActivity.java
                Pair<Integer, String> p = (Pair<Integer, String>)mArrayList.get(position);
                int id_To_Search = p.first; // position + 1;

                // Check whether the details frame is visible
                View detailsFrame = getActivity().findViewById(R.id.contactdetails_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {

                    // display on the same Activity
                    ContactDetailsFragment details = ContactDetailsFragment.newInstance(id_To_Search);

                    // Execute a transaction, replacing any existing fragment
                    // with this one inside the frame.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contactdetails_fragment_container, details);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }else {
                    Bundle dataBundle = new Bundle();

                    dataBundle.putInt("id", id_To_Search);
                    Intent intent = new Intent(getActivity().getApplicationContext(), DisplayContact.class);
                    intent.putExtras(dataBundle);
                    startActivityForResult(intent, REQUEST_CODE_NEW_CONTACT);
                }
            }
        });


        // set the onclick listener for the button
        mNewContactButton = (Button) getActivity().findViewById(R.id.new_button);
        mTextView = (TextView) getActivity().findViewById(R.id.textView6);
        int amount = mydb.getTotal();
        mTextView.setText("Total Amount Donated: $" + Integer.toString(amount));

        mNewContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View detailsFrame = getActivity().findViewById(R.id.contactdetails_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {

                    // display on the same Activity
                    ContactDetailsFragment details = ContactDetailsFragment.newInstance(0);

                    // Execute a transaction, replacing any existing fragment
                    // with this one inside the frame.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contactdetails_fragment_container, details);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }else {

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", 0);

                    Intent intent = new Intent(getActivity().getApplicationContext(), DisplayContact.class);
                    intent.putExtras(dataBundle);   //

                    startActivity(intent);          //
                }

            }
        });



    }

    @Override
    public void onResume() {
        // Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        refresh();
    }

    public void refresh(){

        if (mydb == null)
            mydb = new DBHelper(getActivity());

        // Get all the contacts from the database
        mArrayList = mydb.getAllContacts();
        //mTextView.setText("Hi");

        ArrayList<String> array_list = new  ArrayList<String>();

        for (int i=0; i<mArrayList.size(); i++){
            Pair<Integer, String> p = (Pair<Integer, String>)mArrayList.get(i);
            array_list.add(p.second);
        }
        // Put all the contacts in an array
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, array_list);

        // Display the contacts in the ListView object
        obj = (ListView)mLayoutView.findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);

        // Check the orientation of the display
        //mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        View detailsFrame = getActivity().findViewById(R.id.contactdetails_fragment_container);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    }



}