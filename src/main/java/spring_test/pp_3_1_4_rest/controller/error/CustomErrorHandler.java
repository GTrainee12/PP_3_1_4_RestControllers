package spring_test.pp_3_1_4_rest.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorHandler  implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred.";
        String errorDetails = "";
        if (statusCode != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "Page Not Found!";
                errorDetails = "The page you were looking for could not be found.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "Access Denied!";
                errorDetails = "You do not have permission to access this page.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Internal Server Error!";
                errorDetails = "Something went wrong on our end. Please try again later.";
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);
        model.addAttribute("statusCode", statusCode);

        return "errors/error";
    }
}
