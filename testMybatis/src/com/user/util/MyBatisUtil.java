package com.user.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
public class MyBatisUtil {
	private static SqlSessionFactory sessionFactory=null;
	private  static InputStream  inputStream=null;
	private  MyBatisUtil(){
		
	}
		public static  SqlSession  getSession(){

			try {
				if(sessionFactory==null){
				inputStream=Resources.getResourceAsStream("mybatis-config.xml");
				sessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(inputStream!=null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			return  sessionFactory.openSession();
		}
}
