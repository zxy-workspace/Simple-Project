package com.simple.web.controller.system;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.simple.common.core.redis.RedisCache;
import com.simple.common.utils.uuid.IdUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.simple.common.constant.Constants;
import com.simple.common.core.domain.AjaxResult;
import com.simple.common.core.domain.entity.SysMenu;
import com.simple.common.core.domain.entity.SysUser;
import com.simple.common.core.domain.model.LoginBody;
import com.simple.common.core.domain.model.LoginUser;
import com.simple.common.utils.ServletUtils;
import com.simple.framework.web.service.SysLoginService;
import com.simple.framework.web.service.SysPermissionService;
import com.simple.framework.web.service.TokenService;
import com.simple.system.service.ISysMenuService;

/**
 * 登录验证
 * 
 * @author zhang.xinyu
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/ssoLogin")
    public AjaxResult ssoLogin(@RequestBody LoginBody loginBody){
        String token = loginService.ssoLogin(loginBody.getUsername());
        return new AjaxResult(200,"成功！",token);
    }

    @GetMapping("/rabbitMessage")
    public AjaxResult rabbitMessage(){
        String messageId = String.valueOf(IdUtils.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("topicExchange", "#", map);
        return new AjaxResult(200,"操作成功",map);
    }

    @Autowired
    private RedisCache redisCache;

    @GetMapping("text")
    public void text(){
        redisCache.setCacheObject("zhang","123");
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

}
