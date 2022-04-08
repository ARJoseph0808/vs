package com.bishe.visualizationsystem.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.visualizationsystem.admin.bean.Info;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.User;
import com.bishe.visualizationsystem.admin.mapper.InfoMapper;
import com.bishe.visualizationsystem.admin.mapper.PatientMapper;
import com.bishe.visualizationsystem.admin.mapper.UserMapper;
import com.bishe.visualizationsystem.admin.service.InfoService;
import com.bishe.visualizationsystem.admin.service.PatientService;
import com.bishe.visualizationsystem.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Joseph
 * @date 2022/3/11
 * @apiNote
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    InfoService infoService;
    @Autowired
    InfoMapper infoMapper;
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    PatientService patientService;

    @GetMapping("/user_add")
    public String user_add(){
        return "user/add";
    }

    @PostMapping("/user_upload")
    public String user_upload(@RequestParam("name") String name,
                              @RequestParam("password") String password,
                              @RequestParam("role") Integer role) throws IOException {


        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);
        userMapper.insert(user);



        return "main";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam(value = "pn",defaultValue = "1")Integer pn,
                             RedirectAttributes ra){
        Long userid = id;
        User user = userService.getById(id);
        if(user.getRole() == -1){
            String name = user.getName();
            QueryWrapper<Info> wrapper = new QueryWrapper<>();
            //通过QueryWrapper设置条件
            //ge gt le lt
            //查询age>=30的记录
            //第一个参数是字段的名称 ， 第二个参数是设置的值
            wrapper.eq("name" , name);
            List<Info> infoList = infoMapper.selectList(wrapper);
            Info info = infoList.get(0);
            id = info.getId();
            infoService.removeById(id);
            QueryWrapper<Patient> pwrapper = new QueryWrapper<>();
            pwrapper.eq("name",name);
            List<Patient> patients = patientMapper.selectList(pwrapper);
            for(Patient patient : patients){
                patientService.removeById(patient.getId());
            }
            userService.removeById(userid);
            ra.addAttribute("pn",pn);
            return "redirect:/patientuser_table";
        }
        userService.removeById(userid);
        ra.addAttribute("pn",pn);
        return "redirect:/user_table";
    }

    @GetMapping("/user/update")
    public String user_update(){
        return "user/update";
    }

    @GetMapping("/user/update/{id}")
    public String toupadateUser(@PathVariable("id") Long id, Model model){

        User user = userService.getById(id);
        model.addAttribute("updateuser",user);

        return "user/update";
    }

    @GetMapping("/user/changepassword/{id}")
    public String toChangePassword(@PathVariable("id") Long id, Model model){

        User user = userService.getById(id);
        model.addAttribute("updateuser",user);


        return "user/changepassword";
    }

    @PostMapping("/updateuser")
    public String uploaduser(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("password") String password,
                             @RequestParam("role") Integer role) throws IOException {


        User user  = userMapper.selectById(id);
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);
        userService.updateById(user);


        return "main";
    }

    @GetMapping("/user_table")
    public String user_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, Model model, HttpSession session){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

//        List<Users> ulist = usersService.list();
        //创建对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //通过queryWrapper设置条件
        //查询id>=30记录
        //第一个参数字段名称，第二个参数设置值
        User u = (User) session.getAttribute("loginUser");
        int temp = u.getRole() - 1;
        queryWrapper.le("role",temp);
        queryWrapper.ge("role",0);
//        List<Users> ulist = usersMapper.selectList(queryWrapper);
        //ge表示大于等于、gt表示大于、le表示小于等于、lt表示小于
        //eq等于、ne不等于



        //构造分页参数
        Page<User> upage = new Page<>(pn,2);
        //调用page进行分页
        Page<User> usersPage = userService.page(upage,queryWrapper);


        usersPage.getRecords();
        usersPage.getCurrent();
        usersPage.getPages();
        usersPage.getTotal();


        model.addAttribute("users",usersPage);

        return "user/table";
    }

    @GetMapping("/patientuser_table")
    public String patientuser_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, Model model, HttpSession session){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

//        List<Users> ulist = usersService.list();
        //创建对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //通过queryWrapper设置条件
        //查询id>=30记录
        //第一个参数字段名称，第二个参数设置值

        queryWrapper.lt("role",0);
//        List<Users> ulist = usersMapper.selectList(queryWrapper);
        //ge表示大于等于、gt表示大于、le表示小于等于、lt表示小于
        //eq等于、ne不等于



        //构造分页参数
        Page<User> upage = new Page<>(pn,2);
        //调用page进行分页
        Page<User> usersPage = userService.page(upage,queryWrapper);


        usersPage.getRecords();
        usersPage.getCurrent();
        usersPage.getPages();
        usersPage.getTotal();


        model.addAttribute("users",usersPage);

        return "user/table";
    }
}
