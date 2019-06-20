package wang.dreamland.www.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.dreamland.www.common.Constants;
import wang.dreamland.www.common.DateUtils;
import wang.dreamland.www.common.MD5Util;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.OpenUser;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.entity.UserInfo;
import wang.dreamland.www.service.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PersonalController extends BaseController {
    private final static Logger log = Logger.getLogger(PersonalController.class);

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private SolrService solrService;


    /*
    * 初始化个人主页数据
    * */
    @RequestMapping("/list")
    public String findList(Model model, @RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "manage",required = false) String manage) {
        User user = (User) getSession().getAttribute("user");
        UserContent content = new UserContent();
        UserContent personalContent = new UserContent();
        if (user != null) {
            model.addAttribute("user", user);
            content.setuId(user.getId());
            personalContent.setuId(user.getId());
        } else {
            return "../login";
        }
        log.info("初始化个人主页信息");

        if(StringUtils.isNotBlank(manage)){
            model.addAttribute("manage",manage);
        }

        //查询梦分类
        List<UserContent> categorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys", categorys);
        //发布的梦，不含私密梦
        content.setPersonal("0");
        pageSize = 4;
        PageHelper.Page<UserContent> page = findAll(content, pageNum, pageSize);
        model.addAttribute("page", page);

        //查询私密梦
        personalContent.setPersonal("1");
        PageHelper.Page<UserContent> page2 = findAll(personalContent, pageNum, pageSize);
        model.addAttribute("page2", page2);

        //查询热梦
        UserContent hitContent = new UserContent();
        hitContent.setPersonal("0");
        PageHelper.Page<UserContent> hotPage = findAllByUpvote(hitContent, pageNum, pageSize);
        model.addAttribute("hotPage", hotPage);
        return "personal/personal";
    }

    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String, Object> findByCategory(Model model, @RequestParam(value = "category", required = false) String category,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map map = new HashMap<String, Object>();
        User user = (User)getSession().getAttribute("user");
        if (user == null){
            map.put("pageCate","fail");
            return map;
        }
        pageSize = 4;
        //根据分类名称、用户 ID、页码和每页显示记录数查询出分页数据，存入 map 中。
        PageHelper.Page<UserContent> pageCate = userContentService.findByCategory(category, user.getId(), pageNum, pageSize);
        map.put("pageCate", pageCate);
        return map;
    }


    /*
    * 根据用户id查询私密梦
    * */
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String,Object> findPersonal(Model model,@RequestParam(value = "pageNum",required = false) Integer pageNum ,
                                           @RequestParam(value = "pageSize",required = false) Integer pageSize) {

        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user==null) {
            map.put("page2","fail");
            return map;
        }
        pageSize = 4; //默认每页显示4条数据
        PageHelper.Page<UserContent> page = userContentService.findPersonal(user.getId(), pageNum, pageSize);
        map.put("page2",page);
        return map;
    }

    /*
    * 个人页面删除文章*/
    @RequestMapping("/deleteContent")
    public String deleteContent(Model model, @RequestParam(value = "cid",required = false) Long cid) {

        User user = (User)getSession().getAttribute("user");
        if(user==null) {
            return "../login";
        }
        //因为存在外键关系，删除文章之前先将 upvote 和 comment 表中与此文章 id 相关数据删除
        commentService.deleteByContentId(cid);
        upvoteService.deleteByContentId(cid);
        userContentService.deleteById(cid);
        solrService.deleteById(cid);  //同步删除solr索引库里对应的信息.
        return "redirect:/list?manage=manage";
    }

    /**
     * 进入个人资料修改页面
     */
    @RequestMapping("/profile")
    public String profile(Model model, @RequestParam(value = "email",required = false) String email,
                          @RequestParam(value = "password",required = false) String password,
                          @RequestParam(value = "phone",required = false) String phone) {
        User user = (User)getSession().getAttribute("user");
        if(user==null){
            return "../login";
        }
        if (StringUtils.isNotBlank(email)){
            user.setEmail(email);
            user.setPassword(MD5Util.encodeToHex(Constants.SALT + password));
            user.setPhone(phone);
            userService.update(user);
        }
        UserInfo userInfo =   userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);

        return "personal/profile";
    }


    /*
    * 保存个人头像
    * */
    @RequestMapping("/saveImage")
    @ResponseBody
    public  Map<String, Object> saveImage(Model model, @RequestParam(value = "url", required = false) String url){
        Map map = new HashMap<String, Object>();
        User user = (User)getSession().getAttribute("user");
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg", "success");
        return map;
    }


     /*
     * 点击修改个人信息页面的保存按钮
     * */
    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model, @RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "nick_name",required = false)String nickName,
                               @RequestParam(value = "sex",required = false)String sex,
                               @RequestParam(value = "address",required = false)String address,
                               @RequestParam(value = "birthday",required = false) String birthday){
        User user = (User)getSession().getAttribute("user");
        if (user == null){
            return "../login";
        }
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        boolean flag = false;
        if (userInfo == null){
            userInfo = new UserInfo();
        }else {
            flag = true;
        }
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date newBirthday = DateUtils.StringtoDate(birthday, "yyyy-MM-dd");
        userInfo.setBirthday(newBirthday);
        if (!flag){
            userInfoService.add(userInfo);
        }else{
            userInfoService.update(userInfo);
        }
        user.setNickName(nickName);
        userService.update(user);
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        return "personal/profile";
    }

    /*
    * 进入修改密码页面
    * */
    @RequestMapping("/repassword")
    public String repassword(Model model){
        User user = (User)getSession().getAttribute("user");
        if (user != null){
            model.addAttribute("user", user);
            return "personal/repassword";
        }
        return "../login";
    }


    /*
    * 修改密码操作
    * */
    @RequestMapping("/updatePassword")
    public String updatePassword(Model model, @RequestParam(value = "old_password",required = false) String oldPassword,
                                 @RequestParam(value = "password",required = false) String password){
        User user = (User)getSession().getAttribute("user");
        if (user != null){
            oldPassword = MD5Util.encodeToHex(Constants.SALT + oldPassword);
            if (user.getPassword().equals(oldPassword)){
                password = MD5Util.encodeToHex(Constants.SALT + password);
                user.setPassword(password);
                userService.update(user);
                model.addAttribute("message", "success");
            }else {
                model.addAttribute("message", "fail");
            }
        }
        model.addAttribute("user", user);
        return "personal/passwordSuccess";
    }






}
