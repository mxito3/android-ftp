package com.example.hello;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
public class MainActivity extends ActionBarActivity{
	
	private ListenCommand listen;
	protected void onCreate(Bundle savedInstanceState) {
		//���ӵ�������
		
		
		
		// ��ʼ����������������֮���ȡ����
			//test();
			super.onCreate(savedInstanceState);
			// ����������
			setContentView(R.layout.activity_main);

			listen=new ListenCommand((EditText) findViewById(R.id.editText1),(Button) findViewById(R.id.button1));
			new Thread(listen).start();
	}

}



	

