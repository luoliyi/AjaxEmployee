package Controller;

import JsonUtils.Utils;
import com.sun.deploy.net.HttpRequest;
import nf.luoliyi.bo.GoodsBo;
import nf.luoliyi.vo.Employee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@WebServlet("/doServlet")
//继承BaseServlet
public class doServlet extends BaseServlet {

    //执行
    GoodsBo goodsbo=new GoodsBo();

    //获得总数
    public void getSum(HttpServletRequest request,HttpServletResponse response){

        List<Employee> employeeList=goodsbo.getAllGoods();
        try {

            response.getWriter().print(Utils.toJson(employeeList.size()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询全部
    public  void getAllEmployee(HttpServletRequest request,HttpServletResponse response){

        List<Employee> employeeList=goodsbo.getAllGoods();
        try {

            response.getWriter().print(Utils.toJson(employeeList));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //分页查询
    public  void getAllEmployeeByPage(HttpServletRequest request,HttpServletResponse response){

        int pageno=Integer.parseInt(request.getParameter("pageno"));
        int size=Integer.parseInt(request.getParameter("size"));

        System.out.println(pageno+","+size);

        List<Employee> goodsList=goodsbo.getAllGoodsByPage(pageno,size);
        try {
            response.getWriter().print(Utils.toJson(goodsList));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //删除
    public  void delete(HttpServletRequest request,HttpServletResponse response){

        int eid=Integer.parseInt(request.getParameter("eid"));
        int result=goodsbo.delete(eid);
        try {
            response.getWriter().print(Utils.toJson(result));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //添加
    public  void  insert(HttpServletRequest request,HttpServletResponse response){
        System.out.println("come in");
        String ename=request.getParameter("ename");
        int eage=Integer.parseInt(request.getParameter("eage"));
        String esex=request.getParameter("esex");
        String eemail=request.getParameter("eemail");
        String edesc=request.getParameter("edesc");

        int intsex=0;
        if(esex=="男"){
            intsex=1;
        }else{
            intsex=0;
        }
        Employee employee=new Employee(ename,eage,intsex,eemail,edesc);
        System.out.println(employee.toString());
       int result=goodsbo.insert(employee);
       if(result>0) {
           try {
               response.getWriter().print(Utils.toJson(result));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    }
    public  void  deleteAll(HttpServletRequest request,HttpServletResponse response){
        String arr=request.getParameter("arr");
        String[]items=arr.split(",");
        int result=0;
        for (int i=0;i<items.length;i++){
            result+=goodsbo.delete(Integer.parseInt(items[i]));
        }
        try {
            response.getWriter().print(Utils.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改
    public  void  update(HttpServletRequest request,HttpServletResponse response){

        //gno,gname,gprice,gdate,gstate
        int eid=Integer.parseInt(request.getParameter("eid"));
        String ename=request.getParameter("ename");
        int eage=Integer.parseInt(request.getParameter("eage"));
        String esex=request.getParameter("esex");
        String eemail=request.getParameter("eemail");
        String edesc=request.getParameter("edesc");

        int intsex=0;
        if(esex=="男"){
            intsex=1;
        }else{
            intsex=0;
        }

        Employee employee=new Employee(eid,ename,eage,intsex,eemail,edesc);
        System.out.println(employee.toString());
        int result=goodsbo.update(employee);
        if(result>0) {
            try {
                response.getWriter().print(Utils.toJson(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
