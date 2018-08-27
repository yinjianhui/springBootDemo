package com.huisir.springboot.demo.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

// @PermissionConstants.ADMIN_PRODUCT_MANAGEMENT
public class ProductController {

    /**
     * 产品列表
     *
     * @return
     */
    @RequestMapping("/")
    @RequiredPermission(PermissionConstants.ADMIN_PRODUCT_LIST) // 权限注解
    public String list() {
         // 省略产品列表查询逻辑
        return "login";
    }

    /**
     * 产品详情
     *
     * @return
     */
    @RequestMapping("/detail")
    @RequiredPermission(PermissionConstants.ADMIN_PRODUCT_DETAIL) // 权限注解
    public String detail() {
        // 省略查询产品详情的逻辑
        return "login";
    }

    /**
     * 删除产品
     *
     * @return
     */
    @RequestMapping("/del")
    public String delete() {
        // 省略删除产品的逻辑
        return "login";
    }
}
