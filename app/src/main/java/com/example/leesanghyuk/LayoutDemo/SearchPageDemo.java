package com.example.leesanghyuk.LayoutDemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CONSTANTS_MAIN;
import com.example.leesanghyuk.BackTools.PoetInfoAdapter;
import com.example.leesanghyuk.POJO.CommentInfo;
import com.example.leesanghyuk.POJO.PoetInfo;
import com.example.leesanghyuk.POJO.SearchPoemInfo;
import com.example.sf.CONSTANTS_SF;
import com.example.sf.DataBase.Poet;
import com.example.sf.PoetryInfo.PoetryList;
import com.example.sf.Server.Server;
import com.example.sf.TimeShaft.PoetChoice;
import com.example.sf.amap3d.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SearchPageDemo extends AppCompatActivity {
    public static final String SEARCH_INFO="SEARCH_INFO";
    ArrayList<SearchPoemInfo> poetInfos;

    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_demo);

        Intent getPoetId = getIntent();
        word=getPoetId.getStringExtra(SearchPageDemo.SEARCH_INFO);
        initmData();

    }

    public void initLayout(){

        ListView mListView = (ListView) findViewById(R.id.search_page_list_view);
        LayoutInflater inflater=getLayoutInflater();
        PoetInfoAdapter adapter=new PoetInfoAdapter(inflater,poetInfos);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView textView= (TextView) findViewById(R.id.id);
                Toast.makeText(SearchPageDemo.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SearchPageDemo.this, TtsDemo.class);
                intent.putExtra(TtsDemo.POETRIES_ID_EXTRA,poetInfos.get((int)id).getId());
                intent.putExtra(TtsDemo.PUSH_STORY_EXTRA,true);
                startActivity(intent);
                //                startActivity(new Intent(ListViewActivity.this, NormalActivity.class));
                //暂定
            }
        });
    }

    private final static String URL= CONSTANTS_MAIN.URL_ROOT;
    private void initmData(){
        Server server=new Server(handler, URL+"/poetryrelative/search_poetry_info");
        server.post(word);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result=msg.obj.toString();
            try {
                JSONObject jsonObject=new JSONObject(result);
                poetInfos=new ArrayList<>();
                for(int i=0;i<jsonObject.length()-1;i++){
                    int id=jsonObject.getJSONObject(i+"").getInt("id");
                    int poet_id=jsonObject.getJSONObject(i+"").getInt("poet_id");
                    String title=jsonObject.getJSONObject(i+"").getString("title");
                    String content=jsonObject.getJSONObject(i+"").getString("content");

                    SearchPoemInfo info=new SearchPoemInfo();
                    info.setId(id);
                    info.setContent(content);
                    info.setPoet_id(poet_id);
                    info.setTitle(title);

                    poetInfos.add(info);
                }
                initLayout();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
}
