package com.harrykid.groupmaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class SettingActivity extends AppCompatActivity {

    public void buatdatapeserta(){
        File datapesertadefault = new File(Environment.getExternalStorageDirectory()+"/Kelompok","Data Peserta.list");
        try{
            PrintWriter tulis = new PrintWriter(datapesertadefault);
            tulis.println("A1");
            tulis.println("A2");
            tulis.println("A3");
            tulis.println("A4");
            tulis.println("A5");
            tulis.println("B1");
            tulis.println("B2");
            tulis.println("B3");
            tulis.println("B4");
            tulis.println("B5");
            tulis.close();
        }catch(Exception e){}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = Environment.getExternalStorageDirectory().toString()+"/Kelompok/Data Peserta.list";
        File setting= new File(getFilesDir(),"setting.txt");
        if(setting.exists()){
            try{
                FileReader fileReader = new FileReader(setting);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    path=line;
                }
                fileReader.close();
            }catch(Exception e){}
        }
        setContentView(R.layout.activity_setting);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Bantuan");
        }catch(Exception e){
            Log.d("HARRY123",e.getMessage());
        }
        Button b1,b2;
        final TextView tv;
        b1= (Button) findViewById(R.id.button1);
        b2= (Button) findViewById(R.id.button2);
        tv= (TextView) findViewById(R.id.textView2);
        tv.setText(path);
        File file = new File(tv.getText().toString());
        if(!file.exists()){
            buatdatapeserta();
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                try{
                    startActivityForResult(Intent.createChooser(intent, "Pilih file"),1001);
                }catch(android.content.ActivityNotFoundException e){
                    Toast.makeText(SettingActivity.this, "Harap install File Explorer",Toast.LENGTH_SHORT).show();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                File file = new File(tv.getText().toString());
                Log.d("HARRY123","path2 = "+tv.getText().toString());
                Uri uri = FileProvider.getUriForFile(SettingActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(uri, "text/plain");
                try{
                    startActivity(intent);
                }catch(android.content.ActivityNotFoundException e){
                    Toast.makeText(SettingActivity.this, "Harap install MiXplorer",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        TextView tv;
        tv= (TextView) findViewById(R.id.textView2);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            Uri currFileURI = data.getData();
            String path=currFileURI.getPath();
            File file = new File(getFilesDir(),"setting.txt");
            File test = new File(path);
            if(test.exists()){
                if(path.matches("(.*).txt") || path.matches("(.*).list")){
                    try{
                        FileWriter pr = new FileWriter(file,false);
                        pr.write(path);
                        pr.close();
                        tv.setText(path);
                    }catch(Exception e){}
                }else{
                    Toast.makeText(SettingActivity.this, "File harus berformat .txt atau .list", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(SettingActivity.this, "Terjadi kesalahan, coba File Manager lain", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
