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
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

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
        final EditText name = (EditText) findViewById(R.id.register_name);

        final Activity activity = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new CreateUser(username.getText().toString(), password.getText().toString(), name.getText().toString(), activity).execute();
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

            // Create a new user on the server.
            // This is temporary and mainly to demonstrate how it's done.
            User user = null;
            try {
                user = new User(email, name, password);
            } catch (AlreadyExistsException e) {
                // a user already exists with this email address
                // handle it
            } catch (NetworkFailureException e) {
                // there was some sort of issue with the the connection or the server
                // handle it
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null){
                /*
                    At this point, user can be operated on and updated, deleted etc.
                    A reference to it should be stored somewhere central so that all activies
                    can access it.

                    The created user can also be retrieved later on using the User(email,pass)
                    constructor.
                 */

                // legacy code
                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", user.getId());
                editor.putString("password", user.getPassword());
                editor.putString("email", user.getEmail());
                editor.putString("name", user.getName());
                editor.putBoolean("logged_in", true);
                editor.commit();

                System.out.println("userid="+user.getId());

                MainActivity.userID =  user.getId();
                ServerAccess.setCurrentUser(user);
                activity.setProgressBarIndeterminateVisibility(false);
                activity.finish();
            }
        }
    }
}