

package edu.yu.introtoalgs;

import java.util.ArrayList;
import java.util.LinkedList;

/** Defines the API for the BTKeysAtSameLevel assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */


import java.util.List;
import java.util.Queue;
import java.util.Stack; 

public class BTKeysAtSameLevel {
  private List<List<Integer>> levels = new ArrayList<>();;
  
  /** Constructor
   */
  public BTKeysAtSameLevel() {
    // fill this in!
    this.levels = new ArrayList<>();
  }

  /** Given a String representation of a binary tree whose keys are Integers,
   * computes a List whose elements are List of keys at the same level (or
   * depth) from the root.  The top-level List is ordered by increasing
   * distance from the root so that the first List element consists of the
   * root's key, followed by the keys of all the root's immediate children,
   * etc.  At a given level, the List is ordered from left to right.  See the
   * requirements doc for an example.
   *
   * The String representation of the binary tree is defined as follows.  Keys
   * consist of a single integer value, from 0 to 9.  The string consists only
   * of parentheses and single digit integers: it must begin with an integer
   * (the value of the root node) followed by zero, one or two pairs of
   * parentheses. A pair of parentheses represents a child binary tree with
   * (recursively) the same structure. If a given node only has one child, that
   * child will be the left child node of the parent.
   * 
   * Note: the "empty" tree is represented by the empty string, and this method
   * should therefore return an empty List.
   *
   * @param treeInStringRepresentation a binary tree represented in the
   * notation defined above.
   * @return a List whose elements are Lists of the tree's (integer) key
   * values, ordered in increasing distance from the root.  Each List element
   * contains the keys at a given level, ordered from left to right.
   * @throws IllegalArgumentException if the String is null, or doesn't
   * correspond to a valid String representation of a binary tree as defined
   * above.
   */

  public List<List<Integer>> compute(final String treeInStringRepresentation) {
    // I suggest substituting a better implementation!
    /*if(errorChecker(treeInStringRepresentation) == false){
      //return new IllegalArgumentException();
      return null;
    }*/
    //Node root = constructTree(treeInStringRepresentation);
    //this.height = height(root);
    //for(int i = 0; i < this.height; i++){
    //  levels.add(new ArrayList<Integer>());
    //}
    //getLevels(root);
    //return levels;
    
    //checking if empty string
    if(treeInStringRepresentation == ""){
      return levels;
    }
    //checking for all possible errors
    if(errorChecker(treeInStringRepresentation) == false){
      throw new IllegalArgumentException();
    }

    //creating root level
    levels.add(new ArrayList<Integer>());
    levels.get(0).add(Character.getNumericValue(treeInStringRepresentation.charAt(0)));
    
    //main algorithm
    int listCounter = 0;
    Stack<Character> mow = new Stack<>();
    for(int i = 1; i < treeInStringRepresentation.length(); i++){
      if(treeInStringRepresentation.charAt(i) == '('){
        mow.push(treeInStringRepresentation.charAt(i));
        listCounter = mow.size();
        while(listCounter >= levels.size()){
          levels.add(new ArrayList<Integer>());
        }
        
      }else if(Character.isDigit(treeInStringRepresentation.charAt(i))){
        levels.get(listCounter).add(Character.getNumericValue(treeInStringRepresentation.charAt(i)));
      }else if(treeInStringRepresentation.charAt(i) == ')'){
        mow.pop();
      }
    }
    return levels;
  } // compute

