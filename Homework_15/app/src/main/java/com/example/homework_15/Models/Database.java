package com.example.homework_15.Models;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class Database {
    public static void Initialize(Context ctx) {
        try (SQLiteDatabase db = ctx.openOrCreateDatabase("HW15", Context.MODE_PRIVATE, null)) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR);");
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<User> getUsers(Context ctx) {
        ArrayList<User> users = new ArrayList<>();

        try (
            SQLiteDatabase db = ctx.openOrCreateDatabase("HW15", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT id, name FROM users", null)
                ) {

            int idIdx = cursor.getColumnIndex("id");
            int nameIdx = cursor.getColumnIndex("name");

            if (cursor.moveToFirst()) {
                do {
                    users.add(new User(cursor.getInt(idIdx), cursor.getString(nameIdx)));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return users;
    }

    public static int createUser(Context ctx, String name) {
        try (
                SQLiteDatabase db = ctx.openOrCreateDatabase("HW15", Context.MODE_PRIVATE, null);
        ) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            return (int) db.insert("users", null, values);
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public static void updateUser(Context ctx, int id, String name) {
        try (
                SQLiteDatabase db = ctx.openOrCreateDatabase("HW15", Context.MODE_PRIVATE, null);
        ) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            db.update("users", values, "id=?", new String[]{Integer.toString(id)});
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("DefaultLocale")
    public static void deleteUser(Context ctx, int id) {
        try (
                SQLiteDatabase db = ctx.openOrCreateDatabase("HW15", Context.MODE_PRIVATE, null);
        ) {
            db.delete("users", "id=?", new String[]{Integer.toString(id)});
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
