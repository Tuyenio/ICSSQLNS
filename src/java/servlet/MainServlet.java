package servlet;

import controller.CongViecController;
import controller.EmployeeController;
import controller.LuongController;
import controller.PhongBanController;
import controller.ThongBaoController;
import controller.ReportController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet để route requests đến các controller tương ứng
 */
@WebServlet(urlPatterns = {
    "/employee/*", "/task/*", "/salary/*", "/department/*", 
    "/notification/*", "/report/*", "/attendance/*"
})
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Controllers
    private EmployeeController employeeController;
    private CongViecController taskController;
    private LuongController salaryController;
    private PhongBanController departmentController;
    private ThongBaoController notificationController;
    private ReportController reportController;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeController = new EmployeeController();
        taskController = new CongViecController();
        salaryController = new LuongController();
        departmentController = new PhongBanController();
        notificationController = new ThongBaoController();
        reportController = new ReportController();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = path.substring(contextPath.length());
        
        String result = "";
        
        try {
            if (servletPath.startsWith("/employee")) {
                result = employeeController.processRequest(request, response, request.getSession());
            } else if (servletPath.startsWith("/task")) {
                result = taskController.processRequest(request, response, request.getSession());
            } else if (servletPath.startsWith("/salary")) {
                result = salaryController.processRequest(request, response, request.getSession());
            } else if (servletPath.startsWith("/department")) {
                result = departmentController.processRequest(request, response, request.getSession());
            } else if (servletPath.startsWith("/notification")) {
                result = notificationController.processRequest(request, response, request.getSession());
            } else if (servletPath.startsWith("/report")) {
                result = reportController.processRequest(request, response, request.getSession());
            } else {
                result = "index.jsp";
            }
            
            // Xử lý kết quả
            if (result.startsWith("redirect:")) {
                String redirectPath = result.substring(9);
                if (!redirectPath.startsWith("http")) {
                    redirectPath = contextPath + "/" + redirectPath;
                }
                response.sendRedirect(redirectPath);
            } else if (result.equals("json")) {
                // JSON response đã được xử lý trong controller
                return;
            } else {
                // Forward to JSP
                request.getRequestDispatcher("/" + result).forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
