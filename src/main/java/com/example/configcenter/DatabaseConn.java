package com.example.configcenter;

import java.sql.*;

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
29                 System.out.println("Succeeded connecting to the Database!");		  
			//查询
			ps = ct.prepareStatement("select * from service_table");
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
				if(ct != null)
				{
					ct.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			
		}
		
	}
}
————————————————
版权声明：本文为CSDN博主「wtu刘猛」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_38588973/java/article/details/78544380