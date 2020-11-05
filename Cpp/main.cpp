#include <iostream>
#include <queue>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <mutex>
#include <ctime>
#include <condition_variable>

using namespace std;

// see https://cpluspluspedia.com/en/tutorial/9785/semaphore -> TODO: implement our own this is just for reference
class Semaphore {
public:
    Semaphore (int count_ = 0)
    : count(count_) 
    {
    }
    
    inline void notify( int tid ) {
        std::unique_lock<std::mutex> lock(mtx);
        count++;
        //cout << "thread " << tid <<  " notify" << endl;
        //notify the waiting thread
        cv.notify_one();
    }
    inline void wait( int tid ) {
        std::unique_lock<std::mutex> lock(mtx);
        while(count == 0) {
            //cout << "thread " << tid << " wait" << endl;
            //wait on the mutex until notify is called
            cv.wait(lock);
            //cout << "thread " << tid << " run" << endl;
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

Semaphore S(3); //can we get this constructed more dynamically?
queue<Request> request_queue; //request queue

//sleep a thread for a random amount of time
void sleep_master()
{
	sleep(rand() % 5 + 1); // sleep randomly for 1-5s
}

//slaves(i.e. consumers) enter here
void *thread_routine(void *id)
{
	//printf("Thread #%ld entered\n", (long)id);
	while(true){
		S.wait((long)id);
		if(!request_queue.empty()){
			Request top = request_queue.front();
			request_queue.pop();
			cout << "Consumer " << (long)id << ": assigned Req: " << top.getID() << " for " << top.length << "s" << endl;
			sleep(top.length);
			cout << "Consumer " << long(id) << ": completed Req: " << top.getID() << endl;
		}
		S.notify((long)id);
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
		Request next_request(count);
		curr_time = time(0);
		cout << "Producer: New request ID: " << next_request.getID() << ", L= " << next_request.length << "time:" << ctime(&curr_time) << endl;
		request_queue.push(next_request);
		//sleep
		sleep_master();
		count++;
	}

	return 0;
}









