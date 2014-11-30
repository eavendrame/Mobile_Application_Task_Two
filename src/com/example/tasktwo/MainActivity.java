package com.example.tasktwo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	EditText inputString;
	Button inputButton;
	TextView outputData;
	String responseString = null;
	Product[] products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        inputString = (EditText) findViewById(R.id.inputString);
        outputData = (TextView) findViewById(R.id.outputData);
        
        inputButton = (Button) findViewById(R.id.inputButton);
        inputButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				products = null;
				toRequest();
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    private void toRequest() {
    	new RequestResults().execute("https://services.nexusinds.com/DummySvc/DummySvc.svc/GetData/" + inputString.getText().toString());
	}
    
    private void toMap(String string) {
    	string = string.substring(2, string.length()-2);
    	String[] result = string.split("\\},\\{");
    	if (result.length == 0) {
    		return;
    	}
    	products = new Product[result.length];
    	
    	for (int count = 0; count < result.length; count++) {
    		if (count == 60) {
    			count = 60;
    		}
    		String[] keyValue = result[count].split("\\\",\\\"");
    		if (keyValue.length < 2) {
    			continue;
    		}
    		
    		String[] temp = keyValue[0].split("\\\":\\\"");
    		String desc = (temp.length < 2) ? "" : temp[1].replace("\"", "");
    		temp = keyValue[1].split("\\\":\\\"");
    		String num = (temp.length < 2) ? "" : temp[1].replace("\"", "");
    		products[count]= new Product(desc, num);
    	}
    }
    
    public Product[] getProducts() {
    	return products;
    }
    
    private class RequestResults extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    if (responseString.length() > 2) {
                    	toMap(responseString);
                    }
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        	outputData.setText("Loading...");
        }
        
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        
        @Override
        protected void onPostExecute(Void result) {
        	outputData.setText("");
        	Intent nextActivity = new Intent(getApplicationContext(), ResultsActivity.class);
        	nextActivity.putExtra("products", getProducts());
        	startActivity(nextActivity);
        }
    }
}
