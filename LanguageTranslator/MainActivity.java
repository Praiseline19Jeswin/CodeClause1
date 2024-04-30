package com.example.app7;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private Spinner fromspinner,tospinner;
    private TextInputEditText edt;
    private ImageView mic;
    private MaterialButton translate;
    private TextView ttext;
    String[] fromlanguages={"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Tamil","Urdu"};
    String[] tolanguages={"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Tamil","Urdu"};
    private static final int REQUEST_PERMISSION_CODE=1;
    int lcode,fromlcode,tolcode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        fromspinner=findViewById(R.id.fromspinner);
        tospinner=findViewById(R.id.tospinner);
        edt=findViewById(R.id.edt);
        mic=findViewById(R.id.mic);
        translate=findViewById(R.id.translate);
        ttext=findViewById(R.id.ttext);
        fromspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromlcode=getlcode(fromlanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromadapter=new ArrayAdapter(this,R.layout.spinner_item,fromlanguages);
        fromadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromspinner.setAdapter(fromadapter);
        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tolcode=getlcode(tolanguages[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toadapter=new ArrayAdapter(this,R.layout.spinner_item,tolanguages);
        toadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tospinner.setAdapter(toadapter);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttext.setText("");
                if(edt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your text to translate",Toast.LENGTH_LONG).show();
                }else if(fromlcode==0){
                    Toast.makeText(MainActivity.this,"Please select source language",Toast.LENGTH_LONG).show();
                }else if(tolcode==0){
                    Toast.makeText(MainActivity.this,"Please select target language",Toast.LENGTH_LONG).show();
                }else{
                    translateText(fromlcode,tolcode,edt.getText().toString());
                }
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to Convert into Text");
                try{
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void translateText(int fromlcode,int tolcode,String source){
        ttext.setText("Downloading Model...");
        FirebaseTranslatorOptions options=new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlcode)
                .setTargetLanguage(tolcode)
                .build();
        FirebaseTranslator translator= FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ttext.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        ttext.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed to translate "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed to download language model "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PERMISSION_CODE){
            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String>result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edt.setText(result.get(0));
            }
        }
    }

    public int getlcode(String language){
        int lcode=0;
        switch (language){
            case "English":
                lcode= FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                lcode= FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                lcode= FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                lcode= FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                lcode= FirebaseTranslateLanguage.BG;
                break;
            case "Bengali":
                lcode= FirebaseTranslateLanguage.BE;
                break;
            case "Catalan":
                lcode= FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                lcode= FirebaseTranslateLanguage.CS;
                break;
            case "Welsh":
                lcode= FirebaseTranslateLanguage.CY;
                break;
            case "Hindi":
                lcode= FirebaseTranslateLanguage.HI;
                break;
            case "Urdu":
                lcode= FirebaseTranslateLanguage.UR;
                break;
            case "Tamil":
                lcode = FirebaseTranslateLanguage.TA;
                break;
            default:
                lcode=0;
        }
        return lcode;
    }
}