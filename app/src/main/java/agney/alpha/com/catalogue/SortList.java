package agney.alpha.com.catalogue;

import java.util.List;

/**
 * Created by Agney on 21-01-2016.
 */
public class SortList {
    static int minPrice;
    static int position = 0;
    public static int findMin(List<Attributes> list) {

        minPrice = list.get(0).getSellingPrice();
        for(int i=0;i<list.size();i++) {
            int SP = list.get(i).getSellingPrice();
            if(minPrice >= SP) {

                minPrice = SP;
                position = i;

            }
        }

        return position;
    }
}
