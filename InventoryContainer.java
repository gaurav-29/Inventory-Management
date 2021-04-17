package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InventoryContainer extends AppCompatActivity {

    DBAdapter dbAdapter;
    Context ctx = this;
    ArrayList<Inventory> InventoryList = new ArrayList<Inventory>();
    RecyclerView recinventory;
    FloatingActionButton btnaddinventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_container);

        init();
        GetDataFromTable();
        HandleEvent();
    }

    private void GetDataFromTable() {

        Cursor c = dbAdapter.view_data();   // This will call view_data() method of DBAdapter class.

        if(c!=null)     // view_data() may return null, so we have to check.

        // Difference between null and 0 :  0 is a numeric value, null is not a numeric value. It is like a blank(empty) cell.
        {
            int size = c.getCount(); //get count method returns how many object(record) cursor has
            if(size==0)
            {
                Toast.makeText(ctx,"No data found",Toast.LENGTH_LONG).show();
            }
            else
            {
                //local variables
                String productname,suppliername,mobile;
                int id, price, quantity;

                while(c.moveToNext()==true)     // this is the library method of Cursor class.
                {
                    productname = c.getString(c.getColumnIndex("product_name"));
                    suppliername = c.getString(c.getColumnIndex("supplier_name"));
                    mobile = c.getString(c.getColumnIndex("mobile"));
                    id = c.getInt(c.getColumnIndex("_id"));
                    price = c.getInt(c.getColumnIndex("price"));
                    quantity = c.getInt(c.getColumnIndex("quantity"));
                    Inventory inv = new Inventory(productname,suppliername,mobile,id,price,quantity);  // from here values of variables will go to Inventory.java
                    InventoryList.add(inv);
                }
                InventoryAdapter adapter = new InventoryAdapter(InventoryList,ctx); // from here CourseList and ctx will pass to Constructor of InventoryAdapter.java.(passing data from one java file to another).
                recinventory.setLayoutManager(new GridLayoutManager(ctx,1));
                recinventory.setItemAnimator(new DefaultItemAnimator());
                recinventory.setAdapter(adapter); //it will call onCreateViewHolder method of InventoryAdapter class
            }
            c.close(); //close cursor. if not closed, it will give error.
        }
    }

    private void HandleEvent() {

        btnaddinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, AddEditInventory.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {

        recinventory = findViewById(R.id.recinventory);
        btnaddinventory = findViewById(R.id.btnaddinventory);
        dbAdapter = new DBAdapter(ctx, DBAdapter.DATABASE, null, DBAdapter.VERSION);
    }
}
