#include <iostream>
#include <queue>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <mutex>
#include <ctime>
#include <condition_variable>

using namespace std;

// see https://cpluspluspedia.com/en/tutorial/9785/semaphore is the reference here
class Semaphore {
public:
    Semaphore (int count_ = 0)
    {
			this->count = count_; // use arrow operator for accessign members of a struct via ptr
    }
    
    inline void notify( int tid ) { // keeps these methods atomic?
        std::unique_lock<std::mutex> lock(mtx);
        count++;
        cv.notify_one();
    }
    inline void wait( int tid ) {
        std::unique_lock<std::mutex> lock(mtx);
        while(count == 0) {
            cv.wait(lock);
        }
        count--;
    }
private:
    std::mutex mtx;
    std::condition_variable cv;
    int count;
};

// Requests have and ID and random length of "execution time"
class Request
{
	int request_ID;
	public:
	int length = rand() % 10 +1; //random length 1-10s
	Request(int r);
	int getID();
};

Request::Request(int r){
		request_ID = r;
}

int Request::getID(){
	return request_ID;
}

//global variables (shared between threads)
int QUEUE_SIZE = 5;
Semaphore S(1); //while more is tempting, multple threads should not be able to access queue simultaneously
queue<Request> request_queue; //request queue

//sleep a thread for a random amount of time
void sleep_master()
{
	sleep(rand() % 5 + 1); // sleep randomly for 1-5s
}

//slaves(i.e. consumers) enter here
void *thread_routine(void *id)
{
	time_t fin_time;
	//printf("Thread #%ld entered\n", (long)id);
	while(true){
		S.wait((long)id);
		if(!request_queue.empty()){
			Request top = request_queue.front();
			request_queue.pop();
			cout << "Consumer " << (long)id << ": assigned Req: " << top.getID() << " for " << top.length << "s" << endl;
			S.notify((long)id);
			sleep(top.length);
			fin_time = time(0);
			cout << "Consumer " << long(id) << ": completed Req: " << top.getID() << " at t=" << ctime(&fin_time) << endl;
		}else{
			S.notify((long)id);
		}
	}	

	//pthread_exit(NULL);//kill - debug
}

//main entry point
int main()
{
	int N; //number of slave threads/request queue size
	cout << "Input amount of Slave threads: ";
	cin >> N;

	int ID[N]; //thread ids
	pthread_t threads[N]; //threads
	
	//generate slaves
	for(int i=0; i < N; i++){
		ID[i] = pthread_create(&threads[i], NULL, thread_routine, (void*)i);
	}
	
	int count = 0;
	time_t curr_time;
	//generate requests
	while(true){
		if(request_queue.size() != QUEUE_SIZE){ //arbitrary size for thread
			Request next_request(count);
			curr_time = time(0);
			cout << "Producer: New request ID: " << next_request.getID() << ", L= " << next_request.length << " time:" << ctime(&curr_time) << endl;
			request_queue.push(next_request);
			count++;
		}
		//sleep
		sleep_master();
	}

	return 0;
}









