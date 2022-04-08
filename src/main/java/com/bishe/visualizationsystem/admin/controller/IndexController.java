package com.bishe.visualizationsystem.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.visualizationsystem.admin.bean.Info;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.User;
import com.bishe.visualizationsystem.admin.mapper.InfoMapper;
import com.bishe.visualizationsystem.admin.mapper.UserMapper;
import com.bishe.visualizationsystem.admin.service.PatientService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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
    UserMapper userMapper;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    InfoMapper infoMapper;

    @Autowired
    PatientService patientService;

    /**
     * 来登录页
     * @return
     */
    @GetMapping(value = {"/","/login"})
    public String loginPage(){

        return "login";
    }

    @PostMapping(value = {"/login"})
    public String main(User user, String code,HttpSession session, Model model){

        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "login";
        }

        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(user.getName())){
            map.put("name",user.getName());
            List<User> users = userMapper.selectByMap(map);
            if(users.isEmpty()){
                model.addAttribute("msg","账号错误");
                //回到登录页
                return "login";
            }
            User u = users.get(0);
            if(u.getPassword().equals(user.getPassword())){
                session.setAttribute("loginUser",u);
                if(u.getRole() == -1){
                    QueryWrapper<Info> wrapper = new QueryWrapper<>();
                    //通过QueryWrapper设置条件
                    //ge gt le lt
                    //查询age>=30的记录
                    //第一个参数是字段的名称 ， 第二个参数是设置的值
                    wrapper.eq("name" , u.getName());
                    List<Info> infolist = infoMapper.selectList(wrapper);
                    Info info = infolist.get(0);
                    session.setAttribute("patientuser",info);

                    return "redirect:/patientuser.html";
//                    return "patientuser";
                }else{
                    //登陆成功 重定向到main.html 重定向防止表单重复提交
                    return "redirect:/main.html";
                }
                /*//登陆成功 重定向到main.html 重定向防止表单重复提交
                return "redirect:/main.html";*/
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

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将突图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
//            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    /**
     * 去patientuser页面
     * @return
     */
    @GetMapping("/patientuser.html")
    public String patientuserPage(HttpSession session, Model model){

        Info info = (Info) session.getAttribute("patientuser");
        String name = info.getName();
        //构造分页参数
        Page<Patient> ppage = new Page<>(1,2);
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(true, "name", name);
        //调用page进行分页
        Page<Patient> patientPage = patientService.page(ppage,queryWrapper);


        patientPage.getRecords();
        patientPage.getCurrent();
        patientPage.getPages();
        patientPage.getTotal();


        session.setAttribute("info",info);
        model.addAttribute("patients",patientPage);

        return "patientuser";
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
