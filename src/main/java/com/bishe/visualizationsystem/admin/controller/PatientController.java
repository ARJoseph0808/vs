package com.bishe.visualizationsystem.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.visualizationsystem.admin.Util.TxtToJson;
import com.bishe.visualizationsystem.admin.bean.Info;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.bean.User;
import com.bishe.visualizationsystem.admin.mapper.InfoMapper;
import com.bishe.visualizationsystem.admin.mapper.PatientMapper;
import com.bishe.visualizationsystem.admin.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Joseph
 * @date 2022/3/11
 * @apiNote
 */
@Controller
public class PatientController {
    @Autowired
    PatientService patientService;
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    InfoMapper infoMapper;

    @Value("${upload.path}")
    String uploadpath;

    @GetMapping("/patient_add")
    public String patient_add(){
        return "patient/add";
    }

    @PostMapping("/patient_upload")
    public String patient_upload(@RequestParam("name") String name,
                                 @RequestParam("collectdata") MultipartFile collectdata,
                                 @RequestParam("analysisdata") MultipartFile analysisdata, HttpSession session) throws IOException {




        String collectdatapath = "";
        String analysisdatapath = "";
        User u = (User) session.getAttribute("loginUser");


        if(!collectdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename1 = collectdata.getOriginalFilename();
            collectdata.transferTo(new File(uploadpath+name+u.getName()+originalFilename1));
            //TxtToJson
            TxtToJson txtToJson = new TxtToJson();
            collectdatapath = name+u.getName()+originalFilename1;
            String path = uploadpath+name+u.getName()+originalFilename1;
            txtToJson.txtToJson(path);
        }
        if(!analysisdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename2 = analysisdata.getOriginalFilename();
            analysisdata.transferTo(new File(uploadpath+name+u.getName()+originalFilename2));
            //TxtToJson
            TxtToJson txtToJson = new TxtToJson();
            analysisdatapath = name+u.getName()+originalFilename2;
            String path = uploadpath+name+u.getName()+originalFilename2;
            txtToJson.txtToJson(path);
        }

        Patient patient = new Patient();
        patient.setName(name);
        patient.setAnalysisdatapath(analysisdatapath);
        patient.setCollectdatapath(collectdatapath);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        patient.setCreatetime(sdf.format(d));
        patientMapper.insert(patient);

        Info info = (Info) session.getAttribute("info");

        String r = "redirect:/patient_table?id=" + info.getId();
        return r;
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id,
                                @RequestParam(value = "pn",defaultValue = "1")Integer pn,HttpSession session,
                                RedirectAttributes ra){
        Info info = (Info) session.getAttribute("info");
        patientService.removeById(id);

        ra.addAttribute("pn",pn);
        String r = "redirect:/patient_table?id=" + info.getId();
        return r;
    }

    @GetMapping("/patient/echarts/{id}")
    public String echartsPatient(@PathVariable("id") Long id, HttpSession session){

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

    @GetMapping("/patient/update/{id}")
    public String toupadatePatient(@PathVariable("id") Long id, Model model){

        Patient patient = patientService.getById(id);
        model.addAttribute("updatepatient",patient);


        return "patient/update";
    }

    @PostMapping("/update")
    public String upload(@RequestParam("id") Long id,
                         @RequestParam("name") String name,
                         @RequestParam("collectdata") MultipartFile collectdata,
                         @RequestParam("analysisdata") MultipartFile analysisdata,
                         @RequestParam("collectdatapath") String collectdatapath,
                         @RequestParam("analysisdatapath") String analysisdatapath, HttpSession session) throws IOException {

        User u = (User) session.getAttribute("loginUser");

        if(!collectdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename1 = collectdata.getOriginalFilename();
            collectdata.transferTo(new File(uploadpath+name+u.getName()+originalFilename1));
            collectdatapath = name+u.getName()+originalFilename1;
            TxtToJson txtToJson = new TxtToJson();
            String path = uploadpath+name+u.getName()+originalFilename1;
            txtToJson.txtToJson(path);
        }
        if(!analysisdata.isEmpty()){
            //保存至服务器或者磁盘
            String originalFilename2 = analysisdata.getOriginalFilename();
            analysisdata.transferTo(new File(uploadpath+name+u.getName()+originalFilename2));
            analysisdatapath = name+u.getName()+originalFilename2;
            TxtToJson txtToJson = new TxtToJson();
            String path = uploadpath+name+u.getName()+originalFilename2;
            txtToJson.txtToJson(path);
        }

        Patient patient = patientMapper.selectById(id);
        patient.setAnalysisdatapath(analysisdatapath);
        patient.setCollectdatapath(collectdatapath);
        patientMapper.updateById(patient);

        return "main";
    }

    @GetMapping("/patient_table")
    public String patient_table(@RequestParam(value="pn",defaultValue = "1") Integer pn, @RequestParam("id") Long id,HttpSession session,Model model){
        Info info = infoMapper.selectById(id);
        String name = info.getName();
        //构造分页参数
        Page<Patient> ppage = new Page<>(pn,2);
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

//        return "table/patient_table";
        return "patient/table";
    }

}
