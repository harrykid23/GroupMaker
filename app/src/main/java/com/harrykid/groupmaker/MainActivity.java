package com.harrykid.groupmaker;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {
    Button b1,b2,b3;
    RadioGroup rg;
    RadioButton rb1,rb2;
    EditText ed1,ed2,ed3;
    TextView tv;
    int u=0;
    int n=0;
    int k=0;
    int[] urutan= new int[20000];
    boolean[] a= new boolean[20000];
    boolean[][] terpakai= new boolean[10000][10000];
    String splitter;
    String nama;
    int mode;
    Random angka= new Random();
    String gaguna="Kelompok";
    public int getNumber(int p,int q){
        boolean baleni = true;
        int coba = 0;
        Random angka = new Random();
        while(baleni){
            coba= angka.nextInt(q+1);
            if(coba>=p && coba<=q){
                baleni=false;
            }
        }
        return coba;
    }

    public void acak(int p, int q, int l, int o){
        int i,store;
        boolean cekfalse = true;
        int o2 = k-o;
        int diisi = 0;
        int diisi2 = 0;
        store=0;
        boolean ulang;
        int[] tukar = new int[20000];
        boolean[] cek = new boolean[20000];
        for(i=0;i<=k-1;i++){
            if(terpakai[l-1][i]){
                cekfalse=false;
            }
        }
        for(i=p;i<=q;i++){
            tukar[i]=urutan[i];
            cek[i]=false;
        }
        for(i=p;i<=q;i++){
            ulang=true;
            while(ulang){
                store= getNumber(p,q);
                if(cekfalse && !cek[store]){
                    ulang=false;
                }else if(!cekfalse && terpakai[l-1][i%k] && !cek[store] && ((store%k<=o && store%k!=0) || diisi>=o)){
                    ulang=false;
                    diisi++;
                }else if(!cekfalse && !terpakai[l-1][i%k] && !cek[store] && ((store%k>o || store%k==0) || diisi2>=o2)){
                    ulang=false;
                    diisi2++;
                }
            }
            cek[store]=true;
            urutan[i]= tukar[store];
            if(store%k<=o && store%k!=0){
                terpakai[l][i%k]=true;
            }
        }
    }

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

    public  void checkStoragePermission() {
        String TAG = "HARRY123";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        String TAG = "HARRY123";
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Izin ditolak",Toast.LENGTH_SHORT).show();
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            finish();
            //close the app
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStoragePermission();
        setContentView(R.layout.activity_main);

        //Locate the button in activity_main.xml
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText3);
        rb1 = (RadioButton) findViewById(R.id.radio0);
        rb2 = (RadioButton) findViewById(R.id.radio1);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        tv = (TextView) findViewById(R.id.textView1);
        b3.setEnabled(false);
        //Capture button clicks
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                File wadah = new File(Environment.getExternalStorageDirectory()+"/Kelompok");
                if(!wadah.exists()) wadah.mkdir();
                String path = Environment.getExternalStorageDirectory()+"/Kelompok/Data Peserta.list";
                File datapeserta = new File(path);
                if(!datapeserta.exists()){
                    buatdatapeserta();
                }
                //klik button OK
                new testAsync().execute("");
            }});
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                b1.setEnabled(true);
                b3.setEnabled(false);
                ed1.setEnabled(true);
                ed2.setEnabled(true);
                ed3.setEnabled(true);
                rb1.setEnabled(true);
                rb2.setEnabled(true);
                tv.setEnabled(true);
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");
            }
        });
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                File file;
                if(u!=0){
                    file = new File(Environment.getExternalStorageDirectory()+"/Kelompok",gaguna+"("+u+").txt");
                }else{
                    file = new File(Environment.getExternalStorageDirectory()+"/Kelompok",gaguna+".txt");
                }
                Uri uri = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "text/plain");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try{
                    startActivity(intent);
                }catch(android.content.ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this, "Harap install MiXplorer",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public class testAsync extends AsyncTask<String, Void, Integer>{
        private ProgressDialog pd;

        @Override
        protected void onPreExecute(){
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            int[] nan = new int[10001];
            String[] na = new String[10001];
            boolean errorkah = false;
            boolean errorini = false;
            boolean baleni;
            int i,j,l;
            u=0;
            n=0;
            k=0;
            int RESULTAN = 1;
            int tombolcek = rg.getCheckedRadioButtonId();
            if(tombolcek==rb1.getId()){
                mode=1;
            }else{
                mode=2;
            }
            if(ed1.getText().toString().length() !=0 && ed2.getText().toString().length()!=0 && ed3.getText().toString().length()!=0){
                for(i=0;i<=10000;i++){
                    nan[i]=0;
                    na[i]="0";
                }
                gaguna=ed1.getText().toString();
                splitter= ed2.getText().toString();
                k= Integer.parseInt(ed3.getText().toString());
                if(splitter.matches(",(.*)") || splitter.matches("(.*),,(.*)") || splitter.matches("(.*),")){
                    errorkah=true;
                }
                na = splitter.split(",");
                for(i=1;i<=10000;i++){
                    if(i==10000){
                        errorkah = true;
                        break;
                    }
                    try{
                        nan[i]= Integer.parseInt(na[i-1]);
                        if(Integer.parseInt(na[i-1])==0){
                            errorkah = true;
                        }
                    }catch(Exception e){}
                    if(nan[i]==0){break;}
                }
                for(i=1;i<=9999;i++){
                    n=n+nan[i];
                    if(nan[i]==0){
                        break;
                    }
                }
            }

            String[] peserta= new String[10000];
            //Otak-atik data peserta
            if(mode==2 && !errorkah && !errorini){
                for(i=1;i<=9999;i++){
                    peserta[i]= "P.no"+i;
                }
                String path = Environment.getExternalStorageDirectory()+"/Kelompok/Data Peserta.list";
                Log.d("HARRY123","path1 = "+path);
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
                File datapeserta = new File(path);
                if(!datapeserta.exists() && (path.matches(Environment.getExternalStorageDirectory()+"/Kelompok/Data Peserta.list"))){
                    buatdatapeserta();
                }else if(!datapeserta.exists()){
                    errorini=true;
                }
                if(!errorini && !errorkah){
                    try{
                        FileReader fileReader = new FileReader(datapeserta);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        i = 0;
                        while ((line = bufferedReader.readLine()) != null) {
                            i = i+1;
                            if(line.length()>10){
                                line = line.substring(0,10);
                            }
                            peserta[i]= line;
                            if(i==n) break;
                        }
                        fileReader.close();
                    }catch(Exception e){}
                }
            }

            if(k>0 && n>0 && n<10000 && !errorkah && !errorini){
                try{
                    for(i=0;i<=19999;i++){
                        a[i]=false;
                        urutan[i]=0;
                        if(i<=9999){
                            for(j=0;j<=9999;j++){
                                terpakai[i][j]=false;
                            }
                        }
                    }
                    //Lanjut
                    File file = new File(Environment.getExternalStorageDirectory()+"/Kelompok",gaguna+".txt");
                    while(file.exists()){
                        u=u+1;
                        file = new File(Environment.getExternalStorageDirectory()+"/Kelompok",gaguna+"("+u+").txt");
                    }
                    if(u==0){
                        nama=gaguna;
                    }else{
                        nama=gaguna+"("+u+")";
                    }
                    PrintWriter pr = new PrintWriter(file);

                    //CODING
                    int total = 0;
                    int jmlsplit = 0;
                    for(i=1;i<=9999;i++){
                        if(nan[i]==0){
                            jmlsplit=i-1;
                            break;
                        }
                    }
                    for(i=1;i<=jmlsplit;i++){
                        total=total+nan[i];
                        for(j=total-nan[i]+1;j<=total;j++){
                            baleni=true;
                            while(baleni){
                                urutan[j]=getNumber(total-nan[i]+1,total);
                                if(!a[urutan[j]]){
                                    baleni=false;
                                }
                            }
                            a[urutan[j]]=true;
                        }
                    }
                    total=0;
                    for(i=1;i<=jmlsplit;i++){
                        total=total+nan[i];
                        if(total%k!=0){
                            acak(k*(total/k)+1, k*((total/k)+1),i,total%k);
                        }
                    }
                    pr.print("Subjek          : "); pr.println(gaguna);
                    pr.print("Jumlah Peserta  : "); pr.println(total);
                    pr.print("Jumlah Kelompok : "); pr.println(k);
                    pr.println();
                    if(mode==1){
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("======");
                            }else{
                                pr.print("=======");
                            }
                        }
                        pr.println();
                        pr.print("|");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2==0){
                                for(j=1;j<=3-(Integer.toString(i).length()/2);j++){
                                    pr.print(" ");
                                }
                                pr.print(i);
                                for(j=1;j<=3-(Integer.toString(i).length()/2);j++){
                                    pr.print(" ");
                                }
                            }else{
                                for(j=1;j<=3-((Integer.toString(i).length()+1)/2);j++){
                                    pr.print(" ");
                                }
                                pr.print(i);
                                for(j=1;j<=3-((Integer.toString(i).length()+1)/2);j++){
                                    pr.print(" ");
                                }
                            }
                            pr.print("|");
                        }
                        pr.println();
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("======");
                            }else{
                                pr.print("=======");
                            }
                        }
                    }else{
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("============");
                            }else{
                                pr.print("=============");
                            }
                        }
                        pr.println();
                        pr.print("|");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2==0){
                                for(j=1;j<=6-(Integer.toString(i).length()/2);j++){
                                    pr.print(" ");
                                }
                                pr.print(i);
                                for(j=1;j<=6-(Integer.toString(i).length()/2);j++){
                                    pr.print(" ");
                                }
                            }else{
                                for(j=1;j<=6-((Integer.toString(i).length()+1)/2);j++){
                                    pr.print(" ");
                                }
                                pr.print(i);
                                for(j=1;j<=6-((Integer.toString(i).length()+1)/2);j++){
                                    pr.print(" ");
                                }
                            }
                            pr.print("|");
                        }
                        pr.println();
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("============");
                            }else{
                                pr.print("=============");
                            }
                        }
                    }
                    pr.println();
                    boolean habis = total%k==0;
                    if(mode==1){
                        for(i=1;i<=(total/k)+1;i++){
                            if(i==(total/k)+1 && habis){
                                break;
                            }
                            pr.print("|");
                            for(j=1;j<=k;j++){
                                if(Integer.toString(j).length()%2==0){
                                    pr.print(" ");
                                }
                                if(urutan[((i-1)*k)+j]!=0){
                                    for(l=1;l<=5-Integer.toString(urutan[((i-1)*k)+j]).length();l++){
                                        pr.print(" ");
                                    }
                                    pr.print(urutan[((i-1)*k)+j]);
                                }else{
                                    pr.print("     ");
                                }
                                pr.print("|");
                            }
                            pr.println();
                        }
                    }else{
                        for(i=1;i<=(total/k)+1;i++){
                            if(i==(total/k)+1 && habis){
                                break;
                            }
                            pr.print("|");
                            for(j=1;j<=k;j++){
                                if(urutan[((i-1)*k)+j]!=0){
                                    pr.print(peserta[urutan[((i-1)*k)+j]]);
                                    for(l=1;l<=11-peserta[urutan[((i-1)*k)+j]].length();l++){
                                        pr.print(" ");
                                    }
                                }else{
                                    pr.print("           ");
                                }
                                if(Integer.toString(j).length()%2==0){
                                    pr.print(" |");
                                }else{
                                    pr.print("|");
                                }
                            }
                            pr.println();
                        }
                    }
                    if(mode==1){
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("======");
                            }else{
                                pr.print("=======");
                            }
                        }
                    }else{
                        pr.print("=");
                        for(i=1;i<=k;i++){
                            if(Integer.toString(i).length()%2!=0){
                                pr.print("============");
                            }else{
                                pr.print("=============");
                            }
                        }
                    }
                    pr.println();
                    pr.close();
                    //Toast.makeText(MainActivity.this,"Kelompok berhasil dibuat! Tersimpan di Penyimpanan Internal/Kelompok/"+nama+".txt",Toast.LENGTH_LONG).show();
                    RESULTAN = 1;
                }catch(Exception e){
                    if (e instanceof NumberFormatException){
                        e.printStackTrace();
                    }else{
                        throw new RuntimeException(e);
                    }
                }
            }else if(errorini){
                //Toast.makeText(MainActivity.this,"Data Peserta error!",Toast.LENGTH_SHORT).show();
                RESULTAN= 3;
            }else{
                //Toast.makeText(MainActivity.this,"Data tidak valid!",Toast.LENGTH_SHORT).show();
                RESULTAN= 2;
            }
            return RESULTAN;
        }
        @Override
        protected void onPostExecute(Integer i){
            super.onPostExecute(i);
            if(i==1){
                ed1.setEnabled(false);
                ed2.setEnabled(false);
                ed3.setEnabled(false);
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                tv.setEnabled(false);
                b1.setEnabled(false);
                b3.setEnabled(true);
                Toast.makeText(MainActivity.this,"Kelompok berhasil dibuat! Tersimpan di Penyimpanan Internal/Kelompok/"+nama+".txt",Toast.LENGTH_LONG).show();
            }else if(i==2){
                Toast.makeText(MainActivity.this,"Data tidak valid!",Toast.LENGTH_SHORT).show();
            }else if(i==3){
                Toast.makeText(MainActivity.this,"Data Peserta error!",Toast.LENGTH_SHORT).show();
            }
            if(pd.isShowing()){
                pd.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("DEBUG","ITEM CLICKED");
        int id = item.getItemId();
        if (id == R.id.About) {
            Intent i = new Intent(this,AboutActivity.class);
            this.startActivity(i);
            return true;
        }else{
            if (id == R.id.Help) {
                Intent i = new Intent(this,HelpActivity.class);
                this.startActivity(i);
                return true;
            }
            /* Experimental
            else{
                if (id == R.id.Setting) {
                    Intent i = new Intent(this,SettingActivity.class);
                    this.startActivity(i);
                    return true;
                }
            }
            */
        }
        return super.onOptionsItemSelected(item);
    }
}
