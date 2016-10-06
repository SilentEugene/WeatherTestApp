package pershin.eugene.weathertestapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "weatherTestApp";
    private static final String TABLE_CITIES = "cities";
    private static final String KEY_ID = "id";
    private static final String KEY_ID_CITY = "id_city";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CITIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_CITY + " INTEGER," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
        onCreate(db);
    }

    @Override
    public void addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_CITY, city.getIdCity());
        values.put(KEY_NAME, city.getName());

        db.insert(TABLE_CITIES, null, values);
        db.close();
    }

    @Override
    public City getCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CITIES, new String[]{KEY_ID, KEY_ID_CITY, KEY_NAME},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            City city = new City(Integer.parseInt(cursor.getString(1)), cursor.getString(2));

            cursor.close();
            return city;
        }

        return null;
    }

    @Override
    public List<City> getAllCities() {
        List<City> cityList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CITIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                City city = new City(Integer.parseInt(cursor.getString(1)), cursor.getString(2));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return cityList;
    }

    @Override
    public int getCitiesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    @Override
    public int updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, city.getName());
        values.put(KEY_ID_CITY, city.getIdCity());

        return db.update(TABLE_CITIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(city.getIdCity()) });
    }

    @Override
    public void deleteCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITIES, KEY_ID + " = ?", new String[] { String.valueOf(city.getIdCity()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITIES, null, null);
        db.close();
    }
}
