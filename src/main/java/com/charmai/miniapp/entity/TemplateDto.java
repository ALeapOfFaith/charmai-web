package com.charmai.miniapp.entity;

public class TemplateDto {
        private String id;
        private String name;
        private String description;
//        private String type_cue;
//        private int gender;
        private String cover_url;
//        private int used;

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
        public void setCover_url(String cover_url){this.cover_url = cover_url;}
        public String getCover_url(){return cover_url;}

//        public void setUsed(int used) {
//            this.used = used;
//        }
//        public int getUsed() {
//            return used;
//        }

    }