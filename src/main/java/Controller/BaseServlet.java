package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {

    //都会先请求service.//操作
    public  void service(HttpServletRequest request,HttpServletResponse response){

        try{
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");

            //方法名称
            String action=request.getParameter("action");
            if(!(action.equals("")||action=="")){

                //用反射。找方法。
                //this-->当前类 action=getSum;
                Method method=this.getClass().getDeclaredMethod(action,HttpServletRequest.class,HttpServletResponse.class);
                method.setAccessible(true);
                method.invoke(this,request,response);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
