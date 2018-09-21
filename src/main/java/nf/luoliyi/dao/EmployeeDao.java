package nf.luoliyi.dao;

import Conn.Conn;
import nf.luoliyi.vo.Employee;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    //查询全部
    public List<Employee>getAllEmployee(){
        List<Employee> list=new ArrayList<Employee>();
        String sql="select * from Employee";
        Employee employee=null;
        ResultSet rs=Conn.executeQuery(sql,new Object[]{});
        try {
            while (rs.next()){
              employee=new Employee();
              employee.setEid(rs.getInt(1));
              employee.setEname(rs.getString(2));
              employee.setEage(rs.getInt(3));
              employee.setEsex(rs.getInt(4));
              employee.setEemail(rs.getString(5));
              employee.setEdesc(rs.getString(6));
              list.add(employee);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //分页查询
    public List<Employee>getAllEmployeeByPage(int page,int size){
        int limit=(page-1)*size;
        List<Employee> list=new ArrayList<Employee>();
        String sql="select * from Employee limit ?,?";
        Employee employee=null;
        ResultSet rs=Conn.executeQuery(sql,new Object[]{limit,size});
        try {
            while (rs.next()){
                employee=new Employee();
                employee.setEid(rs.getInt(1));
                employee.setEname(rs.getString(2));
                employee.setEage(rs.getInt(3));
                employee.setEsex(rs.getInt(4));
                employee.setEemail(rs.getString(5));
                employee.setEdesc(rs.getString(6));
                list.add(employee);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //新增
    public  int insert(Employee e){
        String sql="insert into Employee(ename,eage,esex,eemail,edesc) values(?,?,?,?,?)";
        return  Conn.executeUpdate(sql,new Object[]{e.getEname(),e.getEage(),e.getEsex(),e.getEemail(),e.getEdesc()});
    }

    //修改
    public  int update(Employee e){
        String sql="update Employee set ename=?,eage=?,esex=?,eemail=?,edesc=? where eid=?";
        return  Conn.executeUpdate(sql,new Object[]{e.getEname(),e.getEage(),e.getEsex(),e.getEemail(),e.getEdesc(),e.getEid()});
    }

    //删除
    public  int delete(int eid){
        String sql="delete from Employee where eid=?";
        return  Conn.executeUpdate(sql,new Object[]{eid});
    }
}
