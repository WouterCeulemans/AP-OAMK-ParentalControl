package be.ap.contactapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import android.net.Uri;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    EditText nameTxt, phoneTxt, addressTxt, emailTxt;
    ImageView contactImageImageView;
    List<ContactClass> contactClasses = new ArrayList<ContactClass>();
    ListView contactListView;
    Uri imageUri = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt                 = (EditText)  findViewById(R.id.txtName);
        phoneTxt                = (EditText)  findViewById(R.id.txtPhone);
        addressTxt              = (EditText)  findViewById(R.id.txtAddress);
        emailTxt                = (EditText)  findViewById(R.id.txtEmail);
        contactListView         = (ListView)  findViewById(R.id.listView);
        contactImageImageView   = (ImageView) findViewById(R.id.imgViewContactImage);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("New ContactClass");
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
                contactClasses.add(new ContactClass(0, nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), imageUri));
                nameTxt.setText("");
                phoneTxt.setText("");
                addressTxt.setText("");
                emailTxt.setText("");

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

        contactImageImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select ContactClass Image"), 1);
            }
        });
    } 
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        if (resCode == RESULT_OK)
        {
            if (resCode == 1)
            {
                imageUri = data.getData();
                contactImageImageView.setImageURI(data.getData());

            }
        }
    }

    private void populateList()
    {
        ArrayAdapter<ContactClass> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<ContactClass>
    {
        public ContactListAdapter()
        {
            super(MainActivity.this, R.layout.listview_item, contactClasses);
        }
        @Override
        public View getView(int position , View view, ViewGroup parent)
        {// adapter voor onze list.
            if (view == null )
            {
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            }
            ContactClass currentContactClass = contactClasses.get(position);

            TextView name = ( TextView) view.findViewById(R.id.contactName);
            name.setText(currentContactClass.getName());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContactClass.get_phone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContactClass.get_email());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContactClass.get_address());

            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContactClass.get_imageUri());

            return view;
        }
    }


    private ArrayList<Contact> GetAllContactsAndroid()
    {
        Cursor cur =
    }

    /*@Override
     public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
