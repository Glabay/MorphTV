package org.jsoup.examples;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks {
    public static void main(String[] strArr) throws IOException {
        Validate.isTrue(strArr.length == 1, "usage: supply url to fetch");
        print("Fetching %s...", strArr[0]);
        strArr = Jsoup.connect(strArr).get();
        Elements select = strArr.select("a[href]");
        Elements select2 = strArr.select("[src]");
        strArr = strArr.select("link[href]");
        print("\nMedia: (%d)", Integer.valueOf(select2.size()));
        Iterator it = select2.iterator();
        while (it.hasNext()) {
            if (((Element) it.next()).tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)", ((Element) it.next()).tagName(), ((Element) it.next()).attr("abs:src"), ((Element) it.next()).attr("width"), ((Element) it.next()).attr("height"), trim(((Element) it.next()).attr("alt"), 20));
            } else {
                print(" * %s: <%s>", ((Element) it.next()).tagName(), ((Element) it.next()).attr("abs:src"));
            }
        }
        print("\nImports: (%d)", Integer.valueOf(strArr.size()));
        strArr = strArr.iterator();
        while (strArr.hasNext()) {
            Element element = (Element) strArr.next();
            print(" * %s <%s> (%s)", element.tagName(), element.attr("abs:href"), element.attr("rel"));
        }
        print("\nLinks: (%d)", Integer.valueOf(select.size()));
        strArr = select.iterator();
        while (strArr.hasNext()) {
            Element element2 = (Element) strArr.next();
            print(" * a: <%s>  (%s)", element2.attr("abs:href"), trim(element2.text(), 35));
        }
    }

    private static void print(String str, Object... objArr) {
        System.out.println(String.format(str, objArr));
    }

    private static String trim(String str, int i) {
        if (str.length() <= i) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str.substring(0, i - 1));
        stringBuilder.append(".");
        return stringBuilder.toString();
    }
}
