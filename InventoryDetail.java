package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class InventoryDetail extends AppCompatActivity {

    TextView lblproductname2, lblsuppliername2, lblmobile2, lblprice2, lblquantity2;
    DBAdapter dbAdapter;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);

        init();

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("inventoryid2") == true)
        {
            int inventoryid2 = getIntent().getExtras().getInt("inventoryid2");

            String FieldList[] = {"_id", "product_name", "supplier_name", "mobile", "price", "quantity"};
            String Condition[] = {String.valueOf(inventoryid2)};
            // Below statement will call the view_data_for_edit() method of DBAdapter class and returned result will be assign to Cursor type object inventory.
            Cursor inventory2 = dbAdapter.view_data_for_edit(FieldList, Condition); // To view the data here for selected inventory id.

            if (inventory2 != null && inventory2.getCount() >= 1) {
                while(inventory2.moveToNext()==true) {
                    lblproductname2.setText(inventory2.getString(inventory2.getColumnIndex("product_name")));
                    lblsuppliername2.setText(inventory2.getString(inventory2.getColumnIndex("supplier_name")));
                    lblmobile2.setText(inventory2.getString(inventory2.getColumnIndex("mobile")));
                    lblprice2.setText(inventory2.getString(inventory2.getColumnIndex("price")));
                    lblquantity2.setText(inventory2.getString(inventory2.getColumnIndex("quantity")));
                }
                inventory2.close();
            }
        }
    }

    private void init() {

        lblproductname2 = findViewById(R.id.lblproductname2);
        lblsuppliername2 = findViewById(R.id.lblsuppliername2);
        lblmobile2 = findViewById(R.id.lblmobile2);
        lblprice2 = findViewById(R.id.lblprice2);
        lblquantity2 = findViewById(R.id.lblquantity2);
        dbAdapter = new DBAdapter(ctx, DBAdapter.DATABASE, null, DBAdapter.VERSION);
    }
}
