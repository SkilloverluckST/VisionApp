package com.example.stridley.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String image;
    RequestQueue queue;
    String URL = "";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        Button galleryButton = (Button) findViewById(R.id.galleryButton);

        imageView = (ImageView) findViewById(R.id.imageView);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        else if(requestCode == 0)
        {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            Toast.makeText(MainActivity.this, "Image captured!", Toast.LENGTH_SHORT).show();
            image = encodeBase(bitmap);
        }
        else if(requestCode == 1)
        {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(MainActivity.this, "Image retrieved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);
                    image = encodeBase(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//   private void getAnalysis(String image) {
//       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
//               new Response.Listener<JSONObject>() {
//                   @Override
//                   public void onResponse(JSONObject response) {

//                   }
//               },
//               new Response.ErrorListener() {
//                   @Override
//                   public void onErrorResponse(VolleyError error) {

//                   }
//               }){
//                   @Override
//                   protected Map<String, String> getParams()
//                   {
//                       return params;
//                   }
//               };
//   }

    private String encodeBase(Bitmap bm)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] b = stream.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
