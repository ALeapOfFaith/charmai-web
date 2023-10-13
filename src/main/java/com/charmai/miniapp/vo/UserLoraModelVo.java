package com.charmai.miniapp.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数字分身
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-03 23:56:44
 */
@Data
public class UserLoraModelVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    private String loraName;
    /**
     * 记录创建的时间
     */
    private Date createTime;

}
