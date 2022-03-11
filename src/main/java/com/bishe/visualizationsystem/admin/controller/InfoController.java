package com.bishe.visualizationsystem.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.visualizationsystem.admin.bean.Info;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.User;
import com.bishe.visualizationsystem.admin.mapper.InfoMapper;
import com.bishe.visualizationsystem.admin.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * @author Joseph
 * @date 2022/3/11
 * @apiNote
 */
@Controller
public class InfoController {
    @Autowired
    InfoService infoService;
    @Autowired
    InfoMapper infoMapper;

    @GetMapping("/patient_info_add")
    public String patient_add(){
        return "patient/addinfo";
    }

    @GetMapping("/patient_info_delete/{id}")
    public String deletePatient(@PathVariable("id") Long id,
                                @RequestParam(value = "pn",defaultValue = "1")Integer pn,HttpSession session,
                                RedirectAttributes ra){
        infoService.removeById(id);

        ra.addAttribute("pn",pn);
        return "redirect:/patient_info_table";
    }

    @PostMapping("/patient_info_upload")
    public String patient_upload(@RequestParam("name") String name,
                                 @RequestParam("age") Integer age,
                                 @RequestParam("sex") Integer sex,
                                 @RequestParam("medicalhistory") String medicalhistory,
                                 @RequestParam("diagnosis") String diagnosis) throws IOException {

        Info info = new Info();
        info.setName(name);
        info.setAge(age);
        info.setSex(sex);
        info.setMedicalhistory(medicalhistory);
        info.setDiagnosis(diagnosis);
        infoMapper.insert(info);

        return "main";
    }

    @GetMapping("/patient_info_table")
    public String patient_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, Model model){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id,name,age,sex,medicalhistory,diagnosis").orderByAsc("name");
        //构造分页参数
        Page<Info> ipage = new Page<>(pn,2);
        //调用page进行分页
        Page<Info> infoPage = infoService.page(ipage,queryWrapper);

        infoPage.getRecords();
        infoPage.getCurrent();
        infoPage.getPages();
        infoPage.getTotal();
        System.out.println(infoPage);
        System.out.println(infoPage.getRecords());
        System.out.println(infoPage.getCurrent());
        System.out.println(infoPage.getPages());;
        System.out.println(infoPage.getTotal());


        model.addAttribute("infos",infoPage);

//        return "table/patient_table";
        return "patient/info";
    }

    @PostMapping("/patient_info_search")
    public String patient_search(String searchname, Model model){

        //表格内容遍历

        //从数据库中查出user表中的用户进行展示

//        List<Patient> plist = patientService.list();


        //构造分页参数
        Page<Info> ipage = new Page<>(1,5);
        //条件封装
        QueryWrapper<Info> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,name,age,sex,medicalhistory,diagnosis");
        queryWrapper.like(true, "name", searchname);
        //调用page进行分页
        Page<Info> infoPage = infoService.page(ipage,queryWrapper);



        infoPage.getRecords();
        infoPage.getCurrent();
        infoPage.getPages();
        infoPage.getTotal();


        model.addAttribute("infos",infoPage);

//        return "table/patient_table";
        return "patient/info";
    }


    @GetMapping("/patient_info/update/{id}")
    public String toupadatePatient(@PathVariable("id") Long id, Model model){

        Info info = infoService.getById(id);
        model.addAttribute("updatepatientinfo",info);


        return "patient/infoupdate";
    }

    @PostMapping("/updateinfo")
    public String upload(@RequestParam("id") Long id,
                         @RequestParam("name") String name,
                         @RequestParam("age") Integer age,
                         @RequestParam("sex") Integer sex,
                         @RequestParam("medicalhistory") String medicalhistory,
                         @RequestParam("diagnosis") String diagnosis) throws IOException {


        Info info = infoMapper.selectById(id);
        info.setName(name);
        info.setAge(age);
        info.setSex(sex);
        info.setMedicalhistory(medicalhistory);
        info.setDiagnosis(diagnosis);
        infoMapper.updateById(info);

        return "main";
    }

}
