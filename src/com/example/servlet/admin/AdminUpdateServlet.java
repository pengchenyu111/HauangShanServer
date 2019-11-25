package com.example.servlet.admin;

import com.example.pojo.DailyNum;
import com.example.utils.DBUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(
        name = "AdminUpdateServlet",
        urlPatterns = {"/AdminUpdateServlet"}
)
public class AdminUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获取参数
        String adminAccount = null;
        String adminSex = null;
        String adminAge = null;
        String adminWorkYear = null;
        String adminWorkDay = null;
        String adminWorkTime = null;
        String adminPhone = null;
        String adminIntroduction = null;//todo 管理员头像还没加入

        String[] temp;
        Map<String,String[]> data = request.getParameterMap();
        Set<String> keys = data.keySet();
        for(String key : keys) {
            String k  = key;
            temp = data.get(key);
            if("adminAccount".equals(k)) {
                adminAccount = temp[0];
            }else if ("adminSex".equals(k)){
                adminSex = temp[0];
            }else if("adminAge".equals(k)) {
                adminAge = temp[0];
            }else if ("adminWorkYear".equals(k)) {
                adminWorkYear = temp[0];
            }else if ("adminWorkDay".equals(k)) {
                adminWorkDay = temp[0];
            }else if ("adminWorkTime".equals(k)) {
                adminWorkTime = temp[0];
            }else if ("adminPhone".equals(k)) {
                adminPhone = temp[0];
            }else if ("adminIntroduction".equals(k)) {
                adminIntroduction = temp[0];
            }
        }
        System.out.println(adminAccount+","+adminSex+","+adminAge+","+adminPhone+","+adminWorkYear+","+adminWorkDay+","+adminWorkTime+","+adminIntroduction);

        // 返回码
        String resultCode="";
        String resultMessage="";

        //存放返回结果的List
        List<DailyNum> dailyNums = new ArrayList<DailyNum>();

        //连接数据库
        try {
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            String sql =
                    "update huangshan.tb_admin " +
                            "set adminSex = "+adminSex+",adminAge = "+adminAge+",adminWorkYear="+adminWorkYear+"," +
                            "";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                //获得查询到的每个属性
                String dateName = String.valueOf(resultSet.getDate("dateName"));
                String dateWeek = resultSet.getString("dateWeek");
                int totalNum = resultSet.getInt("totalNum");
                int orderNum = resultSet.getInt("orderNum");

                //放入新的对象中
                DailyNum num = new DailyNum();
                System.out.println(num);

                //放入List
                dailyNums.add(num);
            }

            //将List转化为json数据格式
            Gson gson = new Gson();
            String respData = gson.toJson(dailyNums);

            //写入结果
            PrintWriter pw = response.getWriter();
            pw.append(respData);
            pw.flush();

            //关闭连接
            DBUtil.close(connection, statement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
