package com.example.intel.kospenmove02;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    private EditText ic;
    private EditText name;
    private EditText address;
    private Spinner birthdayDaySpinner;
    private Spinner birthdayMonthSpinner;
    private Spinner birthdayYearSpinner;
    private Spinner genderSpinner;
    private Button testButton;

    private MyDBHandler dbHandler;

    private TextView testOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization - START
        dbHandler = new MyDBHandler(this, null, null, 1);
        testOutput = (TextView) findViewById(R.id.testOutputId);
        ic = (EditText) findViewById(R.id.icId);
        name = (EditText) findViewById(R.id.nameId);
        address = (EditText) findViewById(R.id.addressId);
        birthdayDaySpinner = (Spinner) findViewById(R.id.birthdayDayId);
        birthdayMonthSpinner = (Spinner) findViewById(R.id.birthdayMonthId);
        birthdayYearSpinner = (Spinner) findViewById(R.id.birthdayYearId);
        genderSpinner = (Spinner) findViewById(R.id.genderId);
        testButton = (Button) findViewById(R.id.testButtonId);
        // Initialization - END

        printDatabase();
    }

    public void testButtonClicked(View view){
//        // Test 1 - to test the input-output function of Spinner
//        String birthdayDay = String.valueOf(birthdayDaySpinner.getSelectedItem());
//        String birthdayMonth = String.valueOf(birthdayMonthSpinner.getSelectedItem());
//        String birthdayYear = String.valueOf(birthdayYearSpinner.getSelectedItem());
//        String gender = String.valueOf(genderSpinner.getSelectedItem());
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(birthdayDay);
//        sb.append("-");
//        sb.append(birthdayMonth);
//        sb.append("-");
//        sb.append(birthdayYear);
//
//
//        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
//        // Test 1 - END

        // Test 2 - to check data in Sqlite database
        testOutput.setText("");
        printDatabase();
        // Test 2 - END
    }

    public void addButtonClicked(View view){
        String str_ic = ic.getText().toString();
        String str_name = name.getText().toString();

        String str_birthdayDay = String.valueOf(birthdayDaySpinner.getSelectedItem());
        String str_birthdayMonth = String.valueOf(birthdayMonthSpinner.getSelectedItem());
        String str_birthdayYear = String.valueOf(birthdayYearSpinner.getSelectedItem());
        StringBuilder sb = new StringBuilder();
        sb.append(str_birthdayDay);
        sb.append("-");
        sb.append(str_birthdayMonth);
        sb.append("-");
        sb.append(str_birthdayYear);;

        String str_gender = String.valueOf(genderSpinner.getSelectedItem());
        String str_address = address.getText().toString();

//        // ====================================
//        //      1-Using SQLiteOpenHelper
//        // ====================================
//        Person person = new Person(str_name);
//        dbHandler.addPerson(person);
//        // ==================================== END


        // ====================================
        //      2-Using ContentProvider
        // ====================================
        Uri newlyInsertedRowUri;
        ContentValues values = new ContentValues();

        values.put(KospenuserContract.Kospenusers.NAME, str_name);

        newlyInsertedRowUri = getContentResolver().insert(
                KospenuserContract.Kospenusers.CONTENT_URI,
                values
        );
        // ==================================== END

        printDatabase();
    }

    public void printDatabase(){
//        // ====================================
//        //      1-Using SQLiteOpenHelper
//        // ====================================
//        String dbString = dbHandler.databaseToString();
//        testOutput.setText(dbString);
//        // ==================================== END

        // ====================================
        //      2-Using ContentProvider
        // ====================================
        String[] projection = {
                KospenuserContract.Kospenusers._ID,
                KospenuserContract.Kospenusers.NAME
        };
        String selection = null;
        String[] selectionArgs = {""}; /**  This defines a one-element String array to contain the selection argument. */
        selectionArgs = null; /**  If the input is the empty string */
        String sortOrder = "_ID ASC";

        @Nullable
        Cursor queryCursor = getContentResolver().query(
                KospenuserContract.Kospenusers.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        int index = queryCursor.getColumnIndex(KospenuserContract.Kospenusers.NAME);
        if(queryCursor != null) {
            if(queryCursor.getCount() < 1) {
                testOutput.setText("Query returns empty result.");
            } else {
                String dbOutput = "";
                StringBuilder sb = new StringBuilder(50);
                while(queryCursor.moveToNext()) {
                    if(queryCursor.getString(index)!=null) {
                        sb.setLength(0);
                        sb.setLength(50);
                        sb.append(queryCursor.getString(index));

                        dbOutput += sb.toString();
                        dbOutput += "\n";
                    }
                }
                testOutput.setText(dbOutput);
            }
        } else {
            testOutput.setText("Null cursor error!");
//            throw new NullPointerException("null cursor error from content provider occured!");
        }
        // ==================================== END

        ic.setText("");
        name.setText("");
        address.setText("");
    }

    public void deleteButtonClicked(View view){
//        // ====================================
//        //      1-Using SQLiteOpenHelper
//        // ====================================
//        dbHandler.deletePerson(name.getText().toString());
//        printDatabase();
//        // ==================================== END

        // ====================================
        //      2-Using ContentProvider
        // ====================================
        int numDeletedRow;
        String selection = null;
        String[] selectionArgs = {""};

        selection = KospenuserContract.Kospenusers.NAME + " = ?";
        selectionArgs[0] = name.getText().toString();

        numDeletedRow = getContentResolver().delete(
                KospenuserContract.Kospenusers.CONTENT_URI,
                selection,
                selectionArgs
        );
        // ==================================== END

        printDatabase();
    }

}




