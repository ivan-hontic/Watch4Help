package hr.foi.hontic.ivan.watch4help;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase mydatabase;
    TextView txtCurrentNum;
    EditText editTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrentNum = (TextView) findViewById(R.id.txtCurrentNumber);
        txtCurrentNum.setText("Current number: "+getCurrentNumber());

        editTxt = (EditText) findViewById(R.id.editNumber);

    }

    public String getCurrentNumber(){
        mydatabase = openOrCreateDatabase("dbHelp", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS settingsTable(Number VARCHAR);");

        Cursor resultSet = mydatabase.rawQuery("Select * from settingsTable",null);

        resultSet.moveToFirst();

        String dataMessage=resultSet.getString(0);
        mydatabase.close();
        resultSet.close();

        return dataMessage;
    }

    public void setNewNumber(String number){
        mydatabase = openOrCreateDatabase("dbHelp", MODE_PRIVATE, null);

        mydatabase.execSQL("DROP TABLE settingsTable;");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS settingsTable(Number VARCHAR);");
        ContentValues contentValues = new ContentValues();
        contentValues.put("Number", number);

        mydatabase.insert("settingsTable", null, contentValues);
        mydatabase.close();
    }

    public void setNewNumber(View v){

        String text = editTxt.getText().toString();
        if ( !TextUtils.isEmpty(text) ) {
            Log.d("MainActivity", "Number: "+text);
        }
        txtCurrentNum.setText("Current number: "+text);
        setNewNumber(text);
    }


}
