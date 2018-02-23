package com.company;
import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    // 1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз, порядок должен быть именно ABСABСABС. Используйте wait/notify/notifyAll.
    // монитор для потока
    private final Object monitor = new Object();
    private final Object monitor2 = new Object();

    private  volatile int count = 0;
    private final int  count_end = 9;
    private  volatile char currentLetter = 'A';

    public static void main(String[] args) {
        Main m = new Main();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                m.PrintA();
            }
        });
        Thread t2 = new Thread(() -> m.PrintB());
        Thread t3 = new Thread(() -> m.PrintC());


      // Thread t5 = new Thread();

        t1.start();
        t2.start();
        t3.start();

       // 2. Написать совсем небольшой метод, в котором 3 потока построчно пишут данные в файл (штук по 10 записей, с периодом в 20 мс)

        Thread t4 = new Thread(() -> m.writeIntoFile("1.txt", "T4"));
        Thread t5 = new Thread(() -> m.writeIntoFile("1.txt", "T5"));
        Thread t6 = new Thread(() -> m.writeIntoFile("1.txt", "T6"));

        t4.start();
        t5.start();
        t6.start();

       // writeIntoFile("1.txt");


       /* 3. *** Написать класс МФУ, на котором возможны одновременная печать и сканирование документов,
                при этом нельзя одновременно печатать два документа или сканировать (при печати в консоль
        выводится сообщения "отпечатано 1, 2, 3,... страницы", при сканировании тоже самое только "отсканировано...", вывод в консоль все также с периодом в 50 мс.)
*/	// write your code here
    }

    synchronized  void writeIntoFile (String filename, String nameThread)
    {
       synchronized (monitor2) {
           try {
               FileWriter fw = new FileWriter(filename, true);
               for (int i = 1; i < 11; i++)
                   fw.write(i + " reader; ");
               fw.write("\n ---------------------- \n");
               fw.flush();
               TimeUnit.SECONDS.sleep(2);
               System.out.println("\n поток " + nameThread + " завершен \n ----------------");


           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }


    void  PrintA()
    {
        synchronized (monitor)
        {
            try {
                while (count!=count_end)
                {
                 //   while (currentLetter!='A')
                 //       monitor.wait();
                    if (currentLetter == 'A')
                    { System.out.print("А");
                    currentLetter = 'B';
                    count++;
                    monitor.notifyAll();
                    }else monitor.wait();
                 //   monitor.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void PrintB()
    {
        synchronized (monitor)
        {
            try {
                while (count!=count_end)
                {
                  //  while (currentLetter!='B')
                    //    monitor.wait();
                    if (currentLetter == 'B')
                    {System.out.print("B");
                    currentLetter = 'C';
                        count++;
                        monitor.notifyAll();
                    } else monitor.wait();
                 //   monitor.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    void PrintC()
    {
        synchronized (monitor)
        {
            try {

                while (count!=count_end)
                    {
                    //while (currentLetter!='C')
                    //    monitor.wait();
                    if (currentLetter == 'C')
                    { System.out.print("C");
                    currentLetter = 'A';
                        count++;
                   monitor.notifyAll();

                    } else monitor.wait();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
