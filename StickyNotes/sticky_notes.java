package com.example.app4;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class sticky_notes {
    String getnote(Context context){
        File f=new File(context.getFilesDir().getPath()+"/notes.txt");
        StringBuilder t=new StringBuilder();
        try{
            BufferedReader br =new BufferedReader(new FileReader(f));
            String l;
            while((l=br.readLine())!=null){
                t.append(l);
                t.append("\n");
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return t.toString();
    }
    void setnote(String text, Context context){
        String t=text;
        FileOutputStream fos=null;
        try{
            fos=context.getApplicationContext().openFileOutput("notes.txt",Context.MODE_PRIVATE);
            fos.write(t.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
