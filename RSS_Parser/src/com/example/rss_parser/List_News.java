package com.example.rss_parser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class List_News extends Activity
{
	String RSS,URL,TAG;
	ArrayList<Rss_Item> RSS_ITEMS=null;
	public static Rss_Item SELECT_RSS = null;
	SQLiteDatabase SQLITEDATABASE;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.rss_list_news);
		 ListView LIST_RSS=(ListView) findViewById(R.id.rss_list);
		 LIST_RSS.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		 {
		      public void onItemClick(AdapterView<?> av, View view, int index,long arg3) 
		      {
		    	  SELECT_RSS = RSS_ITEMS.get(index);
		    	  Intent intent = new Intent(List_News.this, News_Info.class);
		    	  intent.putExtra("RSS", RSS);
		      	  intent.putExtra("URL", URL);
		      	  intent.putExtra("TAG", TAG);
		    	  startActivity(intent);
		      }
		 });
		 RSS=getIntent().getExtras().getString("RSS");
		 URL=getIntent().getExtras().getString("URL");
		 TAG=getIntent().getExtras().getString("TAG");
		 if(checkInternetConnection(this))
		 {
			 Toast MESS_RSS=Toast.makeText(getApplicationContext(),"Соединение успешно установлено!",Toast.LENGTH_SHORT);
             MESS_RSS.show();
             HTTPConnection CONN=new HTTPConnection();
    		 CONN.execute(new String[]{URL});
    		 try 
    		 {
    			saveToDataBase();
    			RSS_ITEMS=(ArrayList<Rss_Item>)CONN.get();
    			RSS_Adapter ADAPTER=new RSS_Adapter(this,R.layout.news_item,RSS_ITEMS);
    			LIST_RSS.setAdapter(ADAPTER);
    			
    		 } 
    		 catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		 } 
    		 catch (ExecutionException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		 } 
		 }
		 else
		 {
			 Toast MESS_RSS=Toast.makeText(getApplicationContext(),"Ошибка подключения!",Toast.LENGTH_SHORT);
             MESS_RSS.show();
             SQLITEDATABASE = openOrCreateDatabase("RSS_DATABASE", Context.MODE_PRIVATE, null);
             RSS_ITEMS=Rss_Item.get_RSS_ITEMS_FROM_DB(TAG,SQLITEDATABASE);
             RSS_Adapter ADAPTER=new RSS_Adapter(this,R.layout.news_item,RSS_ITEMS);
 			 LIST_RSS.setAdapter(ADAPTER);
		 }
		 
	}
	
	private class RSS_Adapter extends ArrayAdapter<Rss_Item>
	{
		Context context;
	    public RSS_Adapter(Context context, int resourceId,ArrayList<Rss_Item> items) 
	    {
	        super(context, resourceId, items);
	        this.context = context;
	    }
	     
	    private class ViewHolder {
	        TextView TITLE;
	        TextView DESC;
	    }
	     
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        ViewHolder HOLDER = null;
	        Rss_Item ITEM = getItem(position);
	        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) 
	        {
	            convertView = mInflater.inflate(R.layout.news_item, null);
	            HOLDER = new ViewHolder();
	            HOLDER.TITLE = (TextView) convertView.findViewById(R.id.news_title);
	            HOLDER.DESC = (TextView) convertView.findViewById(R.id.content);
	            convertView.setTag(HOLDER);
	        } 
	        else
	        {
	            HOLDER = (ViewHolder) convertView.getTag();
	        }      
	        HOLDER.TITLE.setText(ITEM.getTITLE());
	        HOLDER.DESC.setText(ITEM.getDESCR());
	        return convertView;
	    }
	}
	public void goToStartPage(View v)
	{
		Intent intent = new Intent(List_News.this, StartPage.class);
		startActivity(intent);
	}
	private boolean checkInternetConnection(final Context context)
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	        return true;
	    }
	    wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	        return true;
	    }
	    wifiInfo = cm.getActiveNetworkInfo();
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	        return true;
	    }
	    return false;
	}
	private void saveToDataBase() throws InterruptedException, ExecutionException
	{
		String []TAGS=StartPage.TAGS;
		String []URLS=StartPage.URLS;
		SQLITEDATABASE = openOrCreateDatabase("RSS_DATABASE", Context.MODE_PRIVATE, null);
		for(int COUNTER=0;COUNTER<TAGS.length;COUNTER++)
		{
			HTTPConnection CONN=new HTTPConnection();
   		 	CONN.execute(new String[]{URLS[COUNTER]});
			ArrayList<Rss_Item> _ITEMS=(ArrayList<Rss_Item>)CONN.get();;
			SQLITEDATABASE.execSQL("DROP TABLE IF EXISTS "+TAGS[COUNTER]);
			SQLITEDATABASE.execSQL("CREATE TABLE "+TAGS[COUNTER]+" (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, TITLE VARCHAR, DESCR VARCHAR, DATE VARCHAR, LINK VARCHAR, CONTENT VARCHAR);");
			for(int COUNTER2=0;COUNTER2<_ITEMS.size();COUNTER2++)
			{
				Rss_Item ITEM=_ITEMS.get(COUNTER2);
				ContentValues newValues = new ContentValues();
				newValues.put("TITLE", ITEM.getTITLE());
				newValues.put("DESCR", ITEM.getDESCR());
				newValues.put("DATE", ITEM.getDATE());
				newValues.put("LINK", ITEM.getLINK());
				newValues.put("CONTENT", ITEM.getCONTENT());
				SQLITEDATABASE.insert(TAGS[COUNTER], null, newValues);
			}
		}
	}
}
