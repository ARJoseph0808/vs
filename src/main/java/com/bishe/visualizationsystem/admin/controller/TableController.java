package com.bishe.visualizationsystem.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.Users;
import com.bishe.visualizationsystem.admin.mapper.PatientMapper;
import com.bishe.visualizationsystem.admin.mapper.UsersMapper;
import com.bishe.visualizationsystem.admin.service.PatientService;
import com.bishe.visualizationsystem.admin.service.UsersService;
import com.bishe.visualizationsystem.admin.test.TxtToJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joseph
 * @date 2021/12/21
 * @apiNote
 */
@Slf4j
@Controller
public class TableController {
    @Autowired
    UsersService usersService;
    @Autowired
    PatientService patientService;
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    UsersMapper usersMapper;

    TxtToJson txtToJson = new TxtToJson();


    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam(value = "pn",defaultValue = "1")Integer pn,
                             RedirectAttributes ra){

        usersService.removeById(id);

        ra.addAttribute("pn",pn);
        return "redirect:/user_table";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id,
                             @RequestParam(value = "pn",defaultValue = "1")Integer pn,
                             RedirectAttributes ra){

        patientService.removeById(id);

        ra.addAttribute("pn",pn);
        return "redirect:/patient_table";
    }

    @GetMapping("/patient/echarts/{id}")
    public String echartsPatient(@PathVariable("id") Long id,HttpSession session){

        Patient patient = patientService.getById(id);
//        String name = patient.getName();
        String cpath = patient.getCollectdatapath().substring(0,patient.getCollectdatapath().length()-3) + "json";
        String cname = patient.getCollectdatapath().substring(0,patient.getCollectdatapath().length()-4);
        String apath = patient.getAnalysisdatapath().substring(0,patient.getAnalysisdatapath().length()-3) + "json";
        String aname = patient.getAnalysisdatapath().substring(0,patient.getAnalysisdatapath().length()-4);
        session.setAttribute("cpath",cpath);
        session.setAttribute("cpatientname",cname);
        session.setAttribute("apath",apath);
        session.setAttribute("apatientname",aname);
        System.out.println(apath);
        return "echarts";
    }


    @GetMapping("/patient/update")
    public String form_layouts(){
        return "form/update";
    }

    @GetMapping("/user/update")
    public String user_update(){
        return "user/update";
    }

    @GetMapping("/patient/update/{id}")
    public String toupadatePatient(@PathVariable("id") Long id, Model model){

        Patient patient = patientService.getById(id);
        model.addAttribute("updatepatient",patient);


        return "patient/update";
    }

    @GetMapping("/user/update/{id}")
    public String toupadateUser(@PathVariable("id") Long id, Model model){

        Users user = usersService.getById(id);
        model.addAttribute("updateuser",user);


        return "user/update";
    }

    @GetMapping("/user/changepassword/{id}")
    public String toChangePassword(@PathVariable("id") Long id, Model model){

        Users user = usersService.getById(id);
        model.addAttribute("updateuser",user);


        return "user/changepassword";
    }

    @PostMapping("/update")
    public String upload(@RequestParam("id") Long id,
                         @RequestParam("name") String name,
                         @RequestParam("age") Integer age,
                         @RequestParam("sex") Integer sex,
                         @RequestParam("collectdata") MultipartFile collectdata,
                         @RequestParam("analysisdata") MultipartFile analysisdata,
                         @RequestParam("collectdatapath") String collectdatapath,
                         @RequestParam("analysisdatapath") String analysisdatapath, HttpSession session) throws IOException {

        Users u = (Users) session.getAttribute("loginUser");

        if(!collectdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename1 = collectdata.getOriginalFilename();
            collectdata.transferTo(new File("/root/txt/"+name+u.getName()+originalFilename1));
            collectdatapath = name+u.getName()+originalFilename1;
            String path = "/root/txt/"+name+u.getName()+originalFilename1;
            txtToJson.txtToJson(path);
        }
        if(!analysisdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename2 = analysisdata.getOriginalFilename();
            analysisdata.transferTo(new File("/root/txt/"+name+u.getName()+originalFilename2));
            analysisdatapath = name+u.getName()+originalFilename2;
            String path = "/root/txt/"+name+u.getName()+originalFilename2;
            txtToJson.txtToJson(path);
        }

        Patient patient = patientMapper.selectById(id);
        patient.setAge(age);
        patient.setName(name);
        patient.setSex(sex);
        patient.setAnalysisdatapath(analysisdatapath);
        patient.setCollectdatapath(collectdatapath);
        patientMapper.updateById(patient);

        return "main";
    }

    @PostMapping("/updateuser")
    public String uploaduser(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("password") String password,
                             @RequestParam("role") Integer role) throws IOException {


        Users users  = usersMapper.selectById(id);
        users.setName(name);
        users.setPassword(password);
        users.setRole(role);
        usersMapper.updateById(users);


        return "main";
    }




    @GetMapping("/patient_table")
    public String patient_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, Model model){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

//        List<Patient> plist = patientService.list();


        //构造分页参数
        Page<Patient> ppage = new Page<>(pn,2);
        //调用page进行分页
        Page<Patient> patientPage = patientService.page(ppage,null);



        patientPage.getRecords();
        patientPage.getCurrent();
        patientPage.getPages();
        patientPage.getTotal();


        model.addAttribute("patients",patientPage);

//        return "table/patient_table";
        return "patient/table";
    }

    @GetMapping("/user_table")
    public String user_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, Model model,HttpSession session){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

//        List<Users> ulist = usersService.list();
        //创建对象
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        //通过queryWrapper设置条件
        //查询id>=30记录
        //第一个参数字段名称，第二个参数设置值
        Users u = (Users) session.getAttribute("loginUser");
        int temp = u.getRole() - 1;
        queryWrapper.le("role",temp);
//        List<Users> ulist = usersMapper.selectList(queryWrapper);
        //ge表示大于等于、gt表示大于、le表示小于等于、lt表示小于
        //eq等于、ne不等于




        //构造分页参数
        Page<Users> upage = new Page<>(pn,2);
        //调用page进行分页
        Page<Users> usersPage = usersService.page(upage,queryWrapper);



        usersPage.getRecords();
        usersPage.getCurrent();
        usersPage.getPages();
        usersPage.getTotal();


        model.addAttribute("users",usersPage);

        return "user/table";
    }




}
