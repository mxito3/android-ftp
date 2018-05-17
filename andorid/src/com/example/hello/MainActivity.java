package com.example.hello;

import android.support.v7.app.ActionBarActivity;
import android.text.StaticLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity {
	private EditText command;
	private Button submitButton;// 提交按钮
	private static Scanner ctrlScanner;
	private static PrintWriter ctrlWriter;
	private static InputStream dataIs;
	private static OutputStream dataOs;
	private static Scanner dataScanner;
	private static PrintWriter dataWriter;
	private static Scanner userInputScanner;
	private static Socket ctrlSocket;
	private static Socket dataSocket;
	private static int runOrNot = 0;
	private static byte[] buff = new byte[1024];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 加载主界面
		setContentView(R.layout.activity_main);
		// 初始化这两个变量用来之后获取内容
		
		//连接到服务器
		//connect("");
		command = (EditText) findViewById(R.id.editText1);
		submitButton = (Button) findViewById(R.id.button1);

		// 监听按钮
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(command.getText());

			}
		});

	}

	public void connect(String[] args) throws IOException {

		// get IP address and port
		InetAddress addr = null;
		try {
			addr = (InetAddress) Inet4Address.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Couldn't resolve ip_address input to valid IP address");
			return;
		}
		Integer port = new Integer(args[1]);
		ctrlSocket = new Socket(addr, port);
		ctrlScanner = new Scanner(ctrlSocket.getInputStream());
		ctrlWriter = new PrintWriter(ctrlSocket.getOutputStream(), true);
		System.out.println(
				"Control socket established to " + ctrlSocket.getInetAddress() + " port " + ctrlSocket.getPort());
		runOrNot = 1;
		Long connectionId = ctrlScanner.nextLong();
		System.out.println("Received connection id from server: " + connectionId);
		dataSocket = new Socket(addr, port + 1);
		dataIs = dataSocket.getInputStream();
		dataOs = dataSocket.getOutputStream();
		dataScanner = new Scanner(dataIs);
		dataWriter = new PrintWriter(dataOs, true);
		dataWriter.println(connectionId.toString());
		System.out
				.println("Data socket established to " + dataSocket.getInetAddress() + " port " + dataSocket.getPort());
	}

	public void contorl(String command) throws IOException
	{
		String inputLine = "";
		String userCommand = "";
		StringBuilder userArg;
			if(runOrNot==0)
			{
				System.out.println("process end!");
			}
			userArg = new StringBuilder();
		
			//get command and command string arguments
			
			inputLine = command.trim();
			
			String[] commandStrings = inputLine.split(" ");
			if (commandStrings != null && commandStrings.length > 0 && !commandStrings[0].trim().equals(""))
			{ 
				
				userCommand = commandStrings[0].trim();	//the command given
				
				//reconstruct string following the command
				for (int i = 1; i<commandStrings.length; ++i) {
					userArg.append(commandStrings[i]);
					userArg.append(" ");
				}
				
				if(userCommand.equals("touch"))
				{
					if (do_touch(userArg.toString().trim())) {
						System.out.println("create file "+userArg.toString().trim()+" with success!");
					} else {
						System.out.println("Server encountered an error");
					}
				}
				else if(userCommand.equals("exit"))
				{
					do_quit();
					userInputScanner.close();
					ctrlSocket.close();
					dataSocket.close();	
					runOrNot=0;
				}
			}
			
			
	}

	private static void do_quit() {
		ctrlWriter.println("CLOSE");
	}

	private static boolean do_touch(String fileName) {
		boolean result = false;
		ctrlWriter.println("touch");
		dataWriter.println(fileName);
		// 接受服务器发来的是否成功的消息
		if (ctrlScanner.next().equals("OK")) {
			result = true;

		}
		return result;

	}
}
