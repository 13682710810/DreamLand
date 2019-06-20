package wang.dreamland.www.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.UserContentService;
import wang.dreamland.www.service.UserService;
import wang.dreamland.www.common.PageHelper.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class BaseController {
    public static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UserService userService;

    public boolean isLogin(Long id){
        if (id != null){
            User user = userService.findById(id);
            if (user != null){
                return true;
            }
        }
        return false;
    }

    //判断用户是否登录
    public boolean userIsLogined(){
        User user = (User)getSession().getAttribute("user");
        if (user == null){
            return false;
        }
        return true;
    }

    public User getUser(Long id){
        User user = userService.findById(id);
        return user;
    }

    public List<UserContent> getUserContentList(Long uid){
        List<UserContent> list = userContentService.findByUserId(uid);
        return list;
    }

    public List<UserContent> getAllUserContentList(){
        List<UserContent> list = userContentService.findAll();
        return list;
    }

    public Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize){
        Page<UserContent> page = userContentService.findAll(content, pageNum, pageSize);
        return page;
    }

    public Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize){
        Page<UserContent> page = userContentService.findAll(content, comment, pageNum, pageSize);
        return page;
    }

    public Page<UserContent> findAll(Integer pageNum, Integer pageSize){
        Page<UserContent> page = userContentService.findAll(pageNum ,pageSize);
        return page;
    }

    public Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize){
        Page<UserContent> page = userContentService.findAllByUpvoteNum( content,pageNum ,pageSize);
        return page;
    }
    //获取request
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }
    //获取response
    public static HttpServletResponse getResponse(){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
    //获取session
    public static HttpSession getSession(){
        HttpSession session = null;
        try{
            session = getRequest().getSession();
        }catch (Exception e){}
        return session;
    }
    //获取客户端ip地址（可以穿透代理）
    public static String getClientIpAddress(){
        HttpServletRequest request = getRequest();
        for (String header: HEADERS_TO_TRY){
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)){
                return ip;
            }
        }
        return request.getRemoteAddr(); //返回客户端的ip地址
    }

}
