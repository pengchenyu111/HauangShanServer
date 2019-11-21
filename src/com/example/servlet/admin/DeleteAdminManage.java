package com.example.servlet.admin;

import com.example.constant.ResultCode;
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
import java.util.Set;

/**
 * 处理获取所有管理员的信息
 */

@WebServlet(
        name = "DeleteAdminManage",
        urlPatterns = {"/DeleteAdminManage"}
)
public class DeleteAdminManage extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public DeleteAdminManage(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符集
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 返回码
        String resultCode="";
        String resultMessage="";
        String responseData = null;

        //取得前端传来的数据
        String scenicId = null;
        String adminAccount = null;
        Map<String,String[]> data = request.getParameterMap();
        if (data != null){
            Set<String> keys = data.keySet();
            for(String key : keys) {
                String k  = key;
                String[] temp = data.get(key);
                if("adminAccount".equals(k)) {
                    adminAccount = temp[0];
                }else if("scenicId".equals(k)) {
                    scenicId = temp[0];
                }
            }
        }
        System.out.println("adminAccount:"+adminAccount+","+"scenicId:"+scenicId);

        //存放返回结果的map
        Map<String,String> map = new HashMap<>();

        //数据库做删除操作
        if (scenicId != null && adminAccount != null){
            //连接数据库
            try {
                Connection connection = DBUtil.getConnect();
                Statement statement = connection.createStatement();

                String sql =
                        "DELETE from huangshan.tb_scenicmanage " +
                                "where scenicId = '"+scenicId+"' and adminAccount = '"+adminAccount+"';";
                System.out.println(sql);

                int rows = statement.executeUpdate(sql);
                System.out.println(rows+"行已被删除！");

                //返回结果
                resultCode = ResultCode.DELETE_ADMIN_SUCCESS;
                resultMessage = ResultCode.DELETE_ADMIN_SUCCESS_MSG;
                map.put("resultCode",resultCode);
                map.put("resultMessage",resultMessage);
                Gson gson = new Gson();
                responseData = gson.toJson(map);


                //关闭连接
                DBUtil.close(connection, statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            resultCode = ResultCode.DELETE_ADMIN_FAIL;
            resultMessage = ResultCode.DELETE_ADMIN_FAIL_MSG;
            map.put("resultCode",resultCode);
            map.put("resultMessage",resultMessage);
            Gson gson = new Gson();
            responseData = gson.toJson(map);
        }

        //写入结果
        PrintWriter pw = response.getWriter();
        pw.append(responseData);
        pw.flush();

    }
}
