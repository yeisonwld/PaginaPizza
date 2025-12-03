package com.prueba3.pagina3;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

public class Index {

@Controller
@RequestMapping("/index")
public class IndexController{
    @GetMapping("/home")
    public String  Home () {
        return "index";
    }
    
}

}
