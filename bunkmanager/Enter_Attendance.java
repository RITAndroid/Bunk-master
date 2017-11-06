package com.rit.vishwajeet.bunkmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Enter_Attendance extends AppCompatActivity {

    public  static Spinner spinner;
    public  static Button add_lect,min_lect,add_bunk,min_bunk,save,but_fetch_data;
    public  static TextView Total_lect,Bunked_lect,Attend_per;
    public static String mylec1,mybunk1,percentage1,Selected_subject;
    public static Float Total_lectures,Total_bunks,percentage;
    public static int attended_no,bunk_no;
    dbAdapter dbadapter=new dbAdapter(this);
    dbAdapter.dbhelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__attendance);
        spinner = (Spinner) findViewById(R.id.spinner);

       add_lect= (Button) findViewById(R.id.butt_con_plus);
        min_lect= (Button) findViewById(R.id.but_con_minus);
        add_bunk = (Button) findViewById(R.id.but_bunk_plus);
        min_bunk = (Button) findViewById(R.id.but_bunk_minus);
        save = (Button) findViewById(R.id.but_save_att);

        Total_lect = (TextView) findViewById(R.id.text_lect_con);
        Bunked_lect = (TextView) findViewById(R.id.text_bunk_lec);
        Attend_per = (TextView) findViewById(R.id.text_per_att);

        helper = new dbAdapter.dbhelper(this);

        ArrayList<String> sub = helper.getAllsubjects();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,sub);
        spinner.setAdapter(adapter);


        add_lect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //message.Message(context,"clicked");

                    int mylec = Integer.parseInt(Total_lect.getText().toString());
                    mylec =mylec+1;
                    mylec1 = String.valueOf(mylec);
                    Total_lect.setText(mylec1);
                    calculate_percentage();
            }
        });
        min_lect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //message.Message(context,"clicked");
                int check1 = Integer.parseInt(Bunked_lect.getText().toString());
                int mylec = Integer.parseInt(Total_lect.getText().toString());
                if (mylec>0){
                    if(mylec==check1){mylec =mylec-1;
                        check1=check1-1;
                        mybunk1=String.valueOf(check1);
                        Bunked_lect.setText(mybunk1);
                    }else {
                        mylec =mylec-1;
                    }
                }
                mylec1 = String.valueOf(mylec);
                Total_lect.setText(mylec1);
                calculate_percentage();
            }
        });
        add_bunk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //message.Message(context,"clicked");
                int check1 = Integer.parseInt(Total_lect.getText().toString());
                int mylec = Integer.parseInt(Bunked_lect.getText().toString());
                if(mylec<check1)
                {mylec =mylec+1;
                mybunk1 = String.valueOf(mylec);
                Bunked_lect.setText(mybunk1);
                calculate_percentage();}

            }
        });
        min_bunk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //message.Message(context,"clicked");

                int mylec = Integer.parseInt(Bunked_lect.getText().toString());
                if(mylec>0){
                mylec =mylec-1;}
                mybunk1 = String.valueOf(mylec);
                Bunked_lect.setText(mybunk1);
                calculate_percentage();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_subject = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),Selected_subject, Toast.LENGTH_SHORT).show();
                attended_no = dbadapter.getlect(Selected_subject);
                String val1 = String.valueOf(attended_no);
                Total_lect.setText(val1);
                bunk_no = dbadapter.getbunk(Selected_subject);
                String val2 =String.valueOf(bunk_no);
                Bunked_lect.setText(val2);
                calculate_percentage();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //message.Message(context,"clicked");
                int mylec = Integer.parseInt(Total_lect.getText().toString());
                int mybunk = Integer.parseInt(Bunked_lect.getText().toString());
                int id=dbadapter.updateinfo(Selected_subject,mylec,mybunk);
                if(id>0){
                    message.Message(getApplicationContext(),"Successfully added attendance");
                }
                else{
                    message.Message(getApplicationContext(),"Error while inserting");
                }

            if(percentage<75){
                get_notification();
            }

            }
        });
    }

       public void calculate_percentage(){
        Total_lectures = Float.parseFloat(Total_lect.getText().toString());
        Total_bunks = Float.parseFloat(Bunked_lect.getText().toString());
        percentage = (Total_bunks/Total_lectures)*100;
        percentage = 100 -percentage;
        percentage1 = String.valueOf(percentage);
        Attend_per.setText(percentage1);
    }
    public void get_notification(){

        NotificationManager notificationmgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(this)
                .setSmallIcon(R.drawable.academy)
                .setContentTitle("Alert")
                .setContentText("Attendance running low in "+Selected_subject)
                .build();
        notificationmgr.notify(0,notif);
    }

}
