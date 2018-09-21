package nf.luoliyi.bo;

import nf.luoliyi.dao.EmployeeDao;
import nf.luoliyi.vo.Employee;
import java.util.List;

public class GoodsBo {
    EmployeeDao employeedao=new EmployeeDao();
    //查询全部
    public List<Employee> getAllGoods(){
        return employeedao.getAllEmployee();
    }
    //分页查询
    public  List<Employee>getAllGoodsByPage(int page,int size){
        return  employeedao.getAllEmployeeByPage(page, size);
    }
    //添加
    public int insert(Employee e){
        return employeedao.insert(e);
    }
    //修改
    public int update(Employee e){
        return employeedao.update(e);
    }
    //删除
    public int delete(int eid){
        return employeedao.delete(eid);
    }
}
