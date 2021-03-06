package com.ykse.blogs.controller;

import java.util.List;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ykse.blogs.bean.Blogs;
import com.ykse.blogs.bean.Comment;
import com.ykse.blogs.bean.Pagination;
import com.ykse.blogs.bean.User;
import com.ykse.blogs.service.BlogsService;
import com.ykse.blogs.service.CommentService;
import com.ykse.blogs.service.UserService;

import net.sf.json.JSONObject;

/**
 * 评论控制器
 * 
 * <li>主要含：展示评论、添加评论、删除评论</li>
 * 
 * @author tao.huang
 * @version $Id: CommentController.java, v 0.1 2016年11月14日 下午6:45:14 tao.huang Exp $
 */
@Controller
@RequestMapping("/listBlogs")
public class CommentController {  
	
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogsService blogsService;
    @Autowired
    private UserService userService;
   
    /**
     * 展示评论
     * 
     * @param blogsId
     * @param request
     * @return
     */
    @RequestMapping(value="/listComment", method=RequestMethod.GET)
    public ModelAndView getComments(String blogsId, HttpServletRequest request) {
        Integer bid = (blogsId == null || blogsId == "") ? 1 : Integer.parseInt(blogsId);
        Blogs blogs = blogsService.getBlogsById(bid);
        
        ModelAndView modelAndView = new ModelAndView("blogs/listComment");
        Pagination<Comment> page = new Pagination<Comment>();
        
        String pageNum = (String)request.getParameter("pageNum");
        String numPerPage = (String)request.getParameter("numPerPage");
        Integer pagenum = (pageNum == null || pageNum == "")? 1 : Integer.parseInt(pageNum); 
        Integer numperpage = (numPerPage == null || numPerPage == "")? 10 : Integer.parseInt(numPerPage); 
        page.setCurrentPage(pagenum);
        page.setNumPerPage(numperpage);
        page.setTotalCount(blogs.getCommentCount());
       
        int startRow, endRow;
        startRow = (page.getCurrentPage() - 1) * page.getNumPerPage();
        endRow = page.getNumPerPage();
       
        
        List<Comment> comments = commentService.getCommentsByBlogsId(bid, startRow, endRow);
        page.setContent(comments);
        
        String blogsTitle = blogs.getBlogsTitle();
        String blogsContent = blogs.getBlogsContent();
        
        page.setTotalCount(blogs.getCommentCount());             
        page.calcutePage();
        Integer uid = blogs.getUser().getUserId();
        request.setAttribute("page", page);
        request.setAttribute("blogsId", bid);
        request.setAttribute("userId", uid);
        request.setAttribute("blogsTitle", blogsTitle);
        request.setAttribute("blogsContent", blogsContent);
        return modelAndView;
    }
    
    @RequestMapping(value="/addComment", method=RequestMethod.GET)    
    public ModelAndView addComment(Model model, String userId, String blogsId){
        model.addAttribute("userId", userId);
        model.addAttribute("blogsId", blogsId);
        return new ModelAndView("/blogs/addComment");
    }
    
    @ResponseBody
    @RequestMapping(value="/saveComment", method=RequestMethod.POST,produces = "application/json; charset=utf-8")    
    public String setComment(String blogsId, String userId, String commentContent){
        Integer bid = (blogsId == null || blogsId == "") ? 1 : Integer.parseInt(blogsId);
        Integer uid = (userId == null || userId == "") ? 1 : Integer.parseInt(userId);
        
        Comment comment = new Comment();
        Blogs blogs = blogsService.getBlogsById(bid);
        comment.setBlogs(blogs);
        User user = userService.getUserById(uid);
        comment.setUser(user);
        comment.setCommentContent(commentContent);
        JSONObject result = new JSONObject();
        if(commentService.saveComment(comment)){
            result.put("message", "更改成功！");
            result.put("statusCode", "200");
            result.put("dialog", "true");
            return result.toString();
        }
        result.put("message", "更改失败！");
        result.put("statusCode", "300");
        result.put("dialog", "closeCurrent");
        return result.toString();    
    }
    
    @ResponseBody
    @RequestMapping(value="/deleteComment", method=RequestMethod.DELETE)    
    public String deleteComment(String commentId){
        Integer cid = (commentId == null || commentId == "") ? 0 : Integer.parseInt(commentId);
        commentService.deleteComment(cid);
        return "suc";
    }
    
}  
