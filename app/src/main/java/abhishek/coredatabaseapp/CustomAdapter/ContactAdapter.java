package abhishek.coredatabaseapp.CustomAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import abhishek.coredatabaseapp.Controller.ContactActivity;
import abhishek.coredatabaseapp.DatabaseHelper.DatabaseHandler;
import abhishek.coredatabaseapp.Model.Contact;
import abhishek.coredatabaseapp.R;

/**
 * Created by abhishek on 8/12/14.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public int layoutResourceId;
    public Context context;
    public ArrayList<Contact> data = new ArrayList<Contact>();
    public View rowView;
    public ViewHolder holder;
    public DatabaseHandler db;


    public ContactAdapter(Context context, int layoutResourceId, ArrayList<Contact> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView name,email,phone;
        Button btnDelete,btnEdit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        Contact rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_contact_list_row, null);
            holder = new ViewHolder();

            holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
            holder.name = (TextView)convertView.findViewById(R.id.nameTextView);
            holder.phone = (TextView)convertView.findViewById(R.id.mobileTextView);
            holder.email = (TextView)convertView.findViewById(R.id.emailTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btnEdit.setTag(rowItem.getId());
        holder.btnDelete.setTag(rowItem.getId());
        holder.name.setText(rowItem.getName());
        holder.phone.setText(rowItem.getPhone());
        holder.email.setText(rowItem.getEmail());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getContext(), ContactActivity.class);
                intent.putExtra("ADDEDITKEY","Edit");
                intent.putExtra("CONTACTID",view.getTag().toString());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Contact");
                builder.setMessage("Are you sure you want to delete contact ?");
                builder.setCancelable(false);
                final int contactId = Integer.parseInt(view.getTag().toString());

                // Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db = new DatabaseHandler(getContext());
                        db.DeleteContact(contactId);
                        data.remove(position);
                        Toast.makeText(context, "Contact has been deleted successfully", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        return convertView;
    }




}
