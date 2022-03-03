package com.bishe.visualizationsystem.admin.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Joseph
 * @date 2021/12/26
 * @apiNote
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("users")
public class Users {
    /**
     * 所有属性都应该在数据库中
     */

    //以下是数据库的字段
    @TableId(type = IdType.AUTO)
    private Long id;
    private String password;
    private String name;
    private Integer role;




}
