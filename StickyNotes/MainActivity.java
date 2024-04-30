package com.example.app4;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText edt;
    private Button add,sub,bold,underline,italic;
    private TextView size;
    float cursize;
    sticky_notes note=new sticky_notes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edt=findViewById(R.id.edt);
        add=findViewById(R.id.add);
        sub=findViewById(R.id.sub);
        bold=findViewById(R.id.bold);
        underline=findViewById(R.id.underline);
        italic=findViewById(R.id.italic);
        size=findViewById(R.id.size);
        cursize=edt.getTextSize();
        size.setText(""+cursize);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size.setText(""+cursize);
                cursize++;
                edt.setTextSize(cursize);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size.setText(""+cursize);
                cursize--;
                edt.setTextSize(cursize);
            }
        });
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt.getTypeface().isBold()){
                    edt.setTypeface(Typeface.DEFAULT);
                }
                else{
                    edt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        });
        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt.getPaintFlags()==8){
                    edt.setPaintFlags(edt.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                }
                else{
                    edt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                }
            }
        });
        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt.getTypeface().isItalic()){
                    edt.setTypeface(Typeface.DEFAULT);
                }
                else{
                    edt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                }
            }
        });

    }
    public void savebtn(View view){
        note.setnote(edt.getText().toString(),this);
        updateWidget();
        Toast.makeText(this,"Your notes have been updated",Toast.LENGTH_SHORT).show();
    }
    private void updateWidget(){
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
        RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.widget_layout);
        ComponentName thiswidget=new ComponentName(this,AppWidget.class);
        remoteViews.setTextViewText(R.id.tw,edt.getText().toString());
        appWidgetManager.updateAppWidget(thiswidget,remoteViews);
    }
}