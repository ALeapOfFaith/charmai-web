package com.charmai.miniapp.entity;

public class SearchVo {

        private String category;
        private String search_key;
        private int page_size;
        private int page_num;
        public void setCategory(String category) {
            this.category = category;
        }
        public String getCategory() {
            return category;
        }

        public void setSearch_key(String search_key) {
            this.search_key = search_key;
        }
        public String getSearch_key() {
            return search_key;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }
        public int getPage_size() {
            return page_size;
        }

        public void setPage_num(int page_num) {
            this.page_num = page_num;
        }
        public int getPage_num() {
            return page_num;
        }

}
