package com.example.hello;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
public class MainActivity extends ActionBarActivity {
	private EditText command;
	private Button submitButton;//�ύ��ť
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����������
		setContentView(R.layout.activity_main);
		//��ʼ����������������֮���ȡ����
		command=(EditText) findViewById(R.id.editText1);
		submitButton=(Button) findViewById(R.id.button1);
		
		//������ť
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(command.getText());
				
			}
		});
		
	}

}
