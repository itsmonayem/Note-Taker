package com.spring.notetaker.helper;

public class CommonUtils {
    public static Pagination getPagination(Pagination paginationInfo,int currentPage, int totalPage, String queryRequest) {
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setTotalPages(totalPage);
        paginationInfo.setQueries(queryRequest);
        return paginationInfo;
    }
}
