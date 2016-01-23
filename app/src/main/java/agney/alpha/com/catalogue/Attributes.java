package agney.alpha.com.catalogue;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Attributes implements Comparable<Attributes> {
    String title;
    String productDescription;
    String website;
    String category;
    String imageUrl;
    int maximumRetailPrice;
    int sellingPrice;
    String productUrl;
    final static ArrayList<Attributes> list= new ArrayList<>();

    public Attributes() {
        this.title=null;
        this.website=null;
    }
    public Attributes(JSONObject jsonObject,String title,String category) {
        try {
            this.title = title;
            this.category = category;
            this.website = jsonObject.getString("website");
            this.sellingPrice = jsonObject.getInt("price");
            this.imageUrl = jsonObject.getString("image");
            this.productUrl = jsonObject.getString("url");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Attributes(JSONObject jsonObject) {
        try {
            this.title = jsonObject.getString("title");
            this.productDescription = jsonObject.getString("productDescription");
            JSONObject sellp = jsonObject.getJSONObject("maximumRetailPrice");
            JSONObject imageURLs = jsonObject.getJSONObject("imageUrls");
            this.imageUrl = imageURLs.getString("400x400");
            this.maximumRetailPrice=sellp.getInt("amount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { return this.title; }
    public String getProductDescription() {
        return this.productDescription;
    }
    public String getWebsite() {return this.website; }
    public int getSellingPrice() { return this.sellingPrice; }
    public String getProductUrl() { return this.productUrl; }
    public static ArrayList<Attributes> fromJson(JSONObject jObj) {

        final ArrayList<Attributes> siteList= new ArrayList<>();
        siteList.clear();
        String model;
        try {
            JSONArray products =  jObj.getJSONArray("product");
            for(int i=0;i<products.length();i++) {
                JSONObject d = (JSONObject) products.get(i);
                model=d.getString("brand") + " " + d.getString("model");
                String category = d.getString("section");
                JSONArray stores = d.getJSONArray("stores");
                for(int j=0;j<stores.length();j++) {
                    JSONObject c = (JSONObject) stores.get(j);
                    siteList.add(new Attributes(c, model,category));
                }
            }
        } catch(JSONException e) {
                e.printStackTrace();
            }
        return siteList;

    }
    public static List<Attributes> forInfo(JSONObject jObj) {

        final List<Attributes> fliplist= new ArrayList<>();
        try {
            JSONArray items = jObj.getJSONArray("productInfoList");
            for(int i=0;i<items.length();i++) {
                JSONObject c = (JSONObject) items.get(i);
                JSONObject baseInfo = c.getJSONObject("productBaseInfo");
                JSONObject attrib = baseInfo.getJSONObject("productAttributes");
                fliplist.add(new Attributes(attrib));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return fliplist;

    }

    public static List<Attributes> forList (JSONObject jObj) {
        JSONObject c = new JSONObject();
        String model;
        try {
            JSONArray products =  jObj.getJSONArray("product");
            for(int i=0;i<products.length();i++) {
                JSONObject d = (JSONObject) products.get(i);
                model=d.getString("brand") + " " + d.getString("model");
                String category = d.getString("section");
                JSONArray stores = d.getJSONArray("stores");
                if(stores.length()!=0) {
                    c = (JSONObject) stores.get(0);
                }
                list.add(new Attributes(c,model,category));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public int compareTo(Attributes another) {
        int comparePrice = another.getSellingPrice();
        return this.sellingPrice-comparePrice;
    }



}

