package com.example.leesanghyuk.LayoutDemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CONSTANTS_MAIN;
import com.example.sf.PoetryInfo.PoetryList;
import com.example.sf.Server.Server;
import com.example.sf.amap3d.R;
import com.example.sf.amap3d.*;
import com.example.LJJ.Global.*;

import java.sql.Timestamp;
import java.util.Date;

public class PushStoryDemo extends AppCompatActivity implements View.OnClickListener{
    private EditText push_content;
    private EditText push_title;
    private TextView push_button_t;
    private TextView push_locate_t;
    private ImageView push_button;
    private ImageView push_locate;
    private ImageView push_back;
    private MyApp app;//全局变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_story_demo);
        app=(MyApp)getApplication();
        init();
    }

    public void init(){
        push_title=(EditText)findViewById(R.id.push_title);
        push_content= (EditText) findViewById(R.id.push_content);
        push_button= (ImageView) findViewById(R.id.push_button);
        push_locate=(ImageView)findViewById(R.id.push_locate);
        push_button_t=(TextView) findViewById(R.id.push_button_t);
        push_locate_t=(TextView) findViewById(R.id.push_locate_t);
        push_locate_t.setText(app.getAddress());
        push_back=(ImageView)findViewById(R.id.push_back);

        push_button.setOnClickListener(this);
        push_locate.setOnClickListener(this);
        push_locate_t.setOnClickListener(this);
        push_button_t.setOnClickListener(this);
        push_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.push_button:
                insertStory();
                Toast.makeText(this, "发布故事", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PushStoryDemo.this, TtsDemo.class);
                intent.putExtra(TtsDemo.POETRIES_ID_EXTRA,id);
                intent.putExtra(TtsDemo.PUSH_STORY_EXTRA,true);
                startActivity(intent);
                break;
            case R.id.push_button_t:
                insertStory();
                Toast.makeText(this, "发布故事", Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(PushStoryDemo.this, TtsDemo.class);
                intent2.putExtra(TtsDemo.POETRIES_ID_EXTRA,id);
                intent2.putExtra(TtsDemo.PUSH_STORY_EXTRA,true);
                startActivity(intent2);
                break;
            case R.id.push_back:
                this.finish();
                break;
            case R.id.push_locate:
                Intent tolocate=new Intent(PushStoryDemo.this,MainActivity.class);
                app.setIswait(true);
                startActivity(tolocate);

                break;
            case R.id.push_locate_t:
                break;
        }
    }

    Handler handler_for_story=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private int id=89757;//故事编号，应该怎么弄？
    private int poet_id=97;//故事作者，也就是用户名 需要季杰提供。
    private final static String URL= CONSTANTS_MAIN.URL_ROOT;
    private void insertStory(){
        Toast.makeText(PushStoryDemo.this,app.getNowLoc().latitude+":"+app.getNowLoc().longitude,Toast.LENGTH_SHORT).show();
        Server server=new Server(handler_for_story, URL+"/commentrelative/set_story");
        String sql="insert into poetries(id,poet_id,content,title) values("
                +id+","+poet_id+",\""+push_content.getText().toString()+"\",\""+push_title.getText().toString()+"\")";
        server.post(sql);
    }
}
