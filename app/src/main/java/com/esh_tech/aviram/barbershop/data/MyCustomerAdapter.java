package com.esh_tech.aviram.barbershop.data;

/**
 * Created by Aviram on 11/07/2017.
 */

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;


/**
 *  class that defines the custom adapter for the list of customers
 * @author Irit
 *
 */
public class MyCustomerAdapter extends ArrayAdapter<Customer>
{
    Filter mFilter;
    Activity activityContext;
    ArrayList<Customer> mItems;
    ArrayList<Customer> fullList;

    //    Database
    BarbershopDBHandler dbHandler;

    public MyCustomerAdapter(Activity listActivity,
                             int viewResourceId, List<Customer> objects)
    {
        super((Context) listActivity, viewResourceId, objects);
        activityContext = listActivity;
        mFilter = new MyNameFilter();
        mItems = new ArrayList<Customer>(objects);
        fullList = new ArrayList<Customer>(objects);
        dbHandler = new BarbershopDBHandler(listActivity);
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }
    @Override
    public Customer getItem(int position)
    {
        return mItems.get(position);
    }
    @Override
    public int getPosition(Customer item)
    {
        return mItems.indexOf(item);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter()
    {
        if(mFilter == null)
            mFilter = new MyNameFilter();
        return mFilter;
    }

    private class MyNameFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();

            synchronized (this)
            {
//                mItems = new ArrayList<Customer>(fullList);
                mItems = dbHandler.getAllCustomers();
            }
            if(constraint != null && constraint.toString().length() > 0)
            {
                final ArrayList<Customer> items = mItems;
                final int count = items.size();
                final ArrayList<Customer> newItems = new ArrayList<Customer>(count);
                for(int i = 0, l = count; i < l; i++)
                {
                    // use the lower characters just for the comparison
                    Customer m = items.get(i);
                    if(m.getName().toLowerCase().startsWith(constraint.toString()))
                        newItems.add(items.get(i));
                }
                result.count = newItems.size();
                result.values = newItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = fullList;
                    result.count = fullList.size();
                }
            }
            return result;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            // NOTE: this function is *always* called from the UI thread.
            mItems = (ArrayList<Customer>)results.values;
            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Customer customerPosition = mItems.get(position);

        LayoutInflater inflater = activityContext.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_contact_row, parent, false);

        // if this is a group reminder hat this user didn't create, we add the
        // the name of the reminder originator in the paranthesis
        TextView propertyText = (TextView) row.findViewById(R.id.customerNameET);
        TextView propertyPhoneText = (TextView) row.findViewById(R.id.customerPhoneEt);
        ImageView customerIcon = (ImageView)row.findViewById(R.id.customerIconIv);
//        propertyText.setText(customerPosition.toString());	// changed from reminderNames to mItems
        propertyText.setText(customerPosition.getName());
        propertyPhoneText.setText(customerPosition.getPhone());
        if(dbHandler.getUserPictureByID(customerPosition.get_id()) != null)
            customerIcon.setImageBitmap(dbHandler.getUserPictureByID(customerPosition.get_id()));
        else if (customerPosition.get_id()!= -1 && customerPosition.getPhoto() != null) {
            customerIcon.setImageBitmap(customerPosition.getPhoto());
        }else {
            if (customerPosition.getGender() == 1) customerIcon.setImageResource(R.drawable.usermale48);
            else customerIcon.setImageResource(R.drawable.userfemale48);
        }

        row.setTag(customerPosition.get_id());

        return row;
    }
}
