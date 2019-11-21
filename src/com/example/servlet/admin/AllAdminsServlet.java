package com.example.servlet.admin;

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
import java.util.HashMap;
import java.util.Map;

/**
 * 处理获取所有管理员的信息
 */

@WebServlet(
        name = "AllAdminsServlet",
        urlPatterns = {"/AllAdminsServlet"}
)
public class AllAdminsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public AllAdminsServlet(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 返回码
        String resultCode="";
        String resultMessage="";

        //存放返回结果的map
        Map<String,String> map = new HashMap<>();

        //存放数据的字符串
        String temp = "[";

        //连接数据库
        try {
            Connection connection = DBUtil.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            String sql =
                    "SELECT tb_admin.adminAccount,adminName,adminSex,adminAge,adminWorkYear,adminWorkDay,adminWorkTime,adminPhone,adminIntroduction,tb_scenic.scenicId,scenicName,scenicLatitude,scenicLongitude " +
                    "FROM "+DBUtil.TABLE_ADMIN+","+DBUtil.TABLE_SCENICMANAGE+","+DBUtil.TABLE_SCENIC+" " +
                    "WHERE tb_admin.adminAccount = tb_scenicmanage.adminAccount AND tb_scenic.scenicId = tb_scenicmanage.scenicId;";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                //todo 还没有写入头像
                //获得查询到的每个属性
                String adminAccount = resultSet.getString("adminAccount");
                String adminName = resultSet.getString("adminName");
                String adminSex = resultSet.getString("adminSex");
                String adminAge = String.valueOf(resultSet.getInt("adminAge"));
                String adminWorkYear = String.valueOf(resultSet.getInt("adminWorkYear"));
                String adminWorkDay = resultSet.getString("adminWorkDay");
                String adminWorkTime = resultSet.getString("adminWorkTime");
                String adminPhone = resultSet.getString("adminPhone");
                String adminIntroduction = resultSet.getString("adminIntroduction");
                String scenicId = resultSet.getString("scenicId");
                String scenicName = resultSet.getString("scenicName");
                String scenicLatitude = String.valueOf(resultSet.getDouble("scenicLatitude"));
                String scenicLongitude = String.valueOf(resultSet.getDouble("scenicLongitude"));

                //装入map
                map.put("adminAccount",adminAccount);
                map.put("adminName",adminName);
                map.put("adminSex",adminSex);
                map.put("adminAge",adminAge);
                map.put("adminWorkYear",adminWorkYear);
                map.put("adminWorkDay",adminWorkDay);
                map.put("adminWorkTime",adminWorkTime);
                map.put("adminPhone",adminPhone);
                map.put("adminIntroduction",adminIntroduction);
                map.put("scenicId",scenicId);
                map.put("scenicName",scenicName);
                map.put("scenicLatitude",scenicLatitude);
                map.put("scenicLongitude",scenicLongitude);

                //将map转化为json数据格式
                Gson gson = new Gson();
                String respData = gson.toJson(map);

                //连接各个结果
                temp+=(respData+",");
            }

            // 去除末尾 ，
            temp=temp.substring(0,temp.length()-1);

            //写入结果
            PrintWriter pw = response.getWriter();
            pw.append(temp);
            pw.append("]");
            pw.flush();

            DBUtil.close(connection, statement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
