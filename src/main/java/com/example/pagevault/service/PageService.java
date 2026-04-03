package com.example.pagevault.service;

import com.example.pagevault.model.Page;
import com.example.pagevault.model.User;
import java.util.List;

public interface PageService {
    Page createPage(Page page);
    Page updatePage(Page page);
    void deletePage(Long pageId);
    Page getPageById(Long pageId);
    List<Page> getPagesByUser(User user);
}

