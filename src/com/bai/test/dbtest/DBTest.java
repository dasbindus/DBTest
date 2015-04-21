package com.bai.test.dbtest;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DBTest extends ActionBarActivity {
	private static final String TAG = "DBTest";
	SQLiteDatabase db;
	Button btn = null;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbtest);
		// ����������ݿ�
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()
				+ "/my.db3", null);
		Log.i(TAG, "�������ݿ�");
		listView = (ListView) findViewById(R.id.show);
		btn = (Button) findViewById(R.id.ok);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��ȡ�û�����
				String title = ((EditText) findViewById(R.id.title)).getText()
						.toString();
				String content = ((EditText) findViewById(R.id.content))
						.getText().toString();
				try {
					insertData(db, title, content);
					Cursor cursor = db.rawQuery("select * from news_inf", null);
					inflateList(cursor);
				} catch (SQLiteException e) {
					// ִ��DDL�������ݱ�
					db.execSQL("create table news_inf(_id integer"
							+ " primary key autoincrement,"
							+ " news_title varchar(50),"
							+ " news_content varchar(255))");
					insertData(db, title, content);
					// ִ�в�ѯ
					Cursor cursor = db.rawQuery("select * from news_inf", null);
					inflateList(cursor);
				}
			}
		});
	}

	/**
	 * ��������
	 */
	private void insertData(SQLiteDatabase db, String title, String content) {
		// ִ�в������ݵĲ���
		db.execSQL("insert into news_inf values(null , ? , ?)", new String[] {
				title, content });
	}

	/**
	 * ���listView
	 * 
	 * @param cursor
	 */
	@SuppressLint("NewApi")
	private void inflateList(Cursor cursor) {
		// ���SimpleCursorAdapter
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(DBTest.this,
				R.layout.line, cursor, new String[] { "news_title",
						"news_content" }, new int[] { R.id.my_title,
						R.id.my_content },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
	}

	public void onDestroy() {
		super.onDestroy();
		// �˳�����ʱ�ر�SQliteDatabase
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbtest, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
