package com.charmai.common.core.domain.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮箱登录对象
 *
 * @author Lion Li
 */

@Data
public class EmailLoginBody {

    /**
     * 邮箱
     */
    @NotBlank(message = "{user.email.not.blank}")
    @Email(message = "{user.email.not.valid}")
    private String email;

    /**
     * 邮箱code
     */
    @NotBlank(message = "{email.code.not.blank}")
    private String emailCode;

}
