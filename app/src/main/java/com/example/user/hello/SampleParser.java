package com.example.user.hello;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

/**
 * Created by user on 2017-08-26.
 */

public class SampleParser {
    public final static String SAMPLE_URL="http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=zzn4oRQfMmEushbBZAfYxe6IIiA2rLhq%2BN4K9jY%2BNwOpWeNWQkZ0%2Fkeraknoev9ckDmYJqxPjzLsQ7PfVTr8qw%3D%3D&contentTypeId=15&areaCode=1&sigunguCode=&cat1=&cat2=&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=1 ";

    public SampleParser(){
        try{
            apiParserSearch();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public ArrayList<SampleDTO> apiParserSearch() throws Exception{
        URL url = new URL(SAMPLE_URL);
        InputStream in = url.openStream();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(true);

        XmlPullParser xpp = factory.newPullParser();

      //  BufferedInputStream bis = new BufferedInputStream(url.openStream());

        xpp.setInput(in, "utf-8");
        String tag = null;
        boolean isItemTag=false;
        int event_type = xpp.getEventType();
        ArrayList<SampleDTO> list = new ArrayList<SampleDTO>();

       Double xpos=null,ypos=null;
        String name=null;
        while (event_type != XmlPullParser.END_DOCUMENT) {

            if (event_type == XmlPullParser.START_TAG) {

                tag = xpp.getName();
                if(tag.equals("item")) isItemTag=true;

            } else if (event_type == XmlPullParser.TEXT&&isItemTag) {
                if (tag.equals("mapx")) {
                    xpos = parseDouble(xpp.getText());

                }  if (tag.equals("mapy")) {
                    ypos = parseDouble(xpp.getText());
                } else if (tag.equals("title")) {
                    name = xpp.getText();
                }

            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("item")) {
                    SampleDTO entity = new SampleDTO();
                    entity.setXpos(Double.valueOf(xpos));
                    entity.setYpos(Double.valueOf(ypos));
                    entity.setName(name);

                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }
        System.out.println(list.size());
        return list;
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub

        new SampleParser();

    }
}
