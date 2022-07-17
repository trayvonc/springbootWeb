package com.fan.config;

import com.fan.pojo.User;
import com.fan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;
import java.util.Date;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行=>授权");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        simpleAuthorizationInfo.addStringPermission("user:add");

        //拿到当前对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        simpleAuthorizationInfo.addStringPermission(currentUser.getPerms());
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行=>认证");
//        String name="root";
//        String password="123456";


        HashedCredentialsMatcher credentialsMatcher  = new HashedCredentialsMatcher("md5");
        credentialsMatcher.setHashIterations(1);
        UsernamePasswordToken userToken=(UsernamePasswordToken) authenticationToken;
        User user = userService.queryUserByName(userToken.getUsername());

        if(user==null){
            return null;
        }




        return new SimpleAuthenticationInfo(user,user.getPwd(),"");

        // 模拟从Realm中获取的认证信息
//        String password = new SimpleHash("md5", user.getPwd()).toString();
//        System.out.println(password);
//        return new SimpleAuthenticationInfo("", password, "");
    }
}
