package com.juyao.httpdemo;

import java.util.List;

public class AllBannerData extends FullHttpResult<AllBannerData>{

    /**
     * data : [{"image":"http://gank.io/images/cfb4028bfead41e8b6e34057364969d1","title":"干货集中营新版更新","url":"https://gank.io/migrate_progress"},{"image":"http://gank.io/images/aebca647b3054757afd0e54d83e0628e","title":"- 春水初生，春林初盛，春风十里，不如你。","url":"https://gank.io/post/5e51497b6e7524f833c3f7a8"},{"image":"https://pic.downk.cc/item/5e7b64fd504f4bcb040fae8f.jpg","title":"盘点国内那些免费好用的图床","url":"https://gank.io/post/5e7b5a8b6d2e518fdeab27aa"}]
     * status : 100
     */

    private List<DataDTO> data;
    private int status;

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataDTO {
        /**
         * image : http://gank.io/images/cfb4028bfead41e8b6e34057364969d1
         * title : 干货集中营新版更新
         * url : https://gank.io/migrate_progress
         */

        private String image;
        private String title;
        private String url;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
