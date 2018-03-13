package com.example.rss_parser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class News_Info extends Activity 
{
	String RSS,URL,TAG;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
        RSS=getIntent().getExtras().getString("RSS");
		URL=getIntent().getExtras().getString("URL");
		TAG=getIntent().getExtras().getString("TAG");
		Rss_Item SELECT_RSS = List_News.SELECT_RSS;
	    TextView TITLE= (TextView)findViewById(R.id.news_title);
	    TextView DATE = (TextView)findViewById(R.id.news_data);
	    TextView CONTENT = (TextView)findViewById(R.id.news_content);
	    TextView LINK = (TextView)findViewById(R.id.news_link);
	    TITLE.setText(SELECT_RSS.getTITLE());
	    DATE.setText(SELECT_RSS.getDATE());
	    CONTENT.setText(SELECT_RSS.getCONTENT());
	    LINK.setText(SELECT_RSS.getLINK());
	    CONTENT.setMovementMethod(new ScrollingMovementMethod());

	}
	
	public void goToRSSList(View v)
	{
		Intent intent = new Intent(News_Info.this, List_News.class);
    	intent.putExtra("RSS", RSS);
    	intent.putExtra("URL", URL);
    	intent.putExtra("TAG", TAG);
        startActivity(intent);
	}
}
