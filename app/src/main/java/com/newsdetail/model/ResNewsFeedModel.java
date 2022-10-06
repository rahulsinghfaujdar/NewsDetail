package com.newsdetail.model;

/**
 * News Response Model
 * with
 * Sample Data
 * {
 *             "source": {
 *                 "id": "techcrunch",
 *                 "name": "TechCrunch"
 *             },
 *             "author": "Alex Wilhelm",
 *             "title": "Is this what an early-stage slowdown looks like?",
 *             "description": "Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between. Today we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wit…",
 *             "url": "http://techcrunch.com/2020/02/10/is-this-what-an-early-stage-slowdown-looks-like/",
 *             "urlToImage": "https://techcrunch.com/wp-content/uploads/2020/02/GettyImages-dv1637047.jpg?w=556",
 *             "publishedAt": "2020-02-10T17:06:42Z",
 *             "content": "Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between.\r\nToday we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wi… [+648 chars]"
 *  }
 */
public class ResNewsFeedModel {

    private source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public ResNewsFeedModel(ResNewsFeedModel.source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public ResNewsFeedModel.source getSource() {
        return source;
    }

    public void setSource(ResNewsFeedModel.source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

   public static class source {
        private String id;
        private String name;

       public source(String id, String name) {
           this.id = id;
           this.name = name;
       }

       public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
