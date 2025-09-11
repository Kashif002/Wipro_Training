package com.Thread;

class UploadFile extends Thread{
	String username;
	
	public UploadFile(String username) {
		this.username = username;
	}
	
	public void run() {
		System.out.println(username + ". PLease upload the file");
		try {
			Thread.sleep(2000);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(username + " uploaded the file");
	}
}

public class Task1 {
	public static void main(String[] args) throws InterruptedException {
		UploadFile threadA = new UploadFile("Kashif");
		UploadFile threadB = new UploadFile("Sana");
		
		threadA.start();
		threadA.join();
		threadB.start();
		threadB.join();
		
//		try {
//			threadA.join();
//			threadB.join();
//		}
//		catch(InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("Task completed");
	}
}