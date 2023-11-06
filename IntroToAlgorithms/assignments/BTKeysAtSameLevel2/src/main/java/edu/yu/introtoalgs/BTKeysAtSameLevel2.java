

package edu.yu.introtoalgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BTKeysAtSameLevel2 {
    private List<List<Integer>> levels = new ArrayList<>();;

    /** Constructor
     */
    public BTKeysAtSameLevel2() {
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
        //checking if empty string
        if(treeInStringRepresentation.isBlank() || treeInStringRepresentation.isBlank()){
            return levels;
        }
        //checking for all possible errors
        if(!errorChecker(treeInStringRepresentation)){
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
                /*while(listCounter >= levels.size()){
                    levels.add(new ArrayList<Integer>());
                }*/
                if(listCounter >= levels.size()){
                    levels.add(new ArrayList<Integer>());
                }

            }else if(Character.isDigit(treeInStringRepresentation.charAt(i))){
                levels.get(listCounter).add(Character.getNumericValue(treeInStringRepresentation.charAt(i)));
            }else{ //if(treeInStringRepresentation.charAt(i) == ')'){
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
        if(!notGood){
            return false;
        }

        char[] toArray = treeInStringRepresentation.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : toArray) {
            if (c == '(' || c == ')') {
                result.append(c);
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
        return childChecker(result.toString());
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

        for (List<Integer> child : children) {
            if (child.size() >= 3) {
                return false;
            }
        }
        return true;
    }
}   // class