package com.frizo.ucc.server.utils.common;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestBuilder {

    private PageRequestBuilder(){
    }

    public static PageRequestDetail create(){
        return new PageRequestBuilder.PageRequestDetail();
    }

    public static class PageRequestDetail{
        private int pageNumber = 0;
        private int pageSize = 20;
        private String sortBy = "createdAt";
        private String direction = "DESC";

        public PageRequestDetail pageNumber(int num){
            this.pageNumber = num;
            return this;
        }

        public PageRequestDetail pageSize(int size){
            this.pageSize = size;
            return this;
        }

        public PageRequestDetail sortBy(String sortBy){
            if (sortBy != null && !sortBy.equals("")){
                this.sortBy = sortBy;
            }
            return this;
        }

        public PageRequestDetail direction(String direction){
            if (direction != null && !direction.equals("")){
                this.direction = direction;
            }
            return this;
        }

        public PageRequest build(){
            Sort.Direction direction = Sort.Direction.DESC;
            if (this.direction.equals("ASC")){
                direction = Sort.Direction.ASC;
            }
            Sort sort = Sort.by(direction, this.sortBy);
            return PageRequest.of(this.pageNumber, this.pageSize, sort);
        }
    }
}
