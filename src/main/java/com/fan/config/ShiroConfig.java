package com.fan.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.fan.utils.CustomFormAuthenticationFilter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        Map<String,String> filterMap=new LinkedHashMap<>();
        filterMap.put("/emps","perms[user:admin]");
//        filterMap.put("/user/update","perms[user:update]");//url-权限
//        filterMap.put("/user/update","authc");
        filterMap.put("/user/login","anon");
        filterMap.put("/empqiandao/**","anon");
        filterMap.put("/validatecodeServlet","anon");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        shiroFilterFactoryBean.setLoginUrl("/user/login");//请求url
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        Map<String, Filter> filters=new HashMap<String, Filter>() ;
        filters.put("authc",new CustomFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager SecurityManager = new DefaultWebSecurityManager();
        SecurityManager.setRealm(userRealm);
        return SecurityManager;

    }

    //创建Realm对象，需要自定义类
//    @Bean
//    public UserRealm userRealm(){
//        return new UserRealm();
//    }
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();

        // 设置加密算法
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("md5");
        // 设置加密次数
        credentialsMatcher.setHashIterations(1);
        userRealm.setCredentialsMatcher(credentialsMatcher);

        return userRealm;
    }
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}
