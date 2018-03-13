package com.example.rss_parser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;

public class Rss_Item
{
	 public String TITLE;
	 public String DESCR;
	 public String CONTENT;
	 public String DATE;
	 public String LINK;

	 public Rss_Item(String TITLE, String CONTENT, String DESCR, String DATE, String LINK) 
	 {
		 this.TITLE = TITLE;
		 this.CONTENT = CONTENT;
		 this.DESCR = DESCR;
		 this.DATE = DATE;
		 this.LINK = LINK;
	 }
	 
	 public String getTITLE()
	 {
		 return this.TITLE;
	 }
	 
	 public String getCONTENT()
	 {
		 return this.CONTENT;
	 }
	 
	 public String getDATE()
	 {
		 return this.DATE;
	 }
	 
	 public String getLINK()
	 {
		 return this.LINK;
	 }
	 
	 public String getDESCR()
	 {
		 return this.DESCR;
	 }
	 
	 public static ArrayList<Rss_Item> get_RSS_ITEMS(String URL)
	 {
		 ArrayList<Rss_Item> RSS_ITEMS = new ArrayList<Rss_Item>();
		 try
		 {
			 URL URL_URL=new URL(URL);
			 HttpURLConnection CONNECTION = (HttpURLConnection)URL_URL.openConnection();
			 if (CONNECTION.getResponseCode() == HttpURLConnection.HTTP_OK)
			 {
				 DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		         DocumentBuilder DB = DBF.newDocumentBuilder();
		         Document DOC = DB.parse(new InputSource(URL_URL.openStream()));
		         DOC.getDocumentElement().normalize();
		         NodeList NODE_LIST = DOC.getElementsByTagName("item");
				 if (NODE_LIST.getLength() > 0) 
				 {
					 for (int i = 0; i < NODE_LIST.getLength(); i++) 
					 {
						 Element ENTRY = (Element)NODE_LIST.item(i);
				         Element NEWS_TITLE = (Element)ENTRY.getElementsByTagName("title").item(0);
				         Element NEWS_DESCR = (Element)ENTRY.getElementsByTagName("description").item(0);
				         Element NEWS_CONTENT = (Element)ENTRY.getElementsByTagName("mailru:full-text").item(0);
				         Element NEWS_DATE = (Element)ENTRY.getElementsByTagName("pubDate").item(0);
				         Element NEWS_LINK = (Element)ENTRY.getElementsByTagName("link").item(0);
				         String STITLE = NEWS_TITLE.getFirstChild().getNodeValue();
				         String SDESCR = NEWS_DESCR.getFirstChild().getNodeValue();
				         String SCONTENT = NEWS_CONTENT.getFirstChild().getNodeValue();
				         String SDATE = NEWS_DATE.getFirstChild().getNodeValue();
				         String SLINK = NEWS_LINK.getFirstChild().getNodeValue();
				         Rss_Item RSS_ITEM = new Rss_Item(STITLE,SCONTENT,SDESCR,SDATE,SLINK);
				         RSS_ITEMS.add(RSS_ITEM);
					 }
				 }
			 }
		 }
		 catch (Exception e) 
		 {
		     e.printStackTrace();
		 }
		 return RSS_ITEMS;
	 }
	 
	 public static ArrayList<Rss_Item> get_RSS_ITEMS_FROM_DB(String TAG,SQLiteDatabase SQLITEDATABASE)
	 {
		 ArrayList<Rss_Item> RSS_ITEMS = new ArrayList<Rss_Item>();
		 String TITLE,DESCR,LINK,CONTENT,DATE;
		 Cursor CURSOR = SQLITEDATABASE.query(TAG,null,null,null,null,null,null);
		 if(CURSOR.moveToFirst())
		 {
			 do
			 {
				 TITLE=CURSOR.getString(CURSOR.getColumnIndex("TITLE"));
				 DESCR=CURSOR.getString(CURSOR.getColumnIndex("DESCR"));
				 DATE=CURSOR.getString(CURSOR.getColumnIndex("DATE"));
				 CONTENT=CURSOR.getString(CURSOR.getColumnIndex("CONTENT"));
				 LINK=CURSOR.getString(CURSOR.getColumnIndex("LINK"));
				 Rss_Item RSS_ITEM = new Rss_Item(TITLE,CONTENT,DESCR,DATE,LINK);
				 RSS_ITEMS.add(RSS_ITEM);
			 }
			 while (CURSOR.moveToNext());
		 }
		 CURSOR.close();
		 return RSS_ITEMS;
	 }
}
