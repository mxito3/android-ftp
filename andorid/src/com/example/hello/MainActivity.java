package com.example.hello;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
public class MainActivity extends ActionBarActivity {
	private EditText command;
	private Button submitButton;//提交按钮
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//加载主界面
		setContentView(R.layout.activity_main);
		//初始化这两个变量用来之后获取内容
		command=(EditText) findViewById(R.id.editText1);
		submitButton=(Button) findViewById(R.id.button1);
		
		//监听按钮
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(command.getText());
				
			}
		});
		
	}

}
