package CPP;

import java.lang.Thread;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class main{

	public static void main(String[] args) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		int N;
		MyQueue request_queue = new MyQueue();
		System.out.println("Input amount of Slave threads: ");
		N = in.nextInt();
		int ID[] = new int[N];
		sThread threads[] = new sThread[N];
		
		
		
		for(int i = 0; i<N; i++) {
			ID[i] = i;
		}
		//generate slaves
		for (int id : ID){
			threads[id] = new sThread(request_queue.front());
			threads[id].thread_routine(id, request_queue);
			//sleep
			sleep_master();
		}
		int max;
		System.out.println("Intput number of jobs done: ");
		max = in.nextInt();
		mThread master = new mThread(max);
		int count = 0;
		
		//generate requests
        while(count<max) { 
            master.generateThreads(request_queue, count);
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
    private Request arr[];          // array to store queue elements
    public Request front;          // front points to front element in the queue
    private int rear;           // rear points to last element in the queue
    private int capacity;       // maximum capacity of the queue
    private int count;          // current size of the queue
    
    public synchronized void push(Request re) {
    	arr[rear+1]=re;
    	count++;
    }
    public synchronized void dequeue() {
        // check for queue underflow
        if (empty()){
            System.out.println("UnderFlow\nProgram Terminated");
            System.exit(1);
        }
 
        System.out.println("Removing " + front);
 
        front = arr[count+1];
        count--;
    }
    
    public synchronized Boolean empty(){
        return (count == 0);
    }	
    public synchronized int size() {
    	return count;
    }
	public Request front() {
		return front;
	}
	public int rear() {
		return rear;
	}   
}

class sThread implements Runnable{
	private Request re;
	public sThread(Request re) {
		this.re = re;
	}
	public static void thread_routine(int id, MyQueue request_queue) throws InterruptedException{
		//Print "thread *ID* entered"
		System.out.println("thread " + id + " entered");
		while(true) {
			if(!request_queue.empty()) {
				Request top = request_queue.front();
				request_queue.dequeue();
				System.out.println("Consumer " + id + ": assigned Req: " + top.getID() + " for " 
						+ top.getLength() + "s");
				//S.notify(id);
				Thread.sleep(top.getLength());
				System.out.println("Consumer " + id + ": completed Req: " + top.getID());
			}	
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
}

class mThread {
	
	private int max;
	
	public mThread(int max) {
		this.max = max;
	}
	
	void generateThreads(MyQueue q, int count) {
		long curr_time = System.currentTimeMillis();
		if(q.size() != 5) { //arbitrary thread size
            int rlength = (int)(1+Math.random()*10); // random length between 1-10s
            Request next_request = new Request(count, rlength);
            System.out.println("Producer: New request ID: " + next_request.getID() 
                + ", L= "+next_request.getLength()+ "time: " + curr_time);
            q.push(next_request);
            count++;
        }
	}
	
}
