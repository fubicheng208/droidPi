/**
 *
 */
package com.work.mdroidpi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class ClientThread implements Runnable
{
	public static final String TAG = "ClientThread";
	private Socket socket;
	// 定义向UI线程发送消息的Handler对象
	private Handler handler;
	// 定义接收UI线程的消息的Handler对象
	public Handler revHandler;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;
	OutputStream os = null;
	private String ip;
	private String port;

	public ClientThread(Handler handler, String i, String p)
	{
		this.handler = handler;
		ip = i;
		port = p;
	}

	public void run()
	{
		try
		{
			socket = new Socket(ip, Integer.parseInt(port));
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			os = socket.getOutputStream();
			// 启动一条子线程来读取服务器响应的数据
			new Thread()
			{
				@Override
				public void run()
				{
					String content = null;
					// 不断读取Socket输入流中的内容。
					try
					{
						while ((content = br.readLine()) != null)
						{
							//BASE64解密
							Log.i(TAG, content);
							content = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
							Log.i(TAG, content);
							// 每当读到来自服务器的数据之后，发送消息通知程序界面做相应的更新
							JSONObject jsonObject = new JSONObject(content);
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = jsonObject;
							handler.sendMessage(msg);
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.start();
			// 为当前线程初始化Looper
			Looper.prepare();
			// 创建revHandler对象

			//发送请求处理
			revHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					if (msg.what == 0x345)
						try {
							os.write((msg.obj).toString().getBytes());
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			};
			// 启动Looper
			Looper.loop();
		}
		catch (SocketTimeoutException e1)
		{
			Message msg = new Message();
			msg.what = 0x456;
			handler.sendMessage(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}