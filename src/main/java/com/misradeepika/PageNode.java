package com.misradeepika;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a single node of the path tree.
 *
 * @author Deepika Misra
 */
public class PageNode {

    private String page;
    private int count;  //This is the number of times a node has been visited by a user in the sequence by other users.
    private List<PageNode> children;

    PageNode(String page) {
        this(page, 1);
    }

    PageNode(String page, int count) {
        this(page, count, new ArrayList<>());
    }

    PageNode(String page, int count, List<PageNode> children) {
        this.page = page;
        this.count = count;
        this.children = new ArrayList<>(children);
    }

    public String getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public List<PageNode> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void incrementCount() {
        this.count++;
    }

    //returns the instance of child PageNode if this node contains a child with the given page, else returns null
    public PageNode getChildNode(String page) {
        if (children.isEmpty()) {
            return null;
        }
        //Note: if we defined children as a HashSet and override the equality and hashcode methods in such a way that the PageNodes
        //are only compared by the page attribute, we can do a children.contains(new PageNode(page))
        PageNode result = null;
        for (PageNode child: children) {
            if (page.equals(child.getPage())) {
                result = child;
                break;
            }
        }
        return result;
    }

    //Adds the given page as a child node and returns the PageNode instance
    public PageNode addChildNode(String page) {
        PageNode childNode = new PageNode(page);
        children.add(childNode);
        return childNode;
    }
}
