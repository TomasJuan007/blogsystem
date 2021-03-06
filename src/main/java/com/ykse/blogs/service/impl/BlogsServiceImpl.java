package com.ykse.blogs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ykse.blogs.bean.Blogs;
import com.ykse.blogs.dao.BlogsDao;
import com.ykse.blogs.service.BlogsService;

@Service
public class BlogsServiceImpl implements BlogsService {

    @Autowired
    private BlogsDao blogsDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Blogs> getBlogsAll(int startRow, int endRow) {
        return blogsDao.getBlogsAll(startRow, endRow);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveBlogs(Blogs blogs) {

        return blogsDao.saveBlogs(blogs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateBlogs(Blogs blogs) {

        return blogsDao.updateBlogs(blogs);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Blogs getBlogsById(int blogsId) {
        return blogsDao.getBlogsById(blogsId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer getBlogsCount() {
        return blogsDao.getBlogsCount();
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean addCountById(int blogsId) {
        if(blogsDao.addCountById(blogsId) != true)
            return false;
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean subtractCountById(int blogsId) {
        if(blogsDao.subtractCountById(blogsId) != true)
                return false;
        return true;
    }
}
