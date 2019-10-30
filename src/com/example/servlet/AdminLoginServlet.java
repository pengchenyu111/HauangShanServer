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
//		设置字符格式，要养成好习惯
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

//		获得url中的adminAccount 和 adminPassword 的值
        String adminAccount = request.getParameter("adminAccount");
        String adminPassword = request.getParameter("adminPassword");

        String resultCode = "";
        String resultMessage = "";

        if (adminAccount == null || adminPassword == null){
            resultCode = "003";
            resultMessage = "账号或密码不能为空";
        }else if (adminAccount.indexOf("or") != -1){
            //防止SQL注入
            resultCode = "004";
            resultMessage = "账号含有非法字符";
        }else if (adminPassword.indexOf("or") != -1){
            //防止SQL注入
            resultCode = "005";
            resultMessage = "密码含有非法字符";
        }else{
            try {
//			建立连接并查询数据库
                Connection connection = DBUtil.getConnect();
                Statement statement = connection.createStatement();
                ResultSet resultSet;

                String sql = String.format("select * from %s where adminAccount='%s' and adminPassword='%s'", DBUtil.TABLE_ADMIN,adminAccount,adminPassword);
                System.out.println(sql);

                resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    resultCode = "001";
                    resultMessage = "登录成功";
                }else {
                    resultCode = "002";
                    resultMessage = "账号或密码不正确";
                }
                DBUtil.close(connection, statement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



//		用map封装需要返回的数据
        Map<String, String> map = new HashMap<>();
        map.put("resultCode", resultCode);
        map.put("resultMessage", resultMessage);

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

        String adminAccount = null;
        String adminPassword = null;
        String[] temp;

//		获取传过来的map
//		注意: getParameterMap()方法返回的map的格式是Map<String,String[]>，因为一个key 可能对应多个value值，所以用了个String[]
//		注意： keySet()方法获得key集合并不是严格按照map中的key的顺序来的，所以下面要精确判断，不能主观地按照map中key的顺序；
        Map<String,String[]> data = request.getParameterMap();
        Set<String> keys = data.keySet();
        for(String key : keys) {
            String k  = key;
            temp = data.get(key);
            if("adminAccount".equals(k)) {
                adminAccount = temp[0];
            }else if("adminPassword".equals(k)) {
                adminPassword = temp[0];
            }
        }
        System.out.println("adminAccount:"+adminAccount+","+"adminPassword:"+adminPassword);

        String resultCode = "";
        String resultMessage = "";

        if (adminAccount == null || adminPassword == null){
            resultCode = "003";
            resultMessage = "账号或密码不能为空";
        }else if (adminAccount.indexOf("or") != -1){
            //防止SQL注入
            resultCode = "004";
            resultMessage = "账号含有非法字符";
        }else if (adminPassword.indexOf("or") != -1){
            //防止SQL注入
            resultCode = "005";
            resultMessage = "密码含有非法字符";
        }else{
            try {
                Connection connection = DBUtil.getConnect();
                Statement statement = connection.createStatement();

                String sql = String.format("select * from %s where adminAccount='%s' and adminPassword='%s'", DBUtil.TABLE_ADMIN,adminAccount,adminPassword);
                System.out.println(sql);

                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    resultCode = "001";
                    resultMessage = "登录成功";
                }else {
                    resultCode = "002";
                    resultMessage = "账号或密码不正确";
                }
                DBUtil.close(connection, statement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("resultCode", resultCode);
        map.put("resultMessage", resultMessage);


        Gson gson = new Gson();
        String respData = gson.toJson(map);

        PrintWriter pw = response.getWriter();
        pw.append(respData);
        pw.flush();

    }

}
