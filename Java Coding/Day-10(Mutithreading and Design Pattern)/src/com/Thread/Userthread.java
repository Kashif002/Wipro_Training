package com.Thread;

class BillingSystem{
private boolean isBilled = false;

public synchronized void billUser(String user) {
System.out.println("Billing started " +user);
try {
Thread.sleep(2000);
}catch(InterruptedException e) {
e.printStackTrace();
}
isBilled=true;
System.out.println("Billing is completed for " + user);
notify();
}

public synchronized void waitForBilling(String user) {
// TODO Auto-generated method stub
while(!isBilled) {
try {
wait();
}catch(InterruptedException e) {
e.printStackTrace();
}
}
System.out.println("billing done");
isBilled = false;

}
}
public class Userthread extends Thread{
private String username;
private BillingSystem billingSystem;
public Userthread(String username, BillingSystem billingSystem) {
super();
this.username = username;
this.billingSystem = billingSystem;
}

@Override
public void run() {
System.out.println(username + "is placing the order..");
try {
Thread.sleep(1000);
}catch(InterruptedException e) {
e.printStackTrace();
}
System.out.println(username + "has placed the order");

new Thread(() -> billingSystem.billUser(username)).start();

billingSystem.waitForBilling(username);
}


public static void main(String[] args) {
BillingSystem billingSystem = new BillingSystem();

System.out.println("Synchronous order ");
Userthread userA = new Userthread("User-A",billingSystem);
Userthread userB = new Userthread("User-B",billingSystem);
Userthread userC = new Userthread("User-C",billingSystem);

try {
userA.start();
userA.join();

userB.start();
userB.join();

userC.start();
userC.join();
}catch(InterruptedException e) {
e.printStackTrace();
}
System.out.println("All users have completed their order and billings");
}
}