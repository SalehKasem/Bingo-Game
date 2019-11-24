package application;
	
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			ExecutorService ex = Executors.newCachedThreadPool();
			//getting parameters from cmd
			Parameters params = getParameters();                    
			List<String> list = params.getRaw();    
			//check for wrong input
			if(list.size()>2 || list.size()<2)
				throw new IOException();
			
			int arg1 = Integer.parseInt(list.get(0)); 
			int arg2 = Integer.parseInt(list.get(1)); 
			//check for wrong input
			if(arg1>7 || arg1 <5 || arg2 >12 || arg2<2)
				throw new IOException();
			
			//create new manager
			Manager manager = new Manager(arg1,arg2,stage);
			//Manager manager = new Manager(7,7,stage);
			ex.execute(manager);//execute the manager thread
			Scene scene = new Scene(manager);
			stage.setScene(scene);
			stage.show();
		}
		//catch(IOException e) {
			//e.printStackTrace();
		//}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
