package MySQLUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表结构 自动生成java MVC中的dao
 *
 * @author DLHT 2018年5月10日下午15:00:28 AutoCreateDao.java
 */
public class AutoCreateDao {

    //  mysql 驱动类
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    //  数据库登录用户名
    private static final String USER = "one";

    //  数据库登录密码
    private static final String PASSWORD = ".asamu.654";

    // 数据库连接地址
    private static final String URL = "jdbc:mysql://localhost/mall?serverTimezone=GMT%2B8";

    private static String tablename;

    private String[] colnames; // 列名数组

    private String[] colTypes; // 列名类型数组

    private int[] colSizes; // 列名大小数组

    /**
     * 获取指定数据库中包含的表 TBlist
     *
     * @time 2016年3月4日下午5:54:52
     * @packageName com.util
     * @return 返回所有表名(将表名放到一个集合中)
     * @throws Exception
     */
    public List<String> TBlist() throws Exception {
        // 访问数据库 采用 JDBC方式
        Class.forName(DRIVER);

        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        DatabaseMetaData dbmd = (DatabaseMetaData) con.getMetaData();
        List<String> list = null;

        ResultSet rs = dbmd.getTables(con.getCatalog(), "%", "%", new String[] { "TABLE" });
        if (rs != null) {
            list = new ArrayList<String>();
        }
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            list.add(tableName);
        }
        rs = null;
        dbmd = null;
        con = null;
        return list;
    }

    public void GenEntity(List<String> TBlist, String packageName)throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSetMetaData rsmd = null;

        // 访问数据库 采用 JDBC方式
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(URL, USER, PASSWORD);

        for (int k = 0; k < TBlist.size(); k++) {
            tablename = TBlist.get(k);
            String strsql = "select * from " + tablename;
            pstmt = conn.prepareStatement(strsql);
            rsmd = pstmt.getMetaData();
            int size = rsmd.getColumnCount();
            // 共有多少列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                colnames[i] = rsmd.getColumnName(i + 1);
                colTypes[i] = rsmd.getColumnTypeName(i + 1);
                /*
                if (colTypes[i].equalsIgnoreCase("datetime")) {
                    f_util = true;
                }
                if (colTypes[i].equalsIgnoreCase("image")
                        || colTypes[i].equalsIgnoreCase("text")) {
                    f_sql = true;
                }*/
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }
            markerBean(initcap(tablename), parse(), packageName);
        }
        pstmt = null;
        rsmd = null;
        conn = null;
    }

    /**
     * 解析处理(生成实体类主体代码)
     */
    private String parse() {
        StringBuffer sb = new StringBuffer();
        //导入包
        sb.append("import java.sql.*;\r\n");
        sb.append("import java.util.*;\r\n");
        //导入自己项目的数据库util
        sb.append("import Conn.Conn;\r\n");
        //导入对应实体包
        sb.append("import com.vo." + initcap(tablename) + ";\r\n");

        sb.append("public class " + initcap(tablename) + "DAO {\r\n");
        processAllMethod(sb);
        sb.append("}\r\n");

        return sb.toString();

    }

    /**
     * 创建java 文件 将生成的属性 get/set 方法 保存到 文件中 markerBean
     *
     * @time 2015年9月29日下午4:15:22
     * @packageName fanshe
     * @param className
     *            类名称
     * @param content
     *            类内容 包括属性 getset 方法
     */
    public void markerBean(String className, String content, String packageName) {
        String folder = System.getProperty("user.dir") + "/src/" + packageName + "/";

        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = folder + className + "DAO.java";

        try {
            File newdao = new File(fileName);
            FileWriter fw = new FileWriter(newdao);
            fw.write("package\t" + packageName.replace("/", ".") + ";\r\n");
            fw.write(content);
            fw.flush();
            fw.close();
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        /*生成五个方法：
         * findById,findAll,insert,update,delete
         */
        //findById
        sb.append("\tpublic " + initcap(tablename) + " findById(int id) {\r\n");
        sb.append("\t\tString sql = \"select * from " + initcap(tablename) + " where id=?\";\r\n");
        sb.append("\t\tObject[] in = {id};\r\n");
        //Conn--->自己uitl工具里面的Conn的语句
        sb.append("\t\tResultSet rs = Conn.executeQuery(sql, in);\r\n");
        sb.append("\t\t" + initcap(tablename) + " model = null;\r\n");
        sb.append("\t\ttry {\r\n");
        sb.append("\t\t\tif(rs.next()) {\r\n");
        sb.append("\t\t\t\tmodel = new " + initcap(tablename) + "(\r\n");
        String link="";
        for (int i = 0; i < colnames.length; i++) {
            sb.append(link+"\t\t\t\t\trs.get" + sqlTypeJavaType(colTypes[i]) + "(\"" + colnames[i] + "\")");
            link=",\r\n";
        }
        sb.append(");\r\n");
        sb.append("\t\t\t}\r\n");
        sb.append("\t\t\tConn.close();\r\n");
        sb.append("\t\t} catch (SQLException e) {\r\n");
        sb.append("\t\t\te.printStackTrace();\r\n");
        sb.append("\t\t}\r\n");
        sb.append("\t\treturn model;\r\n");
        sb.append("\t}\r\n");

        //findAll
        sb.append("\tpublic List<" + initcap(tablename) + "> findAll(int id) {\r\n");
        sb.append("\t\tString sql = \"select * from " + initcap(tablename) + "\";\r\n");
        sb.append("\t\tResultSet rs = Conn.executeQuery(sql);\r\n");
        sb.append("\t\tList<" + initcap(tablename) + "> modelList = new ArrayList<" + initcap(tablename) + ">();\r\n");
        sb.append("\t\t" + initcap(tablename) + " model = null;\r\n");
        sb.append("\t\ttry {\r\n");
        sb.append("\t\t\twhile(rs.next()) {\r\n");
        sb.append("\t\t\t\tmodel = new " + initcap(tablename) + "(\r\n");

        link="";
        for (int i = 0; i < colnames.length; i++) {
            sb.append(link+"\t\t\t\t\trs.get" + sqlTypeJavaType(colTypes[i]) + "(\"" + colnames[i] + "\")");
            link=",\r\n";
        }
        sb.append(");\r\n");
        sb.append("\t\t\t\tmodelList.add(model);\r\n");
        sb.append("\t\t\t}\r\n");
        sb.append("\t\t\tConn.close();\r\n");
        sb.append("\t\t} catch (SQLException e) {\r\n");
        sb.append("\t\t\te.printStackTrace();\r\n");
        sb.append("\t\t}\r\n");
        sb.append("\t\treturn modelList;\r\n");
        sb.append("\t}\r\n");

        //insert
        sb.append("\tpublic int insert(" + initcap(tablename) + " model) {\r\n");
        sb.append("\t\tString sql = \"insert into " + initcap(tablename) + " values(?,?,?) \";\r\n");
        sb.append("\t\tObject[] in = {");
        link="";
        for (int i = 1; i < colnames.length; i++) {
            sb.append(link+"model.get"+initcap(colnames[i])+"()");
            link=",";
        }
        sb.append("};\r\n");
        sb.append("\t\treturn Conn.executeUpdate(sql, in);\r\n");
        sb.append("\t}\r\n");

        //update
        sb.append("\tpublic int update(" + initcap(tablename) + " model) {\r\n");
        sb.append("\t\tString sql = \"update " + initcap(tablename) + " set values(?,?,?) \";\r\n");
        sb.append("\t\tObject[] in = {");
        link="";
        for (int i = 1; i < colnames.length; i++) {
            sb.append(link+"model.get"+initcap(colnames[i])+"()");
            link=",";
        }
        sb.append("};\r\n");
        sb.append("\t\treturn Conn.executeUpdate(sql, in);\r\n");
        sb.append("\t}\r\n");


        //delete
        sb.append("\tpublic int delete(int id) {\r\n");
        sb.append("\t\tString sql = \"delete from " + initcap(tablename) + " where id=? \";\r\n");
        sb.append("\t\tObject[] in = {id};\r\n");
        sb.append("\t\treturn Conn.executeUpdate(sql, in);\r\n");
        sb.append("\t}\r\n");

    }



    /**
     * 把输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    @SuppressWarnings("unused")
    private String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal")
                || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar")
                || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("uniqueidentifier")
                || sqlType.equalsIgnoreCase("ntext")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")
                ||sqlType.equalsIgnoreCase("date")){
            return "Date";
        }

        else if (sqlType.equalsIgnoreCase("image")) {
            return "Blob";
        } else if (sqlType.equalsIgnoreCase("text")) {
            return "Clob";
        }
        return "String";
    }


    private String sqlTypeJavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("decimal")
                || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar")
                || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("uniqueidentifier")
                || sqlType.equalsIgnoreCase("ntext")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")
                ||sqlType.equalsIgnoreCase("date")){
            return "Date";
        }

        else if (sqlType.equalsIgnoreCase("image")) {
            return "Blob";
        } else if (sqlType.equalsIgnoreCase("text")) {
            return "Clob";
        }
        return "String";
    }

    public static void main(String[] args) throws Exception {
        AutoCreateDao auto = new AutoCreateDao();
        List<String> list = auto.TBlist();
        auto.GenEntity(list, "com/dao");

    }

}