package utils;

import caskit_api.CaskitApi;
import caskit_api.data.Content;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class UrlHelper {

    public static String getURL(Content content, boolean directUrl) {
        if (directUrl) {
            return CaskitApi.get().directUrl(content);
        } else {
            return CaskitApi.get().url(content);
        }
    }

    public static void open(Content content, boolean directUrl) {
        try {
            if (directUrl) {
                UrlHelper.openWebpage(new URL(CaskitApi.get().directUrl(content)));
            } else {
                UrlHelper.openWebpage(new URL(CaskitApi.get().url(content)));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

}
