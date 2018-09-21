package Conn;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Conn {
    static Connection conn;
    static Statement stmt;
    static PreparedStatement pstmt;
    public static void open(){
        //驱动程序名//不固定，根据驱动
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名******,8.0jar包新增时区。
        String url = "jdbc:mysql://localhost/EmployeeDB?serverTimezone=GMT%2B8";
        // MySQL配置时的用户名
        String user = "one";
        // Java连接MySQL配置时的密码******
        String password = ".asamu.654";

        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            conn = DriverManager.getConnection(url, user, password);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");

        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        try {
            if(!conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //修改表格内容
    public static int executeUpdate(String sql) {
        int result = 0;
        try {
            open();//保证连接是成功的
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }
    /**
     * 如果执行的查询或存储过程，会返回多个数据集，或多个执行成功记录数
     * 可以调用本方法，返回的结果，
     * 是一个List<ResultSet>或List<Integer>集合
     * @param sql
     * @return
     */
    public static Object execute(String sql) {
        boolean b=false;
        try {
            open();//保证连接是成功的
            stmt = conn.createStatement();
            b = stmt.execute(sql);
            //true,执行的是一个查询语句，我们可以得到一个数据集
            //false,执行的是一个修改语句，我们可以得到一个执行成功的记录数
            if(b){
                return stmt.getResultSet();
            }
            else {
                return stmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(!b) {
                close();
            }
        }
        return null;
    }
    /**
     * 查询数据，参数形式
     * select * from student where name=? and sex=?
     */
    public static ResultSet executeQuery(String sql,Object[] in) {
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            stmt = pst;//只是为了关闭命令对象pst
            return pst.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 修改
     */
    public static int executeUpdate(String sql,Object[] in) {
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            stmt = pst;//只是为了关闭命令对象pst
            return pst.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close();
        }
        return 0;
    }
    public static Object execute(String sql,Object[] in) {
        boolean b=false;
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            b = pst.execute();
            //true,执行的是一个查询语句，我们可以得到一个数据集
            //false,执行的是一个修改语句，我们可以得到一个执行成功的记录数
            if(b){
                return pst.getResultSet();
            }
            else {
                System.out.println("****");
                List<Integer> list = new ArrayList<Integer>();
                list.add(pst.getUpdateCount());
                while(pst.getMoreResults()) {
                    list.add(pst.getUpdateCount());
                }
                return list;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(!b) {
                System.out.println("====");
                close();
            }
        }
        return null;
    }
    /**
     * 调用存储过程  proc_Insert(?,?,?)
     * @param procName
     * @param in
     * @return
     */
    public static Object executeProcedure(String procName,Object[] in) {
        open();
        try {
            procName = "{call "+procName+"(";
            String link="";
            for(int i=0;i<in.length;i++) {
                procName+=link+"?";
                link=",";
            }
            procName+=")}";
            CallableStatement cstmt = conn.prepareCall(procName);
            for(int i=0;i<in.length;i++) {
                cstmt.setObject(i+1, in[i]);
            }
            if(cstmt.execute())
            {
                return cstmt.getResultSet();
            }
            else {
                return cstmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 调用存储过程，并有输出参数
     * @procName ，存储过程名称：proc_Insert(?,?)
     * @in ,输入参数集合
     * @output,输出参数集合
     * @type,输出参数类型集合
     */
    public static Object executeOutputProcedure(String procName,
                                                Object[] in,Object[] output,int[] type){
        Object result = null;
        try {
            CallableStatement cstmt = conn.prepareCall("{call "+procName+"}");
            //设置存储过程的参数值
            int i=0;
            for(;i<in.length;i++){//设置输入参数
                cstmt.setObject(i+1, in[i]);
                //print(i+1);
            }
            int len = output.length+i;
            for(;i<len;i++){//设置输出参数
                cstmt.registerOutParameter(i+1,type[i-in.length]);
                //print(i+1);
            }
            boolean b = cstmt.execute();
            //获取输出参数的值
            for(i=in.length;i<output.length+in.length;i++)
                output[i-in.length] = cstmt.getObject(i+1);
            if(b) {
                result = cstmt.getResultSet();
            }
            else {
                result = cstmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        open();
    }
}
