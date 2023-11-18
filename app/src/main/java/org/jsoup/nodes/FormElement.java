package org.jsoup.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class FormElement extends Element {
    private final Elements elements = new Elements();

    public FormElement(Tag tag, String str, Attributes attributes) {
        super(tag, str, attributes);
    }

    public Elements elements() {
        return this.elements;
    }

    public FormElement addElement(Element element) {
        this.elements.add(element);
        return this;
    }

    public Connection submit() {
        String absUrl = hasAttr("action") ? absUrl("action") : baseUri();
        Validate.notEmpty(absUrl, "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
        return Jsoup.connect(absUrl).data(formData()).method(attr("method").toUpperCase().equals("POST") ? Method.POST : Method.GET);
    }

    public List<KeyVal> formData() {
        List arrayList = new ArrayList();
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.tag().isFormSubmittable()) {
                if (!element.hasAttr("disabled")) {
                    String attr = element.attr("name");
                    if (attr.length() != 0) {
                        String attr2 = element.attr("type");
                        if ("select".equals(element.tagName())) {
                            Object obj = null;
                            Iterator it2 = element.select("option[selected]").iterator();
                            while (it2.hasNext()) {
                                arrayList.add(HttpConnection.KeyVal.create(attr, ((Element) it2.next()).val()));
                                obj = 1;
                            }
                            if (obj == null) {
                                element = element.select("option").first();
                                if (element != null) {
                                    arrayList.add(HttpConnection.KeyVal.create(attr, element.val()));
                                }
                            }
                        } else {
                            if (!"checkbox".equalsIgnoreCase(attr2)) {
                                if (!"radio".equalsIgnoreCase(attr2)) {
                                    arrayList.add(HttpConnection.KeyVal.create(attr, element.val()));
                                }
                            }
                            if (element.hasAttr("checked")) {
                                arrayList.add(HttpConnection.KeyVal.create(attr, element.val().length() > 0 ? element.val() : "on"));
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
