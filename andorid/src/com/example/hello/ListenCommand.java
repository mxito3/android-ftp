package com.example.hello;
import java.io.IOException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;


public class ListenCommand implements Runnable{

	private EditText command;
	private Button submitButton;// 提交按钮
	private String userCommand;
	

	private connectServer connect=new connectServer();
	String tag="tag";
	String msg = "Android : ";
	
	 
	   public void run()
	   {
		    //new Thread(connect).start();
		  

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
	  public ListenCommand(EditText command,Button button)
	  {
		  this.command = command;
		  this.submitButton =button;
	  }
}
