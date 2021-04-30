package com.example.sipsupporterapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SipSupporterDBHelper extends SQLiteOpenHelper {
    public SipSupporterDBHelper(@Nullable Context context) {
        super(context, SipSupporterSchema.DB_NAME, null, SipSupporterSchema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE " + SipSupporterSchema.ServerDataTable.NAME + " (");
        builder.append(SipSupporterSchema.ServerDataTable.Cols.PRIMARY_KEY + " INTEGER PRIMARY KEY,");
        builder.append(SipSupporterSchema.ServerDataTable.Cols.CENTER_NAME + " TEXT,");
        builder.append(SipSupporterSchema.ServerDataTable.Cols.IP_ADDRESS + " TEXT,");
        builder.append(SipSupporterSchema.ServerDataTable.Cols.PORT + " TEXT");
        builder.append(");");

        db.execSQL(builder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
