package com.example.productmanager_lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.View;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView idView;
    EditText productBox;
    EditText priceBox;

    Button add;
    Button delete;
    Button find;

    ListView productList;
    ArrayList<String> listItem;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set variable to th ids of xml elements:
        idView = (TextView) findViewById(R.id.idView);
        productBox = (EditText) findViewById(R.id.productBox);
        priceBox = (EditText) findViewById(R.id.priceBox);
        productList = (ListView) findViewById(R.id.productList);
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        find = (Button) findViewById(R.id.find);


        MyDBHandler dbHandler = new MyDBHandler(this);
        listItem = new ArrayList<>();
        // call the viewData() method to display the existing product
        viewData();

        add.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!(productBox.toString().equals("")) && !(priceBox.toString().equals(""))) {
                    newProduct(v);
                    Toast.makeText(MainActivity.this, "data was added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "no data was added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(v);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupProduct(v);
            }
        });

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = productList.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();
                viewData();
            }
        });

    }

    public void newProduct(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this);

        // get price from the text box
        double price = Double.parseDouble(priceBox.getText().toString());

        // get product name fromt the text box
        // use the constructor from the product java
            Product product = new Product(productBox.getText().toString(), price);

            // add the new product of type product to database
            dbHandler.addProduct(product);

            // clear the text boxes
            productBox.setText("");
            priceBox.setText("");


            listItem.clear();
            viewData();




    }

    @SuppressLint("SetTextI18n")
    public void lookupProduct(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this);
        Product product = dbHandler.findProduct(productBox.getText().toString());

        // if found , then disply the poduct details
        // if not then display not foun d
        if (product != null) {

            idView.setText(String.valueOf(product.getID()));
            priceBox.setText(String.valueOf(product.getPrice()));
        } else {
            idView.setText("No Match Found");
        }

    }

    public void removeProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this);
        // delete the product in the database
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());


        // clearing the list of products
        // calling viewData() method to dispplay the updated list of products
        // this means what is displayed int he list is always current


        listItem.clear();
        viewData();
        if (result) {

            idView.setText("Record deleted");
            productBox.setText("");
            priceBox.setText("");
        } else {

            idView.setText("No Match found");
        }
    }

    public void viewData() {
        MyDBHandler dbHandler = new MyDBHandler(this);

        Cursor cursor = dbHandler.viewData();
        // if there are no products in the a toast says there is no data to show
        // otherwise , while there are products keep moving to the next product
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "NO data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                // i just want to display the product name so i only get the column 1 from the trable
                listItem.add(cursor.getString(1));
                listItem.add(cursor.getString(2));
                listItem.add(cursor.getString(3));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);


            // attaching the adapter
            productList.setAdapter(adapter);
        }
    }


}