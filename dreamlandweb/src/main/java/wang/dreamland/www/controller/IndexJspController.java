package wang.dreamland.www.controller;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import wang.dreamland.www.common.DateUtils;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.common.StringUtil;
import wang.dreamland.www.dao.UserMapper;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.Upvote;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class IndexJspController extends BaseController {
    private final static Logger log = Logger.getLogger(IndexJspController.class);
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @Autowired
    private SolrService solrService;

    @RequestMapping("/index_list")
    public String findAllList(Model model, @RequestParam(value = "keyword", required = false)String keyword,
                              @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "pageSize",required = false) Integer pageSize){
        log.info("-------------进入index__list------------");
        User user = (User)getSession().getAttribute("user");
        if (user != null){
            model.addAttribute("user", user);
        }
        if (StringUtils.isNotBlank(keyword)){
            PageHelper.Page<UserContent> page = solrService.findByKeyWords(keyword, pageNum, pageSize);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        }else {
            PageHelper.Page<UserContent> page =  findAll(pageNum, pageSize);
            model.addAttribute("page", page);
        }
        return "../index";
    }

    /**
    *点赞或踩操作
    *cid：文章id
    * uid：用户id
    * upvote：常量，点赞为1， 踩为-1
    */
    @RequestMapping("/upvote")
    @ResponseBody
    public Map<String,Object> upvote(Model model, @RequestParam(value = "id",required = false) long cid,
                                     @RequestParam(value = "uid",required = false) Long uid,
                                     @RequestParam(value = "upvote",required = false) int upvote) {
        log.info( "cid="+cid+",uid="+uid+"upvote="+upvote );
        Map map = new HashMap<String, Object>();
        if (!userIsLogined()){
            map.put("data", "fail");
        }
        Upvote newUpv;
        newUpv = new Upvote();
        newUpv.setContentId(cid);
        User upvoteUser = (User)getSession().getAttribute("user");
        newUpv.setuId(upvoteUser.getId());
        //通过文章id和用户id查找upvote
        Upvote findUpv = upvoteService.findByUidAndConId(newUpv);
        //通过文章id找出对应文章
        UserContent userContent = userContentService.findById(cid);
        //如果传递过来的参数upvote的值为1或者-1来判断用户是进行点赞或者踩操作
        if (upvote == -1){
            //判断该用户和改文章对应的upvote对象是否存在
            if (findUpv != null){
                //如果findUpv对象的dowvote属性为"1",则说明指定用户已经对指定文章进行踩操作
                if ("1".equals(findUpv.getDownvote())){
                    map.put("data", "down");
                    return map;
                }else{
                    //如果findUpv对象的downvote属性不为"1",则说明指定用户还没有对指定文章进行踩操作
                    findUpv.setDownvote("1");                //设置该对象的downvote属性为"1"
                    findUpv.setUpvoteTime(new Date());       //设置操作时间
                    findUpv.setIp(getClientIpAddress());     //设置操作ip
                    upvoteService.update(findUpv);           //更新相应的upvote对象
                }
            }else if (findUpv == null){
                newUpv.setDownvote("1");       //对newUpv对象的downvote属性设置为"1"
                newUpv.setUpvoteTime(new Date());
                newUpv.setIp(getClientIpAddress());
                upvoteService.add(newUpv);     //在表中添加一个新的newUpv对象
            }
            if (userContent.getUpvoteNum() > 0){
                userContent.setUpvoteNum(userContent.getUpvoteNum() - 1);//如果文章的点赞数大于0，则踩一次文章的点赞数-1
            }else {
                userContent.setUpvoteNum(0);
            }
        }
        else if (upvote == 1){
            if (findUpv != null){
                if ("1".equals(findUpv.getUpvote())){
                    map.put("data", "up");
                    return map;
                }else {
                    findUpv.setUpvote("1");
                    findUpv.setUpvoteTime(new Date());
                    findUpv.setIp(getClientIpAddress());
                    upvoteService.update(findUpv);
                }
            }else if(findUpv == null){
                newUpv.setUpvote("1");
                newUpv.setUpvoteTime(new Date());
                newUpv.setIp(getClientIpAddress());
                upvoteService.add(newUpv);
            }
            userContent.setUpvoteNum(userContent.getUpvoteNum() + 1);  //点赞一次文章的点赞数upvoteNum+1
        }
        userContentService.updateById(userContent);     //更新文章
        map.put("data", "success");
        return map;
    }

    //点击评论数或者评论图标操作
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> reply(Model model, @RequestParam(value = "content_id", required = false)Long content_id){
        Map map = new HashMap<String, Object>();
        //根据文章 id 查找出所有的一级评论列表
        List<Comment> list = commentService.findAllFirstComment(content_id);
        if (list != null && list.size() > 0){
            //遍历一级评论列表
            for (Comment c : list){
                //根据文章 id 和一级评论的子评论id(多个id用","号隔开)的字符串查询子评论列表
                List<Comment> childComments = commentService.findAllChildrenComment(c.getConId(), c.getChildren());
                if (childComments != null && childComments.size() > 0){
                    for (Comment childCom: childComments){
                        if (childCom.getById() != null){
                            User byUser = userService.findById(childCom.getById());
                            childCom.setByUser(byUser);
                        }
                    }
                }
                //将子评论列表注入到一级评论的 comList 属性中
                c.setComList(childComments);
            }
        }
        map.put("list", list);
        return map;
    }

    /*
    *发表评论或者评论点赞操作
    * id：评论id
    * content_id：文章id
    * uid：评论者id
    * by_id：被评论者id
    * oSize：评论内容
    * comment_Time：评论时间
    * upvote：点赞数*/
    @RequestMapping("/comment")
    @ResponseBody
    public Map<String, Object> comment(Model model, @RequestParam(value = "id", required = false)Long id,
                                       @RequestParam(value = "content_id", required = false)Long content_id,
                                       @RequestParam(value = "uid",required = false) Long uid ,
                                       @RequestParam(value = "by_id",required = false) Long bid ,
                                       @RequestParam(value = "oSize",required = false) String oSize,
                                       @RequestParam(value = "comment_time",required = false) String comment_time,
                                       @RequestParam(value = "upvote",required = false) Integer upvote){
        Map map = new HashMap<String, Object>();
        if (!userIsLogined()){
            map.put("data", "fail");
            return map;
        }
        //判断评论id是否为 null，为 null 则说明是添加评论，不为null则为评论点赞或取消评论的赞
        if (id == null){
            Date date = DateUtils.StringtoDate(comment_time, "yyyy-MM-dd HH:mm:ss");
            Comment comment = new Comment();
            User commentUser = userService.findById(uid);
            comment.setUser(commentUser);
            comment.setCommTime(date);
            comment.setComContent(oSize);
            comment.setConId(content_id);
            comment.setComId(uid);
            comment.setById(bid);
            comment.setUpvoteNum(0);
            commentService.add(comment);
            map.put("data", comment);
            //评论文章的评论数加1
            UserContent userContent = userContentService.findById(content_id);
            userContent.setCommentNum(userContent.getCommentNum() + 1);
            userContentService.updateById(userContent);
        }else {
            //点赞
            Comment c = commentService.findById(id);
            c.setUpvoteNum(c.getUpvoteNum() + upvote);
            commentService.update(c);
        }
        return map;
    }

    /**
     * 评论块的删除操作
    * id:评论id
    * uid:文章作者id
    * 文章id:con_id
    * 父评论id:fid
    * */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public Map<String, Object> deleteComment(Model model, @RequestParam(value = "id",required = false) Long id,
                                             @RequestParam(value = "uid",required = false) Long uid,
                                             @RequestParam(value = "con_id",required = false) Long con_id,
                                             @RequestParam(value = "fid",required = false) Long fid){
        int num = 0;
        Map map = new HashMap<String, Object>();
        User user = (User)getSession().getAttribute("user");
        if (user == null){
            map.put("data", "fail");
        }else {
            if (user.getId().equals(uid)) {
                //用户是文章作者，则根据评论id查询出评论对象 Comment，然后判断该评论是否含有子评论
                Comment comment = commentService.findById(id);
                if (StringUtils.isBlank(comment.getChildren())) {
                    //如果该评论没有子评论，判断该评论有没有父评论，有父评论，则将该评论id 从父评论的子评论列表中移除。
                    if (fid != null) {
                        Comment fcomm = commentService.findById(fid);
                        //自定义字符串处理函数，返回删除子评论列表中指定 id 后的字符串，参数是以逗号分隔的子评论 id 字符串和要删除的 id
                        String child = StringUtil.getString(fcomm.getChildren(), id);
                        fcomm.setChildren(child);
                        commentService.update(fcomm);
                    }
                    commentService.deleteById(id);
                    num = num + 1;

                } else {
                    //如果该评论有子评论，删除所有子评论，然后删除该评论，将删除的评论数赋值给 num
                    String children = comment.getChildren();
                    commentService.deleteChildrenComment(children);
                    String[] arr = children.split(",");
                    commentService.deleteById(id);
                    num = num + arr.length + 1;  //0 + 所有子评论数（arr.length） + 该评论(1)
                }
                //根据文章 id 查询出文章对象 UserContent
                UserContent content = userContentService.findById(con_id);
                //如果该对象不为空
                if (content != null) {
                    if (content.getCommentNum() - num >= 0) {
                        //如果删除的评论数在该文章总评论数范围之内，则更新删除评论之后的评论数
                        content.setCommentNum(content.getCommentNum() - num);
                    } else {
                        //如果删除的评论数比文章总评论数还多，则更新为0
                        content.setCommentNum(0);
                    }
                    //更新文章
                    userContentService.updateById(content);
                }
                //将最新评论数放入map中
                map.put("data", content.getCommentNum());
            }else {
                //判断该用户是否是文章作者，不是则将 no-access 放入 map，键为“data”，值为 no-access
                map.put( "data","no-access" );
            }
        }
        return map;
    }

    /*
    评论块的评论操作
    * content_id:文章id
    * uid：评论用户id
    * 回复内容：oHfVal
    * 评论时间： now
    * 被评论者id: by_id
    * 一级评论id：id
    */
    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String, Object> addCommentChild(Model model, @RequestParam(value = "id",required = false) Long id ,
                                               @RequestParam(value = "content_id",required = false) Long content_id ,
                                               @RequestParam(value = "uid",required = false) Long uid ,
                                               @RequestParam(value = "by_id",required = false) Long bid ,
                                               @RequestParam(value = "oSize",required = false) String oSize,
                                               @RequestParam(value = "comment_time",required = false) String comment_time,
                                               @RequestParam(value = "upvote",required = false) Integer upvote){
        Map map = new HashMap<String, Object>();
        if (!userIsLogined()){
            map.put("data", "fail");
        }
        //将日期字符串转成自定义格式的日期 Date
        Date date = DateUtils.StringtoDate(comment_time, "yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment();
        comment.setComContent(oSize);
        comment.setCommTime(date);
        comment.setConId(content_id);
        comment.setComId(uid);
        //如果 upvote 为 null，则赋初始值0
        if (upvote == null){
            upvote = 0;
        }
        comment.setById(bid);
        comment.setUpvoteNum(comment.getUpvoteNum() + upvote);
        User commentUser = userService.findById(uid);
        comment.setUser(commentUser);
        //将 comment 信息设置完毕之后插入数据库，返回主键 id，此时该 comment 中 id 是有值的
        commentService.add(comment);

        Comment com = commentService.findById(id);
        //根据评论 id 查询出评论对象 com，判断该评论对象 com 是否有子评论，
        // 如果没有，将刚才添加的子评论 id 添加到 Comment 的 children 字段中。
        if (StringUtils.isBlank(com.getChildren())){
            com.setChildren(comment.getId().toString());
        }else {
            //如果该评论对象 com 已有子评论了，则将刚才添加的子评论的id以逗号形式拼接在子评论 id 后面
            com.setChildren(comment.getChildren() + "," + comment.getId());
        }
        //更新该评论对象 com
        commentService.update(com);
        //将评论对象 comment 放入 map
        map.put("data", comment);

        //根据文章 id 查询出文章对象 userContent，获取评论数，将其值加1，更新 userContent
        UserContent userContent = userContentService.findById( content_id );
        Integer num = userContent.getCommentNum();
        userContent.setCommentNum( num+1 );
        userContentService.updateById( userContent );
        return map;
    }



}
