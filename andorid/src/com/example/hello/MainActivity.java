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
	private Button submitButton;// �ύ��ť
	private String userCommand;
	private connectServer connect=new connectServer();
	String tag="tag";
	String msg = "Android : ";
	protected void onCreate(Bundle savedInstanceState) {
		//���ӵ�������
		
		
		new Thread(connect).start();
		// ��ʼ����������������֮���ȡ����
			//test();
			super.onCreate(savedInstanceState);
			// ����������
			setContentView(R.layout.activity_main);
		
			new Thread(this).start();
			//run();
	
	 /** ��������ɼ�ʱ���� */
	}

	   public void run()
	   {
		    command = (EditText) findViewById(R.id.editText1);
			submitButton = (Button) findViewById(R.id.button1);

			// ������ť
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



	

