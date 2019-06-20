package wang.dreamland.www.controller;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.SolrService;
import wang.dreamland.www.service.UserContentService;

import java.util.Date;

@Controller
public class WriteController extends BaseController {
    private final static Logger log = Logger.getLogger(WriteController.class);

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private SolrService solrService;

    //进入写博客页面
    @RequestMapping("/writedream")
    public String writedream(Model model, @RequestParam(value = "cid",required = false)Long cid){
        User user = (User) getSession().getAttribute("user");
        //文章id不为空说明是修改文章操作
        if (cid != null){
            UserContent content = userContentService.findById(cid);
            model.addAttribute("cont",content);
        }
        model.addAttribute("user", user);
        return "write/writedream";
    }


    //发表梦按钮
    @RequestMapping("/doWritedream")
    public String doWritedream(Model model, @RequestParam(value = "id",required = false) String id,
                               @RequestParam(value = "cid",required = false) Long cid,
                               @RequestParam(value = "category",required = false) String category,
                               @RequestParam(value = "txtT_itle",required = false) String txt_Title,
                               @RequestParam(value = "content",required = false) String content,
                               @RequestParam(value = "private_dream",required = false) String private_dream){
        log.info("调用writeController");
        if(private_dream == null){
            private_dream = "off";
        }
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            //未登录
            model.addAttribute( "error","请先登录！" );
            return "../login";
        }
        UserContent userContent;
        //编辑梦
        if(cid != null){
            userContent = userContentService.findById(cid);
        }
        else{
            userContent = new UserContent();
        }
        userContent.setCategory(category);
        userContent.setContent(content);
        userContent.setRptTime(new Date());
        String imgUrl = user.getImgUrl();
        if(StringUtils.isBlank(imgUrl)){
            String webRoot = getRequest().getSession().getServletContext().getContextPath();
            userContent.setImgUrl(webRoot + "/images/icon_m.jpg");
        }else {
            userContent.setImgUrl(imgUrl);
        }
        //根据前台传来的参数判断是否为私密梦
        if("on".equals(private_dream)){
            userContent.setPersonal( "1" );
        }else{
            userContent.setPersonal( "0" );
        }
        userContent.setTitle(txt_Title);
        userContent.setuId(user.getId());
        userContent.setNickName(user.getNickName());
        //如果cid为null，则为新写的梦，这将梦的点赞数和评论数设为0
        if (cid == null){
            userContent.setUpvoteNum(0);
            userContent.setCommentNum(0);
            userContentService.addContent(userContent);
            solrService.addUserContent(userContent); //更新文章数据到solr数据库
        }else{
            userContentService.updateById(userContent);
            solrService.updateUserContent(userContent);
        }
        model.addAttribute("content", userContent);
        return "write/writesuccess";
    }

    /**
     * writesuccess页面查看梦点击按钮
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/watch")
    public String watchContent(Model model, @RequestParam(value = "cid",required = false) Long cid){
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            //未登录
            model.addAttribute( "error","请先登录！" );
            return "../login";
        }
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("cont", userContent);
        return "personal/watch";
    }
}
