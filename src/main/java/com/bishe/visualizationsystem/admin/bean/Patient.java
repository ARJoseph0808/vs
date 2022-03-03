package com.bishe.visualizationsystem.admin.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joseph
 * @date 2021/12/29
 * @apiNote
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("patient")
public class Patient {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private Integer sex;//男1女0
    private String collectdatapath;
    private String analysisdatapath;




}
