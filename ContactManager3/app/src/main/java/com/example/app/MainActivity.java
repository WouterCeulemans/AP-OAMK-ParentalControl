package com.example.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    EditText nameTxt, phoneTxt, addressTxt, emailTxt;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt         = (EditText) findViewById(R.id.txtName);
        phoneTxt        = (EditText) findViewById(R.id.txtPhone);
        addressTxt      = (EditText) findViewById(R.id.txtAddress);
        emailTxt        = (EditText) findViewById(R.id.txtEmail);
        contactListView = (ListView) findViewById(R.id.listView);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("New Contact");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("NewContact");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("PhoneBook") ;
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("PhoneBook");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addContact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(),addressTxt.getText().toString());

                populateList();

                Toast.makeText(getApplicationContext(),nameTxt.getText().toString() + " has been added to your contacts§", Toast.LENGTH_SHORT).show();
            }
        }));

        nameTxt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void populateList()
    {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private void addContact(String name , String phone, String email, String address)
    {
        Contacts.add(new Contact(name, phone,email,address));
    }

    private class ContactListAdapter extends ArrayAdapter<Contact>
    {
        public ContactListAdapter()
        {
            super(MainActivity.this, R.layout.listview_item, Contacts);
        }
        @Override
        public View getView(int position , View view, ViewGroup parent)
        {
            if (view == null )
            {
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            }
            Contact currentContact = Contacts.get(position);

            TextView name = ( TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.getName());

            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.get_phone());

            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.get_email());

            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.get_address());

            return view;
        }
    }

    /*@Override
     public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
