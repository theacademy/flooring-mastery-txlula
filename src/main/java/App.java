import com.sg.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
//        appContext.scan("com.sg.flooringmastery");
//        appContext.refresh();

        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringMasteryController controller = appContext.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
}
