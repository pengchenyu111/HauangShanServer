package com.example.servlet.admin;

import com.example.pojo.DailyNum;
import com.example.pojo.HourlyNum;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理查询时间区间内的 每日客流总数
 */

@WebServlet(
        name = "SelectHourlyNumServlet",
        urlPatterns = {"/SelectHourlyNumServlet"}
)
public class SelectHourlyNumServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public SelectHourlyNumServlet(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获取参数
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        // 返回码
        String resultCode="";
        String resultMessage="";

        //存放返回结果的List
        List<HourlyNum> hourlyNums = new ArrayList<HourlyNum>();

        //连接数据库
        try {
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            String sql =
                    "SELECT  * FROM huangshan.tb_hourlynum " +
                            "WHERE dateName >= '"+startTime+"' and dateName <= '"+endTime+"';";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                //获得查询到的每个属性
                String dateName = String.valueOf(resultSet.getDate("dateName"));
                String hour = resultSet.getString("hour");
                int hourNum = resultSet.getInt("hourNum");

                //放入新的对象中
                HourlyNum num = new HourlyNum(dateName,hour,hourNum);
                System.out.println(num);

                //放入List
                hourlyNums.add(num);
            }

            //将List转化为json数据格式
            Gson gson = new Gson();
            String respData = gson.toJson(hourlyNums);

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获取参数
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String[] temp;

        Map<String, String[]> data = request.getParameterMap();
        Set<String> keys = data.keySet();
        for (String key : keys) {
            String k = key;
            temp = data.get(key);
            if ("startTime".equals(k)) {
                startTime = temp[0];
            } else if ("endTime".equals(k)) {
                endTime = temp[0];
            }
        }
        System.out.println("startTime:" + startTime + "," + "endTime:" + endTime);

        // 返回码
        String resultCode = "";
        String resultMessage = "";

        //存放返回结果的List
        List<HourlyNum> hourlyNums = new ArrayList<HourlyNum>();

        //连接数据库
        try {
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            String sql =
                    "SELECT  * FROM huangshan.tb_hourlynum " +
                            "WHERE dateName >= '"+startTime+"' and dateName <= '"+endTime+"';";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                //获得查询到的每个属性
                String dateName = String.valueOf(resultSet.getDate("dateName"));
                String hour = resultSet.getString("hour");
                int hourNum = resultSet.getInt("hourNum");

                //放入新的对象中
                HourlyNum num = new HourlyNum(dateName, hour, hourNum);
                System.out.println(num);

                //放入List
                hourlyNums.add(num);
            }

            //将List转化为json数据格式
            Gson gson = new Gson();
            String respData = gson.toJson(hourlyNums);

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
