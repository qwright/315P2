#include <iostream>
#include <queue>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <mutex>
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
        cout << "thread " << tid <<  " notify" << endl;
        //notify the waiting thread
        cv.notify_one();
    }
    inline void wait( int tid ) {
        std::unique_lock<std::mutex> lock(mtx);
        while(count == 0) {
            cout << "thread " << tid << " wait" << endl;
            //wait on the mutex until notify is called
            cv.wait(lock);
            cout << "thread " << tid << " run" << endl;
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
	int length = rand() % 10 +1; //random length 1-10s
	Request(int r){
		request_ID = r;
	}
};

//sleep a thread for a random amount of time
void sleep_thread()
{
	sleep(rand() % 3 +1); // sleep randomly for 1-3s
}

void *thread_routine(void *id)
{
	printf("Thread #%ld entered\n", (long)id);
	pthread_exit(NULL);//kill - debug
}

//main entry point
int main()
{
	int N; //number of slave threads/request queue size
	cout << "Input amount of Slave threads: ";
	cin >> N;

	queue<int> request_queue; //request queue
	int ID[N]; //thread ids
	pthread_t threads[N]; //threads
	
	//generate slaves
	for(int i=0; i < N; i++){
		ID[i] = pthread_create(&threads[i], NULL, thread_routine, (void*)i);
	}

	return 0;
}
