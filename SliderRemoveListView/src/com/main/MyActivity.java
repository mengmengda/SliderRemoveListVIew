package com.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphabet.AlphabetAdapter;
import com.alphabet.AlphabetScrollBar;
import com.alphabet.AlphabetScrollBar.OnSelectAlphabetListener;
import com.custom.provider.Contact;
import com.slider.AsyncAnimation;
import com.slider.SliderListView.SildRemoveListener;
import com.xek.R;

public class MyActivity extends Activity{
	private com.slider.SliderListView listView;
	private AlphabetScrollBar scrollBar;
	private Adapter adapter;
	List<Contact> list = new ArrayList<Contact>();
	MyAsyncQueryHandler asyncHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initScrollBar();
		
		asyncHandler = new MyAsyncQueryHandler(getContentResolver());
		asyncHandler.startQuery(0, null, Uri.parse("content://com.custom.provider/contact"),null, null, null,null);		
		
		listView = (com.slider.SliderListView)findViewById(R.id.listView);
		listView.setViewId(R.id.item);
		listView.setAdapter(adapter = new Adapter());
		listView.setSildRemoveListener(new SildRemoveListener() {
			
			@Override
			public void remove(final int position, final View v) {				
				final LayoutParams lp = v.getLayoutParams();
				final  int oraHeight = lp.height;
				if(lp.height<0)lp.height = v.getHeight();
				final int height = lp.height;
				new AsyncAnimation() {
					@Override
					public void asyncRun(View view,float interpolatedTime) {
						 lp.height = height-(int)(height*interpolatedTime);
						 v.setVisibility(View.INVISIBLE);
						 if(lp.height==0){
							 list.remove(position);
							 alphabetScrollBarVisable();
							 adapter.setList(list);
							 adapter.notifyDataSetChanged();
							 lp.height = oraHeight;
							 v.setLayoutParams(lp);
							 stop();
							 v.setVisibility(View.VISIBLE);
						 }
						 v.setLayoutParams(lp);
					}
				}.setDuration(300).start(v);
			}
		});
	}
	
	public class Adapter extends AlphabetAdapter{
		class Holder{
			public TextView title;
			public ImageView photo;
			public TextView name;
			public TextView phone;
		}
		@Override
		public View getView(int position, View content, ViewGroup parent) {

			Holder holder;
			if(content==null){
				content = LayoutInflater.from(MyActivity.this).inflate(R.layout.itemactivity_main,null);
				holder = new Holder();
				holder.title = (TextView)content.findViewById(R.id.title);
				holder.photo = (ImageView)content.findViewById(R.id.photo);
				holder.name = (TextView)content.findViewById(R.id.name);
				holder.phone = (TextView)content.findViewById(R.id.phone);				
				content.setTag(holder);
			}
			
			
			
			holder = (Holder)content.getTag();
			
			//change the title visable/invisable  and show alphabet 
			titleChange(holder.title,position);
			
			holder.name.setText(list.get(position).name);
			if(list.get(position).photoId>0){
				try{
					long contactId = Long.parseLong(list.get(position).id);
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);
					Bitmap contactPhoto = BitmapFactory.decodeStream(input);
					holder.photo.setBackgroundDrawable(new BitmapDrawable(contactPhoto));
				}catch(Exception e){
					holder.photo.setBackgroundResource(R.drawable.photo);
				}
			}else{
					holder.photo.setBackgroundResource(R.drawable.photo);
			}
			holder.phone.setText(list.get(position).phoneNum);
			return content;
		
		}
		
	}
	
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if(cursor==null)return;
			while(cursor.moveToNext()){
				Contact contact = new Contact();
				contact.id = cursor.getString(0);
				contact.name = cursor.getString(1);
				contact.photoId = cursor.getInt(2);
				contact.phoneNum = cursor.getString(3);
				contact.sortkey = cursor.getString(4);
				list.add(contact);
			}
			Collections.sort(list);
			alphabetScrollBarVisable();
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			super.onQueryComplete(token, cookie, cursor);
		}

	}
	private TextView centerAlphabetText;
	private void initScrollBar(){
		scrollBar = (AlphabetScrollBar)findViewById(R.id.scrollBar);
		centerAlphabetText = (TextView)findViewById(R.id.centerAlphabetText);
		scrollBar.setOnScrollBarListener(new OnSelectAlphabetListener() {
			public void setOnSelectAlphabetListener(String alphabet, boolean show) {
				if (show) {
					centerAlphabetText.setVisibility(View.VISIBLE);
					centerAlphabetText.setText(alphabet);
					if (adapter.getAlphabetMap().get(alphabet) != null) {
						listView.setSelectionFromTop(adapter.getAlphabetMap().get(alphabet), 0);
					}
				} else {
					centerAlphabetText.setVisibility(View.GONE);
				}
			}

		});
	}
	private void alphabetScrollBarVisable(){
		if(list==null||list.size()==0){
			scrollBar.setVisibility(View.INVISIBLE);
		}else{
			scrollBar.setVisibility(View.VISIBLE);
		}
	}
}
