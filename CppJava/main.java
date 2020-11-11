package CPP;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;


public class main{

	public static void main(String[] args) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		int N;
		
		System.out.println("Input amount of Slave threads: ");
		N = in.nextInt();
		sThread threads[] = new sThread[N];
		
		System.out.println("Input number of jobs to be done: ");
		int max;
		max = in.nextInt();
		
		
		MyQueue request_queue = new MyQueue(max);
		mThread master = new mThread();
		int count = 0;
		for(int j = 0; j<N; j++) {
			threads[j] = new sThread(j);
		}

		//generate requests

		for(int i=0; i<max; i++) {
        	request_queue = master.generateRequests(request_queue, count);
        	count++;
		}
        	// run requests
        		int index = 0;
        		while(!request_queue.empty()){
        			for(sThread thread: threads) {
        				thread.thread_routine(thread.getId(), request_queue);
        			}
        		}
        	
        		
		
     }
		
	public static void sleep_master() throws InterruptedException {
		Thread.sleep((int)(1 + Math.random() * 5)); //sleep randomly for 1-5 seconds
	}
}
	
class Request {

	    private int request_ID;
	    private int length; 

	    public Request(int r, int l) {
	        request_ID = r;
	        length = l;
	    }

	    public int getID() {
	        return request_ID;
	    }
	    public int getLength() {
	        return length;
	    }
	}

	class MyQueue{
	    private ArrayList<Request> arr = new ArrayList<Request>();          // array to store queue elements
	    private int count=arr.size();          // current size of the queue
	    //public Request front= arr.get(0);          // front points to front element in the queue
	    //private Request rear= arr.get(count);           // rear points to last element in the queue
	    MyQueue(int max){
	    	this.arr = new ArrayList<Request>(max);
	    }
	    public synchronized void push(Request re) {
	        arr.add(re);
	    }
	    public synchronized Request dequeue() {
	        // check for queue underflow
	        if (empty()){
	            System.out.println("UnderFlow\nProgram Terminated");
	            System.exit(1);
	        }
	        Request r = arr.get(0);
	        arr.remove(0);
	        return r;
	    }

	    public synchronized Boolean empty(){
	        return (arr.size() == 0);
	    }
	    public synchronized int size() {
	        int count = 0;
	        for (Request r: this.arr){
	        	count++;
	        }
	        return count;
	    }
	    public synchronized Request front() {
	        return arr.get(0);
	    }
	    public synchronized Request rear() {
	        return arr.get(count-1);
	    }
	}

class sThread implements Runnable{
	int id;
	sThread(int id){
		this.id = id;
	}

	void setId(int id) {
		this.id=id;
	}
	int getId() {
		return this.id;
	}
	public static MyQueue thread_routine(int id, MyQueue request_queue) throws InterruptedException{
		//Print "thread *ID* entered"
		System.out.println("thread " + id + " entered");
		Request top = request_queue.dequeue();
		System.out.println("Consumer " + id + ": assigned Req: " + top.getID() + " for " + top.getLength() + "s");
		Runnable runnable = new mThread();
		Thread thread=new Thread(runnable);
		thread.start();
		Thread.sleep(top.getLength());
		System.out.println("Consumer " + id + ": completed Req: " + top.getID());
		return request_queue;
	}

	@Override
	public void run() {
        System.out.println("Inside slave : " + Thread.currentThread().getName());
	}
}

class mThread implements Runnable{
	
	private int max;
	
	static MyQueue generateRequests(MyQueue q, int count) {
		long curr_time = System.currentTimeMillis();
        int rlength = (int)(1+Math.random()*10); // random length between 1-10s
        Request next_request = new Request(count, rlength);
        q.push(next_request);
        System.out.println("Producer: New request ID: " + next_request.getID() + ", L= "+next_request.getLength()+ " time: " + curr_time);
        count++;
		return q;
	}	
    @Override
    public void run() {
    }
}	
	
