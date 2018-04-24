package com.example.sf.amap3d;

import android.Manifest;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.ArcOptions;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.TileOverlayOptions;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.LJJ.Global.*;
import com.example.LJJ.Login.LoginActivity;
import com.example.LJJ.MyUser.PersonCenter;
import com.example.LJJ.MyUser.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.example.leesanghyuk.BackTools.DribSearchView;
import com.example.leesanghyuk.BackTools.PopMenu;
import com.example.leesanghyuk.BackTools.PopMenuItem;
import com.example.leesanghyuk.BackTools.PopMenuItemListener;
import com.example.leesanghyuk.LayoutDemo.PushStoryDemo;
import com.example.leesanghyuk.LayoutDemo.SearchPageDemo;
import com.example.leesanghyuk.LayoutDemo.TtsDemo;
import com.example.leesanghyuk.POJO.CommentInfo;
import com.example.sf.CONSTANTS_SF;
import com.example.sf.DataBase.Poetry;
import com.example.sf.DataBase.DataInit;
import com.example.sf.DrawMap.DrawMark.DrawMarksWithMultiThread;
import com.example.sf.PoetryInfo.PoetryList;
import com.example.sf.Server.Server;
import com.example.sf.TimeShaft.ComparedTimeShaft.PoetChoiceClone;
import com.example.sf.TimeShaft.ComparedTimeShaft.PoetChoiceListview.PoetMutipleChoice;
import com.example.sf.TimeShaft.PoetChoice;
import com.example.sf.TimeShaft.Poetries;
import com.example.sf.TimeShaft.TS_Thread;
import com.example.sf.TimeShaft.TS_Thread_NoWait;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class MainActivity extends AppCompatActivity implements LocationSource,AMapLocationListener{

    private DisplayMetrics metrics = new DisplayMetrics();
    private int width;
    private int height;

    private MapView mapView;
    private AMap aMap;
    private Button basicmap;
    private Button rsmap;
    private Button nightmap;
    private Button navimap;
    private ImageButton menubutton;
    private ArrayList<String> res;
    private DrawerLayout drawer;
    private CoordinatorLayout coordin;
    private NavigationView nv;
    private String info;
    //侧滑菜单组件
    private AppBarLayout barlayout;
    private ImageButton personinfo;
    private boolean iscollapse;
    private Criteria criteria;
    private LatLng nowlat;
    private boolean islisten;
    private MyApp app;
    private LocationManager locationManager;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean isFirstLoc=true;
    private User user;
    private String[] title={"我的好友","我的收藏","敬请期待"};
    private MarkerOptions markerOptions;
    private Marker marker;


    //发布文字
    private PopMenu mPopMenu;
    //搜索框
    private EditText editview;
    DribSearchView dribSearchView;
    private MaterialMenuDrawable materialMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置对应的XML布局文件
        app=(MyApp)getApplication();
        mapView = (MapView) findViewById(R.id.map);
        initcomponent();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        aMap = mapView.getMap();
        markerOptions=new MarkerOptions().title("再次点击确认");
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
        initLocate();
        ArcOptions arcOptions = new ArcOptions();
        aMap.setCustomMapStylePath("/main/assets/mapstyle/style.data");
        aMap.setMapCustomEnable(true);
//        aMap.setPointToCenter(40,20);


        registerListeners();

        SpeechUtility.createUtility(com.example.sf.amap3d.MainActivity.this, SpeechConstant.APPID +"=5aa60f6b");
        requestPermissions();

        //initDatabase();
//drawAllPoints();
        drawHeatMap();

        initPopMenu();
        initSearchBar();


        width = metrics.widthPixels;
        height = metrics.heightPixels;
        TimerSControl();
    }

    protected void initLocate(){

        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(app.isIswait());
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    public void initSearchBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBarOverlayLayout);
        setSupportActionBar(toolbar);
        editview = (EditText) findViewById(R.id.search_view);
        editview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if (editview.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this, "请输入搜索信息", Toast.LENGTH_SHORT).show();
                    }
                    //这里写发送信息的方法
                    else {
                        System.out.println(editview.getText().toString()+"    !!!!!!!!");
                        Toast.makeText(MainActivity.this, editview.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SearchPageDemo.class);
                        intent.putExtra(SearchPageDemo.SEARCH_INFO, editview.getText().toString());
                        startActivity(intent);
                    }
                        return true;
                }
                else return false;
            }
        });

        dribSearchView = (DribSearchView) findViewById(R.id.dribSearchView);
        if(app.isIswait()){
            dribSearchView.changeLine();
        }
        dribSearchView.setOnClickSearchListener(new DribSearchView.OnClickSearchListener() {
            @Override
            public void onClickSearch() {
                dribSearchView.changeLine();
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW, true);
            }
        });
        dribSearchView.setOnChangeListener(new DribSearchView.OnChangeListener() {
            @Override
            public void onChange(DribSearchView.State state) {
                switch (state) {
                    case LINE:
                        editview.setVisibility(View.VISIBLE);
                        editview.setFocusable(true);
                        editview.setFocusableInTouchMode(true);
                        editview.requestFocus();
                        break;
                    case SEARCH:
                        editview.setVisibility(View.GONE);
                        break;
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dribSearchView.changeSearch();
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER, true);
            }
        });

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.EXTRA_THIN);
        toolbar.setNavigationIcon(materialMenu);
        materialMenu.setNeverDrawTouch(true);
    }

    protected void initcomponent(){
        LinearLayout ll=(LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.temp,null);
       /* if(app.isIswait()) {
            search.searchBack.setImageResource(R.drawable.back);
            search.setOnClickBack(new bCallBack() {
                @Override
                public void BackAciton() {
                    Intent intent=new Intent(MainActivity.this,PushStoryDemo.class);
                    startActivity(intent);
                    app.setIswait(false);
                }

            });
        }else{
            search.searchBack.setImageResource(R.drawable.back);
            search.setOnClickBack(new bCallBack() {

                @Override

                public void BackAciton() {
                    drawer.openDrawer(Gravity.LEFT);

                }

            });
        }*/
        personinfo=(ImageButton) ll.findViewById(R.id.accimage);
        TextView tv=(TextView) (ll.findViewById(R.id.accinfo));
        if(app.getUser()!=null) {
            info = app.getUser().getNickname() + "|" + (app.getUser().isSex() ? "♂" : "♀");
        }else{
            info="游客登录";
        }
        tv.setText(info);
        nv=(NavigationView) findViewById(R.id.nav);
        nv.addHeaderView(ll);
        mapView = (MapView) findViewById(R.id.map);
        coordin=(CoordinatorLayout)findViewById(R.id.coordin);
        barlayout=(AppBarLayout)findViewById(R.id.titlebar);
        drawer=(DrawerLayout)findViewById(R.id.drawer);


    }

    private void registerListeners(){
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(app.isIswait()) {
                    if (marker == null) {
                        markerOptions.position(latLng);
                        marker = aMap.addMarker(markerOptions);
                    } else {
                        marker.setPosition(latLng);
                    }
                }
                Toast.makeText(MainActivity.this,latLng.latitude+","+latLng.longitude,Toast.LENGTH_SHORT).show();
                barlayout.setExpanded(iscollapse);
                iscollapse=!iscollapse;
                app.setNowLoc(latLng);
               editview.setText(latLng.latitude+","+latLng.longitude);
               app.setAddress(latLng.latitude+","+latLng.longitude);
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(app.isIswait()) {
                    Intent intent = new Intent(MainActivity.this, PushStoryDemo.class);
                    startActivity(intent);
                    app.setIswait(false);
                }
                return false;
            }
        });
      /* search.setOnClickSearch(new ICallBack() {

            @Override

            public void SearchAciton(String string) {



            }

        });
*/



        personinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.getUser()!=null){
                    Intent intent=new Intent(MainActivity.this, PersonCenter.class);
                    startActivity(intent);
                    drawer.closeDrawer(Gravity.LEFT);}
                else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



    void initDatabase(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isFirstRun) {
//    if(true){
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            try {
                new DataInit().initDate(getAssets().open("SU_SHI.xls"));
                new DataInit().initDate(getAssets().open("DU_FU.xls"));
                new DataInit().initDate(getAssets().open("XIN_QI_JI.xls"));
                new DataInit().initDate(getAssets().open("WANG_WEI.xls"));
                new DataInit().initDate(getAssets().open("LI_BAI.xls"));
                new DataInit().initDate(getAssets().open("WANG_YU_CHENG.xls"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    void drawHeatMap(){
        List<Poetry> poetries= DataSupport.select("latitude","longtitude").find(Poetry.class);
        // 构建热力图 HeatmapTileProvider
        HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
        builder.data(Poetry.getLatLngs(poetries)).transparency(0.9);// 设置热力图绘制的数据
// Gradient 的设置可见参考手册
// 构造热力图对象
        HeatmapTileProvider heatmapTileProvider = builder.build();
        // 初始化 TileOverlayOptions
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        tileOverlayOptions.tileProvider(heatmapTileProvider); // 设置瓦片图层的提供者
// 向地图上添加 TileOverlayOptions 类对象
        aMap.addTileOverlay(tileOverlayOptions);
    }
    void drawAllPoints(){
        List<Poetry> poetries= DataSupport.select("latitude","longtitude").find(Poetry.class);
        DrawMarksWithMultiThread drawMarksWithMultiThread=new DrawMarksWithMultiThread(aMap,Poetry.getLatLngs(poetries));
        drawMarksWithMultiThread.createThread();
    }

    void initPopMenu(){
        mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                .addMenuItem(new PopMenuItem("发布故事", getResources().getDrawable(R.drawable.tabbar_compose_idea)))
                .addMenuItem(new PopMenuItem("时间轴", getResources().getDrawable(R.drawable.tabbar_compose_photo)))
                .addMenuItem(new PopMenuItem("对比轴", getResources().getDrawable(R.drawable.tabbar_compose_headlines)))
                .addMenuItem(new PopMenuItem("个人信息", getResources().getDrawable(R.drawable.tabbar_compose_lbs)))
                .addMenuItem(new PopMenuItem("点评", getResources().getDrawable(R.drawable.tabbar_compose_review)))
                .addMenuItem(new PopMenuItem("更多", getResources().getDrawable(R.drawable.tabbar_compose_more)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        if(position==0) {
                            Intent intent =new Intent(MainActivity.this, PushStoryDemo.class);
                            startActivity(intent);
                        }
                        if(position==1){
                            Intent intent=new Intent(MainActivity.this, PoetChoice.class);
                            startActivity(intent);
                        }
                        if(position==2){
                            Handler handler=new Handler(){
                                @Override
                                public void handleMessage(Message msg){
                                    String result=msg.obj.toString();
                                    Intent intent=new Intent(MainActivity.this, PoetMutipleChoice.class);
                                    intent.putExtra("result",result);
                                    startActivity(intent);
                                }
                            };
                            Server server=new Server(handler, CONSTANTS_SF.URL_ROOT+"poetList");
                            String sql="select name,id from poet_l";
                            server.post(sql);
                        }
                        if (position==3){
                            drawer.openDrawer(Gravity.LEFT);
                        }

                    }
                })
                .build();
        findViewById(R.id.rl_bottom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopMenu.isShowing()) {
                    mPopMenu.show();
                }
            }
        });
    }
    /**
     * 方法必须重写
     */


    /*protected String getAddress(LatLng latlng){
        String url = "http://maps.google.com/maps/api/geocode/json";
        HttpGet httpGet = new HttpGet(url + "?sensor=false&address=" + address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getGeoPoint(jsonObject);
    }

    private static GeoPoint getGeoPoint(JSONObject jsonObject) {
        try {
            JSONArray array = (JSONArray) jsonObject.get("results");
            JSONObject first = array.getJSONObject(0);
            JSONObject geometry = first.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");

            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            return new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                if(!app.isIswait()) {
                    app.setNowLoc(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                }
              //  search.et_search.setText(aMapLocation.getLatitude()+","+aMapLocation.getLongitude());
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                app.setAddress(aMapLocation.getAddress());
                editview.setText(aMapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            //    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.onDestroy();
    }
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * 时间轴的控制函数
     */
    private void TimerSControl() {
//        List<Poetry> poetries = DataSupport.where("poet_id=?", String.valueOf(poetId)).find(Poetry.class);


        final Handler hander = new Handler(){
            public void handleMessage(Message m){
                Toast.makeText(MainActivity.this,"HELLO",Toast.LENGTH_SHORT);
                Log.i("TAG", "handleMessage: **********__________________))))))))))))))))))))))))))");
//                ArrayList<Integer> poetryList= (ArrayList<Integer>) m.obj;
                Poetries poetries=(Poetries)m.obj;
//                Log.i(TAG, "handleMessage: "+poetryList.toString());
                Intent intent=new Intent(MainActivity.this,PoetryList.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(TS_Thread.POETRY_ID_LIST,poetries.poetriesId);
                intent.putExtra(TS_Thread.POETRY_TITLE_LIST,poetries.poetriesName);

                startActivity(intent);
            }
        };
        final List<Poetry> poetries=new ArrayList<>();

        final Handler serMsgHander=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                String result=msg.obj.toString();

                extraJson(result,poetries);

if(poetries.size()>0){
                final TS_Thread_NoWait ts = new TS_Thread_NoWait(aMap, poetries, width, height, MainActivity.this,hander);

                //        PortTimerShaft.this.runOnUiThread(ts);
                Thread thread = new Thread(ts);
                thread.start();
            }}
        };
        String sql="select * from "+ CONSTANTS_SF.TABLE_POETRIES_L.TABLE_NAME ;
        Server server=new Server(serMsgHander, CONSTANTS_SF.URL_ROOT+"poetriesList");
        server.post(sql);

    }

    public static void extraJson(String result,List<Poetry> poetries){
//        List<Poetry> poetries=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(result);
            for(int i=0;i<jsonObject.length()-1;i++){
                JSONObject poetriesJson=jsonObject.getJSONObject(i+"");
                Poetry poetry=new Poetry();
//                poetry.setAgeOfPoet(poetriesJson.getInt(CONSTANTS_SF.TABLE_POETRIES_L.AGE));
//                poetry.setCity_ancient(poetriesJson.getString(CONSTANTS_SF.TABLE_POETRIES_L.CITY_OF_ANCIENT));
//                poetry.setCity_current(poetriesJson.getString(CONSTANTS_SF.TABLE_POETRIES_L.CITY_OF_CURRENT));
//                poetry.setCounty_ancient(poetriesJson.getString(CONSTANTS_SF.TABLE_POETRIES_L.COUNTY_OF_ANCIENT));
//                poetry.setCounty_current(poetriesJson.getString(CONSTANTS_SF.TABLE_POETRIES_L.COUNTY_OF_CURRENT));
//                        poetry.setId(poetriesJson.getInt(CONSTANTS_SF.TABLE_POETRIES_L.ID));

                poetry.setLatitude(poetriesJson.getDouble(CONSTANTS_SF.TABLE_POETRIES_L.LATTITUDE));
                poetry.setLongtitude(poetriesJson.getDouble(CONSTANTS_SF.TABLE_POETRIES_L.LONGTITUDE));
                poetry.setTitle(poetriesJson.getString(CONSTANTS_SF.TABLE_POETRIES_L.TITLE));
                poetry.setId(poetriesJson.getInt(CONSTANTS_SF.TABLE_POETRIES_L.ID));
                poetries.add(poetry);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        System.out.println(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

