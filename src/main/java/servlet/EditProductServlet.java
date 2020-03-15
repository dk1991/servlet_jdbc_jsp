package servlet;

import beans.Product;
import utils.DBUtils;
import utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/editProduct"})
public class EditProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EditProductServlet() {
        super();
    }

    // Отобразить страницу редактирования продукта.

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(req);

        String code = (String) req.getParameter("code");

        Product product = null;
        String errorString = null;

        try {
            product = DBUtils.findProduct(conn, code);
        } catch (SQLException e) {
            e.printStackTrace();
            errorString = e.getMessage();
        }

        // Ошибки не имеются.
        // Продукт не существует для редактирования (edit).
        // Redirect sang trang danh sách sản phẩm.
        if (errorString != null && product == null) {
            resp.sendRedirect(req.getServletPath() + "/productList");
            return;
        }

        // Сохранить информацию в request attribute перед тем как forward к views.
        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
        dispatcher.forward(req, resp);
    }

    // После того, как пользователь отредактировал информацию продукта и нажал на Submit.
    // Данный метод будет выполнен.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(req);

        String code = (String) req.getParameter("code");
        String name = (String) req.getParameter("name");
        String priceStr = (String) req.getParameter("price");
        float price = 0;
        try {
            price = Float.parseFloat(priceStr);
        } catch (Exception e) {
        }
        Product product = new Product(code, name, price);

        String errorString = null;

        try {
            DBUtils.updateProduct(conn, product);
        } catch (SQLException e) {
            e.printStackTrace();
            errorString = e.getMessage();
        }

        // Сохранить информацию в request attribute перед тем как forward к views.
        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        // Если имеется ошибка, forward к странице edit.
        if (errorString != null) {
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
            dispatcher.forward(req,resp);
        }
        // Если все хорошо.
        // Redirect к странице со списком продуктов.
        else {
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
    }
}
