package com.example.labexercise4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public  class ContactDetailsFragment extends android.app.Fragment {

    private DBHelper mydb ;

    View mLayoutView;

    TextView name ;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    TextView amount;

    int id_To_Update = 0;
    int donatedAmount = 0;

    // the buttons
    Button mEditButton;
    Button mSaveButton;
    Button mDeleteButton;
    Button mDonateButton;

    ArrayList mArrayList;
    private ListView obj;
    boolean mDualPane;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static ContactDetailsFragment newInstance(int index) {

        ContactDetailsFragment f = new ContactDetailsFragment();

        // Supply index input as an argument.
        // Google recommends using bundles to pass in arguments
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    // Retrieve the index of the contact that will be displayed
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

        mLayoutView = inflater.inflate(R.layout.contact_details_layout, null);

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // initialise the DB
        refresh();

        if (mLayoutView == null)
            return;

        int Value = getShownIndex();
        name   = (TextView) mLayoutView.findViewById(R.id.editTextName);
        phone  = (TextView) mLayoutView.findViewById(R.id.editTextPhone);
        street = (TextView) mLayoutView.findViewById(R.id.editTextStreet);
        email  = (TextView) mLayoutView.findViewById(R.id.editTextEmail);
        place  = (TextView) mLayoutView.findViewById(R.id.editTextCity);
        amount = (TextView) mLayoutView.findViewById(R.id.textView8);

        mydb = new DBHelper(getActivity());

        donatedAmount = mydb.getDonatedAmount(getShownIndex());
        //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(donatedAmount), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(donatedAmount), Toast.LENGTH_SHORT).show();
        amount.setText("Thank you for your donated amounting to $" + Integer.toString(donatedAmount));

        mSaveButton = (Button)mLayoutView.findViewById(R.id.button1);
        mEditButton = (Button)mLayoutView.findViewById(R.id.button_edit);
        mEditButton.setVisibility(View.INVISIBLE);

        mDeleteButton = (Button)mLayoutView.findViewById(R.id.button_delete);
        mDeleteButton.setVisibility(View.INVISIBLE);

        mDonateButton = (Button)mLayoutView.findViewById(R.id.button_donate);
        mDonateButton.setVisibility(View.INVISIBLE);

        //  Bundle extras = getIntent().getExtras();

        // Toast.makeText(getActivity(), Integer.toString(Value), Toast.LENGTH_SHORT).show();

        if(Value>0){
            //means this is in view mode - not the add contact part.
            // Toast.makeText(getActivity(), Integer.toString(Value), Toast.LENGTH_SHORT).show();
            Cursor rs = mydb.getData(Value);
            id_To_Update = Value;
            rs.moveToFirst();

            @SuppressLint("Range") String nam   = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
            @SuppressLint("Range") String phon  = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
            @SuppressLint("Range") String emai  = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
            @SuppressLint("Range") String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
            @SuppressLint("Range") String plac  = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));

            if (!rs.isClosed()) {
                rs.close();
            }

            // Toast.makeText(getActivity(), nam, Toast.LENGTH_SHORT).show();
            setButtonsToViewMode();

            name.setText((CharSequence)nam);
            name.setFocusable(false);
            name.setClickable(false);
            name.setFocusableInTouchMode(false);
            name.setEnabled(false);

            phone.setText((CharSequence)phon);
            phone.setFocusable(false);
            phone.setClickable(false);
            phone.setFocusableInTouchMode(false);
            phone.setEnabled(false);

            email.setText((CharSequence)emai);
            email.setFocusable(false);
            email.setClickable(false);
            email.setFocusableInTouchMode(false);
            email.setEnabled(false);

            street.setText((CharSequence)stree);
            street.setFocusable(false);
            street.setClickable(false);
            street.setFocusableInTouchMode(false);
            street.setEnabled(false);

            place.setText((CharSequence)plac);
            place.setFocusable(false);
            place.setClickable(false);
            place.setFocusableInTouchMode(false);
            place.setEnabled(false);

        }

        // setting the listeners for the buttons
        mEditButton   = (Button) getActivity().findViewById(R.id.button_edit);
        mSaveButton   = (Button) getActivity().findViewById(R.id.button1);
        mDeleteButton = (Button) getActivity().findViewById(R.id.button_delete);
        mDonateButton = (Button) getActivity().findViewById(R.id.button_donate);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setButtonsToEditMode();
                onEditContactClick();

            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run();
                setButtonsToViewMode();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteContactClick();
            }
        });


        mDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = getShownIndex();
                DonateFragment fragment = DonateFragment.newInstance(value);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.contactdetails_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setButtonsToEditMode(){
        mSaveButton.setVisibility(View.VISIBLE);
        mEditButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
        mDonateButton.setVisibility(View.INVISIBLE);
    }
    private void setButtonsToViewMode(){
        mSaveButton.setVisibility(View.INVISIBLE);
        mEditButton.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mDonateButton.setVisibility(View.VISIBLE);
    }

    public void onDeleteContactClick(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.deleteContact)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydb.deleteContact(id_To_Update);
                        Toast.makeText(getActivity().getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                        startActivity(intent);  // back to main activity
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog d = builder.create();
        d.setTitle("Are you sure?");
        d.show();

    }

    public void onEditContactClick(){

        name.setEnabled(true);
        name.setFocusableInTouchMode(true);
        name.setClickable(true);
        name.setFocusable(true);


        phone.setEnabled(true);
        phone.setFocusableInTouchMode(true);
        phone.setClickable(true);
        // phone.setFocusable(true);

        email.setEnabled(true);
        email.setFocusableInTouchMode(true);
        email.setClickable(true);
        // email.setFocusable(true);

        street.setEnabled(true);
        street.setFocusableInTouchMode(true);
        street.setClickable(true);
        //  street.setFocusable(true);

        place.setEnabled(true);
        place.setFocusableInTouchMode(true);
        place.setClickable(true);
        //   place.setFocusable(true);

    }

    public void run()     {

        int Value = getShownIndex();
        if(Value>0){

            // Updating a contact
            if(mydb.updateContact(id_To_Update,name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString())){
                Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                //startActivity(intent);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // inserting a new contact
            if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString())){
                Toast.makeText(getActivity().getApplicationContext(), "Contact inserted!", Toast.LENGTH_SHORT).show();

                // disable inserting  the same contact again


            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Contact NOT inserted. ", Toast.LENGTH_SHORT).show();
            }

            // I don't want to start the same activity in which the fragment is running
            // I want just to refresh the display of a fragment


            //Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
            //startActivity(intent);
        }

        // Refresh the fragment in which the list of data is re-displayed
        ContactListFragment contactListFragment = (ContactListFragment)getFragmentManager().findFragmentById(R.id.contactlist_fragment_container);
        if (contactListFragment!=null){
            contactListFragment.refresh();
        }


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
        int id = getShownIndex();
        Log.d("ContactDetailsFragment", "ID: " + id);
        mArrayList = mydb.getDonatedAmountByid(id);
        //mTextView.setText("Hi");

        ArrayList<String> array_list = new  ArrayList<String>();

        Log.d("ContactDetailsFragment", "arrayList Size: " + mArrayList.size());

        for (int i=0; i<mArrayList.size(); i++){
            Pair<Integer, String> p = (Pair<Integer, String>)mArrayList.get(i);
            Log.d("ContactDetailsFragment", "p.second: " + p.second);
            array_list.add(p.second + " dollars");
        }
        // Put all the contacts in an array
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, array_list);

        // Display the contacts in the ListView object
        obj = (ListView)mLayoutView.findViewById(R.id.listView2);
        obj.setAdapter(arrayAdapter);

        // Check the orientation of the display
        //mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        View detailsFrame = getActivity().findViewById(R.id.contactdetails_fragment_container);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    }

//    public void refresh(){
//
//        if (mydb == null)
//            mydb = new DBHelper(getActivity());
//
//        int Value = getShownIndex();
//
//        amount = (TextView) mLayoutView.findViewById(R.id.textView8);
//
//        donatedAmount = mydb.getDonatedAmount(Value);
//        amount.setText("Thank you for your donated amounting to $" + Integer.toString(donatedAmount));
//    }
}

