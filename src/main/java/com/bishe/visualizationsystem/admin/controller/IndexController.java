package com.bishe.visualizationsystem.admin.controller;
import com.bishe.visualizationsystem.admin.bean.Users;
import com.bishe.visualizationsystem.admin.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joseph
 * @date 2021/12/26
 * @apiNote
 */
@Controller
public class IndexController {

    @Autowired
    UsersMapper usersMapper;

    /**
     * 来登录页
     * @return
     */
    @GetMapping(value = {"/","/login"})
    public String loginPage(){

        return "login";
    }

    @PostMapping(value = {"/login"})
    public String main(Users user, HttpSession session, Model model){

        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(user.getName())){
            map.put("name",user.getName());
            List<Users> users = usersMapper.selectByMap(map);
            if(users.isEmpty()){
                model.addAttribute("msg","账号错误");
                //回到登录页
                return "login";
            }
            Users u = users.get(0);
            if(u.getPassword().equals(user.getPassword())){
                session.setAttribute("loginUser",u);

                //登陆成功 重定向到main.html 重定向防止表单重复提交
                return "redirect:/main.html";
            }else{
                model.addAttribute("msg", "密码错误");
                //回到登录页
                return "login";
            }
        }else{
            model.addAttribute("msg","账号错误");
            //回到登录页
            return "login";
        }

    }

    /**
     * 去main页面
     * @return
     */
    @GetMapping("/main.html")
    public String mainPage(HttpSession session, Model model){
//        //是否登录 拦截器 过滤器
//        Object loginUser = session.getAttribute("loginUser");
//        if(loginUser != null){
//            //跳转页面
//            return "index";
//        }else{
//            //回到登录页
//            model.addAttribute("msg","请重新登录");
//            return "login";
//        }

        return "main";
    }


    @GetMapping("/echarts.html")
    public String echartsPage(HttpSession session, Model model){

        return "echarts";
    }
}
