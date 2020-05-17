package com.example.configcenter;

import java.sql.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/get")
class JdbcConnection
{	
	Connection conn = null;
	
	//创建一个用于发送sql语句的对象
	PreparedStatement ps = null;
	//创建一个用于接收结果集的对象
	ResultSet rs = null;
	//默认构造函数
	
	
	public JdbcConnection()
	{
		try {
			//加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			//得到连接
			conn = DriverManager.getConnection
				("jdbc:mysql://10.109.252.84:3306/service_database?user=root&password=123456" );   
			if(!conn.isClosed())
                 System.out.println("Succeeded connecting to the Database!");		  
			//查询
			ps = conn.prepareStatement("select * from service_table");
			//得到结果
			rs = ps.executeQuery();
			//循环输出
			while(rs.next())
			{
				String a = rs.getString(1);
				String b = rs.getString(2);
				System.out.println(a + " " + b);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("noooooo!!!");
			e.printStackTrace();
		}finally
		{
			try {
				//关闭资源
				if(rs != null)
				{
					rs.close();
				}
				if(ps != null)
				{
					ps.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			
		}
	}
	
	@RequestMapping(value = "/connectDB", method = RequestMethod.GET)
	@ResponseBody
	public String connectDatabase()
	{
		JdbcConnection jdbcConnect = new JdbcConnection();
		return "true";
	}
}