  private boolean errorChecker(String treeInStringRepresentation){
    if(treeInStringRepresentation == null){
      return false;
    }

    if(!Character.isDigit(treeInStringRepresentation.charAt(0))){
      return false;
    }

    boolean notGood = true;
    for(int i = 0; i<treeInStringRepresentation.length(); i++){
      if((Character.isDigit(treeInStringRepresentation.charAt(i))) || treeInStringRepresentation.charAt(i) == '(' ||  treeInStringRepresentation.charAt(i) == ')'){
        continue;
      }else{
        notGood = false;
      }
    }
    if(notGood == false){
      return false;
    }

    char toArray [] = treeInStringRepresentation.toCharArray();
    String result = "";
    for(int i  = 0; i < toArray.length; i++){
      if(toArray[i] == '(' || toArray[i] == ')' ){
        result += toArray[i];
      }
    }
    Stack<Character> ch = new Stack<>();
    for(int i = 0; i<result.length(); i++){
      if(ch.isEmpty()){
        ch.push(result.charAt(i));
      }else if(ch.peek() == '(' && result.charAt(i) == ')'){
        ch.pop();
      }else{
        ch.push(result.charAt(i));
      }
    }
    if(!ch.isEmpty()){
      return false;
    }
    if(!childChecker(result)){
      return false;
    }
    return true;
  }

  public boolean childChecker(String s){
    List<List<Integer>> children = new ArrayList<>();
    Stack<Character> show = new Stack<>();
    int depth = 0;
    for(int i = 0; i < s.length(); i++){
       if(s.charAt(i) == '('){
         if(show.isEmpty()){
          children.add(new ArrayList<Integer>());
          children.get(0).add(1);
         }else if(show.peek() == '('){
           depth = children.size();
           children.add(new ArrayList<Integer>());
           children.get(depth).add(1);
         }else{
           children.get(depth).add(1);
         }
          show.push(s.charAt(i));
       }else if(s.charAt(i) == ')'){
          if(show.peek() == ')'){
            show.push(s.charAt(i));
            depth--;
          }else{
            show.push(s.charAt(i));
          }
       }
    }

    for(int i = 0; i < children.size(); i++){
      if(children.get(i).size() >= 3){
        return false;
      }
    }
    return true;
  }

  
  //different algo to solve problem that actually constructs tree 
  //and then goes through each level adding the values at that level to list
  
  /*private static class Node{
    int data;
    Node left, right;

    private Node(int data){
      this.data = data;
      this.left = this.right = null;
    }
  }*/

  //int height;
  //static int start = 0;

  /*private static Node constructTree(String s){
    if(s.length() == 0 || s == null){
      return null;
    }
    if(start >= s.length()){
      return null;
    }
    int num = 0;
    while(start < s.length() && Character.isDigit(s.charAt(start))){
      int digit = Character.getNumericValue(s.charAt(start));
      num = num * 10 + digit;
      start++;
    }
    Node node = new Node(num);
    if(start >= s.length()){
      return node;
    }
    if(start < s.length() && s.charAt(start) == '('){
      start++;
      node.left = constructTree(s);
    }
    if(start < s.length() && s.charAt(start) == ')'){
      start++;
      return node;
    }
    if(start < s.length() && s.charAt(start) == '('){
      start++;
      node.right = constructTree(s);
    }
    if(start < s.length() && s.charAt(start) == ')'){
      start++;
      return node;
    }
    return node;
  }*/

  /*private int height(Node root){
    if(root == null){
      return 0;
    }else{
      int lHeight = height(root.left);
      int rHeight = height(root.right);
      if(lHeight > rHeight){
        return (lHeight + 1);
      }else{
        return (rHeight + 1);
      }
    }
  }*/

  /*private void getLevels(Node root){
    int counter = 0;
    if(counter >= this.height){
      return;
    }
    if(root == null){
      return;
    }
    Queue<Node> q = new LinkedList<>();
    q.add(root);
    q.add(null);
    while(!q.isEmpty()){
      Node curr = q.poll();
      if(curr == null){
        if(!q.isEmpty()){
          counter++;
          q.add(null);
        }
      }else {
        if(curr.left != null){
          q.add(curr.left);
        }
        if(curr.right != null){
          q.add(curr.right);
        }
        levels.get(counter).add(curr.data);
      } 
    }
  }*/
}   // class