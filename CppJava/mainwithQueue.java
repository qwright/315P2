

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int N;
		Semaphore S = new Semaphore(1);
		MyQueue request_queue = new MyQueue(); //debug
		System.out.println("Input amount of Slave threads: ");
		N = in.nextInt();
		
		int ID[] = new int[N];
		//pthread_t threads[N];
		
		//generate slaves
		for(int i = 0; i<N; i++) {
			//ID[i] = pthread_create(&threads[i], NULL, thread_routine, (void*)i);
		}
		
		int count = 0;
		long curr_time = System.currentTimeMillis();
		
		//generate requests
		while(true) { 
			if(request_queue.size() != 5) { //arbitrary thread size
				Request next_request = new Request(count);
				System.out.println("Producer: New request ID: " + next_request.getID() + ", L= " /*+ next_request.length */+ "time: " + curr_time);
				(request_queue).push(next_request);
				count++;
			}}
			//sleep
			//sleep_master()
		}

	
	void sleep_master() {
		sleep((int)(1 + Math.random() * 5)); //sleep randomly for 1-5 seconds
	}
	
	void sleep(int i){
		
	}
	
	void thread_routine(int id, Semaphore S, MyQueue request_queue) {
		//Print "thread *ID* entered"
		while(true) {
			try {
				S.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!request_queue.empty()) {
				Request top = request_queue.front;
				request_queue.dequeue();
				System.out.println("Consumer " + id + ": assigned Req: " + top.getID() + " for " 
						+ top.length + "s");
				S.notify(id);
				sleep(top.length);
				System.out.println("Consumer " + id + ": completed Req: " + top.getID());
			}
			else S.notify(id);
		}
		//pthread_exit(NULL); //kill
	}
	

}
class MyQueue{
	    private Request arr[];          // array to store queue elements
	    public Request front;          // front points to front element in the queue
	    private int rear;           // rear points to last element in the queue
	    private int capacity;       // maximum capacity of the queue
	    private int count;          // current size of the queue
	    
	    public void push(Request re) {
	    	arr[rear+1]=re;
	    	count++;
	    }
	    public void dequeue() {
	        // check for queue underflow
	        if (empty()){
	            System.out.println("UnderFlow\nProgram Terminated");
	            System.exit(1);
	        }
	 
	        System.out.println("Removing " + front);
	 
	        front = arr[count+1];
	        count--;
	    }
	    
	    public Boolean empty(){
	        return (count == 0);
	    }	
	    public int size() {
	    	return count;
	    }
	    
	
}
class Request {

	private int request_ID;
	int length = (int)(1+Math.random()*10); // random length between 1-10s
	
	public Request() {
		
	}

	public Request(int r) {
		request_ID = r;
	}
	public int getID() {
		return request_ID;
	}
		
}

class Semaphore {

	private int count;
	//private mutex mtx
	//private condition_variable cv
	
	public Semaphore(int count) {
		this.count = count;
	}
	
	void notify(int tid) {
		
		
		count++;
	}	
	void wait(int tid) {
		// std::unique_lock<std::mutex> lock(mtx);
		while(count == 0) {
			System.out.println("thread " + tid + " wait");
			
			//wait on the mutex until notify is called
			//cv.wait(lock);
			System.out.println("thread " + tid + " run");
		}
		
		count--;
	}

}