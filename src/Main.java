import java.io.IOException;


public class Main {
	// Boolean: get further information by user if true 
	public static boolean GET_USER_INFO = false;

	public static void main(String[] args) throws IOException {
		if (args.length > 0 && args[0].equals("setInfo")){
			GET_USER_INFO = true;
		}
		IM2HeaderMapper.MakeOutputDirectories();
		IM2HeaderMapper.SearchThroughDirectory();
	}
	
}
