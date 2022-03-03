package com.bishe.visualizationsystem.admin.controller;

import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.Users;
import com.bishe.visualizationsystem.admin.mapper.PatientMapper;
import com.bishe.visualizationsystem.admin.mapper.UsersMapper;
import com.bishe.visualizationsystem.admin.service.PatientService;
import com.bishe.visualizationsystem.admin.test.TxtToJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * @author Joseph
 * @date 2021/12/22
 * @apiNote 文件上次测试
 */
@Slf4j
@Controller
public class formTestController {

    @Autowired
    PatientService patientService;


    @Autowired
    PatientMapper patientMapper;

    @Autowired
    UsersMapper usersMapper;

    TxtToJson txtToJson = new TxtToJson();


    @GetMapping("/patient_add")
    public String patient_add(){
        return "patient/add";
    }

    @GetMapping("/user_add")
    public String user_add(){
        return "user/add";
    }







    @PostMapping("/patient_upload")
    public String patient_upload(@RequestParam("name") String name,
                                 @RequestParam("age") Integer age,
                                 @RequestParam("sex") Integer sex,
                                 @RequestParam("collectdata") MultipartFile collectdata,
                                 @RequestParam("analysisdata") MultipartFile analysisdata, HttpSession session) throws IOException {

        log.info("上传的信息，name={},age={},sex={}",
                name,age,sex);


        String collectdatapath = "";
        String analysisdatapath = "";
        Users u = (Users) session.getAttribute("loginUser");


        if(!collectdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename1 = collectdata.getOriginalFilename();
            collectdata.transferTo(new File("/root/txt/"+name+u.getName()+originalFilename1));
            //TxtToJson

            collectdatapath = name+u.getName()+originalFilename1;
            String path = "/root/txt/"+name+u.getName()+originalFilename1;
            txtToJson.txtToJson(path);
        }
        if(!analysisdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename2 = analysisdata.getOriginalFilename();
            analysisdata.transferTo(new File("/root/txt/"+name+u.getName()+originalFilename2));
            //TxtToJson
            analysisdatapath = name+u.getName()+originalFilename2;
            String path = "/root/txt/"+name+u.getName()+originalFilename2;
            txtToJson.txtToJson(path);
        }

        Patient patient = new Patient();
        patient.setAge(age);
        patient.setName(name);
        patient.setSex(sex);
        patient.setAnalysisdatapath(analysisdatapath);
        patient.setCollectdatapath(collectdatapath);
        patientMapper.insert(patient);


        return "main";
    }

    @PostMapping("/user_upload")
    public String user_upload(@RequestParam("name") String name,
                                 @RequestParam("password") String password,
                                 @RequestParam("role") Integer role) throws IOException {

        log.info("上传的信息，name={},password={},role={}",
                name,password,role);

        Users users = new Users();
        users.setName(name);
        users.setPassword(password);
        users.setRole(role);
        usersMapper.insert(users);



        return "main";
    }


}
