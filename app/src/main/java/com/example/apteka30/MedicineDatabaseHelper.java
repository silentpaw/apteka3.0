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
    private static final int DATABASE_VERSION = 2;

    // Таблица для лекарств
    private static final String TABLE_MEDICINE = "medicine";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    // Таблица для последних просмотренных лекарств
    private static final String TABLE_RECENTLY_VIEWED = "recently_viewed";
    private static final String COLUMN_ID_RECENTLY_VIEWED = "_id"; // Уникальный идентификатор для таблицы "recently_viewed"
    private static final String COLUMN_MEDICINE_ID = "medicine_id";
    private static final int RECENTLY_VIEWED_LIMIT = 3;  // Лимит на количество последних просмотренных лекарств

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

        String CREATE_RECENTLY_VIEWED_TABLE = "CREATE TABLE " + TABLE_RECENTLY_VIEWED + "("
                + COLUMN_ID_RECENTLY_VIEWED + " INTEGER PRIMARY KEY AUTOINCREMENT," // Уникальный идентификатор
                + COLUMN_MEDICINE_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_RECENTLY_VIEWED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENTLY_VIEWED);
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


    public void addRecentlyViewedMedicine(int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Проверяем, есть ли уже это лекарство в таблице
        Cursor cursor = db.query(TABLE_RECENTLY_VIEWED, new String[]{COLUMN_MEDICINE_ID},
                COLUMN_MEDICINE_ID + "=?", new String[]{String.valueOf(medicineId)}, null, null, null);

        if (cursor.getCount() > 0) {
            // Если лекарство уже существует в таблице, удаляем его
            db.delete(TABLE_RECENTLY_VIEWED, COLUMN_MEDICINE_ID + "=?", new String[]{String.valueOf(medicineId)});
        }

        // Добавляем новое лекарство в таблицу
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_ID, medicineId);
        db.insert(TABLE_RECENTLY_VIEWED, null, values);

        // Удаляем лишние записи, чтобы оставалось только 3 последних просмотренных лекарства
        String deleteQuery = "DELETE FROM " + TABLE_RECENTLY_VIEWED + " WHERE " + COLUMN_ID_RECENTLY_VIEWED + " NOT IN (SELECT " + COLUMN_ID_RECENTLY_VIEWED + " FROM " + TABLE_RECENTLY_VIEWED + " ORDER BY " + COLUMN_ID_RECENTLY_VIEWED + " DESC LIMIT " + RECENTLY_VIEWED_LIMIT + ")";
        db.execSQL(deleteQuery);

        cursor.close();
        db.close();
    }

    public List<Medicine> getRecentlyViewedMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MEDICINE + " WHERE " + COLUMN_ID + " IN (SELECT " + COLUMN_MEDICINE_ID + " FROM " + TABLE_RECENTLY_VIEWED + " ORDER BY " + COLUMN_ID_RECENTLY_VIEWED + " DESC)";
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


