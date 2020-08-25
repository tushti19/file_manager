package com.anddev.myfilemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {


    private String path;
    ListView fileLV,WFilesLV;
    TextView whatsappDoctv,Downloadstv;

    RelativeLayout noFileRl;
    Button others,show;

    ArrayList<Integer> selectedPosDownloads ;
    ArrayList<Integer> selectedPosW ;

    ImageView fileIv ;

    ArrayList<Uri> selectedFilesUri;


    Intent intent;

    ImageButton back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /////////////////////////////link views/////////////////////////////////

        whatsappDoctv = findViewById(R.id.WhatsappDownloads);
        Downloadstv = findViewById(R.id.Downloads);
        noFileRl = findViewById(R.id.no_file_RL);
        WFilesLV = findViewById(R.id.WFiles);
        fileLV = findViewById(R.id.FilesLV);

        fileIv = findViewById(R.id.fileIV);
        show = findViewById(R.id.showSelected);

        WFilesLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        fileLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        others = findViewById(R.id.others);

        back = findViewById(R.id.back);

        selectedFilesUri = new ArrayList<>();



        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(i, 10);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GotData.class));
            }
        });



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(checkPermission())
            StartTasks();
        else
            isStoragePermissionGranted();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 10:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String returnUri = uri.toString();
                    Log.d("Uri", ""+uri);
                    String mimeType = getContentResolver().getType(uri);
                    Log.d("Mime Type", mimeType);



                    Log.d("path", "" + uri.getPath());

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri,mimeType);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if(intent.resolveActivity(getPackageManager()) != null)
                        startActivity(Intent.createChooser(intent,"Open File with"));
                    else
                        Toast.makeText(this, "No app found for opening this document", Toast.LENGTH_SHORT).show();


                    break;
                }

        }

    }

    private void StartTasks()
    {



        final ArrayList<File> WhatsAppArray = new ArrayList<>();
        File whatsappMediaDirectoryName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents");
        Log.d("Whatsapp Path", ""+whatsappMediaDirectoryName.getName());
        final File[] whatsappFiles = whatsappMediaDirectoryName.listFiles();

        if(whatsappFiles != null){




        for (File wFile : whatsappFiles) {
            if (!wFile.getName().startsWith(".")) {
                if (wFile.getName().contains("pdf") || (wFile.getName().contains("ppt")) || (wFile.getName().contains("pptx")) ||
                        (wFile.getName().contains("png")) || (wFile.getName().contains("doc")) || (wFile.getName().contains("xlsx")) ||
                        (wFile.getName().contains("PNG")) || (wFile.getName().contains("SVG")) || (wFile.getName().contains("JPG")) || (wFile.getName().contains("JPEG"))
                || (wFile.getName().contains("jpg")) || (wFile.getName().contains("jpeg")) || (wFile.getName().contains("svg"))) {
                    WhatsAppArray.add(wFile);
                }
            }
        }



        final CustomAdapter WhatsAppAdapter = new CustomAdapter(WhatsAppArray);
        WFilesLV.setAdapter(WhatsAppAdapter);
        setDynamicHeight(WFilesLV);


            WFilesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    Log.d("in","itemclick : "+i);


                    Drawable drawable = getDrawable(R.drawable.selected_item);
                    view.setBackground(drawable);

                    fileIv = view.findViewById(R.id.fileIV);
                    fileIv.setImageResource(R.drawable.tick);



                }
            });

        }

        else
        {
            whatsappDoctv.setVisibility(View.INVISIBLE);
            WFilesLV.setVisibility(View.INVISIBLE);

        }










        path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        Log.d("path of downloads",path);


        final File allFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        final ArrayList<File> arrayList = new ArrayList<>();



        final File[] files = allFiles.listFiles();

        if (files != null){


        for (File file1 : files) {
            if (!file1.getName().startsWith(".")) {
                if (file1.getName().contains("pdf") || (file1.getName().contains("ppt")) || (file1.getName().contains("pptx")) ||
                        (file1.getName().contains("png")) || (file1.getName().contains("doc")) || (file1.getName().contains("xlsx")) ||
                        (file1.getName().contains("PNG")) || (file1.getName().contains("SVG")) || (file1.getName().contains("JPG")) || (file1.getName().contains("JPEG"))
                        || (file1.getName().contains("jpg")) || (file1.getName().contains("jpeg")) || (file1.getName().contains("svg")))
                    arrayList.add(file1);
                Log.d("Name : ", "" + file1.getName());
            }
        }


        Collections.sort(arrayList);

        CustomAdapter adapter = new CustomAdapter(arrayList);
        fileLV.setAdapter(adapter);

        fileLV.setSelector(R.drawable.selected_item);
        setDynamicHeight(fileLV);

        fileLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        fileLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                Log.d("in","itemclick : "+i);
                Drawable drawable = getDrawable(R.drawable.selected_item);
                view.setBackground(drawable);

                fileIv = view.findViewById(R.id.fileIV);
                fileIv.setImageResource(R.drawable.tick);





            }
        });


       }

        else {
            Downloadstv.setVisibility(View.INVISIBLE);
            fileLV.setVisibility(View.INVISIBLE);

        }

        if(files != null && whatsappFiles != null)
            noFileRl.setVisibility(View.VISIBLE);
        else
        {
            noFileRl.setVisibility(View.INVISIBLE);
        }

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(files != null){

                    SparseBooleanArray spD = fileLV.getCheckedItemPositions();
                    for(int i =0;i<spD.size();i++)
                    {
                        Uri uri = FileProvider.getUriForFile(
                                getApplicationContext(),
                                getApplicationContext().getPackageName() + ".provider",
                                arrayList.get(spD.keyAt(i))
                        );
                        selectedFilesUri.add(uri);
                        Log.d("name", arrayList.get(spD.keyAt(i)).getName());
                    }

                }


                if(whatsappFiles != null) {

                    SparseBooleanArray spW = WFilesLV.getCheckedItemPositions();
                    for (int i = 0; i < spW.size(); i++) {
                        Uri uri = FileProvider.getUriForFile(
                                getApplicationContext(),
                                getApplicationContext().getPackageName() + ".provider",
                                WhatsAppArray.get(spW.keyAt(i))
                        );
                        selectedFilesUri.add(uri);
                        Log.d("name", WhatsAppArray.get(spW.keyAt(i)).getName());
                    }
                }

                if(selectedFilesUri.size() != 0) {

                    for (Uri u : selectedFilesUri) {
                        Log.d("Uri selected: ", "" + u);
                    }
                }

                intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("abc",selectedFilesUri);
                intent.putExtras(bundle);
                setResult(10,intent);
                finish();






            }
        });

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(" Permissions","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            StartTasks();
        }
    }




    public class CustomAdapter extends BaseAdapter{

        private ArrayList<File> files;

        public CustomAdapter(ArrayList<File> files) {
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int i) {
            return files.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView( int i, View view, ViewGroup viewGroup) {



            view = getLayoutInflater().inflate(R.layout.file_row,null);
            TextView fileName = view.findViewById(R.id.FileName);



            fileName.setText(files.get(i).getName());
            Log.d("name of file : ",files.get(i).getName());

            ImageButton open = view.findViewById(R.id.openFile);

            fileIv = view.findViewById(R.id.fileIV);


            ContentResolver cR = getApplicationContext().getContentResolver();

            RelativeLayout fileRL = view.findViewById(R.id.fileRL);


            final Uri uri = Uri.fromFile(files.get(i));
            Log.d("Uri", ""+uri);
            MimeTypeMap mime = MimeTypeMap.getSingleton();



            final String fileExt = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(uri));
            final String mimeType = mime.getMimeTypeFromExtension(fileExt);
            Log.d("Mime Type", ""+mimeType);


            if (fileExt.contains("pdf"))
                fileIv.setImageResource(R.drawable.pdf);
            else if(fileExt.contains("doc"))
                fileIv.setImageResource(R.drawable.microsoft_word);
            else if(fileExt.contains("jpeg") || fileExt.contains("png") || fileExt.contains("jpg") || fileExt.contains("JPG") || fileExt.contains("PNG"))
                fileIv.setImageResource(R.drawable.file_image);
            else if(fileExt.contains("ppt") || fileExt.contains("pptx"))
                fileIv.setImageResource(R.drawable.ppt);
            else if(fileExt.contains("xlsx"))
                fileIv.setImageResource(R.drawable.microsoft_excel);
            else
                fileIv.setImageResource(R.drawable.file1);

            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri,mimeType);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(intent.resolveActivity(getPackageManager()) != null)
                        startActivity(Intent.createChooser(intent,"Open File with"));
                    else
                        Toast.makeText(MainActivity.this, "No app found for opening this document", Toast.LENGTH_SHORT).show();



                }
            });
          view.setClickable(false);




            return view;
        }
    }


    private void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG Granted","Permission is granted");
                return true;
            } else {

                Log.v("TAG Revoked","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG Granted","Permission is granted");
            return true;
        }
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


}