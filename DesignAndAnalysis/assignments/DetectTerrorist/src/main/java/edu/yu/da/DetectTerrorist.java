package edu.yu.da;

/** Defines the API for specifying and solving the DetectTerrorist problem (see
 * the requirements document).
 *
 * Students MAY NOT change the public API of this class, nor may they add ANY
 * constructor.
 *
 * @author Avraham Leff
 */

public class DetectTerrorist {

    private int terrorist;

    /** Constructor: represents passengers to be detected as an array in which
     * the ith value is the weight of the ith passenger.  After the constructor
     * completes, clients can invoke getTerrorist() for a O(1) lookup cost.
     *
     * @param passengers an array of passenger weights, indexed 0..n-1.  All
     * passengers that are not terrorists have the same weight: that weight is
     * greater than the weight of the terrorist.  Exactly one passenger is a
     * terrorist.
     */
    public DetectTerrorist(final int[] passengers) {
        // fill me in!
        if(passengers.length == 1){
           this.terrorist = 0;
        }else if(passengers.length % 2 != 0){
            if(passengers[0] > passengers[passengers.length - 1]){
               this.terrorist = passengers.length - 1;
            }else if(passengers[0] < passengers[passengers.length - 1]){
                this.terrorist = 0;
            }else{
                this.terrorist = binarySearch(passengers, 0, passengers.length - 2);
            }
        }else{
            this.terrorist = binarySearch(passengers, 0, passengers.length - 1);
        }

        //iterative solution
//        for(int i = 0; i < passengers.length - 4; i+=4){
//            if(passengers[i] + passengers[i + 1] != passengers[i + 2] + passengers[i + 3]){
//                if(passengers[i] + passengers[i + 1] > passengers[i + 2] + passengers[i + 3]){
//                    if(passengers[i] + passengers[i + 2] > passengers[i + 1] + passengers[i + 3]){
//                        terrorist = i + 3;
//                    }else{
//                        terrorist = i + 2;
//                    }
//                }else{
//                    if(passengers[i] + passengers[i + 2] < passengers[i + 1] + passengers[i + 3]){
//                        terrorist = i;
//                    }else{
//                        terrorist = i + 1;
//                    }
//                }
//                break;
//            }
//        }
    }   // constructor

    private int binarySearch(int[] arr, int l, int r) {
        if(l + 1 == r){
            if(arr[l] > arr[r]){
                return r;
            }else{
                return l;
            }
        }
        int mid = l + (r - l) / 2;


        int left = 0;
        int right = 0;
        for(int i = l; i <= r; i++){
            if(i <= mid){
                left += arr[i];
            }else{
                right += arr[i];
            }
        }

        if (left < right){
            if((mid - l)%2 == 0) {
                return binarySearch(arr, l, mid + 1);
            }else{
                return binarySearch(arr, l, mid);
            }
        }else if(left > right){
            if((r - mid + 1)%2 == 0){
                return binarySearch(arr, mid, r);
            }else{
                return binarySearch(arr, mid + 1, r);
            }
        }else{
            return -1;
        }
    }



    /** Returns the index of the passenger who has been determined to be a
     * terrorist.
     *
     * @return the index of the terrorist element.
     */
    public int getTerrorist() {
        return this.terrorist;
    }

} // DetectTerrorist