package com.example.hello;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity implements Runnable{
	private EditText command;
	private Button submitButton;// 提交按钮
	private String userCommand;
	private connectServer connect=new connectServer();
	String tag="tag";
	String msg = "Android : ";
	protected void onCreate(Bundle savedInstanceState) {
		//连接到服务器
		
		
		new Thread(connect).start();
		// 初始化这两个变量用来之后获取内容
			//test();
			super.onCreate(savedInstanceState);
			// 加载主界面
			setContentView(R.layout.activity_main);
		
			new Thread(this).start();
			//run();
	
	 /** 当活动即将可见时调用 */
	}

	   public void run()
	   {
		    command = (EditText) findViewById(R.id.editText1);
			submitButton = (Button) findViewById(R.id.button1);

			// 监听按钮
			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					userCommand=command.getText().toString();
					Log.v(tag,userCommand);
					try {
						connect.contorl(userCommand);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	   }
}



	

