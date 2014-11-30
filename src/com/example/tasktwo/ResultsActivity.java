package com.example.tasktwo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ResultsActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_results);
		
		ArrayList<String> results = new ArrayList<String>();
		Parcelable[] products = getIntent().getParcelableArrayExtra("products");
		
		if (products != null) {
			for (Parcelable parcel : products) {
				Product p = (Product)parcel;
				results.add("Product number: " + p.getPartNum() + "  ,  Product description: " + p.getPartDescription());
			}
		}
		else {
			results.add("No results found for your request.");
		}
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
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
