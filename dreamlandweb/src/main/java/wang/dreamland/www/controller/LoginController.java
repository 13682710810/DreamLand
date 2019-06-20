package wang.dreamland.www.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.dreamland.www.common.Constants;
import wang.dreamland.www.common.MD5Util;
import wang.dreamland.www.common.RandStringUtils;
import wang.dreamland.www.entity.OpenUser;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.service.UserInfoService;
import wang.dreamland.www.service.UserService;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController extends BaseController {
    private final static Logger log = Logger.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate; //mq消息模板

    @Autowired
    private UserInfoService userInfoService;

    /*
    * 手机快捷登录和账号密码登录
    * */
    @RequestMapping("/doLogin")
    public String doLogin(Model model, @RequestParam(value = "username", required = false) String email,
                          @RequestParam(value = "password", required = false) String password,
                          @RequestParam(value = "code", required = false) String code,
                          @RequestParam(value = "state", required = false) String state,
                          @RequestParam(value = "pageNum", required = false) String pageNum,
                          @RequestParam(value = "pageSize", required = false) String pageSize,
                          @RequestParam(value = "telephone",required = false) String telephone,
                          @RequestParam(value = "phone_code",required = false) String phone_code){

        if(StringUtils.isNotBlank(telephone)){
            String yzm = redisTemplate.opsForValue().get(telephone);
            if(phone_code.equals(yzm)){
                User user = userService.findByPhone(telephone);
                getSession().setAttribute("user", user);
                model.addAttribute("user", user);
                return "redirect:/list";
            }else {
                model.addAttribute("error", "phone_fail");
                return "../login";
            }
        }else{
            //账号登录
            if(StringUtils.isBlank(code)){
                model.addAttribute("error", "fail");
                return "../login";
            }
            int b = checkValidateCode(code);
            if (b == -1){
                model.addAttribute("error", "fail");
                return "../login";
            }else if(b == 0){
                model.addAttribute("error", "fail");
                return "../login";
            }
            password = MD5Util.encodeToHex(Constants.SALT + password);
            User user = userService.login(email, password);
            if (user != null){
                if("0".equals(user.getState())){
                    model.addAttribute("email", email);
                    model.addAttribute("error", "active");
                    return "../login";
                }
                log.info("用户登录成功");
                getSession().setAttribute("user", user); //将用户信息保存到session中，以便之后的15分钟免登陆
                model.addAttribute("user", user);
                return "/personal/personal";
            }else {
                log.info("用户登录失败");
                model.addAttribute("email", email);
                model.addAttribute("error", "fail");
                return "../login";
            }
        }

    }

    /*
    * 检查验证码
    * */
    public int checkValidateCode(String code){
        Object vercode = getRequest().getSession().getAttribute("VERCODE_KEY");
        if(vercode == null){
            return -1;
        }
        if(!code.equalsIgnoreCase(vercode.toString())){
            return 0;
        }
        return 1;
    }

    /*
    * 实现15分钟免登陆
    * */
    @RequestMapping("/login")
    public String login(Model model){
        User user = (User)getSession().getAttribute("user"); //实现15分钟免登陆，获取 Session 中的User，如果 User 不为 null 则直接跳转到个人主页
        if (user != null){
            return "redirect:/list";
        }
        return "../login";
    }

    /*
    * 发送短信
    * */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Map<String, Object> index(Model model, @RequestParam(value="telephone", required = false)final String telephone){
        Map map = new HashMap<String, Object>();
        try{
            final String code = RandStringUtils.getCode();  //生成随机六位数字验证码
            //将6位随机验证码保存到 Redis 中，时效为60秒，key=手机号，value=验证码。
            redisTemplate.opsForValue().set(telephone, code, 60, TimeUnit.SECONDS);
            log.debug("...............短信验证码为" + code);
            //调用ActiveMQ jmsTemplate，发送一条消息给MQ
            jmsTemplate.send("login_msg", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("telephone", telephone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });
            System.out.println("111111111");
        }catch (Exception e){
            map.put("msg", false);
        }
        map.put("msg", true);
        return map;
    }

    /*
    * 退出登录
    * */
    @RequestMapping("/loginout")
    public String exit(Model model) {
        log.info( "退出登录" );
        getSession().removeAttribute( "user" );
        getSession().invalidate();  //使 Session 失效，释放资源
        return "../login";
    }

    @RequestMapping("/to_login")
    public String toLogin(Model model){
        HttpServletRequest request = getRequest();
        String url = "";
        try{
            //调用第三方接口 API 获取 URL，
            // URL 中携带了 client_id 即 AppID、Scope 和回调地址等，
            // 然后重定向到该 URL
            url = new Oauth().getAuthorizeURL(request);
        }catch (QQConnectException e){
            e.printStackTrace();
        }
        return "redirect:" + url;
    }




}
