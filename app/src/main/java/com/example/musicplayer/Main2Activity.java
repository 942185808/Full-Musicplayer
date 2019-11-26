package com.example.musicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    String[] filePathList2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ListView lv = (ListView) findViewById(R.id.lv);

        AssetManager assetManager2 = getAssets();
        try {
            filePathList2 = assetManager2.list("");
        } catch (IOException e) {
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tv_name,filePathList2);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("是否添加歌曲");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Main2Activity.this, MainActivity.class); startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();
                return false;
            }

        });
    }
}
