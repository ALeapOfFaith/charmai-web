package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 数字分身
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-03 23:56:44
 */
@Data
@TableName("user_lora_model")
public class UserLoraModelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 仅用于唯一标识记录，无其他用途
     */
    @TableId
    private Integer id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 数字分身的存储地址
     */
    private String loraUrl;
    private String loraName;
    /**
     * 逻辑删除标识，del_flag=0表示该数字分身正在使用中
     */
    private Integer delFlag;
    /**
     * 记录创建的时间
     */
    private Date createTime;

}
