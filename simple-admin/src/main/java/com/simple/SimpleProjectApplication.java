package com.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author zhangxinyu
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SimpleProjectApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SimpleProjectApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
