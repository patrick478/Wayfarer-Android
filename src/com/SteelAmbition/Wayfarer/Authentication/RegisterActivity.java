package com.SteelAmbition.Wayfarer.Authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.crouton.Crouton;
import com.SteelAmbition.Wayfarer.crouton.Style;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 13/07/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends SherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        Button loginButton = (Button) findViewById(R.id.btnRegister);
        final EditText username = (EditText) findViewById(R.id.register_email);
        final EditText password = (EditText) findViewById(R.id.register_password);
        final EditText firstname = (EditText) findViewById(R.id.register_firstName);
        final EditText surname = (EditText) findViewById(R.id.register_surname);

        final Activity activity = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new CreateUser(username.getText().toString(), password.getText().toString(), firstname.getText().toString(), surname.getText().toString(), activity).execute();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
        }
        return true;
    }


    private class CreateUser extends AsyncTask<String, Void, HttpResponse>{

        private final String firstname;
        private final String lastname;
        HttpResponse response;
		private Activity activity;
		private final String username;
		private final String password;

        public CreateUser(String username, String password, String firstname, String lastname, Activity activity){
        	this.activity = activity;
        	this.username = username;
        	this.password = password;
            this.firstname = firstname;
            this.lastname = lastname;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(String... params) {
            // Create a newtopic HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();


            HttpPut httppost = new HttpPut("http://wayfarer-server.herokuapp.com/users");



            try {
                // Add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("email", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("name[first]", firstname));
                nameValuePairs.add(new BasicNameValuePair("name[last]", lastname));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return response;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
             if(result.getStatusLine().getStatusCode()!= HttpStatus.SC_CREATED){
                   Crouton.showText(activity, "Creation failed, try again", Style.ALERT);
             }

             else{
                 HttpEntity entity = response.getEntity();

                 String response = null;
                 if(entity!=null){
                     try {
                         InputStream instream = entity.getContent();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                         StringBuilder sb = new StringBuilder();
                         String line = null;
                         try {
                             while ((line = reader.readLine()) != null) {
                                 sb.append(line + "\n");
                             }
                         }
                         catch (IOException e) {
                             e.printStackTrace();
                         }
                         finally {
                             try {
                                 response = sb.toString();
                                 instream.close();
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }
                     }
                     catch (IOException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     }

                 }

                 try {
                     JSONObject json = new JSONObject(response);

                     JSONObject name= json.getJSONObject("name");

                     SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                     SharedPreferences.Editor editor = sharedPreferences.edit();
                     editor.putString("email", json.getString("email"));

                     editor.putString("id", json.getString("id"));
                     MainActivity.userID =  json.getString("id");

                     editor.putString("firstname", name.getString("first"));
                     editor.putString("lastname", name.getString("last"));

                     editor.putBoolean("logged_in", true);
                     editor.commit();
                 } catch (JSONException e) {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                 }

                 activity.finish();

             }



        }
    }
}