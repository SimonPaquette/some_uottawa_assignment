/**
 * Simon Paquette
 * 300044038
 * CSI 3531
 * Devoir 3
 */


import java.util.concurrent.Semaphore;

/**
 * The Sleeping Teaching Assistant Problem
 * The main class to run the program
 */
public class SleepingTA {

    //Variable settings for the simulation
    public static final int NUMBER_OF_STUDENTS = 16;
    public static final int TIME_MIN_PROG = 2000;
    public static final int RANDOM_PROG = 20000;
    public static final int TIME_MIN_HELP = 500;
    public static final int RANDOM_HELP = 1000;

    /**
     * Execute The Sleeping Teaching Assistant Problem
     * @param args 
     */
    public static void main(String[] args) {
        
        //Create TA
        TA teacher = new TA();

        //Create n Students
        Student[] students = new Student[SleepingTA.NUMBER_OF_STUDENTS];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(teacher);
        }
        
        //Start all threads
        teacher.start();
        for (Student s : students) {
            s.start();
        }
    }

    /**
     * Sleep for a undetermined time  
     * @param init Minimal time to sleep
     * @param rand Add a random time
     * @throws InterruptedException
     */
    public static void rand_wait(int init, int rand) throws InterruptedException {

        //Calculate total time to sleep
        int time = (int) (rand * Math.random());
        Thread.sleep(init+time);     
    }
}

/**
 * Student thread that do programmation and seeking help to his TA
 */
class Student extends Thread {

    //Student informations
    private static int new_id = 0;
    private int id;
    private TA teacher;

    /**
     * Student thread associated with his TA
     * @param teacher TA 
     */
    public Student(TA teacher) {
        this.id = new_id++;
        this.teacher = teacher;
    }

    /**
     * 
     */
    public String toString() {
        return "Student-#"+this.id;
    }

    /**
     * 
     */
    public void run() {
        System.out.println(this.toString() + " thread has started.");

        while (true) {

            //Student doing programmation for a random time
            try {
                SleepingTA.rand_wait(SleepingTA.TIME_MIN_PROG, SleepingTA.RANDOM_PROG);
            } 
            catch (InterruptedException e) { 
                break;
            }
		    
            //Student seeking help
            System.out.println(this.toString() + " is seeking help.");

            //TA is sleeping
            if (teacher.sleep.availablePermits() == 0) {

                //Student waking up the TA
                System.out.println(this.toString() + " wakes up the TA.");
                teacher.sleep.release();
                
                //Student entering in the office to get help
                try {
                    teacher.office.acquire();
                    teacher.help(this);
                }
                catch (InterruptedException e) { 
                    break; 
                }
            }

            //TA is wake up
            else if (teacher.sleep.availablePermits() == 1) {

                //Student wait in the hallway if a chair is available
                if (teacher.hallway.tryAcquire()) {
                    System.out.println(this.toString() + " is waiting in the hallway.");

                    //Student may enter in the office when the TA is available
                    try {
                        teacher.office.acquire();
                        teacher.hallway.release();
                        teacher.help(this);
                    }
                    catch (InterruptedException e) { 
                        break; 
                    }
                }

                //Student will come back later because no chair available
                else {
                    System.out.println(this.toString() + " will come back later.");
                }             
            }
        }
    }    
}

/**
 * TA thread that may sleep or help students
 */
class TA extends Thread {

    //Semaphore for the simulation
    public Semaphore sleep;
    public Semaphore office;
    public Semaphore hallway;

    /**
     * TA thread creating semaphore
     */
    public TA() {
        sleep = new Semaphore(0,true);
        office = new Semaphore(1,true);
        hallway = new Semaphore(3,true); 
    }

    /**
     * 
     */
    public void run() {
        System.out.println("TA thread has started.");
    }

    /**
     * Help a Student thread in the office and check for other students in the hallway
     * @param student To be helped in the office
     * @throws InterruptedException
     */
    public void help(Student student) throws InterruptedException {

        //Random wait for helping a student
        System.out.println(student.toString() + " is being helped by the TA.");
        SleepingTA.rand_wait(SleepingTA.TIME_MIN_HELP, SleepingTA.RANDOM_HELP);
        System.out.println(student.toString() + " exits the office.");

        //Available to get new student
        office.release();

        //No new students, may takes a nap
        if (hallway.availablePermits() == 3) {
            sleep.acquire();
            System.out.println("TA takes a nap.");
        }
    }
}