package com.kingland.neusoft.course.controller;

import com.kingland.neusoft.course.mapper.dao.UserModel;
import com.kingland.neusoft.course.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Resource
private UserService userService;

@Autowired
private HttpServletRequest request;

@Autowired
private HttpSession session;
@RequestMapping("userLogin.do")
public ModelAndView userLogin(User user) {
        ModelAndView mv = new ModelAndView();
        String resultStr = null;
        User tmpUser = userService.getUserByUserName(user.getUser_name());
        if (tmpUser == null) { // 先检查用户是否存在
        resultStr = new String("登录失败：用户不存在！");
        mv.setViewName("login.jsp");
        } else { // 用户存在时继续判断密码
        Boolean pwdCheck = user.getUser_password().equals(tmpUser.getUser_password());
        if (pwdCheck) { // 密码正确
        Date tmpDate = new Date(); // 获取当前时间
        tmpUser.setUser_lastLoginTime(tmpDate); // 将登录时间放入对象
        // 执行更新
        userService.modifyUserLastLoginTime(tmpUser);

        // 判断是否能正常登录
        if (tmpUser.getUser_status() != 1) { // 非禁用状态
        // 登录成功，将用户对象添加到session
        HttpSession session = request.getSession();
        session.setAttribute("USER", tmpUser);

        // 处理是否从查看贴子页面登录的
        String tipIdStr = null;
        if (request.getParameter("tipId") != null) {
        // 记录传过来的贴子id
        tipIdStr = request.getParameter("tipId");
        }
        if (tipIdStr == null || tipIdStr.equals("null")) {
        mv.setViewName("redirect:toMainPage.do");
        } else {
        // 如果用户是在贴子详情中登录的，返回对应的贴子
        mv.setViewName("redirect:showTip.do?tipId=" + tipIdStr);
        }
        } else { // user_status == 1 表示被禁用，不能登录
        resultStr = new String("登录失败：用户已被禁用！请联系管理员（626753724@qq.com）。");
        mv.setViewName("login.jsp");
        }
        } else { // 密码不正确
        resultStr = new String("登录失败！密码不正确！");
        mv.setViewName("login.jsp");
        }
        }
        request.setAttribute("myInfo", resultStr);
        return mv;
        }
