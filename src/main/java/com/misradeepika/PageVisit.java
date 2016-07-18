package com.misradeepika;

/**
 * This indicates a single page that was visited by a user. 
 * Represents an individual entity of the input data.
 *
 * @author Deepika Misra
 */
public class PageVisit {
    private String user;
    private String page;

    PageVisit(String user, String page) {
        this.user = user;
        this.page = page;
    }

    public String getUser() {
        return user;
    }

    public String getPage() {
        return page;
    }
}
