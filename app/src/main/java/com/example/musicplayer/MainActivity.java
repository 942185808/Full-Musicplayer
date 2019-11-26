package com.example.musicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myTag";
    private MediaPlayer mediaPlayer;
    private int count = 0;
    String[] filePathList;
    String[] filename;
    private int currentPosition;
    private int cu;
    int a = 5;
    private SeekBar seekBar;
    private Timer timer;
    private List<String> list;
    private int pp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.ListView);

        AssetManager assetManager = getAssets();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setLooping(false);
        try {
            filePathList = assetManager.list("");
        } catch (IOException e) {
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tv_name,filePathList);
        lv.setAdapter(adapter);
        final TextView txtLoopState = (TextView) findViewById(R.id.txtLoopState);
        final Button buttonStart = (Button) findViewById(R.id.buttonStart);
        final Button buttonPause = (Button) findViewById(R.id.buttonPause);
        final Button buttonStop = (Button) findViewById(R.id.buttonStop);
        final Button buttonLoop = (Button) findViewById(R.id.buttonLoop);
        final Button buttonlast = (Button) findViewById(R.id.button_last);
        final Button add =(Button)findViewById(R.id.add);
      final Button buttonnext = (Button) findViewById(R.id.button_next);
      final Button buttonrandom = (Button) findViewById(R.id.button_random);
   final TextView randomview =(TextView)findViewById(R.id.randomView);
        buttonnext.setEnabled(false);
        buttonlast.setEnabled(false);
        buttonPause.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonLoop.setEnabled(false);
        seekBar = findViewById(R.id.seekBar);

//***************************************************************
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除歌曲");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=position;i<filePathList.length-1;i++)
                        {
                            filePathList[i]=filePathList[i+1];
                        }
                        filePathList[filePathList.length-1]="";
                        adapter.notifyDataSetChanged();
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        count = 0;
                        buttonnext.setEnabled(false);
                        buttonlast.setEnabled(false);
                        buttonPause.setEnabled(false);
                        buttonStop.setEnabled(false);
                        buttonLoop.setEnabled(false);
                        buttonStart.setEnabled(true);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();
                return false;
            }

        });
//***************************************************************
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(pp==0) {
                    next();
                    int ct=filePathList.length-3;
                    int ct1 = 0;
                    if (count == ct1) {
                        buttonlast.setEnabled(false);
                    }
                    else
                        buttonlast.setEnabled(true);
                    if(count>=ct)
                    {
                        buttonnext.setEnabled(false);
                    }
                    else
                        buttonnext.setEnabled(true);
                }
                if(pp==1)
                {
                    random();
                    int ct=filePathList.length-3;
                    int ct1 = 0;
                    if (count == ct1) {
                        buttonlast.setEnabled(false);
                    }
                    else
                        buttonlast.setEnabled(true);
                    if(count>=ct)
                    {
                        buttonnext.setEnabled(false);
                    }
                    else
                        buttonnext.setEnabled(true);
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {try {

                cu = position;

                //关闭timer任务（实时获取歌曲的进度）
                stopTimer();
                // 调用开启音乐的方法
                count=position;
                startMusic();
                int ct=filePathList.length-3;
                int ct1 = 0;
                if (count == ct1) {
                    buttonlast.setEnabled(false);
                }
                else
                    buttonlast.setEnabled(true);
                if(count>=ct)
                {
                    buttonnext.setEnabled(false);
                }
                else
                    buttonnext.setEnabled(true);
                buttonStart.setEnabled(false);
                buttonPause.setEnabled(true);
                buttonStop.setEnabled(true);
                buttonLoop.setEnabled(true);

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    startMusic();
                    buttonStart.setEnabled(false);
                    buttonnext.setEnabled(true);
                    buttonPause.setEnabled(true);
                    buttonStop.setEnabled(true);
                    buttonLoop.setEnabled(true);
                }catch(IOException e){}

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Main2Activity.class); startActivity(intent);

            }
        });
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    buttonPause.setText("Play");
                    mediaPlayer.pause();
                    buttonlast.setEnabled(false);
                    buttonnext.setEnabled(false);
                } else {
                    buttonPause.setText("Pause");
                    mediaPlayer.start();
                    if (count != 0)
                        buttonlast.setEnabled(true);
                    int ct = filePathList.length - 3;
                    if (count != ct)
                        buttonnext.setEnabled(true);
                }

            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                count = 0;
                buttonnext.setEnabled(false);
                buttonlast.setEnabled(false);
                buttonPause.setEnabled(false);
                buttonStop.setEnabled(false);
                buttonLoop.setEnabled(false);
                buttonStart.setEnabled(true);
            }
        });
        buttonLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(TAG, "Looping");
                boolean loop = mediaPlayer.isLooping();
                mediaPlayer.setLooping(!loop);


                if (!loop) {
                    txtLoopState.setText("循环播放");
                    buttonrandom.setEnabled(false);
                }
                else {
                    txtLoopState.setText("一次播放");
                    buttonrandom.setEnabled(true);
                }
            }
        });
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buttonlast.setEnabled(true);
                    AssetManager assetManager = getAssets();
                    count++;
                    int ct = filePathList.length - 3;
                    if (count == ct) {
                        buttonnext.setEnabled(false);
                    }
                    buttonlast.setEnabled(true);
                    AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filePathList[count]);
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    int duration = mediaPlayer.getDuration();
                    //将歌曲 的总长度赋值给seekbar
                    seekBar.setMax(duration);
                    //使seekbar实时获取歌曲的进度
                    getProgress();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AssetManager assetManager = getAssets();
                    count--;
                    int ct1 = 0;
                    if (count == ct1) {
                        buttonlast.setEnabled(false);
                    }
                    buttonnext.setEnabled(true);
                    AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filePathList[count]);
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    int duration = mediaPlayer.getDuration();
                    //将歌曲 的总长度赋值给seekbar
                    seekBar.setMax(duration);
                    //使seekbar实时获取歌曲的进度
                    getProgress();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //获取进度条的进度
                int p = seekBar.getProgress();
                //将进度条的进度赋值给歌曲
                mediaPlayer.seekTo(p);
                //开始音乐继续获取歌曲的进度
                getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //取消timer任务
                stopTimer();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {


            }
        });
        buttonrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pp==0) {
                    randomview.setText("random mode");
                    pp=1;
                }
                else
                {
                    randomview.setText("no random mode");
                    pp=0;
                }
            }
        });

    }

    private void getProgress () {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                //获取歌曲的进度
                int p = mediaPlayer.getCurrentPosition();

                //将获取歌曲的进度赋值给seekbar
                seekBar.setProgress(p);
            }
        }, 0, 2000);
    }
    public void stopTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
        }
    }
    private void startMusic() throws IOException {
        // 关闭正在播放的歌曲（避免播放多首歌曲）
        mediaPlayer.reset();
        AssetManager assetManager = getAssets();
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filePathList[count]);
        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.start();
        //获取当前歌曲的总长度
        int duration = mediaPlayer.getDuration();
        //将歌曲 的总长度赋值给seekbar
        seekBar.setMax(duration);
        //使seekbar实时获取歌曲的进度
        getProgress();
        Toast toast=Toast.makeText(this,filePathList[count],Toast.LENGTH_SHORT);
        toast.show();
    }
    private void next() {
        count++;

        try {
            startMusic();
        }
        catch (IOException e){};
    }
    private void random() {
        Random r=new Random();
        count=r.nextInt(4);
        try {
            startMusic();
        }
        catch (IOException e){};
    }

}