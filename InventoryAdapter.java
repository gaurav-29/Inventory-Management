package com.example.inventorymanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter {

    private ArrayList<Inventory> InventoryList;
    private Context ctx;
    DBAdapter dbAdapter;

    public InventoryAdapter(ArrayList<Inventory> inventoryList, Context ctx) {

        InventoryList = inventoryList;
        this.ctx=ctx;
        dbAdapter = new DBAdapter(ctx, DBAdapter.DATABASE, null, DBAdapter.VERSION);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View InquiryRow = inflater.inflate(R.layout.inventory_row, null); //this is the layout we want to repeat for each course
        MyWidgetContainer container = new MyWidgetContainer(InquiryRow);
        return container;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        MyWidgetContainer container = (MyWidgetContainer) holder;
        final Inventory CurrentInventory = InventoryList.get(position);
        container.lblproductname.setText(CurrentInventory.getProduct_name());
        container.lblprice.setText(CurrentInventory.getPrice() + " Rs");

        container.btneditinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SwitchIntent = new Intent(ctx, AddEditInventory.class);
                SwitchIntent.putExtra("inventoryid", CurrentInventory.getId());
                //Activity MyActivity = (Activity) ctx;       // We have to write this bcoz this class does not extending AppCompatActivity class.
                ctx.startActivity(SwitchIntent);
            }
        });

        container.btndeleteinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Condition[] = {String.valueOf(CurrentInventory.getId())};
                dbAdapter.delete_data(Condition);  // To delete data from table in database. This will call delete_data() method in DBAdapter class.
                InventoryList.remove(position);    // To remove specified object from specified position from arraylist (from current screen)
                notifyDataSetChanged();            // To refresh the recyclerview.
                Toast.makeText(ctx, "Inventory Deleted", Toast.LENGTH_LONG).show();
            }
        });

        container.btnviewinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent SwitchIntent = new Intent(ctx,InventoryDetail.class);
                SwitchIntent.putExtra("inventoryid2",CurrentInventory.getId()); // CurrentCourse object is sent through Parcelable interface in Course.java
                ctx.startActivity(SwitchIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return InventoryList.size();
    }

    class MyWidgetContainer extends RecyclerView.ViewHolder     // MyWidgetContainer class must extend RecyclerView.ViewHolder class.
    {
        TextView lblproductname,lblprice;
        ImageView btneditinventory,btndeleteinventory,btnviewinventory;

        public MyWidgetContainer(@NonNull View itemView) {  //this constructor is in parent class (ViewHolder) so we have to implement it cumpulsary here.
            super(itemView);  // this super(itemView) will call parent class constructor.

            lblproductname = itemView.findViewById(R.id.lblproductname);
            lblprice = itemView.findViewById(R.id.lblprice);
            btneditinventory = itemView.findViewById(R.id.btneditinventory);
            btndeleteinventory = itemView.findViewById(R.id.btndeleteinventory);
            btnviewinventory = itemView.findViewById(R.id.btnviewinventory);
        }
    }
}
