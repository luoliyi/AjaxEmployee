package nf.luoliyi.dao;
import nf.luoliyi.vo.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

public class EmployeeDaoTest {

    @Test
    public void getAllEmployee() {
        EmployeeDao edao=new EmployeeDao();
        List<Employee> employeeList=edao.getAllEmployee();
        for (Employee e:employeeList){
            System.out.println(e.getEname());
        }
    }

    @Test
    public void getAllEmployeeByPage() {
        EmployeeDao edao=new EmployeeDao();
        List<Employee> employeeList=edao.getAllEmployeeByPage(1,4);
        for (Employee e:employeeList){
            System.out.println(e.getEname());
        }
    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}
