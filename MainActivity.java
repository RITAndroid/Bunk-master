package com.rit.vishwajeet.bunkmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1,b2,refresh,aboutUs;
    public static int attended_no,bunk_no;
    public String date;
    public static Float Total_lectures,Total_bunks,percentage;
    Spinner spinner;
    TextView View_conducted,View_bunked,View_percentage,View_allowed_bunks,Last_Date;
    dbAdapter dbadapter=new dbAdapter(this);
    dbAdapter.dbhelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // SQLiteDatabase sqLiteDatabase= helper.getWritableDatabase();


        b1= (Button) findViewById(R.id.but_add_sub);
        b2= (Button) findViewById(R.id.but_enter_att);
        refresh = (Button) findViewById(R.id.but_refresh);
        aboutUs = (Button) findViewById(R.id.button_about_us);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, AddSubject.class);
                startActivity(startIntent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, Enter_Attendance.class);
                startActivity(startIntent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, about_us.class);
                startActivity(startIntent);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner3);
        View_conducted = (TextView) findViewById(R.id.textView_conduct);
        View_bunked = (TextView) findViewById(R.id.textView_bunk);
        View_percentage = (TextView) findViewById(R.id.textView_percent);
        View_allowed_bunks= (TextView) findViewById(R.id.Text_allowed_bunk);
        Last_Date = (TextView) findViewById(R.id.text_date);

        helper = new dbAdapter.dbhelper(this);

        ArrayList<String> sub = helper.getAllsubjects();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,sub);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selected_subject = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),Selected_subject, Toast.LENGTH_SHORT).show();
                attended_no = dbadapter.getlect(Selected_subject);
                String val1 = String.valueOf(attended_no);
                View_conducted.setText(val1);
                bunk_no = dbadapter.getbunk(Selected_subject);
                String val2 =String.valueOf(bunk_no);
                View_bunked.setText(val2);
              //  date = dbadapter.getdate(Selected_subject);
                //Last_Date.setText(date);
                calculate_percentage();
                allowed();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    public void calculate_percentage(){
        Total_lectures = Float.parseFloat(View_conducted.getText().toString());
        Total_bunks = Float.parseFloat(View_bunked.getText().toString());
        percentage = (Total_bunks/Total_lectures)*100;
        percentage = 100 -percentage;
        String percentage1 = String.valueOf(percentage);
        View_percentage.setText(percentage1);
    }
    public void allowed(){
        if(percentage<75){
            View_allowed_bunks.setText("0");
        }
        else{
            Float temp_per=percentage,tmp_bunk;
            int i=0;
            tmp_bunk=Total_bunks;
            while (temp_per>75){

                tmp_bunk=tmp_bunk+1;
                temp_per=(tmp_bunk/Total_lectures)*100;
                temp_per=100-temp_per;
                i=i+1;
            }
            View_allowed_bunks.setText(" "+i);
        }
    }
}
