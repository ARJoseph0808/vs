package com.bishe.visualizationsystem.admin.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joseph
 * @date 2022/3/11
 * @apiNote
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("info")
public class Info {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private Integer sex;
    private String medicalhistory;
    private String diagnosis;

}
