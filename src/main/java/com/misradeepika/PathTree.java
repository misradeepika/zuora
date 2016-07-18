package com.misradeepika;

/**
 * Represents the entire path tree through a root node.
 *
 * @author Deepika Misra
 */
public class PathTree {

    private PageNode root;

    PathTree(PageNode root) {
        this.root = root;
    }

    public PageNode getRoot(){
        return root;
    }
}
