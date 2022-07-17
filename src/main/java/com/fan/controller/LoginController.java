package com.fan.controller;

import com.fan.pojo.User;
import com.fan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @RequestMapping("/user/login")
    public String login(String username, String password, Model model, HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            Session session = subject.getSession();
            session.setAttribute("loginUser",username);
            //增加是否签到
            User user=userService.queryUserByName(username);
//            session.setAttribute("userId",user.getId());
//            System.out.println(user.getId());
//            //首先获取session
//            HttpSession session1 = request.getSession();
//            //往session中存入你想要的东西
//            session1.setAttribute("userId",user.getId());
//            System.out.println(session1.getAttribute("userId"));

            Date lastDate=user.getSign();
            Date nowDate=new Date();
            int lastDay = Integer.parseInt(String.format("%td",lastDate));
            int nowDay = Integer.parseInt(String.format("%td",nowDate));
//            System.out.println(lastDay);
//            System.out.println(nowDay);
            if(lastDay!=nowDay){
                session.setAttribute("hasSign","签到");
                System.out.println("签到");
            }
            else{
                session.setAttribute("hasSign","已签到");
                System.out.println("已签到");
            }
//            session.setAttribute("hasSign","true");
            return "redirect:/main.html";
        }catch (UnknownAccountException e){
            model.addAttribute("msg","用户名不存在");
            return "index";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","密码错误");
            return "index";
        }



    }
//    @ResponseBody
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password, Model model, HttpSession session){
//        if(!StringUtils.isEmpty(username)&&password.equals("123456")) {
//            session.setAttribute("loginUser",username);
//            return "redirect:/main.html";
//        }else{
//            model.addAttribute("msg","用户名或者密码错误");
//            return "index";
//        }
//    }
    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }
}
