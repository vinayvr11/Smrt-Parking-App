package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class googlemap extends AppCompatActivity {

    DetectorActivity detectorActivity;
    public static boolean exitCamera=false;
    public static boolean entryCamera = false;
    UserForm userForm;
    Database data;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
        EditText editText1 = (EditText) findViewById(R.id.parkingSpaceLeft);
        editText1.setText(Integer.toString(detectorActivity.num));
        GoogleMaps();
        entryCamera();
        exitCamera();
        form();
        seeInfo();
        //Database installation
        data = new Database(this);
        userForm = new UserForm();
        detectorActivity = new DetectorActivity();
        listView  = (ListView) findViewById(R.id.listView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
    }



    private void exitCamera(){
        Button exitButton = (Button) findViewById(R.id.ExitCamera);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitCamera=true;
                entryCamera = false;
                Intent entryIntent = new Intent(googlemap.this,DetectorActivity.class);

                startActivity(entryIntent);
            }
        });
    }

    private void entryCamera(){
        Button entryButton = (Button) findViewById(R.id.EntryCamera);
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryCamera = true;
                exitCamera = false;
                Log.d("Camera Entry","Camera entry state"+entryCamera);
                Intent entryIntent = new Intent(googlemap.this,DetectorActivity.class);

                startActivity(entryIntent);
            }
        });
    }

    private void GoogleMaps(){
        Button mapButton = (Button) findViewById(R.id.SearchParking);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmIntentUri = Uri.parse("geo:0,0?q=parking area");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

    }

    private void form(){
        Button formButton = (Button) findViewById(R.id.form);
        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(googlemap.this,UserForm.class);
                startActivity(intent);
            }
        });
    }


    //Button to see the information of parking place available
    //and to check the total capacity of the parking area
    public ArrayList<String> list = new ArrayList<String>();
    public static HashMap<String,String> map = new HashMap<String,String>();
    public String message;



    private void seeInfo(){


        Button infoButton = (Button) findViewById(R.id.seeInfo);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.parkingSpaceLeft);
                Cursor res = data.getData();

                 StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()) {
                    map.put("parkingName",res.getString(0));
                    map.put("parkingRent",res.getString(1));
                    map.put("parkingCapacity",res.getString(2));
                    //Log.d("parking","parking"+res.getString(3));

                    list.add("ParkingName:"+" "+res.getString(0)+"\n"+
                            "ParkingRent:"+" "+res.getString(1)+"\n"+
                            "ParkingRent:"+" "+res.getString(2));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(googlemap.this,android.R.layout.simple_list_item_1,list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("AdapterItem","adapterItem"+adapterView.getItemAtPosition(i));

                        String[] info = adapterView.getItemAtPosition(i).toString().split("\n");
                        String[] splitCapacity = info[2].split(" ");
                        databaseReference.setValue(detectorActivity.num);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 message = dataSnapshot.getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.d("Message Success","Message"+message);
                        textView.setText(" "+message+"/"+splitCapacity[1]);
                    }
                });
            }
        });
    }
}
