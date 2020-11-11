package RoundG;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;


public class main{

	public static void main(String[] args) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		int N;
		
		System.out.println("Input amount of Slave threads: ");
		N = in.nextInt();
		int ID[] = new int[N];
		sThread threads[] = new sThread[N];
		
		System.out.println("Input number of jobs to be done: ");
		int max;
		max = in.nextInt();
		
		
		//generate slaves
		MyQueue request_queue = new MyQueue(max);
		int idCount = 0;
		
		mThread master = new mThread(max);
		int count = 0;
		int jobsToDo = max;
		//generate requests

		for(int i=0; i<N; i++) {
        	generateThreads(request_queue, count);
        	count++;
		}
			
        	
        		int index = 0;
        		while(request_queue.size() != 5) {
        			thread_routine(index, request_queue);
        			index++;
        	}
        	count++;
     }
	public static void thread_routine(int id, MyQueue request_queue) throws InterruptedException{
		//Print "thread *ID* entered"
		System.out.println("thread " + id + " entered");
		while(true) {
				Request top = request_queue.front();
				request_queue.dequeue();
				System.out.println("Consumer " + id + ": assigned Req: " + top.getID() + " for " 
						+ top.getLength() + "s");
				Thread.sleep(top.getLength());
				System.out.println("Consumer " + id + ": completed Req: " + top.getID());
			
		}
	}
	static void generateThreads(MyQueue q, int count) {
		long curr_time = System.currentTimeMillis();
		if(q.size() != 5) { //arbitrary thread size
            int rlength = (int)(1+Math.random()*10); // random length between 1-10s
            Request next_request = new Request(count, rlength);
            q.push(next_request);
            System.out.println("Producer: New request ID: " + next_request.getID() 
                + ", L= "+next_request.getLength()+ " time: " + curr_time);
            count++;
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
	    public synchronized void dequeue() {
	        // check for queue underflow
	        if (empty()){
	            System.out.println("UnderFlow\nProgram Terminated");
	            System.exit(1);
	        }
	        arr.remove(0);
	    }

	    public synchronized Boolean empty(){
	        return (count == 0);
	    }
	    public synchronized int size() {
	        return count;
	    }
	    public synchronized Request front() {
	        return arr.get(0);
	    }
	    public synchronized Request rear() {
	        return arr.get(count);
	    }
	}

class sThread implements Runnable{
	private Request re;
	int id;
	public sThread() {
		this.re = null;
		id = 0;
	}
	public sThread(Request re) {
		this.re = re;
	}
	void setId(int id) {
		this.id=id;
	}
	int getId() {
		return this.id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}

class mThread implements Runnable{
	
	private int max;
	
	public mThread(int max) {
		this.max = max;
	}
	

	public static void main(String[] args) {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Runnable...");
        Runnable runnable = new mThread(5);

        System.out.println("Creating Thread...");
        Thread thread = new Thread(runnable);

        System.out.println("Starting Thread...");
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());
    }
}	
	