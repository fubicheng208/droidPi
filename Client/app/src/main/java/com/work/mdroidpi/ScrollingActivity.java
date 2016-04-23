package com.work.mdroidpi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.Dialog;

import org.json.JSONException;
import org.json.JSONObject;


public class ScrollingActivity extends AppCompatActivity {

    public static final String TAG = "ScrollingActivity";
    public static final String ACTIVITY = "ActivityLife";
    private mButton[] bt = new mButton[41];
    private Switch[] switches = new Switch[41];
    public String ip;
    public String port;
    public String value;
    public boolean isOut;

    //private Socket socket;
    private Handler handler;
    //private PrintWriter pw;
    //private BufferedReader br;
    // 定义与服务器通信的子线程
    ClientThread clientThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY, "onCreate");
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化控件（至少要有个界面先跳出来，再getPorts()
        initView();
        try {
            getPorts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //判断是否未设置IP,
        // 获得IP地址和PORT
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String s1, s2;
        s1 = sp.getString("ip", "");
        s2 = sp.getString("port", "");
        if (s1.equals("") || s2.equals("")) {
            Intent intent = new Intent(ScrollingActivity.this, settings.class);
            //true意味着要在Settings界面跳出提示信息
            intent.putExtra("TAG", true);
            startActivity(intent);
        } else {
            ip = s1;
            port = s2;
        }
        //检测是否已联网



        /*handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //对应于获取端口所有状态
                if(msg.what == 0x123){

                }
                //对应于获取一个端口状态
                else if(msg.what == 0x124){

                }
                else if(msg.what == 0x125){

                }
            }
        };*/
       /* try {
            socket  = new Socket(ip,Integer.parseInt(port));
        }catch (UnknownHostException e){
            e.printStackTrace();
            Log.e(TAG,"unknown host");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"io exception");
        }
        if(socket == null){
            Log.e(TAG, "socket null");
        }
        else{
            try {
                pw = new PrintWriter(socket.getOutputStream());
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if(pw!=null&&br!=null){
                    //另起线程监听服务器消息
                    new Thread(this).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        handler = new Handler() //①
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 如果消息来自于服务器返回
                if (msg.what == 0x123)
                {
                  //判断是哪个方法的返回
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String method = jsonObject.getString("Method");
                        //如果是GetPorts返回的消息
                        if(method.equals("Result1")){
                            resetAllView(jsonObject);
                        }
                        //如果是GetPort返回的消息
                        else if(method.equals("Result2")){

                        }
                        //如果是SetPort返回的消息
                        else{
                            String result = jsonObject.getString("Result");
                            //设置失败要把switch和button还原
                            if(result.equals("Failed")){
                                int i = jsonObject.getInt("Port");
                                String value = jsonObject.getString("Value");
                                rollbackUI(i, value);
                                Dialog dialog = new Dialog(ScrollingActivity.this, "Error","Set port failed!Please check your network.");
                                dialog.show();
                            }
                            //如果设置成功，则不做修改
                            else{
                                Log.i(TAG, "设置port成功！");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(msg.what == 0x456){
                    Dialog dialog = new Dialog(ScrollingActivity.this, "Error","Unable to connect to server," +
                            "please check your network");
                    dialog.show();
                }
            }
        };
        clientThread = new ClientThread(handler, ip, port);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start(); //①

        //创建公用的监听器
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断被点击的控件类型
                int t = Integer.parseInt(v.getTag().toString());

                //改变button的颜色,如果为true，即原来是绿色，要变为黄色，反之
                if (bt[t].get_Pressed()) {
                    Log.d(TAG, String.valueOf(bt[t].get_Pressed()));
                    int color = getResources().getColor(R.color.colorOrange);
                    v.setBackgroundColor(color);
                    bt[t].change_Pressed();
                    Log.d(TAG, String.valueOf(bt[t].get_Pressed()));
                    value = "Out_high";
                } else {
                    Log.d(TAG, String.valueOf(bt[t].get_Pressed()));
                    int color = getResources().getColor(R.color.colorGreen);
                    v.setBackgroundColor(color);
                    bt[t].change_Pressed();
                    Log.d(TAG, String.valueOf(bt[t].get_Pressed()));
                    value = "Out_low";
                }

                //提交状态变化给SocketServer
                try {
                    isOut = false;
                    setPort(t,value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

                /*Dialog dialog = new Dialog(ScrollingActivity.this, "TEST", v.getTag().toString());
                dialog.show();*/

        };

        //Switch的监听器
        Switch.OnCheckListener onCheckListener = new Switch.OnCheckListener() {
            @Override
            public void onCheck(Switch view, boolean check) {
                //如果转换为未被打开，即为in状态则按钮为黑色,并设置为不可点击
                if (!check) {
                    Log.d(TAG, "!check");
                    int t = Integer.parseInt(view.getTag().toString());
                    value = "In";
                    bt[t].setEnabled(false);
                    try {
                        setPort(t,value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //如果转换为checked则为out状态，要根据原来是否pressed判断变为什么颜色,并重新可点击
                else {
                    Log.d(TAG, "checked");
                    int t = Integer.parseInt(view.getTag().toString());
                    //true对应绿色 low，false对应黄色 high
                    if(bt[t].get_Pressed()){
                        value = "Out_low";
                    }
                    else{
                        value = "Out_high";
                    }
                    try {
                        isOut = true;
                        setPort(t, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bt[t].setEnabled(true);
                }
            }
        };

        //为每个按钮和switch设置TAG和监听器
        for (int i = 1; i <= 40; i++) {
            bt[i].setTag(i);
            bt[i].setOnClickListener(onClickListener);
            switches[i].setTag(i);
            switches[i].setOncheckListener(onCheckListener);
        }

        //去掉自动添加的fab
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }

    /*@Override
    public void run() {
        try {
            StringBuffer content= new StringBuffer();
            String str;
            while((str=br.readLine())!=null){
                content.append(str);
            }
            *//*InputStream ins=socket.getInputStream();
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=ins.read(buffer))!=-1){
                os.write(buffer);
            }*//*
            //第一步，生成Json字符串格式的JSON对象
            JSONObject jsonObject=new JSONObject(content.toString());
            Log.i(TAG,content.toString());
            //第二步，从JSON对象中取值如果JSON 对象较多，可以用json数组
            String method = jsonObject.getString("method");
            //如果是setPort返回的消息
            if(method.equals("result3")){

            }
            //如果是GetPort返回的消息
            else if(method.equals("result2")){

            }
            //如果是GetPorts返回的消息
            else{

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public void getPorts() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Method", "GetPorts");
        jsonObject.put("Encode", "Base64");
        String str = jsonObject.toString();
        Log.i("Result", str);
        //BASE64加密
        final String result = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        Log.i("Result", result);
        try
        {
            // 将数据封装成Message，
            // 然后发送给子线程的Handler
            Message msg = new Message();
            msg.what = 0x345;
            msg.obj = result;
            clientThread.revHandler.sendMessage(msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //提交控件状态变化给服务器
    public void setPort(int port, String value) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Method", "SetPort");
        jsonObject.put("Encode", "Base64");
        jsonObject.put("Port", port);
        jsonObject.put("Value", value);
        String str = jsonObject.toString();
        Log.i("Result", str);
        //BASE64加密
        final String result = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        Log.i("Result", result);
        /*//写入流
        pw.println(result);
        //清空缓冲区数据
        pw.flush();*/
        try
        {
            // 将数据封装成Message，
            // 然后发送给子线程的Handler
            Message msg = new Message();
            msg.what = 0x345;
            msg.obj = result;
            clientThread.revHandler.sendMessage(msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //SetPort给服务器发送请求后 revhandler收到回复发不成功消息通知前台回滚UI所用方法
    private void rollbackUI(int i,String value){
        if(value.equals("In")){
            bt[i].setEnabled(true);
            switches[i].setChecked(true);
        }
        else if(value.equals("Out_high")){
            if(isOut == true){
                bt[i].setEnabled(false);
                switches[i].setChecked(false);
            }
            else {
                int color = getResources().getColor(R.color.colorGreen);
                bt[i].setBackgroundColor(color);
                bt[i].change_Pressed();
                Log.d(TAG, String.valueOf(bt[i].get_Pressed()));
            }
        }
        else{
            if(isOut == true){
                bt[i].setEnabled(false);
                switches[i].setChecked(false);
            }
            else {
                int color = getResources().getColor(R.color.colorOrange);
                bt[i].setBackgroundColor(color);
                bt[i].change_Pressed();
                Log.d(TAG, String.valueOf(bt[i].get_Pressed()));
            }
        }

    }

    //getPortsg给服务器发送请求后 revhandler收到回复发消息通知前台更新UI所用方法
    private void resetAllView(JSONObject jsonObject) throws JSONException {
        int i;
        for(i=1;i<=40;i++){
            if(i<=9){
                String s = "Port0" + i;
                String result_port = jsonObject.getString(s);
                //黄的对应false 绿的对应true
                if(result_port.equals("Out_high")){
                    switches[i].setChecked(true);
                    bt[i].set_pressed(false);
                    int color = getResources().getColor(R.color.colorOrange);
                    bt[i].setBackgroundColor(color);
                }
                else if(result_port.equals("Out_low")){
                    switches[i].setChecked(true);
                    bt[i].set_pressed(true);
                    int color = getResources().getColor(R.color.colorGreen);
                    bt[i].setBackgroundColor(color);
                }
                else{
                    switches[i].setChecked(false);
                }
            }
            else
            {
                String s = "Port" + i;
                String result_port = jsonObject.getString(s);
                //黄的对应false 绿的对应true
                if(result_port.equals("Out_high")){
                    switches[i].setChecked(true);
                    bt[i].set_pressed(false);
                    int color = getResources().getColor(R.color.colorOrange);
                    bt[i].setBackgroundColor(color);
                }
                else if(result_port.equals("Out_low")){
                    switches[i].setChecked(true);
                    bt[i].set_pressed(true);
                    int color = getResources().getColor(R.color.colorGreen);
                    bt[i].setBackgroundColor(color);
                }
                else{
                    switches[i].setChecked(false);
                }
            }
        }

    }


    //初始化界面
    private void initView() {
        //初始化BUTTON
        bt[1] = (mButton) findViewById(R.id.button);
        bt[2] = (mButton) findViewById(R.id.button2);
        bt[3] = (mButton) findViewById(R.id.button3);
        bt[4] = (mButton) findViewById(R.id.button4);
        bt[5] = (mButton) findViewById(R.id.button5);
        bt[6] = (mButton) findViewById(R.id.button6);
        bt[7] = (mButton) findViewById(R.id.button7);
        bt[8] = (mButton) findViewById(R.id.button8);
        bt[9] = (mButton) findViewById(R.id.button9);
        bt[10] = (mButton) findViewById(R.id.button10);
        bt[11] = (mButton) findViewById(R.id.button11);
        bt[12] = (mButton) findViewById(R.id.button12);
        bt[13] = (mButton) findViewById(R.id.button13);
        bt[14] = (mButton) findViewById(R.id.button14);
        bt[15] = (mButton) findViewById(R.id.button15);
        bt[16] = (mButton) findViewById(R.id.button16);
        bt[17] = (mButton) findViewById(R.id.button17);
        bt[18] = (mButton) findViewById(R.id.button18);
        bt[19] = (mButton) findViewById(R.id.button19);
        bt[20] = (mButton) findViewById(R.id.button20);
        bt[21] = (mButton) findViewById(R.id.button21);
        bt[22] = (mButton) findViewById(R.id.button22);
        bt[23] = (mButton) findViewById(R.id.button23);
        bt[24] = (mButton) findViewById(R.id.button24);
        bt[25] = (mButton) findViewById(R.id.button25);
        bt[26] = (mButton) findViewById(R.id.button26);
        bt[27] = (mButton) findViewById(R.id.button27);
        bt[28] = (mButton) findViewById(R.id.button28);
        bt[29] = (mButton) findViewById(R.id.button29);
        bt[30] = (mButton) findViewById(R.id.button30);
        bt[31] = (mButton) findViewById(R.id.button31);
        bt[32] = (mButton) findViewById(R.id.button32);
        bt[33] = (mButton) findViewById(R.id.button33);
        bt[34] = (mButton) findViewById(R.id.button34);
        bt[35] = (mButton) findViewById(R.id.button35);
        bt[36] = (mButton) findViewById(R.id.button36);
        bt[37] = (mButton) findViewById(R.id.button37);
        bt[38] = (mButton) findViewById(R.id.button38);
        bt[39] = (mButton) findViewById(R.id.button39);
        bt[40] = (mButton) findViewById(R.id.button40);

        //初始化SWITCH
        switches[1] = (Switch) findViewById(R.id.switchView1);
        switches[2] = (Switch) findViewById(R.id.switchView2);
        switches[3] = (Switch) findViewById(R.id.switchView3);
        switches[4] = (Switch) findViewById(R.id.switchView4);
        switches[5] = (Switch) findViewById(R.id.switchView5);
        switches[6] = (Switch) findViewById(R.id.switchView6);
        switches[7] = (Switch) findViewById(R.id.switchView7);
        switches[8] = (Switch) findViewById(R.id.switchView8);
        switches[9] = (Switch) findViewById(R.id.switchView9);
        switches[10] = (Switch) findViewById(R.id.switchView10);
        switches[11] = (Switch) findViewById(R.id.switchView11);
        switches[12] = (Switch) findViewById(R.id.switchView12);
        switches[13] = (Switch) findViewById(R.id.switchView13);
        switches[14] = (Switch) findViewById(R.id.switchView14);
        switches[15] = (Switch) findViewById(R.id.switchView15);
        switches[16] = (Switch) findViewById(R.id.switchView16);
        switches[17] = (Switch) findViewById(R.id.switchView17);
        switches[18] = (Switch) findViewById(R.id.switchView18);
        switches[19] = (Switch) findViewById(R.id.switchView19);
        switches[20] = (Switch) findViewById(R.id.switchView20);
        switches[21] = (Switch) findViewById(R.id.switchView21);
        switches[22] = (Switch) findViewById(R.id.switchView22);
        switches[23] = (Switch) findViewById(R.id.switchView23);
        switches[24] = (Switch) findViewById(R.id.switchView24);
        switches[25] = (Switch) findViewById(R.id.switchView25);
        switches[26] = (Switch) findViewById(R.id.switchView26);
        switches[27] = (Switch) findViewById(R.id.switchView27);
        switches[28] = (Switch) findViewById(R.id.switchView28);
        switches[29] = (Switch) findViewById(R.id.switchView29);
        switches[30] = (Switch) findViewById(R.id.switchView30);
        switches[31] = (Switch) findViewById(R.id.switchView31);
        switches[32] = (Switch) findViewById(R.id.switchView32);
        switches[33] = (Switch) findViewById(R.id.switchView33);
        switches[34] = (Switch) findViewById(R.id.switchView34);
        switches[35] = (Switch) findViewById(R.id.switchView35);
        switches[36] = (Switch) findViewById(R.id.switchView36);
        switches[37] = (Switch) findViewById(R.id.switchView37);
        switches[38] = (Switch) findViewById(R.id.switchView38);
        switches[39] = (Switch) findViewById(R.id.switchView39);
        switches[40] = (Switch) findViewById(R.id.switchView40);


    }

    //获得焦点可交互状态。
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY, "onResume"+ ip + port);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clientThread = null;
        Log.i(ACTIVITY, "onPause"+ ip + port);

    }

    //onStop后重新开始状态
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ACTIVITY, "onRestart");
        //判断是否未设置IP,
        // 获得IP地址和PORT
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ip = sp.getString("ip", "");
        port = sp.getString("port", "");
        try {
            clientThread = new ClientThread(handler, ip, port);
            // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
            new Thread(clientThread).start(); //①
            getPorts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "ThirdActivity onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "ThirdActivity onRestoreInstanceState");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
            Intent intent = new Intent(ScrollingActivity.this, settings.class);
            startActivityForResult(intent,1);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(ScrollingActivity.this, about.class);
            startActivityForResult(intent,2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
