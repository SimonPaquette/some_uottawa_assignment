/**
 * Simon Paquette
 * 300044038
 * CSI 3531
 * Devoir 4
 * Question 3
 *
 * gcc producer.c -o producer -lrt
 * producer
 * gcc consumer.c -o consumer -lrt
 * consumer
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>

//math factorial to be used for catalan
int factorial(int n) {
    if (n == 0) {
        return 1;  
    }
    else {
        return(n * factorial(n-1));
    }
}

int catalan(int n) {
    return (factorial(2*n) / (factorial(n+1) * factorial(n)));
}


int main(int argc, char *argv[]) {
    
    //shared memory location
	const int SIZE = 4096;
	const char *name = "Catalan";

	int shm_fd;
	void *ptr;

    //need an argument
    if( argc == 2 ) {
        
        /* create the shared memory segment */
        shm_fd = shm_open(name, O_CREAT | O_RDWR, 0666);

        /* configure the size of the shared memory segment */
        ftruncate(shm_fd,SIZE);
        
        /* now map the shared memory segment in the address space of the process */
        ptr = mmap(0,SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
        if (ptr == MAP_FAILED) {
            printf("Map failed\n");
            return -1;
        }
        /**
        * Now write to the shared memory region.
        *
        * Note we must increment the value of ptr after each write.
        */
        char str[128] = "";
        int n = atoi(argv[1]);

        for (int i = 1; i <= n; i++) {
            
            int c = catalan(i);

            //convert number and add to final string
            char temp[16];
            sprintf(temp,"%d",c);

            char space[2] = " ";
            strcat(str, space);
            strcat(str, temp);
        }

        sprintf(ptr,"%s",str);
        ptr += strlen(str);
    } 
    
    else {
        printf("Need one argument to start the process for catalan number.\n");
    }

	return 0;
}
