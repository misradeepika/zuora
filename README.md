The problem:

Given a dataset that represents a user's navigation of a website, find the top N most frequently visited paths.
 
The Data:
 
The data comes from a web server's access log where you typically get the following fields: timestamp, IP address, request string, response code, user agent and cookies. For brevity, we provide a dataset that has the user and page parsed out.
User

Page

U1	/
U1	subscribers
U2	/
U2	subscribers
U1	filter
U1	export
U2	filter
U2	export
U3	/
U3	catalog
U3	edit

The following words are used to describe the function that needs to be written: Find the top N most popular 3-page paths, where a path is three sequential page visits by a user.
 
The example I use is from U1, where the traversal is: / -> subscribers -> filter -> export
In the above example, we have two paths:
1.      / -> subscribers -> filter
2.      subscribers -> filter -> export


My Solution:

Input data: I assume the input to be a list of the user strings and the pages visited. I use the class PageVisit to denote the user and page tuple

Algorithm: In the first step, I convert my input into a map that has a list of pages visited per user (Map<String, List<String>>). Then, I proceed to build a tree from the pages visited by each user in such a way that each time a node is visited by a user in the same order as another user, the frequency for that node is incremented. For ex: Lets say, user U1 has visited the following pages, home -> subscribers -> filter -> export and another user U2 has visited the following pages home -> subscribers -> list, the tree will have the following structure:

home (2)
  |
subscribers (2)
     /       \
filter (1)   list (1)
    |
  export (1)

The numbers in the brackets indicate how many times each of this node has been visited by a user as part of the same path as another user. Once the tree is built, findind the topN popular paths is only a matter of finding the topN subtrees with a depth of 3

To determine that, I do a breadth first traversal through the tree getting all possible 3 node deep sub trees and getting the sum of their node frequencies. This information is stored in a sorted TreeMap with the frequency total as the key and the list of the 3 node sub tree as the values. Sorting the TreeMap descendingly and extracting the topN values gives me my output in the format of List<List<String>>


Assumptions:
- All users start at the same page
- Users cannot visit the same page consecutively