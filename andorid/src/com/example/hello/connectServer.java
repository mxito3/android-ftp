package com.example.hello;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Scanner;
import android.view.View.OnClickListener;


public class connectServer extends ActionBarActivity implements Runnable{
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
	String inputLine = "";
	static String userCommand = "";
	StringBuilder userArg;

	private EditText command;
    private Button submitButton;// 提交按钮
    String tag="tag";
    String msg = "Android : ";

	public void run() {
		int rawPort=8545;
		
		try {
			System.out.println("在run");
			connect("192.168.1.105",rawPort);
			//listenCommand();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean connect(String addr,int port) throws IOException{
		System.out.println("在connect了");
		System.out.println(addr+"    "+port);
		try {
			System.out.println("在内部try");
			ctrlSocket = new Socket();
			ctrlSocket.connect(new InetSocketAddress(addr,port),1000);
			System.out.println("连接建立");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		System.out.println(e);
		}
		
		ctrlScanner = new Scanner(ctrlSocket.getInputStream());
		System.out.println("ctrl scanner建立成功");
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
		System.out.println("Data socket established to " + dataSocket.getInetAddress() + " port " + dataSocket.getPort());
		return true;
	}
	
	

	
	public boolean contorl(String command) throws IOException
	{
		boolean result=false;
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
						result=true;
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
					result=true;
				}
				else if(userCommand.equals("put"))
				{
					if (put_one(userArg.toString().trim())) {
						//System.out.println("send file "+userArg.toString().trim()+" with success!");
						result=true;
					} else {
						System.out.println("Server encountered an error");
					}
				}
				else if(userCommand.equals("get"))
				{
					if (get(userArg.toString().trim())) {
						//System.out.println("send file "+userArg.toString().trim()+" with success!");
						result=true;
					} else {
						System.out.println("Server encountered an error");
					}
				}
			}
			
			return result;
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

	private static boolean put_one(String fileName) throws IOException {	
		boolean result = false;
		String fname = fileName.trim();
		File inFile = new File("/data/data/com.example.hello/files/"+fname);
		LineNumberReader  lnr = new LineNumberReader(new FileReader(inFile));
		lnr.skip(Long.MAX_VALUE);
		lnr.close();
		if(inFile.length()!=0)
		{
			ctrlWriter.println("PUT " + fileName);
			InputStream fileStream;		
			try {
				fileStream = new FileInputStream(inFile);
				dataWriter.println(lnr.getLineNumber() + 1);
				int recv;		
				int times=0;
				while ((recv = fileStream.read(buff, 0, buff.length)) > 0) {
					dataOs.write(buff,0,recv);
					times++;
					System.out.println("times "+times);
				}
				dataOs.flush();
				fileStream.close();
				ctrlWriter.println(" finish");
				System.out.println("sent file " + fname);
				if (ctrlScanner.next().equals("OK")) {
					System.out.println("send file "+fileName+" to server with success!");
					result = true;
				} 
			
			} catch (IOException e) {
				System.out.println("Error receiving file." + e);
			}			
		
		}
		else
		{
			result=do_touch(fileName);
			if(result)
			{
				System.out.println("send file "+fileName+" to server with success!");
			}
		}
		return result;
	}
	
	
	private static boolean get(String fileName) {
		boolean result = false;
		File outFile = new File("/data/data/com.example.hello/files/"+fileName);
		try {
			if(outFile.exists()) {
			outFile.delete();
			}
			outFile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(outFile);
			
			ctrlWriter.println("GET " + fileName);
			if(ctrlScanner.hasNext())
			{
				String haveOrNot=ctrlScanner.next().trim();
				
				if(haveOrNot.equals("no"))
				{
					System.out.println("server has no file named "+fileName);
					
				}
				else if(haveOrNot.equals("yes"))
				{
					long size = dataScanner.nextLong();
					long len = 0;
					int recv = 0;
					int times=0;
					if (size > 0) {
						while (len<size) {
							recv = dataIs.read(buff,0,buff.length);
							if(recv!=-1)
							{
								len += recv;
							}
							fileOutputStream.write(buff,0,recv);
							times++;
							System.out.println("times "+times);
						}
					}
					
					fileOutputStream.close();
					if(ctrlScanner.hasNext())
					{
						if (ctrlScanner.next().trim().equals("OK")) {
							System.out.println("Received file " + fileName);
							result = true;
						} else {
							outFile.delete();
						}
					}
				}
				else {
					System.out.println("提示信息出问题了");
				}
			}
		} catch (IOException e) {
			System.out.println("Error while runing get: " +e);
		}
		
		return result;
	}
	

	
}
