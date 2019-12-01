package com.example.servlet.admin;

import com.example.constant.ResultCode;
import com.example.pojo.Notification;
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

@WebServlet(
        name = "NotificationServlet",
        urlPatterns = {"/NotificationServlet"}
)
public class NotificationServlet extends HttpServlet {

    private static final String SELECT_ALL_NOTIFICATION = "selectAllNotification";
    private static final String ADD_NOTIFICATION = "addNotification";
    private static final String DELETE_NOTIFICATION = "deleteNotification";
    private static final String UPDATE_NOTIFICATION = "updateNotification";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获得操作类型
        String handleMethod = request.getParameter("handleMethod");
        switch (handleMethod){
            case SELECT_ALL_NOTIFICATION:
                getSelectAllNotifications(response);
                break;
            case ADD_NOTIFICATION:
                break;
            case DELETE_NOTIFICATION:
                break;
            case UPDATE_NOTIFICATION:
                break;
            default:break;
        }
    }

    private void getSelectAllNotifications(HttpServletResponse response) throws IOException {
        //连接数据库
        //存放返回结果的List
        List<Notification> notifications = new ArrayList<Notification>();

        //连接数据库
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnect();
            statement = connection.createStatement();

            String sql = "SELECT  * FROM huangshan.tb_notification order by sendTime DESC; ";
            System.out.println(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                //获得查询到的每个属性
                int notificationId = resultSet.getInt("notificationId");
                String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultSet.getTimestamp("sendTime"));;
                String sendAdminName = resultSet.getString("sendAdminName");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                int type = resultSet.getInt("type");

                //放入新的对象中
                Notification notification = new Notification(notificationId,sendTime,sendAdminName,title,content,type);
                System.out.println(notification);

                //放入List
                notifications.add(notification);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }

        //转为json
        Gson gson = new Gson();
        String respData = gson.toJson(notifications);

        //写入结果
        PrintWriter pw = response.getWriter();
        pw.append(respData);
        pw.flush();
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String temp[];
        String handleMethod = null;
        String title = null;
        String content= null;
        String sendAdminName= null;
        String sendTime= null;
        String type= null;
        Map<String,String[]> data = request.getParameterMap();
        Set<String> keys = data.keySet();
        for(String key : keys) {
            String k  = key;
            temp = data.get(key);
            if("handleMethod".equals(k)) {
                handleMethod = temp[0];
            }else if ("title".equals(k)){
                title = temp[0];
            }else if ("content".equals(k)){
                content = temp[0];
            }else if ("sendAdminName".equals(k)){
                sendAdminName = temp[0];
            }else if ("sendTime".equals(k)){
                sendTime = temp[0];
            }else if ("type".equals(k)){
                type = temp[0];
            }
        }

        //获得操作类型
        switch (handleMethod){
            case SELECT_ALL_NOTIFICATION:
                getSelectAllNotifications(response);
                break;
            case ADD_NOTIFICATION:
                addNotification(response, title, content, sendAdminName, sendTime, type);
                break;
            case DELETE_NOTIFICATION:
                break;
            case UPDATE_NOTIFICATION:
                break;
            default:break;
        }
    }

    private void addNotification(HttpServletResponse response, String title, String content, String sendAdminName, String sendTime, String type) throws IOException {
        String resultCode = null;
        String resultMessage = null;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBUtil.getConnect();
            statement = connection.createStatement();

            String sql = "INSERT INTO huangshan.tb_notification(sendTime,sendAdminName,title,content,type) " +
                            "VALUES('"+sendTime+"','"+sendAdminName+"','"+title+"','"+content+"',"+type+");";
            System.out.println(sql);

            int row = statement.executeUpdate(sql);
            if (row != 0){
                resultCode = ResultCode.ADD_NOTIFICATION_SUCCESS;
                resultMessage = ResultCode.ADD_NOTIFICATION_SUCCESSS_MSG;
            }else{
                resultCode = ResultCode.ADD_NOTIFICATION_FAIL;
                resultMessage = ResultCode.ADD_NOTIFICATION_FAIL_MSG;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,statement);
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("resultCode", resultCode);
        map.put("resultMessage", resultMessage);
        Gson gson = new Gson();
        String resultData = gson.toJson(map);
        PrintWriter pw = response.getWriter();
        pw.append(resultData);
        pw.flush();
        pw.close();
    }
}
