package com.example.productmanager_lab4;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends  SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productName";
    private static final String COLUMN_PRICE = "price";


    public MyDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.context = context;
    }

    public void onCreate(SQLiteDatabase db){

        String CREATE_PRODUCT_TABLE = "CREATE TABLE "+ TABLE_PRODUCTS
                + "("+COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PRODUCTNAME + " TEXT,"
                + COLUMN_PRICE + " DOUBLE" +
                ")";
        db.execSQL(CREATE_PRODUCT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);


    }

    public void addProduct(Product product){

        SQLiteDatabase db = this.getWritableDatabase();
        // creating an empty set of value
        ContentValues values = new ContentValues();

        // add values to the set
        values.put (COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRICE, product.getPrice());

        // insert the set into the products table and close

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public Product findProduct (String productName){

            SQLiteDatabase db = this.getWritableDatabase();

            // run a query to find the products with the specified product name
            // SELECT  * FROM TABLE_PRODUCTS WHERE COLUMN_PRODUCTNAME = "productname
            String query = "SELECT * FROM " + TABLE_PRODUCTS
                    + "WHERE " + COLUMN_PRODUCTNAME
                    + "= \""+ productName + "\"";
            // parsing the query
             Cursor cursor = db.rawQuery(query, null);

             Product product = new Product();

             if(cursor.moveToFirst()){
                 product.setID(Integer.parseInt(cursor.getString(0)));
                 product.setProductName(cursor.getString(1));
                 product.setPrice(Double.parseDouble(cursor.getString(2)));
                 cursor.close();
             }else{
                 product=null;
             }
            db.close();
             // we return the first product in the query result with the specified product name
             // or null if there is no product
            return product;
    }
    // delete from database
    public Boolean deleteProduct(String productName){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();

        //run a query to find the product with the specified name then delete
        //

        String query =  "SELECT * FROM " + TABLE_PRODUCTS
                        + " WHERE " + COLUMN_PRODUCTNAME
                        + " = \" " + productName + "\"";
            // PARSING THE QUERY
        Cursor cursor = db.rawQuery(query, null);

        // moves cursor to the first row
        // this deletes the first occurrence of the4 product with specified name
        if( cursor.moveToFirst()){
            String idStr = cursor.getString(0);
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = " + idStr, null);
            cursor.close();
            result = true;

        }
        db.close();
        // if product is deleted this return true
        return result;
    }

    public Cursor viewData(){

            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_PRODUCTS;

            Cursor cursor = db.rawQuery(query, null);
            return cursor;


    }



}
