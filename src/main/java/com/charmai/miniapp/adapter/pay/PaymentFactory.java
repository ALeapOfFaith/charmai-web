package com.charmai.miniapp.adapter.pay;

import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-02-22:08
 * @Description:
 */
@Component
public class PaymentFactory {

    @Autowired
    List<Payment> payments;

    public Payment productPayment(Integer orderType) {
        Payment defaultPayment = null;
        for (Payment payment : payments) {
            if (orderType.equals(payment.getOrderType())) {
                defaultPayment = payment;
                break;
            }
        }
        if (ObjectUtils.isEmpty(defaultPayment)) {
            throw new ServiceException(ExceptionConstant.NOT_SUPPORT_ORDER_TYPE_EXCEPTION_MSG, ExceptionConstant.NOT_SUPPORT_ORDER_TYPE_EXCEPTION_CODE);
        } else {
            return defaultPayment;
        }

    }

}
