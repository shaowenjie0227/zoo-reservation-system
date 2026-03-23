package com.example.backend.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * @Author 
 * @Description 订单工具类
 * @Version 1.0
 */
public class OrdersUtils {

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    public static String generateOrderNo() {
        // 随机4位字母
        String randomString = RandomStringUtils.randomAlphabetic(4);

        // 生成6位数字 (000000-999999)
        int randomNumber = new Random().nextInt(1000000);

        // 格式化数字为6位，不足补零
        String formattedNumber = String.format("%06d", randomNumber);

        // 组合成10位订单编号
        return randomString + formattedNumber;
    }
}
