package com.SteelAmbition.Wayfarer.Authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.Network.AlreadyExistsException;
import com.SteelAmbition.Wayfarer.Network.NetworkFailureException;
import com.SteelAmbition.Wayfarer.Network.ServerAccess;
import com.SteelAmbition.Wayfarer.Network.User;
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
                new CreateUser(username.getText().toString(), password.getText().toString(), firstname.getText().toString(), activity).execute();
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


    private class CreateUser extends AsyncTask<String, Void, User>{

        private final String email;
        private final String name;
		private final String password;
        private Activity activity;

        public CreateUser(String email, String password, String name, Activity activity){
        	this.activity = activity;
        	this.email = email;
        	this.password = password;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected User doInBackground(String... params) {

            // Create the user on the server.
            User user = null;
            try {
                user = ServerAccess.createUser(email, name, password);
            } catch (AlreadyExistsException e) {
                Crouton.showText(activity, "A user already exists with that email address.", Style.ALERT);
            } catch (NetworkFailureException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                Crouton.showText(activity, "Creation failed, try again", Style.ALERT);
            }
            // User will be null if anything failed.
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null){

                // set shared preferences (legacy code?)
                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", user.getId());
                editor.putString("email", user.getEmail());
                editor.putString("name", user.getName());
                editor.putBoolean("logged_in", true);
                editor.commit();

                System.out.println("userid="+user.getId());

                MainActivity.userID =  user.getId();

                // original code had these two lines only performing on success
                // and I don't want to presume to change that.
                activity.setProgressBarIndeterminateVisibility(false);
                activity.finish();
            }
        }
    }
}