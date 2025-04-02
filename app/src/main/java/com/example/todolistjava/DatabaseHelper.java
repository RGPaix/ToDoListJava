package com.example.todolistjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo_list.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tarefas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TAREFA = "tarefa";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TAREFA + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean adicionarTarefa(String tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAREFA, tarefa);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<String> obterTodasTarefas() {
        ArrayList<String> tarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TAREFA}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            tarefas.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return tarefas;
    }

    public boolean removerTarefa(String tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_TAREFA + " = ?", new String[]{tarefa});
        db.close();
        return result > 0;
    }
}
