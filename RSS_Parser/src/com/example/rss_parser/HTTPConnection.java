package com.example.rss_parser;

import java.util.ArrayList;

import android.os.AsyncTask;

public class HTTPConnection extends AsyncTask<String, Void, ArrayList<Rss_Item>>
{
	public ArrayList<Rss_Item> doInBackground(String... urls) 
	{
		ArrayList<Rss_Item> RSS_ITEMS=Rss_Item.get_RSS_ITEMS(urls[0]);
		return RSS_ITEMS;
	}
}
