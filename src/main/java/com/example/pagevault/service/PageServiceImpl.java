package com.example.pagevault.service;

import com.example.pagevault.model.Page;
import com.example.pagevault.model.PageRepository;
import com.example.pagevault.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private PageRepository pageRepository;

    @Override
    public Page createPage(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public void deletePage(Long pageId) {
        pageRepository.deleteById(pageId);
    }

    @Override
    public Page getPageById(Long pageId) {
        return pageRepository.findById(pageId).orElse(null);
    }

    @Override
    public List<Page> getPagesByUser(User user) {
        return pageRepository.findByOwner(user);
    }
}

