package wang.dreamland.www.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.SolrService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SolrServiceImpl implements SolrService {

    @Autowired
    HttpSolrClient httpSolrClient;

    public PageHelper.Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize) {
        //新建 SolrQuery 对象，然后设置查询条件，
        // title 为要查询的字段，keyword 为要查询的字段值。
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("title:" + keyword);
        //高亮显示必须设置 highlight 属性为 true。
        solrQuery.setHighlight(true);
        //设置高亮的字段 tilte。
        solrQuery.addHighlightField("title");
        //设置高亮的标签头，style 的 color 属性值为 red，即红色高亮显示，也可自定义别的颜色。
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        //设置高亮的标签尾。
        solrQuery.setHighlightSimplePost("</span>");

        //分页开始，设置开始索引和每页显示记录数，按照时间倒序。
        if (pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1){
            pageSize = 7;
        }
        solrQuery.setStart((pageNum - 1) * pageSize);
        solrQuery.setRows(pageSize);
        solrQuery.addSort("rpt_time", SolrQuery.ORDER.desc);

        try{
            //Solr 客户端对象根据查询条件查询 Solr 索引库，将查询结果放入 QueryResponse 对象中
            QueryResponse queryResponse = httpSolrClient.query(solrQuery);
            //根据 QueryResponse 对象获取高亮结果集
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            //根据QueryResponse对象获取所有数据结果集
            SolrDocumentList resultList = queryResponse.getResults();
            long totalNum = resultList.getNumFound();
            List<UserContent> list = new ArrayList<UserContent>();
            //遍历所有数据结果集，获取 Solr 索引库中的数据，将数据设置到文章对象中
            for (SolrDocument solrDocument: resultList){
                UserContent content = new UserContent();
                String id = (String)solrDocument.get("id");
                Object content1 = solrDocument.get("content");
                Object commentNum = solrDocument.get("comment_num");
                Object upvoteNum = solrDocument.get("upvote_num");
                Object nickName = solrDocument.get("nick_name");
                Object imgUrl = solrDocument.get("img_url");
                Object uid = solrDocument.get("u_id");
                Object rpt_time = solrDocument.get("rpt_time");
                Object category = solrDocument.get("category");
                Object personal = solrDocument.get("personal");

                //取得高亮数据集合中的文章标题,主要是title属性，将title设置到文章对象中
                Map<String, List<String>> map = highlighting.get(id);
                String title = map.get("title").get(0);

                content.setId(Long.parseLong(id));
                content.setCommentNum(Integer.parseInt(commentNum.toString()));
                content.setUpvoteNum(Integer.parseInt(upvoteNum.toString()));
                content.setNickName(nickName.toString());
                content.setImgUrl(imgUrl.toString());
                content.setuId(Long.parseLong(uid.toString()));
                content.setTitle(title);
                content.setPersonal(personal.toString());
                Date date = (Date)rpt_time;
                content.setRptTime(date);

                List<String> clist = (ArrayList)content1;
                content.setContent(clist.get(0).toString());
                content.setCategory(category.toString());
                //每遍历一次将结果添加到 list 集合，这样 list 集合就包含了所有的查询结果
                list.add(content);
            }
            PageHelper.startPage(pageNum, pageSize);
            PageHelper.Page page = PageHelper.endPage();
            page.setResult(list);
            page.setTotal(totalNum);
            return page;
        }catch (SolrServerException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUserContent(UserContent userContent) {
        if (userContent != null){
            addDocument(userContent);
        }
    }

    private void addDocument(UserContent userContent) {
        try{
            SolrInputDocument inputDocument = new SolrInputDocument();
            inputDocument.addField("comment_num", userContent.getCommentNum());
            inputDocument.addField("upvote_num", userContent.getUpvoteNum());
            inputDocument.addField("nick_name", userContent.getNickName());
            inputDocument.addField("img_url", userContent.getImgUrl());
            inputDocument.addField("rpt_time", userContent.getRptTime());
            inputDocument.addField("content", userContent.getContent());
            inputDocument.addField("category", userContent.getCategory());
            inputDocument.addField("title", userContent.getTitle());
            inputDocument.addField("u_id", userContent.getuId());
            inputDocument.addField("id", userContent.getId());
            inputDocument.addField("personal", userContent.getPersonal());
            httpSolrClient.add(inputDocument);
            httpSolrClient.commit();
        }catch (SolrServerException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserContent(UserContent userContent) {
        if (userContent != null){
            addDocument(userContent);
        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            httpSolrClient.deleteById(id.toString());
            httpSolrClient.commit();
        }catch (SolrServerException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
