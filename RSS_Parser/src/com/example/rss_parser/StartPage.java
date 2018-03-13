package com.example.rss_parser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StartPage extends Activity 
{

	public static String[] RSS={
    		"Аргументы и факты:Политика",
    		"Аргументы и факты:Культура",
    		"Аргументы и факты:Спорт",
    		"SАргументы и факты:Общество",
    		"Аргументы и факты:Здоровье"};
    public static String[] URLS={
    	"http://www.aif.ru/rss/politics.php",
    	"http://www.aif.ru/rss/culture.php",
    	"http://www.aif.ru/rss/sport.php",
    	"http://www.aif.ru/rss/society.php",
    	"http://www.aif.ru/rss/health.php"
    };
    public static String [] TAGS={
    		"POLITICS",
    		"CULTURE",
    		"SPORT",
    		"SOCIETY",
    		"HEALTH"
    };
    String RSS_ITEM,URL_ITEM,TAG_ITEM;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Spinner USER_RSS = (Spinner) findViewById(R.id.rss);
        ArrayAdapter<String> ADAPTER = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RSS);
        ADAPTER.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ADAPTER.notifyDataSetChanged();
        USER_RSS.setAdapter(ADAPTER);
        OnItemSelectedListener SELECTED_RSS = new OnItemSelectedListener()
        {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
             {
                 RSS_ITEM = (String)parent.getItemAtPosition(position);
                 URL_ITEM = URLS[position];
                 TAG_ITEM = TAGS[position];
                 Toast MESS_RSS=Toast.makeText(getApplicationContext(),"Вы выбрали RSS-ленту: "+RSS_ITEM,Toast.LENGTH_SHORT);
                 MESS_RSS.show();
             }
  
             @Override
             public void onNothingSelected(AdapterView<?> parent) 
             {
             }
        };
        USER_RSS.setOnItemSelectedListener(SELECTED_RSS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_page, menu);
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
    
    public void fetchRSS(View v)
    {
    	Intent intent = new Intent(StartPage.this, List_News.class);
    	intent.putExtra("RSS", RSS_ITEM);
    	intent.putExtra("URL", URL_ITEM);
    	intent.putExtra("TAG", TAG_ITEM);
        startActivity(intent);
    }
}
