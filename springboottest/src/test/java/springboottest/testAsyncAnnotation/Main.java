package springboottest.testAsyncAnnotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.huisir.springboot.demo.async.Task;

public class Main {  
  
     public static void main(String[] args) {  
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();  
          Task service = context.getBean(Task.class);  
  
          for (int i = 0; i < 10; i++) {  
              try {
				service.doTaskOne();
				service.doTaskTwo();  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 执行异步任务  
          }  
          context.close();  
     }  
}  