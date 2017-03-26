package test;

import java.util.Random;

import com.user.model.User;
import com.user.util.UserFactory;

public class testAddData {
	static String[]  userXing={"张","贾","袁","宋","赵","王","司","姚","李"};
	static String[]  userMing={"飞","博","波","磊","阔","阳","保","华","薇","航","铎"};
	
	public static void main(String[] args) {
		
		
     for (int i = 0; i < 100; i++) {
    	 User  user=new  User("user"+i,"123","AAA@aaa.com","shijiazhuang","99998888");
    	 UserFactory.getUserDao().add(user);
    	 
	}
     System.out.println("完成");
		
		
		
		
	}
	
	public  static  String  getUsername(){
		
		Random  random=new Random();
		int xing=  random.nextInt(userXing.length);
		int  ming=random.nextInt(userMing.length);
		
		String  username=userXing[xing]+userMing[ming];
		
		return  username;
	}
	

}
