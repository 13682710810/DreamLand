package wang.dreamland.www.dao;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import wang.dreamland.www.entity.Comment;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */
public interface CommentMapper extends Mapper<Comment> {
    //根据文章id查询所有评论
    List<Comment> selectAll(@Param("cid")long cid);

    //根据文章id查询所有一级评论
    List<Comment> findAllFirstComment(@Param("cid")long cid);

    //根据文章id和二级评论id
    List<Comment> findAllChildrenComment(@Param("cid")long cid, @Param("children")String children);

    //插入评论并返回主键id，返回值是影响行数，而id在comment中
    int insertComment(Comment comment);
}
