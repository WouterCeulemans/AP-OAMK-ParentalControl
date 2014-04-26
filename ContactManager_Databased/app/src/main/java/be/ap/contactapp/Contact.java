package be.ap.contactapp;

import android.net.Uri;


/**
 * Created by nick on 24/02/14.
 */
public class Contact
{
    private String _name, _phone, _email, _address;
    private Uri _imageUri;
    private  int _id;

    public Contact(int id, String name , String phone, String email, String address, Uri imageUri)
    {
        _id         = id;
        _name       = name;
        _phone      = phone;
        _email      = email;
        _address    = address;
        _imageUri   = imageUri;
            }

    public  int getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public String get_phone()
    {
        return _phone;
    }

    public String get_email()
    {
        return _email;
    }

    public String get_address()
    {
        return _address;
    }

    public Uri get_imageUri()
    {
        return _imageUri;
    }
}
