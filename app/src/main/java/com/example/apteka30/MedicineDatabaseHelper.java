package com.example.apteka30;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MedicineDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medicine.db";
    private static final int DATABASE_VERSION = 3; // Обновили версию базы данных

    // Таблица для лекарств
    private static final String TABLE_MEDICINE = "medicine";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    // Таблица для избранных лекарств
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID_FAVORITE = "_id";
    private static final String COLUMN_MEDICINE_ID = "medicine_id";

    private Context context;

    public MedicineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEDICINE_TABLE = "CREATE TABLE " + TABLE_MEDICINE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IMAGE_PATH + " TEXT"
                + ")";
        db.execSQL(CREATE_MEDICINE_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID_FAVORITE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEDICINE_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public void addMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, medicine.getName());
        values.put(COLUMN_DESCRIPTION, medicine.getDescription());
        values.put(COLUMN_IMAGE_PATH, medicine.getImagePath());
        db.insert(TABLE_MEDICINE, null, values);
        db.close();
    }

    public void addMedicineToFavorites(int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_ID, medicineId);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public List<Medicine> getFavoriteMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MEDICINE + " WHERE " + COLUMN_ID + " IN (SELECT " + COLUMN_MEDICINE_ID + " FROM " + TABLE_FAVORITES + ")";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Medicine medicine = new Medicine();
                medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                medicine.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                medicine.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
                medicines.add(medicine);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicines;
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDICINE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Medicine medicine = new Medicine();
                medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                medicine.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                medicine.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
                medicines.add(medicine);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicines;
    }

    public void clearAllMedicines() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINE, null, null);
        db.close();
    }

    public Medicine getMedicineById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDICINE, new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IMAGE_PATH},
                COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            medicine.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            medicine.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)));
            cursor.close();
            return medicine;
        } else {
            return null;
        }
    }
}
