package com.example.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.utils.DBUtil;
import com.google.gson.Gson;




/**
 * 处理管理员的登录
 */
@WebServlet(
        name = "AdminLoginServlet",
        urlPatterns = {"/AdminLoginServlet"}
)
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminLoginServlet() {
        super();
    }


//  	GET请求方式通过url传递参数
//  	相比POST，GET地效率更高
//  	GET多用于查询
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		设置字符格式，要养成号习惯
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

//		获得url中的account 和 password 的值
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        String resCode = "";
        String resMsg = "";

        try {
//			建立连接并查询数据库
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            String sql = "select * from "+DBUtil.TABLE_USER+" where userAccount='"+account+"' and userPassword='"+password+"'";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                resCode = "200";
                resMsg = "OK";
            }else {
                resCode = "201";
                resMsg = "账号或密码不正确";
            }
            DBUtil.close(connection, statement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//		用map封装需要返回的数据
        Map<String, String> map = new HashMap<>();
        map.put("resCode", resCode);
        map.put("resMsg", resMsg);

//		将map转化为json数据格式，这里用到了GSON 第三方包
        Gson gson = new Gson();
        String respData = gson.toJson(map);

//		将最终的结果返回
        PrintWriter pw = response.getWriter();
        pw.append(respData);
        pw.flush();

    }


//  	POST把数据放在HTTP的包体内（request body）来传递
//  	POST多用于修改数据
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String account = null;
        String password = null;
        String[] temp;

//		获取传过来的map
//		注意: getParameterMap()方法返回的map的格式是Map<String,String[]>，因为一个key 可能对应多个value值，所以用了个String[]
//		注意： keySet()方法获得key集合并不是严格按照map中的key的顺序来的，所以下面要精确判断，不能主观地按照map中key的顺序；
        Map<String,String[]> data = request.getParameterMap();
        Set<String> keys = data.keySet();
        for(String key : keys) {
            String k  = key;
            temp = data.get(key);
            if("account".equals(k)) {
                account = temp[0];
            }else if("password".equals(k)) {
                password = temp[0];
            }
        }
        System.out.println("account:"+account+","+"password:"+password);

        String resCode = "";
        String resMsg = "";

        try {
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();

            String sql = String.format("select * from %s where userAccount='%s' and userPassword='%s'", DBUtil.TABLE_USER,account,password);
            System.out.println(sql);

            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                resCode = "200";
                resMsg = "OK";
            }else {
                resCode = "201";
                resMsg = "账号或密码不正确";
            }
            DBUtil.close(connection, statement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("resCode", resCode);
        map.put("resMsg", resMsg);


        Gson gson = new Gson();
        String respData = gson.toJson(map);

        PrintWriter pw = response.getWriter();
        pw.append(respData);
        pw.flush();



    }

}
