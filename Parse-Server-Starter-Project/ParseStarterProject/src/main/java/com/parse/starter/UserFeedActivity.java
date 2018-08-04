package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent intent = getIntent();

        String selectedUsername = intent.getStringExtra("username"); // USERNAME THAT WE SELECTED

        setTitle(selectedUsername + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username", selectedUsername);

        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null) {

                    if(objects.size() > 0) {

                        for(ParseObject object : objects) {

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e == null) {

                                        if(data!=null) {

                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                            ImageView imageView = new ImageView(getApplicationContext());

                                            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                            ));

                                            imageView.setImageBitmap(bitmap);

                                            linearLayout.addView(imageView);

                                        }

                                    } else {

                                        Toast.makeText(UserFeedActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        }

                    } else {

                        Toast.makeText(UserFeedActivity.this, "Sorry! No items to show", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(UserFeedActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
