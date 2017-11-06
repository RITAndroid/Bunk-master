package com.rit.vishwajeet.bunkmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class dbAdapter {
    dbhelper helper ;
    public static String date;
    public dbAdapter(Context context){
        helper=new dbhelper(context);
    }

    public int insertData(String subject){
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbhelper.Sname,subject);
        contentValues.put(dbhelper.Lect_attend,0);
        contentValues.put(dbhelper.Lect_bunk,0);
        int id= (int) db.insert(dbhelper.db_table,null,contentValues);
        return id;
    }

    public int updateinfo(String subject,int conducted,int bunked){
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(dbhelper.Sname,subject);
      /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         date = sdf.format(Calendar
                .getInstance().getTime());*/
        contentValues.put(dbhelper.Lect_attend,conducted);
        contentValues.put(dbhelper.Lect_bunk,bunked);
       // contentValues.put(dbhelper.date, date.valueOf(date));
        String[] whereArgs={subject};
        int id= (int) db.update(dbhelper.db_table,contentValues,dbhelper.Sname +" =? ",whereArgs);
        return id;
    }

    public int deleterow (String SubjectName){

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {SubjectName};
        int count = db.delete(dbhelper.db_table,dbhelper.Sname+"=?",whereArgs);
        return count;
    }

    public int getlect(String selected_subject){
        int lect_con;
        //String select = "select "+dbhelper.Lect_attend+ "from "+ dbhelper.db_table+ " where "+dbhelper.Sname+"="+selected_subject;
        //String select = "Select Conducted from BUNKMANAGER where Subject="+selected_subject;
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] columns = {dbhelper.Lect_attend};
        Cursor cursor= db.query(dbhelper.db_table,columns,dbhelper.Sname+" = '"+selected_subject+"' ",null,null,null,null);
        //Cursor cursor = db.rawQuery(select,null);
        cursor.moveToFirst();
        lect_con =cursor.getInt(cursor.getColumnIndex(dbhelper.Lect_attend));

        return lect_con;
      //return 1;
    }

    public int getbunk(String selected_subject){
        int lect_bunk;
        //String select = "select "+dbhelper.Lect_bunk+" from "+dbhelper.db_table+" where "+dbhelper.Sname+"=" +selected_subject;
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] columns = {dbhelper.Lect_bunk};
        Cursor cursor= db.query(dbhelper.db_table,columns,dbhelper.Sname+" = '"+selected_subject+"' ",null,null,null,null);
        //Cursor cursor = db.rawQuery(select,null);
        cursor.moveToFirst();
        lect_bunk =cursor.getInt(cursor.getColumnIndex(dbhelper.Lect_bunk));
        return lect_bunk;
    }

    public String getdate(String selected_subject){
        String Date;
        //String select = "select "+dbhelper.Lect_bunk+" from "+dbhelper.db_table+" where "+dbhelper.Sname+"=" +selected_subject;
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] columns = {dbhelper.date};
        Cursor cursor= db.query(dbhelper.db_table,columns,dbhelper.Sname+" = '"+selected_subject+"' ",null,null,null,null);
        //Cursor cursor = db.rawQuery(select,null);
        cursor.moveToFirst();
         Date=cursor.getString(cursor.getColumnIndex(dbhelper.date));
        return Date;
    }

   static class dbhelper extends SQLiteOpenHelper {

        private static final String db_name = "bunkdb";
        public static final String db_table = "BUNKMANAGER";
       // private static final String SID = "id";
        public static final String Sname = "Subject";
        public static final String Lect_attend = "Conducted";
        public static final String Lect_bunk = "Bunked";
       private static final String date ="Date";
      // public static final String Create_db = "CREATE DATABASE "+db_name+";";
       //public static final String use_db = "USE "+db_name+";";
        public static final String Create_table = "CREATE TABLE "+db_table+"("+Sname+" varchar(50) primary key," +Lect_attend+ " INTEGER,"+Lect_bunk+" INTEGER"+date+" date);";
        public static final String Drop_table = "DROP TABLE IF EXIST" + db_table;
        private static final int version_no = 1;
        static Context context;

        public dbhelper(Context context) {
            super(context, db_name, null, version_no);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                //sqLiteDatabase.execSQL(Create_db);
               // sqLiteDatabase.execSQL(use_db);
                sqLiteDatabase.execSQL(Create_table);
            } catch (SQLException e) {
                message.Message(context, "unable to create table" + e);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(Drop_table);
            } catch (SQLException e) {
                message.Message(context, "unable to upgrade table" + e);
            }
        }

        public ArrayList<String> getAllsubjects(){
            ArrayList<String> list = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            db.beginTransaction();
            try {
                String selected = "Select "+Sname+" from "+db_table;
                Cursor cursor =db.rawQuery(selected,null);
                if (cursor.getCount() > 0){
                    while (cursor.moveToNext()){
                        String subjects= cursor.getString(cursor.getColumnIndex("Subject"));
                        list.add(subjects);
                    }
                }
                db.setTransactionSuccessful();
            }catch (Exception e){
                message.Message(context,"unable to create list "+e);
            }
            finally {
                db.endTransaction();
                db.close();
            }
            return list;
        }


    }

}