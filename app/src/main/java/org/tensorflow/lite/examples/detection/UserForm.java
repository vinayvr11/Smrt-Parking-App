package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserForm extends AppCompatActivity {

    public static String parkingName;
    public static String parkingRent;
    public static String parkingCapacity;
    Database data;
    public static HashMap<String, String> map = new HashMap<String, String>();
    public static String capacity;
    TextView databaseTextView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    public TextView textView;
    private Button sendButton;
    DetectorActivity detectorActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        data = new Database(this);
        databaseTextView = (TextView) findViewById(R.id.databaseData);
        textView = (TextView) findViewById(R.id.parkingInformationView);
        submitForm();
        viewInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
        detectorActivity = new DetectorActivity();



    }




    public void formText(){
        EditText editText = (EditText) findViewById(R.id.parkinName);
        parkingName = editText.getText().toString();
        EditText editText1 = (EditText) findViewById(R.id.parkingRent);
        parkingRent = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.parkingCapacity);
        parkingCapacity = editText2.getText().toString();
    }

    public void submitForm(){
        Button formButton = (Button) findViewById(R.id.submitForm);
        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formText();
                if(data.insertData(parkingName,parkingRent,parkingCapacity)){
                    Log.d("Data Insertion","Data insertion successfull");
                    Toast.makeText(UserForm.this,"Data Stored",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Data Insertion Fail","Data insertion Fail");
                }
            }
        });
    }


    public void viewInfo(){
        Button infoButton = (Button) findViewById(R.id.information);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = data.getData();


                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()) {
                    map.put("parkingName",res.getString(1));
                    map.put("parkingRent",res.getString(2));
                    map.put("parkingCapacity",res.getString(3));
                    //Log.d("parking","parking"+res.getString(3));

                }
                textView.setText(buffer);

                Log.d("Data Worked", String.valueOf(buffer));

            }
        });

    }





}
