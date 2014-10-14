package com.custom.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;

public class CustomProvider extends ContentProvider{
	private DataBaseOpenHelper database;
	public static final String uri = "com.custom.provider";
	private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
	public static final int MATCH = 1;
	static{
		uriMatcher.addURI(uri,"contact", MATCH);
	}
	@Override
	public int delete(Uri uri, String selection, String[] args) {
		int count = 0;
		switch(uriMatcher.match(uri)){
				case MATCH:
					count = database.getWritableDatabase().delete("contact", selection, args);
			break;
		}
		return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch(uriMatcher.match(uri)){
			case MATCH:
				database.getWritableDatabase().insert("contact", "id", values);
			break;
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		database = new DataBaseOpenHelper(getContext());
		SQLiteDatabase rdb =  database.getReadableDatabase();
		Cursor rcursor =  rdb.rawQuery("select id from contact",null);
		if(rcursor.moveToNext())return false;
		ContentResolver contentResolver=getContext().getContentResolver();
		Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[]{
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
					ContactsContract.CommonDataKinds.Phone.DATA1,
					ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY
				}, 
				null,null,null);
		SQLiteDatabase db =  database.getWritableDatabase();
		db.beginTransaction();
		String sql = "insert or replace into contact (id ,name ,photoId ,phoneNum ,sortkey ) values(?,?,?,?,?)";
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String name = cursor.getString(1);
			String photoId = cursor.getString(2);
			String data1 = cursor.getString(3);
			String sortkey = cursor.getString(4);		
			db.execSQL(sql,new String[]{id,name,photoId,data1,sortkey});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
		switch(uriMatcher.match(uri)){
			case MATCH:
				return database.getWritableDatabase().query("contact", projection, selection, selectionArgs, null,null,null);
		}
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}



}
