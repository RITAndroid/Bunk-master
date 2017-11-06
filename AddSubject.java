package com.rit.vishwajeet.bunkmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddSubject extends AppCompatActivity {

    EditText subject_name;
    Button add_sub,remove_sub,refresh;
    dbAdapter adapter;
    dbAdapter.dbhelper helper;
    Spinner remove_spinner;
    public static String Selected_subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        subject_name = (EditText) findViewById(R.id.editText_subName);
        add_sub = (Button) findViewById(R.id.button_add_subject);
        refresh = (Button) findViewById(R.id.but_refresh);
        remove_sub = (Button) findViewById(R.id.but_remove_sub);
        remove_spinner = (Spinner) findViewById(R.id.spinner_remove_sub);
        adapter =new dbAdapter(this);


        helper = new dbAdapter.dbhelper(this);

        ArrayList<String> sub = helper.getAllsubjects();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,sub);
        remove_spinner.setAdapter(adapter);

        remove_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_subject = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),Selected_subject, Toast.LENGTH_SHORT).show();
                // int attended_no = Adapter.getlect_con(Selected_subject);
                //String val1 = String.valueOf(attended_no);
                //Total_lect.setText(val1);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    public void addSub(View view){
        String subject = subject_name.getText().toString();
        int id=adapter.insertData(subject);
        if(id>0){
            message.Message(this,"Successfully added subject");
        }
        else{
            message.Message(this,"Error while inserting");
        }
    }
    public void delete(View view){
        int count = adapter.deleterow(Selected_subject);
        if(count>0){
            message.Message(this,"Successfully deleted subject");
        }
        else{
            message.Message(this,"Error while deleting");
        }
    }

}
