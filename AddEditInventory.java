package com.example.inventorymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditInventory extends AppCompatActivity {

    EditText txtproductname,txtsuppliername,txtmobile,txtprice,txtquantity;
    String productname,suppliername,mobile,price,quantity;
    int price1,quantity1;
    TextView lbltotal;
    Button btnadd;
    public int InventoryId;
    boolean isEditMode = false;
    DBAdapter dbAdapter;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_inventory);

        init();
        CheckMode();
        HandleEvent();
    }

    private void CheckMode() {

        if (this.getIntent() != null && this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("inventoryid") == true) {
            int inventoryid = getIntent().getExtras().getInt("inventoryid");

            String FieldList[] = {"_id", "product_name", "supplier_name", "mobile", "price", "quantity"};
            String Condition[] = {String.valueOf(inventoryid)};
            // Below statement will call the view_data_for_edit() method of DBAdapter class and returned result will be assign to Cursor type object inventory.
            Cursor inventory = dbAdapter.view_data_for_edit(FieldList, Condition); // To view the data in edit mode for selected inventory id.

            if (inventory != null && inventory.getCount() >= 1) {
                inventory.moveToNext();
                txtproductname.setText(inventory.getString(inventory.getColumnIndex("product_name")));
                txtsuppliername.setText(inventory.getString(inventory.getColumnIndex("supplier_name")));
                txtmobile.setText(inventory.getString(inventory.getColumnIndex("mobile")));
                txtprice.setText(inventory.getString(inventory.getColumnIndex("price")));
                txtquantity.setText(inventory.getString(inventory.getColumnIndex("quantity")));

                btnadd.setText("Save Changes");
                InventoryId = inventory.getInt(inventory.getColumnIndex("_id"));

                inventory.close();
            }
            isEditMode = true;
        }
    }

    private boolean ValidateInput()
    {
        boolean isValid = true;

        productname = txtproductname.getText().toString();  // We should always get text from edittext in string.
        suppliername = txtsuppliername.getText().toString();
        mobile = txtmobile.getText().toString();
        price = txtprice.getText().toString();
        quantity = txtquantity.getText().toString();

        if(productname.trim().length()==0)     // Product name is empty.
        {
            isValid = false;
            txtproductname.setError("Product name is required");
        }
        if(suppliername.trim().length()==0)
        {
            isValid = false;
            txtsuppliername.setError("Supplier name is required");
        }
        if(mobile.trim().length()==0)
        {
            isValid = false;
            txtmobile.setError("Mobile number required");
        }
        if(price.trim().length()==0)
        {
            isValid = false;
            txtprice.setError("Price required");
        }
        if(quantity.trim().length()==0)
        {
            isValid = false;
            txtquantity.setError("Quantiry required");
        }

        if(price.trim().length()!=0 && Integer.parseInt(price)<=0)    // converting string into int through wrapper class.
        {
            isValid = false;
            txtprice.setError("Invalid price");
        }
        if(quantity.trim().length()!=0 && Integer.parseInt(quantity)<=0)
        {
            isValid = false;
            txtquantity.setError("Invalid quantity");
        }

        return isValid;
    }

    private void HandleEvent(){

        txtquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

            // App was crashing on line no 186 & 187 when we press Add Inventory button. Bbcoz when we press button, txtprice & txtquantity will be
            // ("") as per txtprice.setText("") & txtquantity.setText(""). So this listener consider blank value and do the multiplication
            // calculation again bcoz of text changed. And multiplication of blank value causing the crashing of app. So following solution is made.

                   int no1 = 1, no2 = 1;

                    if (!(txtprice.getText().toString().equals("")))
                        no1 = (Integer.parseInt(txtprice.getText().toString()));    // else no1 = 1.

                    if (!(txtquantity.getText().toString().equals("")))
                        no2 = (Integer.parseInt(txtquantity.getText().toString())); // else no1 = 1 so in below calculation app will not crash.

                    int Total = no1 * no2;
                    lbltotal.setText(Total + " Rs");
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ValidateInput() == true)
                {
                    price1 = Integer.parseInt(price);   // Bcoz we have to pass price as integer in insert and edit query.
                    quantity1 = Integer.parseInt(quantity);

                    if (isEditMode == false)
                    {
                        boolean result = dbAdapter.insert_data(productname, suppliername, mobile, price1, quantity1);
                        // insert_data() of DBAdaper class will be called from here and response will be returned to this method.
                        if (result == true) {
                            Toast.makeText(ctx, "Data successfully inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ctx, "Data not inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        {
                        String Conditions[] = {String.valueOf(InventoryId)};
                        boolean response = dbAdapter.edit_data(productname, suppliername, mobile, price1, quantity1, Conditions);
                        // edit_data() of DBAdaper class will be called from here and response will be returned to this method.
                        if (response == true) {
                            Toast.makeText(ctx, "Data successfully updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ctx, "Data not updated", Toast.LENGTH_LONG).show();
                        }
                    }

                    txtproductname.setText("");  // This will clear the Typed data in edittext
                    txtsuppliername.setText("");
                    txtmobile.setText("");
                    txtprice.setText("");
                    txtquantity.setText("");

                    startActivity(new Intent(ctx, InventoryContainer.class));
                    finish();
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void init() {
        txtproductname = findViewById(R.id.txtproductname);
        txtsuppliername = findViewById(R.id.txtsuppliername);
        txtmobile = findViewById(R.id.txtmobile);
        txtprice = findViewById(R.id.txtprice);
        txtquantity = findViewById(R.id.txtquantity);
        lbltotal = findViewById(R.id.lbltotal);
        btnadd = findViewById((R.id.btnadd));

        dbAdapter = new DBAdapter(ctx, DBAdapter.DATABASE,null,DBAdapter.VERSION);
    }
}
