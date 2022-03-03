package com.bishe.visualizationsystem.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.visualizationsystem.admin.bean.Patient;
import com.bishe.visualizationsystem.admin.mapper.PatientMapper;
import com.bishe.visualizationsystem.admin.service.PatientService;
import org.springframework.stereotype.Service;

/**
 * @author Joseph
 * @date 2021/12/29
 * @apiNote
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

}
